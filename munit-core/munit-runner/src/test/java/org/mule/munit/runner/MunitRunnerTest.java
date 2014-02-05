/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.munit.runner;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mule.api.MuleContext;
import org.mule.api.MuleException;
import org.mule.api.registry.MuleRegistry;
import org.mule.munit.config.MunitAfterSuite;
import org.mule.munit.config.MunitBeforeSuite;
import org.mule.munit.config.MunitTestFlow;
import org.mule.munit.runner.output.TestOutputHandler;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class MunitRunnerTest
{
    private static final String AFTER_TEST_DESCRIPTION = "After Test Description";
    private static final String AFTER_TEST_NAME = "AfterTestName";

    private static final String BEFORE_TEST_DESCRIPTION = "Before Test Description";
    private static final String BEFORE_TEST_NAME = "BeforeTestName";


    private MuleContext muleContext;
    private MuleRegistry registry;
    private List<MunitAfterSuite> afterTestFlows;
    private List<MunitBeforeSuite> beforeTestFlows;
    private MunitTestFlow munitTest;
    private MunitAfterSuite afterTest;
    private MunitBeforeSuite beforeTest;

    private TestOutputHandler handler;

    private MuleContextManager manager;


    @Before
    public void setUp()
    {
        muleContext = mock(MuleContext.class);
        registry = mock(MuleRegistry.class);
        munitTest = mock(MunitTestFlow.class);
        manager = mock(MuleContextManager.class);
        afterTest = mock(MunitAfterSuite.class);
        beforeTest = mock(MunitBeforeSuite.class);
        afterTestFlows = Arrays.asList(new MunitAfterSuite[] {afterTest});
        beforeTestFlows = Arrays.asList(new MunitBeforeSuite[] {beforeTest});
        handler = mock(TestOutputHandler.class);

        when(muleContext.getRegistry()).thenReturn(registry);
    }

    /**
     * If it doesn't fail then check that everything is called
     */
    @Test
    public void killMuleMustBeCalled() throws MuleException
    {
        setBehavior();

        MunitRunner<MockSuiteResult> runner = new MunitRunner<MockSuiteResult>(handler, manager, muleContext)
        {

            @Override
            protected MockSuiteResult runSuite() throws Exception
            {
                return null;
            }

            @Override
            protected String getSuiteName()
            {
                return "testSuite";
            }
        };

        runner.run();

        verifyMocks();
    }


    /**
     * If fails after test and kill mule must be called
     */
    @Test(expected = RuntimeException.class)
    public void killMuleMustBeCalledWhenFailed() throws MuleException
    {
        setBehavior();

        MunitRunner<MockSuiteResult> runner = new MunitRunner<MockSuiteResult>(handler, manager, muleContext)
        {

            @Override
            protected MockSuiteResult runSuite() throws Exception
            {
                throw new Exception("error");
            }

            @Override
            protected String getSuiteName()
            {
                return "testSuite";
            }
        };

        runner.run();

        verifyMocks();
    }

    private void verifyMocks() throws MuleException
    {
        verify(handler, times(1)).printDescription(BEFORE_TEST_NAME, BEFORE_TEST_DESCRIPTION);
        verify(handler, times(1)).printDescription(AFTER_TEST_NAME, AFTER_TEST_DESCRIPTION);
        verify(afterTest, times(1)).process(null);
        verify(beforeTest, times(1)).process(null);
        verify(manager, times(1)).killMule(muleContext);
    }

    private void setBehavior()
    {
        when(registry.lookupObjects(MunitBeforeSuite.class)).thenReturn(beforeTestFlows);
        when(registry.lookupObjects(MunitAfterSuite.class)).thenReturn(afterTestFlows);
        when(registry.lookupObjects(MunitTestFlow.class)).thenReturn(Arrays.asList(new MunitTestFlow[] {munitTest}));

        when(afterTest.getDescription()).thenReturn(AFTER_TEST_DESCRIPTION);
        when(afterTest.getName()).thenReturn(AFTER_TEST_NAME);
        when(beforeTest.getDescription()).thenReturn(BEFORE_TEST_DESCRIPTION);
        when(beforeTest.getName()).thenReturn(BEFORE_TEST_NAME);
    }

    private class MockSuiteResult
    {

    }
}
