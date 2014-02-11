/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.munit.common.endpoint;

import org.mule.api.MuleException;
import org.mule.api.processor.MessageProcessor;
import org.mule.modules.interceptor.processors.MuleMessageTransformer;

import java.util.List;

/**
 * <p>
 * This class defines how the outbound endpoint must behave. It has the list of message processors that
 * asserts the incoming payload and the return message of the outbound.
 * </p>
 *
 * @author Mulesoft Inc.
 * @since 3.3.2
 */
public class OutboundBehavior
{

    /**
     * <p>
     * Tranformer of the mule message
     * </p>
     */
    private MuleMessageTransformer muleMessageTransformer;

    /**
     * <p>
     * Exception to be thrown by the outbound endpoint
     * </p>
     */
    private MuleException exception;

    /**
     * <p>
     * The list of message processors for message assertion. These assertions will be called before
     * calling the outbund endpoints.
     * </p>
     */
    private List<MessageProcessor> assertions;

    public OutboundBehavior(MuleMessageTransformer muleMessageTransformer, List<MessageProcessor> assertions)
    {
        this.muleMessageTransformer = muleMessageTransformer;
        this.assertions = assertions;
    }

    public OutboundBehavior(MuleException exception, List<MessageProcessor> assertions)
    {
        this.exception = exception;
        this.assertions = assertions;
    }


    public List<MessageProcessor> getAssertions()
    {
        return assertions;
    }

    public MuleMessageTransformer getMuleMessageTransformer()
    {
        return muleMessageTransformer;
    }

    public MuleException getException()
    {
        return exception;
    }
}
