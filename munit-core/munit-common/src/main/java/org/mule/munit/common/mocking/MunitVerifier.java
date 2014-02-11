/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.munit.common.mocking;

import static junit.framework.Assert.fail;
import org.mule.api.MuleContext;
import org.mule.modules.interceptor.processors.MessageProcessorCall;
import org.mule.modules.interceptor.processors.MessageProcessorId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * This is the general Munit Tool
 * </p>
 *
 * @author Mulesoft Inc.
 * @since 3.3.2
 */
public class MunitVerifier extends MunitMockingTool
{

    public MunitVerifier(MuleContext muleContext)
    {
        super(muleContext);
    }

    /**
     * <p>
     * Defines the name of the message processor to verify call
     * </p>
     *
     * @param name <p>
     *             The name of the message processor to verify call
     *             </p>
     * @return <p>
     *         Itself
     *         </p>
     */
    public MunitVerifier verifyCallOfMessageProcessor(String name)
    {
        this.messageProcessorName = name;
        return this;
    }


    /**
     * <p>
     * Defines the namespace of the message processor to verify call
     * </p>
     *
     * @param namespace <p>
     *                  The namespace of the message processor to verify call
     *                  </p>
     * @return <p>
     *         Itself
     *         </p>
     */
    public MunitVerifier ofNamespace(String namespace)
    {
        this.messageProcessorNamespace = namespace;
        return this;
    }

    /**
     * <p>
     * The times it must be called
     * </p>
     *
     * @param times <p>
     *              The times it must be called
     *              </p>
     */
    public void times(Integer times)
    {
        List<MessageProcessorCall> executedCalls = getExecutedCalls();

        if (executedCalls.size() != times)
        {
            fail("On " + getFullName() + ".Expected " + times +
                 " but got " + executedCalls.size() + " calls");
        }
    }

    /**
     * <p>
     * At least the times it must be called
     * </p>
     *
     * @param atLeast <p>
     *                At least the times it must be called
     *                </p>
     */
    public void atLeast(Integer atLeast)
    {
        checkValidQuery();

        List<MessageProcessorCall> executedCalls = getExecutedCalls();

        if (executedCalls.size() < atLeast)
        {
            fail("On " + getFullName() + ".Expected at least " + atLeast + " but got " + executedCalls.size() + " calls");
        }
    }

    /**
     * <p>
     * At most the times it must be called
     * </p>
     *
     * @param atMost <p>
     *               At most the times it must be called
     *               </p>
     */
    public void atMost(Integer atMost)
    {
        checkValidQuery();
        List<MessageProcessorCall> executedCalls = getExecutedCalls();

        if (executedCalls.size() > atMost)
        {
            fail("On " + getFullName() + ".Expected at most " + atMost + " but got " + executedCalls.size() + " calls");
        }
    }

    /**
     * <p>
     * At least one time called
     * </p>
     */
    public void atLeastOnce()
    {
        checkValidQuery();
        List<MessageProcessorCall> executedCalls = getExecutedCalls();
        if (executedCalls.isEmpty())
        {
            fail("On " + getFullName() + ".It was never called");
        }

    }

    private List<MessageProcessorCall> getExecutedCalls()
    {
        return getManager().findCallsFor(new MessageProcessorId(messageProcessorName,
                                                                messageProcessorNamespace), messageProcessorAttributes);
    }

    public MunitVerifier withAttributes(Map<String, Object> attributes)
    {
        this.messageProcessorAttributes = attributes;
        return this;
    }

    public MunitVerifier withAttributes(Attribute ... attributes)
    {
        Map<String, Object> mapOfAttributes = new HashMap<String, Object>();
        for ( Attribute attribute : attributes )
        {
            mapOfAttributes.put(attribute.getId(), attribute.getValue());
        }
        this.withAttributes(mapOfAttributes);
        return this;
    }


}
