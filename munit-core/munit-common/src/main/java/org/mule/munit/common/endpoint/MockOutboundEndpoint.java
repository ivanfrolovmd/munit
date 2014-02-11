/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.munit.common.endpoint;

import org.mule.MessageExchangePattern;
import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.config.MuleProperties;
import org.mule.api.endpoint.EndpointMessageProcessorChainFactory;
import org.mule.api.endpoint.EndpointURI;
import org.mule.api.endpoint.OutboundEndpoint;
import org.mule.api.expression.ExpressionManager;
import org.mule.api.processor.MessageProcessor;
import org.mule.api.retry.RetryPolicyTemplate;
import org.mule.api.routing.filter.Filter;
import org.mule.api.security.EndpointSecurityFilter;
import org.mule.api.transaction.TransactionConfig;
import org.mule.api.transformer.Transformer;
import org.mule.api.transport.Connector;
import org.mule.munit.common.MunitUtils;
import org.mule.processor.AbstractRedeliveryPolicy;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mocked outbound Endpoint
 * </p>
 *
 * @author Mulesoft Inc.
 * @since 3.3.2
 */
public class MockOutboundEndpoint implements OutboundEndpoint
{

    private OutboundEndpoint realEndpoint;

    public MockOutboundEndpoint(OutboundEndpoint realEndpoint)
    {
        this.realEndpoint = realEndpoint;
    }

    @Override
    public List<String> getResponseProperties()
    {
        return null;
    }

    @Override
    public EndpointURI getEndpointURI()
    {
        return null;
    }

    @Override
    public String getAddress()
    {
        return null;
    }

    @Override
    public String getEncoding()
    {
        return null;
    }

    @Override
    public Connector getConnector()
    {
        return null;
    }

    @Override
    public List<Transformer> getTransformers()
    {
        return null;
    }

    @Override
    public List<Transformer> getResponseTransformers()
    {
        return null;
    }

    @Override
    public Map getProperties()
    {
        return null;
    }

    @Override
    public Object getProperty(Object key)
    {
        return null;
    }

    @Override
    public String getProtocol()
    {
        return null;
    }

    @Override
    public boolean isReadOnly()
    {
        return false;
    }

    @Override
    public TransactionConfig getTransactionConfig()
    {
        return null;
    }

    @Override
    public Filter getFilter()
    {
        return null;
    }

    @Override
    public boolean isDeleteUnacceptedMessages()
    {
        return false;
    }

    @Override
    public EndpointSecurityFilter getSecurityFilter()
    {
        return null;
    }

    @Override
    public EndpointMessageProcessorChainFactory getMessageProcessorsFactory()
    {
        return null;
    }

    @Override
    public List<MessageProcessor> getMessageProcessors()
    {
        return null;
    }

    @Override
    public List<MessageProcessor> getResponseMessageProcessors()
    {
        return null;
    }

    @Override
    public MessageExchangePattern getExchangePattern()
    {
        return realEndpoint.getExchangePattern();
    }

    @Override
    public int getResponseTimeout()
    {
        return 0;
    }

    @Override
    public String getInitialState()
    {
        return null;
    }

    @Override
    public MuleContext getMuleContext()
    {
        return null;
    }

    @Override
    public RetryPolicyTemplate getRetryPolicyTemplate()
    {
        return null;
    }

    @Override
    public String getEndpointBuilderName()
    {
        return null;
    }

    @Override
    public boolean isProtocolSupported(String protocol)
    {
        return false;
    }

    @Override
    public String getMimeType()
    {
        return null;
    }

    @Override
    public AbstractRedeliveryPolicy getRedeliveryPolicy()
    {
        return null;
    }

    @Override
    public boolean isDisableTransportTransformer()
    {
        return false;
    }

    @Override
    public MuleEvent process(MuleEvent event) throws MuleException
    {
        MockEndpointManager manager = (MockEndpointManager) getEndpointManager(event);

        String address = realAddressAsExpression();
        ExpressionManager expressionManager = event.getMuleContext().getExpressionManager();
        if (expressionManager.isValidExpression(address))
        {
            String realAddress = (String) expressionManager.evaluate(address, event);
            OutboundBehavior behavior = manager.getBehaviorFor(realAddress);

            if (behavior == null)
            {
                return realEndpoint.process(event);
            }

            if (behavior.getException() !=null ){
                throw behavior.getException();
            }

            MunitUtils.verifyAssertions(event, behavior.getAssertions());
            if ( behavior.getMuleMessageTransformer() != null ){
                event.setMessage(behavior.getMuleMessageTransformer().transform(event.getMessage()));
            }
        }

        return event;
    }

    private String realAddressAsExpression()
    {
        return "#[string:" + realEndpoint.getAddress() + "]";
    }

    private Object getEndpointManager(MuleEvent event)
    {
        return event.getMuleContext().getRegistry().lookupObject(MuleProperties.OBJECT_MULE_ENDPOINT_FACTORY);
    }

    @Override
    public String getName()
    {
        return "Mocked Endpoint";
    }
}
