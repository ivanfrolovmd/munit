/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule;


import static org.junit.Assert.assertTrue;
import org.mule.api.MuleEvent;
import org.mule.construct.Flow;
import org.mule.tck.MuleTestUtils;
import org.mule.tck.junit4.FunctionalTestCase;
import org.mule.tck.probe.PollingProber;
import org.mule.tck.probe.Probe;

import org.junit.Test;

public class SyncronizeApiTest extends FunctionalTestCase
{

    @Test
    public void runAsyncs() throws Exception
    {
        runFlowAndExpect("runAsyncs",getTestEvent(null),  5000);
    }


    @Test
    public void runAsyncsFlows() throws Exception
    {
        long time = System.currentTimeMillis();
        final Flow flow = lookupFlowConstruct("apiAsyncFlow");
        Synchronizer synchronizer = new Synchronizer(muleContext, 12000)
        {

            @Override
            protected MuleEvent process(MuleEvent event) throws Exception
            {
                return flow.process(event);
            }
        };

        synchronizer.runAndWait(MuleTestUtils.getTestEvent("", MessageExchangePattern.ONE_WAY, muleContext));


        PollingProber prober = new PollingProber(6000, 100);
        prober.check(new Probe()
        {
            @Override
            public boolean isSatisfied()
            {
                return EndedMessageProcessor.ended;
            }

            @Override
            public String describeFailure()
            {
                return "Module is not stopping";
            }
        });

        assertTrue(4000 < System.currentTimeMillis() - time);    }


    protected void runFlowAndExpect(String flowName, MuleEvent event, long timeout) throws Exception
    {
        long time = System.currentTimeMillis();
        final Flow flow = lookupFlowConstruct(flowName);
        Synchronizer synchronizer = new Synchronizer(muleContext, 12000)
        {

            @Override
            protected MuleEvent process(MuleEvent event) throws Exception
            {
                return flow.process(event);
            }
        };

        synchronizer.runAndWait(event);

        assertTrue(timeout < System.currentTimeMillis() - time);

    }

    @Override
    protected String getConfigResources()
    {
        return "mule-config.xml";
    }

    protected Flow lookupFlowConstruct(String name)
    {
        return (Flow) muleContext.getRegistry().lookupFlowConstruct(name);
    }
}
