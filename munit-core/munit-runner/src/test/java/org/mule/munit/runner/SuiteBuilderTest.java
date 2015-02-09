/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.munit.runner;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mule.api.MuleContext;
import org.mule.api.registry.MuleRegistry;
import org.mule.api.registry.RegistrationException;
import org.mule.munit.config.MunitAfterTest;
import org.mule.munit.config.MunitBeforeTest;
import org.mule.munit.config.MunitFlow;
import org.mule.munit.config.MunitTestFlow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class SuiteBuilderTest
{
    private MuleContext muleContext;
    private MuleRegistry registry;
    private List<MunitAfterTest> afterTestFlows = new ArrayList<MunitAfterTest>();
    private List<MunitBeforeTest> beforeTestFlows = new ArrayList<MunitBeforeTest>();
    private MunitTestFlow munitTest;

    @Before
    public void setUp()
    {
        muleContext = mock(MuleContext.class);
        registry = mock(MuleRegistry.class);
        munitTest = mock(MunitTestFlow.class);

        when(muleContext.getRegistry()).thenReturn(registry);
    }

    /**
     * Checks that all the non Ignored Tests are in the suite
     */
    @Test
    public void beforeAndAfterFlowsMustBeRetrieved() throws RegistrationException
    {
        MockSuiteBuilder builder = new MockSuiteBuilder(muleContext, true);
        runTest(builder);
    }


    private void runTest(MockSuiteBuilder builder)
    {
        when(registry.lookupObjects(MunitBeforeTest.class)).thenReturn(beforeTestFlows);
        when(registry.lookupObjects(MunitAfterTest.class)).thenReturn(afterTestFlows);
        when(registry.lookupObjects(MunitTestFlow.class)).thenReturn(Arrays.asList(new MunitTestFlow[] {munitTest}));

        builder.build("test");

        verify(registry, times(1)).lookupObjects(MunitBeforeTest.class);
        verify(registry, times(1)).lookupObjects(MunitAfterTest.class);
        verify(registry, times(1)).lookupObjects(MunitTestFlow.class);
    }

    private class MockSuiteBuilder extends SuiteBuilder<MockSuite, MockTest>
    {

        private boolean mustNotHaveTests;

        protected MockSuiteBuilder(MuleContext muleContext, boolean mustHaveTests)
        {
            super(muleContext);
            this.mustNotHaveTests = mustHaveTests;
        }

        @Override
        protected MockSuite createSuite(String name)
        {
            if (mustNotHaveTests)
            {
                assertFalse(this.tests.isEmpty());
                return new MockSuite();
            }
            assertTrue(this.tests.isEmpty());
            return new MockSuite();
        }

        @Override
        protected MockTest test(List<MunitFlow> beforeTest, MunitTestFlow test, List<MunitFlow> afterTest)
        {
            assertEquals(afterTestFlows, afterTest);
            assertEquals(beforeTestFlows, beforeTest);
            assertEquals(munitTest, test);
            return new MockTest();
        }

        //TODO fix this it sucks
        protected void buildDependencies() {
            
        }


    }

    private class MockSuite
    {

    }

    private class MockTest
    {

    }
}