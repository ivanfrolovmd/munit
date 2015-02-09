package org.mule.munit.runner.utils;

import org.apache.commons.lang.StringUtils;
import org.mule.munit.config.MunitTestFlow;

import java.util.*;

public class MunitTestFlowSorter {

    private boolean ignoreDependencies = true;
    private List<TestFlowSorterWrapper> unorderedTest;


    public MunitTestFlowSorter(List<MunitTestFlow> tests) {
        this.unorderedTest = wrapTests(tests);
        this.validateDependencies();
    }

    public List<MunitTestFlow> sort() {
        if (ignoreDependencies) {
            return unwrapTests(unorderedTest);
        }
        return unwrapTests(orderTestList(this.unorderedTest));
    }

    private List<TestFlowSorterWrapper> wrapTests(List<MunitTestFlow> tests) {
        List<TestFlowSorterWrapper> wrappedTests = new ArrayList<TestFlowSorterWrapper>();
        for (MunitTestFlow test : tests) {
            wrappedTests.add(new TestFlowSorterWrapper(test));
        }
        return wrappedTests;
    }

    private List<MunitTestFlow> unwrapTests(List<TestFlowSorterWrapper> orderedTests) {
        List<MunitTestFlow> unwrappedTests = new ArrayList<MunitTestFlow>();
        for (TestFlowSorterWrapper test : orderedTests) {
            unwrappedTests.add(test.getTest());
        }
        return unwrappedTests;
    }

    private void validateDependencies() {
        Map<String, List<String>> testAndDependencies = new HashMap<String, List<String>>();

        for (TestFlowSorterWrapper test : unorderedTest) {
            testAndDependencies.put(test.getName(), test.getDependsOn());

            if (!test.getDependsOn().isEmpty()) {
                ignoreDependencies = false;
            }
        }

        for (String testName : testAndDependencies.keySet()) {
            for (String dependency : testAndDependencies.get(testName)) {
                if (StringUtils.isNotBlank(dependency) && !testAndDependencies.containsKey(dependency)) {
                    throw new RuntimeException("The test: " + testName + " depends on an non existing test - " + dependency);
                }
            }
        }

    }

    private List<TestFlowSorterWrapper> orderTestList(List<TestFlowSorterWrapper> tests) {
        Stack<TestFlowSorterWrapper> orderedStackTests = new Stack<TestFlowSorterWrapper>();

        List<TestFlowSorterWrapper> unmarkedTest = new ArrayList<TestFlowSorterWrapper>(tests);
        for (TestFlowSorterWrapper t : unmarkedTest) {
            visit(t, unmarkedTest, orderedStackTests);
        }

        return reverseStack(orderedStackTests);
    }

    private void visit(TestFlowSorterWrapper test, List<TestFlowSorterWrapper> tests, Stack<TestFlowSorterWrapper> orderedTests) {
        if (test.isTempMar()) {
            throw new RuntimeException("There are cycles between the tests detected at: " + test.getName());
        }

        if (!test.isTempMar() && !test.isPermMark()) {
            test.setTempMar(true);

            for (TestFlowSorterWrapper t : tests) {
                if (t.getDependsOn().contains(test.getName())) {
                    visit(t, tests, orderedTests);
                }
            }
            test.setPermMark(true);
            test.setTempMar(false);

            orderedTests.push(test);
        }
    }


    private List<TestFlowSorterWrapper> reverseStack(Stack<TestFlowSorterWrapper> orderedStackTests) {
        List<TestFlowSorterWrapper> orderedTests = new ArrayList<TestFlowSorterWrapper>();
        ListIterator<TestFlowSorterWrapper> li = orderedStackTests.listIterator(orderedStackTests.size());
        while (li.hasPrevious()) {
            orderedTests.add(li.previous());
        }

        return orderedTests;
    }

}
