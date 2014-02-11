/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.munit.common.endpoint;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import org.mule.api.DefaultMuleException;
import org.mule.api.MuleException;
import org.mule.api.endpoint.EndpointBuilder;
import org.mule.api.endpoint.EndpointFactory;
import org.mule.api.endpoint.EndpointURI;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Mulesoft Inc.
 * @since 3.3.2
 */
public class MockEndpointManagerTest
{

    public static final OutboundBehavior OUTBOUND_BEHAVIOR = new OutboundBehavior(new DefaultMuleException("error"), null);
    EndpointFactory realFactory;
    EndpointURI endpointURI;
    EndpointBuilder endpointBuilder;

    @Before
    public void setUp()
    {
        realFactory = mock(EndpointFactory.class);
        endpointURI = mock(EndpointURI.class);
        endpointBuilder = mock(EndpointBuilder.class);
    }

    @Test
    public void testCreateInboundEndpoint() throws MuleException
    {
        MockEndpointManager manager = new MockEndpointManager();
        manager.setDefaultFactory(realFactory);

        manager.getInboundEndpoint("uri");

        verify(realFactory).getInboundEndpoint("uri");
    }

    @Test
    public void testCreateInboundEndpointWithEnpointUri() throws MuleException
    {
        MockEndpointManager manager = new MockEndpointManager();
        manager.setDefaultFactory(realFactory);

        manager.getInboundEndpoint(endpointURI);

        verify(realFactory).getInboundEndpoint(endpointURI);
    }

    @Test
    public void testCreateInboundEndpointWithEndpointBuilderi() throws MuleException
    {
        MockEndpointManager manager = new MockEndpointManager();
        manager.setDefaultFactory(realFactory);

        manager.getInboundEndpoint(endpointBuilder);

        verify(realFactory).getInboundEndpoint(endpointBuilder);
    }

    @Test
    public void testCreateOutboundEndpoint() throws MuleException
    {
        MockEndpointManager manager = new MockEndpointManager();
        manager.setDefaultFactory(realFactory);

        assertTrue(manager.getOutboundEndpoint("uri") instanceof MockOutboundEndpoint);

        verify(realFactory).getOutboundEndpoint("uri");
    }

    @Test
    public void testCreateOutboundEndpointWithEnpointUri() throws MuleException
    {
        MockEndpointManager manager = new MockEndpointManager();
        manager.setDefaultFactory(realFactory);

        assertTrue(manager.getOutboundEndpoint(endpointURI) instanceof MockOutboundEndpoint);

        verify(realFactory).getOutboundEndpoint(endpointURI);
    }

    @Test
    public void testCreateOutboundEndpointWithEndpointBuilderi() throws MuleException
    {
        MockEndpointManager manager = new MockEndpointManager();
        manager.setDefaultFactory(realFactory);

        assertTrue(manager.getOutboundEndpoint(endpointBuilder) instanceof MockOutboundEndpoint);

        verify(realFactory).getOutboundEndpoint(endpointBuilder);
    }

    @Test
    public void testAddBehavior()
    {
        MockEndpointManager manager = new MockEndpointManager();
        manager.addBehavior("any", OUTBOUND_BEHAVIOR);

        assertEquals(OUTBOUND_BEHAVIOR, manager.getBehaviorFor("any"));
    }

    @Test
    public void testReset()
    {
        MockEndpointManager manager = new MockEndpointManager();
        manager.addBehavior("any", OUTBOUND_BEHAVIOR);

        manager.resetBehaviors();

        assertTrue(manager.behaviors.isEmpty());
    }

}
