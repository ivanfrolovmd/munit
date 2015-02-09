package org.mule.munit.runner;

import junit.framework.TestCase;
import org.apache.commons.lang.StringUtils;
import org.mule.DefaultMuleEvent;
import org.mule.DefaultMuleMessage;
import org.mule.MessageExchangePattern;
import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.munit.config.MunitFlow;
import org.mule.munit.config.MunitTestFlow;
import org.mule.munit.runner.mule.result.TestResult;
import org.mule.munit.runner.output.TestOutputHandler;
import org.mule.tck.MuleTestUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public abstract class AbstractMunitTest <T extends  AbstractMunitTest> extends TestCase {

    protected static final String FAIL_STATUS = "FAIL";
    protected static final String IGNORE_STATUS = "IGNORE";
    protected static final String SUCCESS_STATUS = "SUCCESS";

    /**
     * <p>The MUnit flows that have to be run before the MUnit test.</p>
     */
    protected List<MunitFlow> before;

    /**
     * <p>The MUnit flows that have to be run after the MUnit test.</p>
     */
    protected List<MunitFlow> after;

    /**
     * <p>The MUnit test.</p>
     */
    protected MunitTestFlow testFlow;

    /**
     * The mule context, used to access mule configuration/registry
     */
    protected MuleContext muleContext;

    /**
     * <p>The Output handler to be use.</p>
     */
    protected TestOutputHandler outputHandler;


    /**
     * The status of the test, just descriptive
     */
    protected String status = "";

    /**
     * The list of test of which this test depends on
     */
    protected List<T> testDependencies;

    @Override
    public int countTestCases() {
        return 1;
    }

    @Override
    protected void runTest() throws Throwable {
        doRun();
    }

    public abstract TestResult doRun() throws Exception;


    public String getName() {
        return testFlow.getName();
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getDependencyNames() {
        List<String> dependsOn = new ArrayList<String>();

        for (String testName : testFlow.getDependsOn().split(",")) {
            if (StringUtils.isNotBlank(testName)) {
                dependsOn.add(testName);
            }
        }
        return dependsOn;
    }

    public List<T> getDependencies() {
        return testDependencies;
    }

    public void setDependencies(List<T> dependencies) {
        this.testDependencies = dependencies;
    }

    protected boolean shouldRun() {
        if (testFlow.isIgnore()) {
            return false;
        }

        if (null != testDependencies) {
            for (AbstractMunitTest t : testDependencies) {
                if (!SUCCESS_STATUS.equals(t.getStatus())) {
                    testFlow.setIgnore(true);
                    return false;
                }
            }
        }

        return true;
    }


    protected void showDescription() {
        outputHandler.printDescription(testFlow.getName(), testFlow.getDescription());
    }

    protected MuleEvent muleEvent() {
        try {
            return new DefaultMuleEvent(new DefaultMuleMessage(null, muleContext), MessageExchangePattern.REQUEST_RESPONSE, MuleTestUtils.getTestFlow(muleContext));
        } catch (Exception e) {
            return null;
        }
    }

    protected void processFlows(MuleEvent event, List<MunitFlow> flows) throws MuleException {
        if (flows != null) {
            for (MunitFlow flow : flows) {
                outputHandler.printDescription(flow.getName(), flow.getDescription());
                flow.process(event);
            }
        }
    }
}
