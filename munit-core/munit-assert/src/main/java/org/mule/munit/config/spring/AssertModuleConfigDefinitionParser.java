/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.munit.config.spring;

import org.mule.api.lifecycle.Disposable;
import org.mule.api.lifecycle.Initialisable;
import org.mule.config.spring.MuleHierarchicalBeanDefinitionParserDelegate;
import org.mule.munit.AssertModule;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * <p>
 * Assert Module Definition Parser
 * </p>
 *
 * @author Mulesoft Inc.
 * @since 3.3.2
 */
public class AssertModuleConfigDefinitionParser
        implements BeanDefinitionParser
{

    public BeanDefinition parse(Element element, ParserContext parserContent)
    {
        parseConfigName(element);

        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(AssertModule.class.getName());
        if (Initialisable.class.isAssignableFrom(AssertModule.class))
        {
            builder.setInitMethodName(Initialisable.PHASE_NAME);
        }
        if (Disposable.class.isAssignableFrom(AssertModule.class))
        {
            builder.setDestroyMethodName(Disposable.PHASE_NAME);
        }

        if (element.hasAttribute("mock-inbounds"))
        {
            builder.addPropertyValue("mockInbounds", Boolean.valueOf(element.getAttribute("mock-inbounds")));
        }

        if (element.hasAttribute("mock-connectors"))
        {
            builder.addPropertyValue("mockConnectors", Boolean.valueOf(element.getAttribute("mock-connectors")));
        }

        if (element.hasAttribute("groups"))
        {
            builder.addPropertyValue("groups", String.valueOf(element.getAttribute("groups")));
        }

        List<String> flowNames = new ArrayList<String>();
        Element exclusions = DomUtils.getChildElementByTagName(element, "exclude-inbound-mocking");
        if (exclusions != null)
        {
            List<Element> excludedFlows = DomUtils.getChildElementsByTagName(exclusions, "flow-name");
            if (excludedFlows != null)
            {
                for (Element excludedFlow : excludedFlows)
                {
                    Node valueNode = excludedFlow.getFirstChild();
                    if (valueNode != null && valueNode.getNodeValue() != null)
                    {
                        flowNames.add(valueNode.getNodeValue());
                    }

                }

            }
        }

        builder.addPropertyValue("mockingExcludedFlows", flowNames);

        BeanDefinition definition = builder.getBeanDefinition();
        definition.setAttribute(MuleHierarchicalBeanDefinitionParserDelegate.MULE_NO_RECURSE, Boolean.TRUE);
        return definition;
    }

    protected void parseConfigName(Element element)
    {
        element.setAttribute("name", "___MunitSpringFactoryPostProcessor");
    }

}
