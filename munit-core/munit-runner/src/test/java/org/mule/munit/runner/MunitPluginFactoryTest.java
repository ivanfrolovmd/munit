/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.munit.runner;

import static org.junit.Assert.assertTrue;
import org.mule.api.MuleContext;
import org.mule.munit.runner.mule.context.MockingConfiguration;
import org.mule.munit.runner.plugins.TestMunitPlugin;

import java.util.ArrayList;
import java.util.Properties;

import org.junit.Test;


public class MunitPluginFactoryTest
{

    @Test
    public void test() throws Exception
    {
        MuleContextManager muleContextManager = new MuleContextManager(new MockingConfiguration(false, new ArrayList<String>(), false, new Properties()));
        MuleContext muleContext = muleContextManager.startMule("munit-config.xml");
        muleContextManager.killMule(muleContext);


        assertTrue(TestMunitPlugin.started);
        assertTrue(TestMunitPlugin.disposed);
        assertTrue(TestMunitPlugin.stopped);
        assertTrue(TestMunitPlugin.initialised);
        assertTrue(TestMunitPlugin.withContext);
    }
}
