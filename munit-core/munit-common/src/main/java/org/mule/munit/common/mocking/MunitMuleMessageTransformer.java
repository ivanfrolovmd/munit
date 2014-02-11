/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.munit.common.mocking;

import org.mule.api.MuleMessage;
import org.mule.modules.interceptor.processors.MuleMessageTransformer;
import org.mule.transformer.AbstractMessageTransformer;

/**
 * <p>
 * {@link org.mule.modules.interceptor.processors.MuleMessageTransformer} executes a {@link org.mule.transformer.AbstractMessageTransformer}
 * </p>
 *
 * @author Mulesoft Inc.
 * @since 3.4
 */
public class MunitMuleMessageTransformer implements MuleMessageTransformer
{
    private AbstractMessageTransformer muleTransformer;

    public MunitMuleMessageTransformer(AbstractMessageTransformer muleTransformer)
    {
        this.muleTransformer = muleTransformer;
    }

    @Override
    public MuleMessage transform(MuleMessage original)
    {
        try
        {
            return (MuleMessage) (muleTransformer)
                    .transformMessage(original, "utf-8");
        }
        catch (Throwable e)
        {
            return original;
        }
    }
}
