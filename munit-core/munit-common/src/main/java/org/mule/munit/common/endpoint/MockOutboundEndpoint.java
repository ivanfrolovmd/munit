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
        return realEndpoint.getResponseProperties();
    }

    @Override
    public EndpointURI getEndpointURI()
    {
        return realEndpoint.getEndpointURI();
    }

    @Override
    public String getAddress()
    {
        return realEndpoint.getAddress();
    }

    @Override
    public String getEncoding()
    {
        return realEndpoint.getEncoding();
    }

    @Override
    public Connector getConnector()
    {
        return realEndpoint.getConnector();
    }

    @Override
    public List<Transformer> getTransformers()
    {
        return realEndpoint.getTransformers();
    }

    @Override
    public List<Transformer> getResponseTransformers()
    {
        return realEndpoint.getResponseTransformers();
    }

    @Override
    public Map getProperties()
    {
        return realEndpoint.getProperties();
    }

    @Override
    public Object getProperty(Object key)
    {
        return realEndpoint.getProperty(key);
    }

    @Override
    public String getProtocol()
    {
        return realEndpoint.getProtocol();
    }

    @Override
    public boolean isReadOnly()
    {
        return realEndpoint.isReadOnly();
    }

    @Override
    public TransactionConfig getTransactionConfig()
    {
        return realEndpoint.getTransactionConfig();
    }

    @Override
    public Filter getFilter()
    {
        return realEndpoint.getFilter();
    }

    @Override
    public boolean isDeleteUnacceptedMessages()
    {
        return realEndpoint.isDeleteUnacceptedMessages();
    }

    @Override
    public EndpointSecurityFilter getSecurityFilter()
    {
        return realEndpoint.getSecurityFilter();
    }

    @Override
    public EndpointMessageProcessorChainFactory getMessageProcessorsFactory()
    {
        return realEndpoint.getMessageProcessorsFactory();
    }

    @Override
    public List<MessageProcessor> getMessageProcessors()
    {
        return realEndpoint.getMessageProcessors();
    }

    @Override
    public List<MessageProcessor> getResponseMessageProcessors()
    {
        return realEndpoint.getResponseMessageProcessors();
    }

    @Override
    public MessageExchangePattern getExchangePattern()
    {
        return realEndpoint.getExchangePattern();
    }

    @Override
    public int getResponseTimeout()
    {
        return realEndpoint.getResponseTimeout();
    }

    @Override
    public String getInitialState()
    {
        return realEndpoint.getInitialState();
    }

    @Override
    public MuleContext getMuleContext()
    {
        return realEndpoint.getMuleContext();
    }

    @Override
    public RetryPolicyTemplate getRetryPolicyTemplate()
    {
        return realEndpoint.getRetryPolicyTemplate();
    }

    @Override
    public String getEndpointBuilderName()
    {
        return realEndpoint.getEndpointBuilderName();
    }

    @Override
    public boolean isProtocolSupported(String protocol)
    {
        return realEndpoint.isProtocolSupported(protocol);
    }

    @Override
    public String getMimeType()
    {
        return realEndpoint.getMimeType();
    }

    @Override
    public AbstractRedeliveryPolicy getRedeliveryPolicy()
    {
        return realEndpoint.getRedeliveryPolicy();
    }

    @Override
    public boolean isDisableTransportTransformer()
    {
        return realEndpoint.isDisableTransportTransformer();
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
        return realEndpoint.getName();
    }
}
