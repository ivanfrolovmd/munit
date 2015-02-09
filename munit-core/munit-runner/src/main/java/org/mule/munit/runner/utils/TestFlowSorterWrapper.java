package org.mule.munit.runner.utils;


import org.apache.commons.lang.StringUtils;
import org.mule.munit.config.MunitTestFlow;

import java.util.ArrayList;
import java.util.List;

public class TestFlowSorterWrapper {

    private final MunitTestFlow test;
    private List<String> dependsOn = new ArrayList<String>();

    private boolean tempMar = false;
    private boolean permMark = false;

    TestFlowSorterWrapper(MunitTestFlow test) {
        this.test = test;
        this.buildDependsOnList();
    }

    public String getName() {
        return test.getName();
    }

    public List<String> getDependsOn() {
        return dependsOn;
    }

    public boolean isTempMar() {
        return tempMar;
    }

    public void setTempMar(boolean tempMar) {
        this.tempMar = tempMar;
    }

    public boolean isPermMark() {
        return permMark;
    }

    public void setPermMark(boolean permMark) {
        this.permMark = permMark;
    }

    public MunitTestFlow getTest() {
        return test;
    }

    private void buildDependsOnList() {
        if (StringUtils.isNotBlank(test.getDependsOn())) {
            for (String testName : test.getDependsOn().split(",")) {
                dependsOn.add(testName);
            }
        }
    }

}
