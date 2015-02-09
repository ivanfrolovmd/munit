/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.munit.runner.mule;

import org.apache.commons.lang.StringUtils;
import org.mule.api.MuleContext;
import org.mule.api.registry.RegistrationException;
import org.mule.munit.AssertModule;
import org.mule.munit.runner.MuleContextManager;
import org.mule.munit.runner.MunitRunner;
import org.mule.munit.runner.mule.result.SuiteResult;
import org.mule.munit.runner.mule.result.notification.NotificationListener;
import org.mule.munit.runner.output.DefaultOutputHandler;
import org.mule.munit.runner.output.TestOutputHandler;


/**
 * <p>
 * The Munit test runner
 * </p>
 *
 * @author Mulesoft Inc.
 * @since 3.3.2
 */
public class MunitSuiteRunner {

    private MuleContext muleContext;
    private MunitSuite suite;
    private TestOutputHandler handler = new DefaultOutputHandler();
    private MuleContextManager muleContextManager = new MuleContextManager(null);
    
    private String groups;

    public String getGroups() {
        return groups;
    }

    public void setGroups(String groups) {
        this.groups = groups;
    }

    public MunitSuiteRunner(String resources) {

        try {
            muleContext = muleContextManager.startMule(resources);

            suite = new MunitSuiteBuilder(muleContext, handler).build(resources);

        } catch (Exception e) {
            muleContextManager.killMule(muleContext);
            throw new RuntimeException(e);
        }

    }

    public SuiteResult run() {

        if (!belongToGroups(groups)) {
            SuiteResult result = new SuiteResult(suite.getName() + " - SKIPED!!!");
            return result;
        }

        return new MunitRunner<SuiteResult>(handler, muleContextManager, muleContext) {

            @Override
            protected SuiteResult runSuite() throws Exception {
                return suite.run();
            }

            @Override
            protected String getSuiteName() {
                return suite.getName();
            }
        }.run();
    }

    public void setNotificationListener(NotificationListener notificationListener) {
        this.suite.setNotificationListener(notificationListener);
    }


    public int getNumberOfTests() {
        return suite.getNumberOfTests();
    }

    /**
     * *
     *
     * @param groups
     * @return true if groups is empty or if groups matches the definitions
     */
    private boolean belongToGroups(String groups) {
        
        AssertModule munitSuiteConfiguration = null;
        try {
            munitSuiteConfiguration = muleContext.getRegistry().lookupObject(AssertModule.class);
        } catch (RegistrationException e) {
            // TODO FIX THIS
            e.printStackTrace();
        }

        if (StringUtils.isBlank(groups)) {
            return true;
        }

        String[] runningGroups = groups.split(",");
        for (String runningGroup : runningGroups) {
            if (munitSuiteConfiguration.getGroupList().contains(runningGroup)) {
                return true;
            }
        }
        return false;
    }

}
