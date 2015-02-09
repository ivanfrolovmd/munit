/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.munit.runner.mule;

import org.apache.commons.lang.StringUtils;
import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.munit.common.MunitCore;
import org.mule.munit.config.MunitFlow;
import org.mule.munit.config.MunitTestFlow;
import org.mule.munit.runner.AbstractMunitTest;
import org.mule.munit.runner.mule.result.TestResult;
import org.mule.munit.runner.mule.result.notification.Notification;
import org.mule.munit.runner.output.TestOutputHandler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import static org.mule.munit.common.MunitCore.buildMuleStackTrace;

/**
 * <p>MUnit Test</p>
 *
 * @author Mulesoft Inc.
 * @since 3.3.2
 */
public class MunitTest extends AbstractMunitTest {

    public MunitTest(List<MunitFlow> before,
                     MunitTestFlow testFlow,
                     List<MunitFlow> after,
                     TestOutputHandler outputHandler, MuleContext muleContext) {
        this.before = before;
        this.after = after;
        this.testFlow = testFlow;
        this.outputHandler = outputHandler;
        this.muleContext = muleContext;
    }

    private Notification buildNotifcationFrom(Throwable t) {
        return new Notification(t.getMessage(), stack2string(t));
    }

    private static String stack2string(Throwable e) {
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            return sw.toString();
        } catch (Exception e2) {
            return "";
        }
    }

    private void runAfter(TestResult result, MuleEvent event) {
        try {
            processFlows(event, after);
        } catch (MuleException e) {
            result.setError(buildNotifcationFrom(e));
        }
    }


    public TestResult doRun() throws Exception {
        TestResult result = new TestResult(getName());
        if (!shouldRun()) {
            this.setStatus(IGNORE_STATUS);
            result.setSkipped(true);
            return result;
        }

        long start = System.currentTimeMillis();
        MuleEvent event = muleEvent();

        try {
            processFlows(event, before);

            result.setSkipped(testFlow.isIgnore());

            showDescription();
            testFlow.process(event);
            if (StringUtils.isNotBlank(testFlow.getExpectExceptionThatSatisfies())) {
                fail("Exception matching '" + testFlow.getExpectExceptionThatSatisfies() + "', but wasn't thrown");
            }
            this.setStatus(SUCCESS_STATUS);
        } catch (final AssertionError t) {
            this.setStatus(FAIL_STATUS);
            result.setFailure(buildNotifcationFrom(t));
        } catch (final MuleException e) {
            try {
                if (!testFlow.expectException(e, event)) {
                    e.setStackTrace(buildMuleStackTrace(event.getMuleContext())
                            .toArray(new StackTraceElement[]{}));
                    result.setError(buildNotifcationFrom(e));
                }
            } catch (final AssertionError t) {
                t.setStackTrace(buildMuleStackTrace(event.getMuleContext())
                        .toArray(new StackTraceElement[]{}));
                result.setFailure(buildNotifcationFrom(t));
            }
        } finally {
            MunitCore.reset(event.getMuleContext());
            runAfter(result, event);
        }

        long end = System.currentTimeMillis();
        result.setTime(new Float(end - start) / 1000);
        return result;

    }


}
