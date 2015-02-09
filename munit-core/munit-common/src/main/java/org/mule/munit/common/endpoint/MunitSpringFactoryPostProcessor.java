/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.munit.common.endpoint;


import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


/**
 * <p>
 * This class changes the endpoint factory and inject the mock manager
 * </p>
 * <p/>
 * <p>
 * This is a piece part of the endpoint mocking. By overriding the endpoint factory we can mock all the outbound/inbound
 * endpoints of a mule application
 * </p>
 *
 * @author Mulesoft Inc.
 * @since 3.3.2
 */
public class MunitSpringFactoryPostProcessor {

    private static Logger logger = Logger.getLogger("Bean definition Processor");

    /**
     * <p>
     * Defines if the inbounds must be mocked or not. This is pure Munit configuration
     * </p>
     */
    protected boolean mockInbounds = true;

    /**
     * <p>
     * Defines if the app connectors for outbound/inbound endpoints have to be mocked. If they are then all
     * outbound endpoints/inbound endpoints must be mocked.
     * </p>
     */
    protected boolean mockConnectors = true;

    /**
     * <p>
     * List of flows which we don't want to mock the inbound message sources
     * </p>
     */
    protected List<String> mockingExcludedFlows = new ArrayList<String>();


    public void setMockInbounds(boolean mockInbounds) {
        this.mockInbounds = mockInbounds;
    }

    public void setMockingExcludedFlows(List<String> mockingExcludedFlows) {
        this.mockingExcludedFlows = mockingExcludedFlows;
    }

    public boolean isMockInbounds() {
        return mockInbounds;
    }

    public boolean isMockConnectors() {
        return mockConnectors;
    }

    public void setMockConnectors(boolean mockConnectors) {
        this.mockConnectors = mockConnectors;
    }

    protected String groups = "";

    public String getGroups() {
        return  groups;
    }
    
    public List<String> getGroupList() {
        List<String> groupList = new ArrayList<String>();
        String[] g = groups.split(",");
        for (String group : g) {
            groupList.add(group);
        }
//        return groups;
        return groupList;
    }

    public void setGroups(String groups) {
        this.groups = groups;
    }

}
