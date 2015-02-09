/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.munit.runner.java;


import junit.framework.TestSuite;
import org.mule.api.MuleContext;
import org.mule.munit.config.MunitFlow;
import org.mule.munit.config.MunitTestFlow;
import org.mule.munit.runner.SuiteBuilder;
import org.mule.munit.runner.utils.MunitTestDependencyBuilder;

import java.util.List;

/**
 * <p>
 * Creates a Munit Suite and its tests to be run with Junit framework
 * </p>
 *
 * @author Mulesoft Inc.
 * @since 3.3.2
 */
public class JunitTestSuiteBuilder extends SuiteBuilder<TestSuite, MunitTest> {


    protected JunitTestSuiteBuilder(MuleContext muleContext) {
        super(muleContext);
    }

    @Override
    protected TestSuite createSuite(String name) {
        TestSuite testSuite = new TestSuite(name);
        for (MunitTest test : tests) {
            testSuite.addTest(test);
        }
        return testSuite;
    }

    @Override
    protected MunitTest test(List<MunitFlow> beforeTest, MunitTestFlow test, List<MunitFlow> afterTest) {
        return new MunitTest(beforeTest, test, afterTest);
    }

    protected void buildDependencies() {
        MunitTestDependencyBuilder dependencyBuilder = new MunitTestDependencyBuilder(tests);
        dependencyBuilder.build();
    }

}
