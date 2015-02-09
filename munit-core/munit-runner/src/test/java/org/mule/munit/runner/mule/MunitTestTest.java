/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.munit.runner.mule;


import org.junit.Before;
import org.mockito.Matchers;
import org.mule.api.DefaultMuleException;
import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.config.MuleProperties;
import org.mule.api.registry.MuleRegistry;
import org.mule.modules.interceptor.processors.MessageProcessorId;
import org.mule.munit.common.endpoint.MockEndpointManager;
import org.mule.munit.common.mp.MockedMessageProcessorManager;
import org.mule.munit.common.mp.MunitMessageProcessorCall;
import org.mule.munit.config.MunitFlow;
import org.mule.munit.config.MunitTestFlow;
import org.mule.munit.runner.mule.result.TestResult;
import org.mule.munit.runner.output.TestOutputHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MunitTestTest {

    private MunitFlow before = mock(MunitFlow.class);
    private MunitFlow after = mock(MunitFlow.class);
    private MunitTestFlow testFlow = mock(MunitTestFlow.class);
    private TestOutputHandler handler = mock(TestOutputHandler.class);
    private MuleEvent muleEvent = mock(MuleEvent.class);
    private MuleContext mockMuleContext = mock(MuleContext.class);
    private MuleRegistry muleRegistry = mock(MuleRegistry.class);
    private MockEndpointManager endpointManager = mock(MockEndpointManager.class);
    private MockedMessageProcessorManager processorManager = mock(MockedMessageProcessorManager.class);

    @Before
    public void setUpMocks() {
        when(muleEvent.getMuleContext()).thenReturn(mockMuleContext);
        when(mockMuleContext.getRegistry()).thenReturn(muleRegistry);
        when(muleRegistry.lookupObject(MuleProperties.OBJECT_MULE_ENDPOINT_FACTORY)).thenReturn(endpointManager);
        when(muleRegistry.lookupObject(MockedMessageProcessorManager.ID)).thenReturn(processorManager);
    }

    /**
     * If everything runs ok, then the before, after and test must be called
     */
    @org.junit.Test
    public void testRunSuccessful() throws Exception {
        MunitTest test = new MockedTest(buildList(before), testFlow, buildList(after), handler);

        TestResult testResult = test.doRun();

        verify(testFlow, times(1)).process(muleEvent);
        verify(before, times(1)).process(muleEvent);
        verify(after, times(1)).process(muleEvent);

        assertTrue(testResult.hasSucceeded());
    }

    /**
     * If Test has a failure, add it to the result
     */
    @org.junit.Test
    public void testRunWithFailure() throws Exception {
        MunitTest test = new MockedTest(buildList(before), testFlow, buildList(after), handler);

        when(testFlow.process(muleEvent)).thenThrow(new AssertionError("Error"));
        TestResult testResult = test.doRun();

        verify(testFlow, times(1)).process(muleEvent);
        verify(before, times(1)).process(muleEvent);
        verify(after, times(1)).process(muleEvent);

        assertFalse(testResult.hasSucceeded());
        assertEquals("Error", testResult.getFailure().getShortMessage());
    }

    /**
     * If Test has an error, add it to the result
     */
    @org.junit.Test
    public void testRunWithError() throws Exception {
        MunitTest test = new MockedTest(buildList(before), testFlow, buildList(after), handler);

        when(processorManager.getCalls()).thenReturn(createTestCalls());
        when(testFlow.process(muleEvent)).thenThrow(new DefaultMuleException("Error"));

        TestResult testResult = test.doRun();

        verify(testFlow, times(1)).process(muleEvent);
        verify(before, times(1)).process(muleEvent);
        verify(after, times(1)).process(muleEvent);

        assertFalse(testResult.hasSucceeded());
        assertEquals("Error", testResult.getError().getShortMessage());
        assertTrue(testResult.getError().getFullMessage().contains("namespace2"));
        assertTrue(testResult.getError().getFullMessage().contains("mp2"));
        assertTrue(testResult.getError().getFullMessage().contains("namespace1"));
        assertTrue(testResult.getError().getFullMessage().contains("mp1"));
    }

    @org.junit.Test
    public void whenAnExpectedExceptionIsThrownItShouldSucceed() throws Exception {
        final MunitTest test = new MockedTest(Collections.<MunitFlow>emptyList(), testFlow, Collections.<MunitFlow>emptyList(), handler);
        when(testFlow.process(Matchers.<MuleEvent>anyObject())).thenThrow(new DefaultMuleException(new IllegalArgumentException()));
        when(testFlow.expectException(Matchers.<Exception>anyObject(), Matchers.<MuleEvent>anyObject())).thenReturn(true);

        TestResult testResult = test.doRun();

        assertTrue(testResult.hasSucceeded());
    }

    @org.junit.Test
    public void whenAnExpetionThatDoesNotMatchIsThrownItShouldFail() throws Exception {
        final MunitTest test = new MockedTest(Collections.<MunitFlow>emptyList(), testFlow, Collections.<MunitFlow>emptyList(), handler);
        when(testFlow.process(Matchers.<MuleEvent>anyObject())).thenThrow(new DefaultMuleException(new IllegalArgumentException()));
        when(testFlow.expectException(Matchers.<Exception>anyObject(), Matchers.<MuleEvent>anyObject())).thenReturn(false);

        TestResult testResult = test.doRun();

        assertFalse(testResult.hasSucceeded());
    }

    @org.junit.Test
    public void whenAnExpetionIsExpectedButNothingIsThrownItShouldFail() throws Exception {
        final MunitTest test = new MockedTest(Collections.<MunitFlow>emptyList(), testFlow, Collections.<MunitFlow>emptyList(), handler);
        testFlow.setExpectExceptionThatSatisfies("something");
        when(testFlow.getExpectExceptionThatSatisfies()).thenReturn("something");
        when(testFlow.expectException(Matchers.<Exception>anyObject(), Matchers.<MuleEvent>anyObject())).thenReturn(false);

//         TestResult testResult = test.run();
        TestResult testResult = test.doRun();

        assertFalse(testResult.hasSucceeded());
        assertTrue(testResult.getFailure().getFullMessage().contains("something"));
    }

    private ArrayList<MunitMessageProcessorCall> createTestCalls() {
        ArrayList<MunitMessageProcessorCall> calls = new ArrayList<MunitMessageProcessorCall>();
        calls.add(new MunitMessageProcessorCall(new MessageProcessorId("mp1", "namespace1")));
        calls.add(new MunitMessageProcessorCall(new MessageProcessorId("mp2", "namespace2")));

        return calls;
    }


    private List<MunitFlow> buildList(MunitFlow... flows) {
        return Arrays.asList(flows);
    }

    private class MockedTest extends MunitTest {

        public MockedTest(List<MunitFlow> before, MunitTestFlow test, List<MunitFlow> after, TestOutputHandler outputHandler) {
            super(before, test, after, outputHandler, mockMuleContext);
        }

        @Override
        protected MuleEvent muleEvent() {
            return muleEvent;
        }
    }
}
