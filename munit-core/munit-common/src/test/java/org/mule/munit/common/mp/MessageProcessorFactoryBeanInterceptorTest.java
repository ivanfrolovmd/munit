/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.munit.common.mp;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mule.api.processor.LoggerMessageProcessor;

import net.sf.cglib.proxy.MethodProxy;
import org.junit.Test;

/**
 * @author Mulesoft Inc.
 * @since 3.4.0
 */
public class MessageProcessorFactoryBeanInterceptorTest
{

    private MethodProxy proxied = mock(MethodProxy.class);

    @Test
    public void whenProxiedDoesNotReturnMessageProcessorThenReturnOriginal() throws Throwable
    {
        Object obj = new Object();
        Object[] args = new Object[0];
        Object realObject = new Object();
        when(proxied.invokeSuper(obj, args)).thenReturn(realObject);

        MessageProcessorFactoryBeanInterceptor interceptor = new MessageProcessorFactoryBeanInterceptor();
        assertEquals(realObject, interceptor.intercept(obj, Object.class.getDeclaredMethod("hashCode"), args, proxied));
    }

    @Test
    public void whenOriginalIsEnhancedThenReturnOriginal() throws Throwable
    {
        Object obj = new LoggerMessageProcessor();
        Object[] args = new Object[0];
        Object realObject = new LoggerMessageProcessor();
        when(proxied.invokeSuper(obj, args)).thenReturn(realObject);

        MessageProcessorFactoryBeanInterceptor interceptor = new MessageProcessorFactoryBeanInterceptor();
        Object enhancedOriginal = interceptor.intercept(obj, Object.class.getDeclaredMethod("hashCode"), args, proxied);

        when(proxied.invokeSuper(obj, args)).thenReturn(enhancedOriginal);

        assertEquals(enhancedOriginal.toString(), interceptor.intercept(obj, Object.class.getDeclaredMethod("hashCode"), args, proxied).toString());
    }

    @Test
    public void processReturnsNull() throws Throwable
    {
        assertNull(new MessageProcessorFactoryBeanInterceptor().process(new Object(), null, null));
    }
}
