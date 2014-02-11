/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.munit.common.mocking;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;

import org.junit.Test;

/**
 *
 */
public class MunitMuleMessageTransformerTest
{

    private AbstractMessageTransformer muleTransformer = mock(AbstractMessageTransformer.class);
    private MuleMessage message = mock(MuleMessage.class);

    @Test
    public void whenExceptionThenReturnSame() throws TransformerException
    {
        when(muleTransformer.transformMessage(message, "utf-8")).thenThrow(new RuntimeException());
        assertEquals(message, new MunitMuleMessageTransformer(muleTransformer).transform(message));
    }

    @Test
    public void whenNoFailuresReturnMessage() throws TransformerException
    {
        when(muleTransformer.transformMessage(null, "utf-8")).thenReturn(message);
        assertEquals(message, new MunitMuleMessageTransformer(muleTransformer).transform(null));
    }
}
