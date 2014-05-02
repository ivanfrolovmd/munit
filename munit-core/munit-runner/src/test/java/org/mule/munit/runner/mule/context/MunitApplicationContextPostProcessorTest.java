/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.munit.runner.mule.context;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mule.api.config.MuleProperties;
import org.mule.api.transport.Connector;
import org.mule.construct.Flow;
import org.mule.munit.common.MunitApplicationContextPostProcessor;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 *
 */
public class MunitApplicationContextPostProcessorTest
{
    private ConfigurableListableBeanFactory beanFactory;

    @Before
    public void setUp()
    {
        beanFactory = mock(ConfigurableListableBeanFactory.class);

    }

    @Test
    public void testPostProcessBeanFactoryWithoutMocking()
    {
        MunitApplicationContextPostProcessor pp = new MunitApplicationContextPostProcessor();
        pp.setMockInbounds(false);
        pp.setMockingExcludedFlows(new ArrayList<String>());
        when(beanFactory.getBeanDefinition(MuleProperties.OBJECT_MULE_ENDPOINT_FACTORY)).thenReturn(new GenericBeanDefinition());

        pp.postProcessBeanFactory(beanFactory);

        verify(beanFactory, times(1)).getBeanDefinition(any(String.class));
    }

    @Test
    public void testPostProcessBeanFactoryWithMockingExcludedFlows()
    {
        MunitApplicationContextPostProcessor pp = new MunitApplicationContextPostProcessor();
        pp.setMockInbounds(true);
        pp.setMockingExcludedFlows(new ArrayList<String>());

        when(beanFactory.getBeanDefinitionNames()).thenReturn(new String[] {"beanName"});
        when(beanFactory.getBeanDefinition("beanName")).thenReturn(createBeanDefinition());
        when(beanFactory.getBeanDefinition(MuleProperties.OBJECT_MULE_ENDPOINT_FACTORY)).thenReturn(new GenericBeanDefinition());
        pp.postProcessBeanFactory(beanFactory);

    }

    @Test
    public void testMockConnectors()
    {
        MunitApplicationContextPostProcessor pp = new MunitApplicationContextPostProcessor();
        pp.setMockConnectors(true);
        pp.setMockingExcludedFlows(new ArrayList<String>());

        when(beanFactory.getBeanDefinitionNames()).thenReturn(new String[] {"beanName"});
        when(beanFactory.getBeanNamesForType(Connector.class)).thenReturn(new String[] {"beanName"});
        when(beanFactory.getBeanDefinition("beanName")).thenReturn(createConnectionDefinition());
        when(beanFactory.getBeanDefinition(MuleProperties.OBJECT_MULE_ENDPOINT_FACTORY)).thenReturn(new GenericBeanDefinition());

        pp.postProcessBeanFactory(beanFactory);
    }

    private BeanDefinition createConnectionDefinition()
    {
        RootBeanDefinition rootBeanDefinition = new RootBeanDefinition();

        rootBeanDefinition.setBeanClass(Connector.class);
        return rootBeanDefinition;

    }

    private RootBeanDefinition createBeanDefinition()
    {
        RootBeanDefinition rootBeanDefinition = new RootBeanDefinition();
        rootBeanDefinition.setBeanClass(Flow.class);
        rootBeanDefinition.setBeanClassName(Flow.class.getName());

        return rootBeanDefinition;
    }
}
