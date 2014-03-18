/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.config;

import org.mule.DefaultMuleEvent;
import org.mule.MessageExchangePattern;
import org.mule.SynchronizedMessageProcessor;
import org.mule.Synchronizer;
import org.mule.api.DefaultMuleException;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.processor.MessageProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * The only processor for the Synchronizer
 * </p>
 *
 * @author Mulesoft Inc.
 * @since 3.4
 */
public class RunAndWait implements MessageProcessor
{

    private List<MessageProcessor> messageProcessors;
    private List<SynchronizedMessageProcessor> synchronizedMessageProcessors = new ArrayList<SynchronizedMessageProcessor>();
    private boolean runAsyc;
    private long timeout;

    @Override
    public MuleEvent process(MuleEvent event) throws MuleException
    {
        MuleEvent processingEvent = event;
        if (runAsyc)
        {
            processingEvent = new DefaultMuleEvent(event.getMessage(), MessageExchangePattern.ONE_WAY,
                                                   event.getFlowConstruct(), event.getSession());
        }

        Synchronizer synchronizer = new Synchronizer(event.getMuleContext(), timeout, synchronizedMessageProcessors)
        {

            @Override
            protected MuleEvent process(MuleEvent event) throws Exception
            {
                for ( MessageProcessor messageProcessor : messageProcessors){
                    event = messageProcessor.process(event);
                }
                return event;
            }
        };

        try
        {
            return synchronizer.runAndWait(processingEvent);
        }
        catch (Exception e)
        {
            throw new DefaultMuleException(e);
        }
    }

    public void setMessageProcessors(List<MessageProcessor> messageProcessor)
    {
        this.messageProcessors = messageProcessor;
    }

    public void setRunAsyc(boolean runAsyc)
    {
        this.runAsyc = runAsyc;
    }

    public void setTimeout(long timeout)
    {
        this.timeout = timeout;
    }

    public void setSynchronizedMessageProcessors(List<SynchronizedMessageProcessor> synchronizedMessageProcessors)
    {
        this.synchronizedMessageProcessors = synchronizedMessageProcessors;
    }
}