/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.munit.runner.java;

import junit.framework.TestCase;
import org.mule.api.MuleEvent;
import org.mule.munit.common.MunitCore;
import org.mule.munit.config.MunitFlow;
import org.mule.munit.config.MunitTestFlow;
import org.mule.munit.runner.AbstractMunitTest;
import org.mule.munit.runner.mule.result.TestResult;
import org.mule.munit.runner.output.DefaultOutputHandler;

import java.util.List;

import static org.mule.munit.common.MunitCore.buildMuleStackTrace;


/**
 * <p>
 * This is the java representation of a Munit test written with mule code.
 * </p>
 * <p>
 * The Munit tests can be converted into Junit tests by extending the class {@link AbstractMuleSuite}. When user do
 * that instead of creating a {@link org.mule.munit.runner.mule.MunitTest} Munit core creates this class which
 * extends from {@link TestCase}
 * </p>
 *
 * @author Mulesoft Inc.
 * @since 3.3.2
 */
public class MunitTest extends AbstractMunitTest {

    public MunitTest(List<MunitFlow> before, MunitTestFlow testFlow, List<MunitFlow> after) {
        this.before = before;
        this.testFlow = testFlow;
        this.after = after;
        this.outputHandler = new DefaultOutputHandler();
        this.muleContext = testFlow.getMuleContext();
    }

    /**
     * <p>
     * Runs the munit flow and handles the result. In case of failure it changes the java stack trace to the mule
     * stack trace.
     * </p>
     *
     * @throws java.lang.Exception
     */
    @Override
    public TestResult doRun() throws Exception {
        if (!shouldRun()) {
            this.setStatus(IGNORE_STATUS);
            return null;
        }

        MuleEvent event = muleEvent();
        processFlows(event, before);

        showDescription();

        try {
            testFlow.process(event);
            this.setStatus(SUCCESS_STATUS);
        } catch (Exception t) {
            if (!testFlow.expectException(t, event)) {
                t.setStackTrace(buildMuleStackTrace(event.getMuleContext()).toArray(new StackTraceElement[]{}));
                this.setStatus(FAIL_STATUS);
                throw t;
            }

        } finally {
            MunitCore.reset(muleContext);
            processFlows(event, after);
        }

        return null;
    }


}

