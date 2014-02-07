/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.munit.common.mp;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import org.mule.api.MuleEvent;
import org.mule.api.processor.LoggerMessageProcessor;
import org.mule.api.processor.MessageProcessor;

import net.sf.cglib.proxy.MethodProxy;
import org.junit.Test;

/**
 * @author Mulesoft Inc.
 * @since 3.4.0
 */
public class WrapperMunitMessageProcessorInterceptorTest
{

    private LoggerMessageProcessor realMessageProcessor = mock(LoggerMessageProcessor.class);
    private MethodProxy proxy = mock(MethodProxy.class);


    @Test
    public void whenIsAMessageProcessorButMethodIsProcessThenCallMock() throws Throwable
    {
        WrapperMunitMessageProcessorInterceptor interceptor = new MockedInterceptor(realMessageProcessor);

        Object mockedValue = interceptor.intercept(new LoggerMessageProcessor(), MessageProcessor.class.getMethod("process", MuleEvent.class), new Object[0], proxy);

        assertEquals("anything", mockedValue);
    }

    @Test
    public void whenIsAMessageProcessorAndMethodIsNotProcess() throws Throwable
    {
        WrapperMunitMessageProcessorInterceptor interceptor = new MockedInterceptor(realMessageProcessor);

        LoggerMessageProcessor obj = new LoggerMessageProcessor();
        interceptor.intercept(obj, LoggerMessageProcessor.class.getMethod("initialise"), new Object[0], proxy);

        verify(realMessageProcessor).initialise();
    }

    @Test
    public void makeItFail() throws Throwable
    {
        WrapperMunitMessageProcessorInterceptor interceptor = new MockedInterceptor(mock(MessageProcessor.class));

        LoggerMessageProcessor obj = new LoggerMessageProcessor();
        interceptor.intercept(obj, LoggerMessageProcessor.class.getMethod("initialise"), new Object[0], proxy);

    }


    private static class MockedInterceptor extends WrapperMunitMessageProcessorInterceptor
    {

        public MockedInterceptor(MessageProcessor realMp)
        {
            super(realMp);
        }

        @Override
        public Object process(Object obj, Object[] args, MethodProxy proxy) throws Throwable
        {
            return "anything";
        }
    }
}
