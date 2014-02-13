/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule;

import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.tck.probe.PollingProber;
import org.mule.tck.probe.Probe;

/**
 *
 */
public abstract class Synchronizer
{

    private MuleContext muleContext;
    private long timeout;

    public Synchronizer(MuleContext muleContext, long timeout)
    {
        this.muleContext = muleContext;
        this.timeout = timeout;
    }

    public MuleEvent runAndWait(MuleEvent event) throws Exception
    {
        final AsyncSynchronizeListener asyncListener = new AsyncSynchronizeListener(event.getMessage().getMessageRootId());
        final PipelineSynchronizeListener pipelineListener = new PipelineSynchronizeListener(event.getMessage().getMessageRootId());
        muleContext.registerListener(asyncListener);
        muleContext.registerListener(pipelineListener);

        MuleEvent returnObject = process(event);

        try
        {
            new SynchronizeProber(timeout, PollingProber.DEFAULT_POLLING_INTERVAL).check(new Probe()
            {
                @Override
                public boolean isSatisfied()
                {
                    return asyncListener.readyToContinue() && pipelineListener.readyToContinue();
                }

                @Override
                public String describeFailure()
                {
                    return "Time out waiting for async executions";
                }
            });
        }
        finally
        {
            muleContext.unregisterListener(asyncListener);
            muleContext.unregisterListener(pipelineListener);
        }

        return returnObject;
    }

    protected abstract MuleEvent process(MuleEvent event) throws Exception;
}
