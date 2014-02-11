/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.munit.common.mocking;

import org.mule.DefaultMuleMessage;
import org.mule.api.MuleMessage;
import org.mule.modules.interceptor.processors.MuleMessageTransformer;
import org.mule.munit.common.MunitUtils;

/**
 * <p>
 * {@link MuleMessageTransformer} that copies one message into the other
 * </p>
 *
 * @author Mulesoft Inc.
 * @since 3.4
 */
public class CopyMessageTransformer implements MuleMessageTransformer
{

    private DefaultMuleMessage returnMessage;

    public CopyMessageTransformer(DefaultMuleMessage returnMessage)
    {
        this.returnMessage = returnMessage;
    }

    @Override
    public MuleMessage transform(MuleMessage original)
    {
        MunitUtils.copyMessage(returnMessage, (DefaultMuleMessage) original);
        return original;
    }
}
