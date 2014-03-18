/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.config;

import org.mule.SynchronizedMessageProcessor;
import org.mule.config.spring.handlers.AbstractMuleNamespaceHandler;
import org.mule.config.spring.parsers.generic.ChildDefinitionParser;

/**
 * @author Mulesoft Inc.
 * @since 3.4
 */
public class SynchronizeNamespaceHandler extends AbstractMuleNamespaceHandler
{
    @Override
    public void init()
    {
        registerBeanDefinitionParser("run-and-wait", new ChildDefinitionParser("messageProcessor", RunAndWait.class));
        registerBeanDefinitionParser("wait-for", new ChildDefinitionParser("synchronizedMessageProcessors", SynchronizedMessageProcessor.class));
    }
}
