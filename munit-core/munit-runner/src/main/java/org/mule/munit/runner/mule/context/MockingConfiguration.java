/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.munit.runner.mule.context;

import java.util.List;
import java.util.Properties;

/**
 * <p>
 * The configuration of the mocking elements for Munit
 * </p>
 *
 * @author Mulesoft Inc.
 * @since 3.3.2
 */
public class MockingConfiguration {

    private boolean mockInbounds;
    private List<String> mockingExcludedFlows;
    private boolean mockConnectors;
    private Properties startUpProperties;
    private String groups;

//    public MockingConfiguration(boolean mockInbounds, List<String> mockingExcludedFlows, boolean mockConnectors, Properties startUpProperties)
//    {
//        this.mockInbounds = mockInbounds;
//        this.mockingExcludedFlows = mockingExcludedFlows;
//        this.mockConnectors = mockConnectors;
//        this.startUpProperties = startUpProperties;
//    }

    public MockingConfiguration(boolean mockInbounds, List<String> mockingExcludedFlows, boolean mockConnectors, Properties startUpProperties) {
        this(mockInbounds, mockingExcludedFlows, mockConnectors, startUpProperties, "");

    }

    public MockingConfiguration(boolean mockInbounds, List<String> mockingExcludedFlows, boolean mockConnectors, Properties startUpProperties, String groups) {
        this.mockInbounds = mockInbounds;
        this.mockingExcludedFlows = mockingExcludedFlows;
        this.mockConnectors = mockConnectors;
        this.startUpProperties = startUpProperties;
        this.groups = groups;
    }

    public List<String> getMockingExcludedFlows() {
        return mockingExcludedFlows;
    }

    public boolean isMockInbounds() {
        return mockInbounds;
    }

    public boolean isMockConnectors() {
        return mockConnectors;
    }

    public Properties getStartUpProperties() {
        return startUpProperties;
    }

    public String getGroups() {
        return groups;
    }
}
