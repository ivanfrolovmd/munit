package org.mule.munit.runner.utils;

import org.mule.munit.runner.AbstractMunitTest;

import java.util.ArrayList;
import java.util.List;

public class MunitTestDependencyBuilder<T extends AbstractMunitTest> {

    private List<T> tests;

    public MunitTestDependencyBuilder(List<T> tests) {
        this.tests = tests;
    }

    public void build() {
        for (T test : tests) {
            test.setDependencies(buildDependencies(test));
        }

    }


    private List<T> buildDependencies(T test) {
        List<T> depedencies = new ArrayList<T>();

        List<String> dependencyNames = test.getDependencyNames();
        for (String name : dependencyNames) {
            depedencies.add(findTestByName(name));
        }

        return depedencies;
    }

    private T findTestByName(String name) {
        for (T t : tests) {
            if (t.getName().equals(name)) {
                return t;
            }
        }
        throw new RuntimeException("The test " + name + " does not belong to this test suite");
    }
}
