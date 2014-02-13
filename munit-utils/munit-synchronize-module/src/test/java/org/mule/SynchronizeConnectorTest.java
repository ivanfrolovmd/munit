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
import org.mule.tck.junit4.FunctionalTestCase;
import org.mule.tck.probe.PollingProber;
import org.mule.tck.probe.Probe;

import org.junit.Test;

public class SynchronizeConnectorTest extends FunctionalTestCase
{

    @Override
    protected String getConfigResources()
    {
        return "mule-config.xml";
    }

    @Test
    public void testFlowWithAsync() throws Exception
    {
        runFlowAndExpect("testFlow", getTestEvent(null), 5000);
    }

    @Test
    public void testAsyncFlow() throws Exception
    {
        long time = System.currentTimeMillis();

        MuleEvent event = getTestEvent(null);
        Flow flow = lookupFlowConstruct("testAsyncFlow");
        flow.process(event);

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
        long l = System.currentTimeMillis() - time;
        System.out.println(l);
        assertTrue(4000 < l);

    }

    protected void runFlowAndExpect(String flowName, MuleEvent event, long timeout) throws Exception
    {
        long time = System.currentTimeMillis();
        Flow flow = lookupFlowConstruct(flowName);
        flow.process(event);
        assertTrue(timeout < System.currentTimeMillis() - time);

    }

    protected Flow lookupFlowConstruct(String name)
    {
        return (Flow) muleContext.getRegistry().lookupFlowConstruct(name);
    }
}
