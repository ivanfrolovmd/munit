/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.munit.runner.mule.context;

import static org.mule.modules.interceptor.connectors.ConnectorMethodInterceptorFactory.addFactoryDefinitionTo;
import org.mule.api.config.MuleProperties;
import org.mule.api.transport.Connector;
import org.mule.construct.Flow;
import org.mule.munit.common.endpoint.MockEndpointManager;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * <p>
 * This class changes the endpoint factory and inject the mock manager
 * </p>
 * <p/>
 * <p>
 * This is a piece part of the endpoint mocking. By overriding the endpoint factory we can mock all the outbound/inbound
 * endpoints of a mule application
 * </p>
 *
 * @author Mulesoft Inc.
 * @since 3.4.0
 */
public class MunitApplicationContextPostProcessor
{

    private static Logger logger = Logger.getLogger("Bean definition Processor");

    /**
     * <p>
     * Defines if the inbounds must be mocked or not. This is pure Munit configuration
     * </p>
     */
    protected boolean mockInbounds = true;

    /**
     * <p>
     * Defines if the app connectors for outbound/inbound endpoints have to be mocked. If they are then all
     * outbound endpoints/inbound endpoints must be mocked.
     * </p>
     */
    protected boolean mockConnectors = true;

    /**
     * <p>
     * List of flows which we don't want to mock the inbound message sources
     * </p>
     */
    protected List<String> mockingExcludedFlows = new ArrayList<String>();



    public void setMockInbounds(boolean mockInbounds)
    {
        this.mockInbounds = mockInbounds;
    }

    public void setMockingExcludedFlows(List<String> mockingExcludedFlows)
    {
        this.mockingExcludedFlows = mockingExcludedFlows;
    }

    public boolean isMockInbounds()
    {
        return mockInbounds;
    }

    public boolean isMockConnectors()
    {
        return mockConnectors;
    }

    public void setMockConnectors(boolean mockConnectors)
    {
        this.mockConnectors = mockConnectors;
    }


    protected String groups = "";


    public String getGroups() {
        return groups;
    }

    public void setGroups(String groups) {
        this.groups = groups;
    }
    
    /**
     * <p>
     * Implementation of the BeanFactoryPostProcessor. It removes the message sources of all the flows except
     * for the ones specified in mockingExcludedFlows. Only if mockInbounds is true.
     * </p>
     *
     * @param beanFactory <p>
     *                    The spring bean factory
     *                    </p>
     * @throws org.springframework.beans.BeansException <p>
     *                        When post processing fails. Never thrown for this implementation
     *                        </p>
     */
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException
    {
        if (isMockInbounds() || isMockConnectors())
        {
            String[] names = beanFactory.getBeanDefinitionNames();
            String[] beanDefinitionNames = names == null ? new String[0] : names;
            for (String name : beanDefinitionNames)
            {
                BeanDefinition beanDefinition = beanFactory.getBeanDefinition(name);
                if (Flow.class.getName().equals(beanDefinition.getBeanClassName()))
                {
                    if (!mockingExcludedFlows.contains(name))
                    {
                        beanDefinition.getPropertyValues().removePropertyValue("messageSource");
                    }
                }
            }


        }

        changeEndpointFactory(beanFactory);

        mockConnectors(beanFactory);

    }

    /**
     * <p>
     * Changes the default EndpointFactory of mule with a Wrapper of it. This wrapper creates mocks of the Outbound
     * Endpoints
     * </p>
     *
     * @param beanFactory <p>
     *                    The spring bean factory
     *                    </p>
     */
    private void changeEndpointFactory(ConfigurableListableBeanFactory beanFactory)
    {
        GenericBeanDefinition endpointFactory = (GenericBeanDefinition) beanFactory.getBeanDefinition(MuleProperties.OBJECT_MULE_ENDPOINT_FACTORY);

        AbstractBeanDefinition abstractBeanDefinition = endpointFactory.cloneBeanDefinition();

        MutablePropertyValues propertyValues = new MutablePropertyValues();
        propertyValues.add("defaultFactory", abstractBeanDefinition);
        endpointFactory.setPropertyValues(propertyValues);
        endpointFactory.setBeanClassName(MockEndpointManager.class.getCanonicalName());
    }

    /**
     * <p>
     * Changes the {@link org.mule.api.transport.Connector} bean definition so they are created as mocks of connectors that do not connect
     * </p>
     * <p/>
     * <p>
     * This action is done only if {@link #isMockConnectors()} is true
     * </p>
     *
     * @param beanFactory <p>
     *                    The bean factory that contains the bean definition
     *                    </p>
     */
    private void mockConnectors(ConfigurableListableBeanFactory beanFactory)
    {
        if (isMockConnectors())
        {
            String[] beanNamesForType = beanFactory.getBeanDefinitionNames();
            if (beanNamesForType != null)
            {
                for (String beanName : beanNamesForType)
                {
                    BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
                    try
                    {
                        if ( Connector.class.isAssignableFrom(Class.forName(beanDefinition.getBeanClassName())))
                        {
                            RootBeanDefinition rootBeanDefinition = RootBeanDefinition.class.cast(beanDefinition);

                            if (beanDefinition.getFactoryMethodName() == null)
                            {
                                addFactoryDefinitionTo(rootBeanDefinition)
                                        .withConstructorArguments(rootBeanDefinition.getBeanClass());
                            }
                            else
                            {
                                logger.info("The connector " + beanName + " cannot be mocked as it already has a factory method");
                            }
                        }
                    }
                    catch (ClassNotFoundException e)
                    {
                        logger.info("Could not create Class for connector matching");
                    }

                }
            }

        }
    }
}
