/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.munit.runner.mule;

import org.mule.api.MuleContext;
import org.mule.munit.config.MunitFlow;
import org.mule.munit.config.MunitTestFlow;
import org.mule.munit.runner.SuiteBuilder;
import org.mule.munit.runner.output.TestOutputHandler;
import org.mule.munit.runner.utils.MunitTestDependencyBuilder;

import java.util.List;


/**
 * <p>Creates a Munit Suite and its tests</p>
 *
 * @author Mulesoft Inc.
 * @since 3.3.2
 */
public class MunitSuiteBuilder extends SuiteBuilder<MunitSuite, MunitTest> {

    private TestOutputHandler handler;


    public MunitSuiteBuilder(MuleContext muleContext,
                             TestOutputHandler handler) {
        super(muleContext);

        if (handler == null) {
            throw new IllegalArgumentException("Handler must not be null");
        }

        this.handler = handler;
    }

    /**
     * @see SuiteBuilder
     */
    @Override
    protected MunitSuite createSuite(String name) {
        MunitSuite suite = new MunitSuite(name);
        for (MunitTest test : this.tests) {
            suite.add(test);
        }

        return suite;
    }

    /**
     * @see SuiteBuilder
     */
    @Override
    protected MunitTest test(List<MunitFlow> beforeTest, MunitTestFlow test, List<MunitFlow> afterTest) {
        return new MunitTest(beforeTest, test, afterTest, handler, muleContext);
    }

    protected void buildDependencies() {
        MunitTestDependencyBuilder dependencyBuilder = new MunitTestDependencyBuilder(tests);
        dependencyBuilder.build();
    }
}
