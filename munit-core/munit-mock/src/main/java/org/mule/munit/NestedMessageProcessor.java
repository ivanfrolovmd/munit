/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.munit;

import org.mule.api.DefaultMuleException;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.NestedProcessor;
import org.mule.api.devkit.NestedProcessorChain;
import org.mule.api.processor.MessageProcessor;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * The NestedMessageProcessor is a wrapper of the devkit Nested processor.
 * <p/>
 * The devkit nested processor lives inside Devkit jars, we don't want that dependency in the common modules. That's
 * why it is wrapped.
 * <p/>
 * This class should die in future versions of Devkit.
 * </p>
 *
 * @author Mulesoft Inc.
 * @since 3.3.2
 */
public class NestedMessageProcessor implements MessageProcessor {

    /**
     * <p>
     * Wrapped nested processor
     * </p>
     */
    private NestedProcessor nestedProcessor;

    public NestedMessageProcessor(NestedProcessor nestedProcessor) {
        this.nestedProcessor = nestedProcessor;
    }

    /**
     * <p>
     * The NestedMessageProcessor does not change the mule event. The nested processors that it wraps are only for
     * munit assertions. it is not valid to modify the event. Munit is a testing framework, not aspect framework.
     * </p>
     *
     * @param event <p>The mule event to be processed</p>
     * @return <p>The same mule event as input</p>
     * @throws MuleException <p>If the mule nested processor fails</p>
     */
    @Override
    public MuleEvent process(MuleEvent event) throws MuleException {

        try {
            Object payload = event.getMessage().getPayload();
            Map<String, Object> invocationProperties = new HashMap<String, Object>();
            for (String name : event.getMessage().getInvocationPropertyNames()) {
                invocationProperties.put(name, event.getMessage().getInvocationProperty(name));
            }

            if(nestedProcessor.getClass().isAssignableFrom(NestedProcessorChain.class)) {
                ((NestedProcessorChain)nestedProcessor).setEvent(event);
            }
            
            nestedProcessor.process(payload, invocationProperties);
            
        } catch (Exception e) {
            throw new DefaultMuleException(e);
        }

        return event;
    }
}
