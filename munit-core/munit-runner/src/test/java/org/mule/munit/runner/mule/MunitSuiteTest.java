/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.munit.runner.mule;


import org.mule.munit.runner.mule.result.SuiteResult;
import org.mule.munit.runner.mule.result.TestResult;
import org.mule.munit.runner.mule.result.notification.NotificationListener;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class MunitSuiteTest {


    /**
     * A suite must have the same tests that were added
     */
    @org.junit.Test
    public void mustHaveTheSameTestThatWereAdded() {
        MunitTest test1 = mock(MunitTest.class);
        MunitTest test2 = mock(MunitTest.class);

        MunitSuite suite = new MunitSuite("testSuite");
        suite.add(test1);
        suite.add(test2);

        assertEquals(2, suite.getNumberOfTests());
    }


    /**
     * When run a suite must call the notification listener and execute the tests
     */
    @org.junit.Test
    public void callNotificationAndExecuteTestsWhenRun() throws Exception {
        MunitTest test = mock(MunitTest.class);
        NotificationListener listener = mock(NotificationListener.class);

        TestResult testResult = new TestResult("testResult");

        when(test.doRun()).thenReturn(testResult);

        MunitSuite suite = new MunitSuite("testSuite");
        suite.add(test);
        suite.setNotificationListener(listener);

        SuiteResult suiteResult = suite.run();

        verify(listener).notify(testResult);
        verify(listener).notifyStartOf(test);

        assertEquals("testSuite", suiteResult.getTestName());
    }

}
