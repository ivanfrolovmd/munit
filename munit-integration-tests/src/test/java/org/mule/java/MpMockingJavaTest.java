/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.java;

import static junit.framework.Assert.assertEquals;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.modules.interceptor.processors.MuleMessageTransformer;
import org.mule.munit.common.mocking.SpyProcess;
import org.mule.munit.runner.functional.FunctionalMunitSuite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class MpMockingJavaTest extends FunctionalMunitSuite
{

    @Override
    protected String getConfigResources()
    {
        return "mule-config.xml";
    }

    @Test
    public void testMockMp() throws Exception
    {
        whenMessageProcessor("echo-component").thenReturn(muleMessageWithPayload("expectedPayload"));

        MuleEvent eventResult = runFlow("echoFlow", testEvent("anotherString"));

        assertEquals("expectedPayload", eventResult.getMessage().getPayload());

    }

    @Test
    public void testMockWithoutChangingPayload() throws Exception
    {
        whenMessageProcessor("create-group").ofNamespace("jira").thenReturnSameEvent();

        MuleEvent eventResult = runFlow("callingJira", testEvent(" Hello world!"));

        assertEquals(" Hello world!", eventResult.getMessage().getPayload());
    }

    @Test
    public void testMpWithParameters() throws Exception
    {
        whenMessageProcessor("create-group")
                .ofNamespace("jira")
                .withAttributes(attributes())
                .thenReturn(muleMessageWithPayload("expectedPayload"));

        MuleEvent eventResult = runFlow("callingJira", testEvent("anotherString"));

        verifyCallOfMessageProcessor("create-group")
                .ofNamespace("jira").atLeast(1);

        verifyCallOfMessageProcessor("create-group")
                .ofNamespace("jira").times(1);

        assertEquals("expectedPayload", eventResult.getMessage().getPayload());

    }

    @Test
    public void demoTest() throws Exception
    {
        whenEndpointWithAddress("jdbc://lookupJob")
                .thenReturn(muleMessageWithPayload(jdbcPayload()));


        whenMessageProcessor("create-group")
                .ofNamespace("jira")
                .withAttributes(anyAttributes())
                .thenReturn(muleMessageWithPayload("createGroupResult"));

        MuleEvent eventResult = runFlow("main", testEvent(" Hello world!]"));

        assertEquals("createGroupResult", eventResult.getMessage().getPayload());
        assertEquals("someGroup", eventResult.getMessage().getInvocationProperty("job"));
    }


    @Test
    public void mockingEndpointWithTransformer() throws Exception
    {
        whenEndpointWithAddress("http://localhost:10443/test")
                .thenApply(new MuleMessageTransformer()
                {
                    @Override
                    public MuleMessage transform(MuleMessage original)
                    {
                        original.setInvocationProperty("newProperty", "propertyValue");
                        return original;
                    }
                });

        MuleEvent eventResult = runFlow("outboundEndPointFlow", testEvent(" Hello world!]"));

        assertEquals("propertyValue", eventResult.getMessage().getInvocationProperty("newProperty"));
    }


    @Test
    public void testWithSpy() throws Exception
    {
        whenMessageProcessor("create-group")
                .ofNamespace("jira")
                .withAttributes(attributes())
                .thenReturn(muleMessageWithPayload("expectedPayload"));

        spyMessageProcessor("create-group")
                .ofNamespace("jira")
                .before(beforeCallSpy())
                .after(afterCallSpy());


        MuleEvent eventResult = runFlow("callingJira", testEvent("anotherString"));

        verifyCallOfMessageProcessor("create-group")
                .ofNamespace("jira").atLeast(1);

        verifyCallOfMessageProcessor("create-group")
                .ofNamespace("jira").times(1);

        assertEquals("expectedPayload", eventResult.getMessage().getPayload());

    }


    @Test(expected = Exception.class)
    public void testingMockTrowsException() throws Exception
    {
        whenMessageProcessor("create-group")
                .ofNamespace("jira")
                .thenThrow(new Exception());

        runFlow("callingJira", testEvent("anotherString"));
    }

    private List<Map<String, String>> jdbcPayload()
    {
        List<Map<String, String>> resultOfJdbc = new ArrayList<Map<String, String>>();
        Map<String, String> r = new HashMap<String, String>();
        r.put("jobtitle", "someGroup");
        resultOfJdbc.add(r);
        return resultOfJdbc;
    }

    private HashMap<String, Object> attributes()
    {
        HashMap<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("groupName", "someGroupName");
        attributes.put("userName", anyString());
        return attributes;
    }

    private HashMap<String, Object> anyAttributes()
    {
        HashMap<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("groupName", anyString());
        attributes.put("userName", anyString());
        return attributes;
    }

    private ArrayList<SpyProcess> afterCallSpy()
    {
        ArrayList<SpyProcess> spyProcesses = new ArrayList<SpyProcess>();
        spyProcesses.add(new AfterSpy());
        return spyProcesses;
    }

    private ArrayList<SpyProcess> beforeCallSpy()
    {
        ArrayList<SpyProcess> spyProcesses = new ArrayList<SpyProcess>();
        spyProcesses.add(new BeforeSpy());
        return spyProcesses;
    }

    private class BeforeSpy implements SpyProcess
    {

        @Override
        public void spy(MuleEvent event) throws MuleException
        {
            assertEquals("anotherString", event.getMessage().getPayload());
        }
    }

    private class AfterSpy implements SpyProcess
    {

        @Override
        public void spy(MuleEvent event) throws MuleException
        {
            assertEquals("expectedPayload", event.getMessage().getPayload());
        }
    }
}
