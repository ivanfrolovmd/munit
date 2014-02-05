/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.munit.runner;

import org.mule.api.MuleContext;
import org.mule.api.context.MuleContextAware;
import org.mule.munit.common.extensions.MunitPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>
 * Loads all the {@link MunitPlugin} that are in the Class Loader, those plugins are set with the {@link MuleContext}
 * (if necessary) but are not initialised
 * </p>
 *
 * @author Mulesoft Inc.
 * @since 3.4
 */
public class MunitPluginFactory
{

    private static Log log = LogFactory.getLog(MunitPluginFactory.class);

    public Collection<MunitPlugin> loadPlugins(MuleContext context)
    {
        List<MunitPlugin> munitPlugins = new ArrayList<MunitPlugin>();
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        try
        {
            Enumeration<URL> resources = contextClassLoader.getResources("META-INF/munit-plugin.properties");
            while (resources.hasMoreElements())
            {
                Properties properties = new Properties();
                Object content = resources.nextElement().getContent();
                properties.load((InputStream) content);
                MunitPlugin plugin = createMunitPlugin(contextClassLoader, properties);
                if (plugin != null)
                {
                    if (plugin instanceof MuleContextAware)
                    {
                        ((MuleContextAware) plugin).setMuleContext(context);
                    }
                    munitPlugins.add(plugin);
                }
            }
        }
        catch (IOException e)
        {
            log.error("Could not read the Classpath in order to get the plugin configurations");
        }


        return munitPlugins;
    }

    private MunitPlugin createMunitPlugin(ClassLoader contextClassLoader, Properties properties)
    {
        String property = properties.getProperty("plugin.className");
        try
        {
            if (property != null && !property.isEmpty())
            {
                return (MunitPlugin) contextClassLoader.loadClass(property).newInstance();
            }

        }
        catch (Throwable e)
        {
            log.error("The class " + property + " could not be load");
        }
        return null;
    }
}
