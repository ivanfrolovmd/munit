/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.munit.runner.plugins;

import org.mule.api.MuleContext;
import org.mule.api.MuleException;
import org.mule.api.context.MuleContextAware;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.munit.common.extensions.MunitPlugin;

public class TestMunitPlugin implements MunitPlugin, MuleContextAware
{

    public static boolean initialised;
    public static boolean started;
    public static boolean stopped;
    public static boolean disposed;
    public static boolean withContext;

    public TestMunitPlugin()
    {
        initialised = false;
        started = false;
        stopped = false;
        withContext = false;
        disposed = false;
    }

    @Override
    public void dispose()
    {
        synchronized (TestMunitPlugin.class)
        {
            disposed = true;
        }
    }

    @Override
    public void initialise() throws InitialisationException
    {
        synchronized (TestMunitPlugin.class)
        {
            initialised = true;
        }
    }

    @Override
    public void start() throws MuleException
    {
        synchronized (TestMunitPlugin.class)
        {
            started = true;
        }
    }

    @Override
    public void stop() throws MuleException
    {
        synchronized (TestMunitPlugin.class)
        {
            stopped = true;
        }
    }

    @Override
    public void setMuleContext(MuleContext context)
    {
        synchronized (TestMunitPlugin.class)
        {
            if (context != null)
            {
                withContext = true;
            }
        }
    }
}
