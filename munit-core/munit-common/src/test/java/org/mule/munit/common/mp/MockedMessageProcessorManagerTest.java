/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.munit.common.mp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import org.mule.DefaultMuleMessage;
import org.mule.api.MuleMessage;
import org.mule.modules.interceptor.matchers.EqMatcher;
import org.mule.modules.interceptor.processors.MessageProcessorBehavior;
import org.mule.modules.interceptor.processors.MessageProcessorCall;
import org.mule.modules.interceptor.processors.MessageProcessorId;
import org.mule.munit.common.mocking.CopyMessageTransformer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Mulesoft Inc.
 * @since 3.3.2
 */
public class MockedMessageProcessorManagerTest
{

    public static final String TEST_NAMESPACE = "testNamespace";
    public static final String TEST_NAME = "testName";
    public static final MessageProcessorId MESSAGE_PROCESSOR_ID = new MessageProcessorId(TEST_NAME, TEST_NAMESPACE);

    private MuleMessage muleMessage;

    @Before
    public void setUp()
    {
        muleMessage = mock(DefaultMuleMessage.class);
    }

    @Test
    public void getCallsWithEmptyMatchers()
    {
        MockedMessageProcessorManager manager = new MockedMessageProcessorManager();
        manager.addCall(createCall());

        List<MessageProcessorCall> callsFor = manager.findCallsFor(MESSAGE_PROCESSOR_ID, new HashMap<String, Object>());

        assertFalse(callsFor.isEmpty());
        assertEquals(MESSAGE_PROCESSOR_ID, callsFor.get(0).getMessageProcessorId());
    }

    @Test
    public void getCallsWithEmptyNonMatchers()
    {
        MockedMessageProcessorManager manager = new MockedMessageProcessorManager();
        manager.addCall(createCall());

        HashMap<String, Object> attributesMatchers = new HashMap<String, Object>();
        attributesMatchers.put("attr", new EqMatcher("attrValue"));
        List<MessageProcessorCall> callsFor = manager.findCallsFor(MESSAGE_PROCESSOR_ID, attributesMatchers);

        assertFalse(callsFor.isEmpty());
        assertEquals(MESSAGE_PROCESSOR_ID, callsFor.get(0).getMessageProcessorId());
    }

    @Test
    public void getCallsWithEmptyInvalidMatchers()
    {
        MockedMessageProcessorManager manager = new MockedMessageProcessorManager();
        manager.addCall(createCall());

        HashMap<String, Object> attributesMatchers = new HashMap<String, Object>();
        attributesMatchers.put("attr", new EqMatcher("attrValu"));
        List<MessageProcessorCall> callsFor = manager.findCallsFor(MESSAGE_PROCESSOR_ID, attributesMatchers);

        assertTrue(callsFor.isEmpty());
    }


    @Test
    public void getCallsWithEmptyInvalidId()
    {
        MockedMessageProcessorManager manager = new MockedMessageProcessorManager();
        manager.addCall(createCall());

        List<MessageProcessorCall> callsFor = manager.findCallsFor(new MessageProcessorId("another", "another"), new HashMap<String, Object>());

        assertTrue(callsFor.isEmpty());
    }

    @Test
    public void validThatResetRemovesAll()
    {
        MockedMessageProcessorManager manager = new MockedMessageProcessorManager();
        manager.addCall(createCall());
        manager.addBehavior(new MessageProcessorBehavior(createCall(), new CopyMessageTransformer((DefaultMuleMessage) muleMessage)));
        manager.addBeforeCallSpyAssertion(new SpyAssertion(null, null));
        manager.addAfterCallSpyAssertion(new SpyAssertion(null, null));

        manager.reset();

        assertTrue(manager.beforeCallSpyAssertions.isEmpty());
        assertTrue(manager.afterCallSpyAssertions.isEmpty());
        assertTrue(manager.calls.isEmpty());
    }

    @Test
    public void getTheBestMatchingBehavior()
    {
        MockedMessageProcessorManager manager = new MockedMessageProcessorManager();
        MessageProcessorCall bestMatchingCall = createCall();
        Map<String, Object> attributes = bestMatchingCall.getAttributes();
        attributes.put("attr2", "attrValue2");

        manager.addBehavior(new MessageProcessorBehavior(createCall(), new CopyMessageTransformer((DefaultMuleMessage) muleMessage)));
        manager.addBehavior(new MessageProcessorBehavior(bestMatchingCall, new CopyMessageTransformer((DefaultMuleMessage) muleMessage)));

        MessageProcessorBehavior matched = manager.getBetterMatchingBehavior(bestMatchingCall);

        assertEquals(bestMatchingCall, matched.getMessageProcessorCall());

    }


    private MunitMessageProcessorCall createCall()
    {
        MunitMessageProcessorCall call = new MunitMessageProcessorCall(MESSAGE_PROCESSOR_ID);
        HashMap<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("attr", "attrValue");
        call.setAttributes(attributes);
        return call;
    }
}
