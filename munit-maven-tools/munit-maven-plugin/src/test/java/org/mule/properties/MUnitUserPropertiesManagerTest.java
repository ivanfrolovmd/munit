/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MUnitUserPropertiesManagerTest {
	private Properties properties;
	private MUnitUserPropertiesManager propertiesManager;
	
	@Before
	public void setUp(){
		propertiesManager = new MUnitUserPropertiesManager();
		properties = (Properties) System.getProperties().clone();
	}

	@After
	public void tearDown() {
		System.setProperties(properties);
	}
	
	@Test
	public void addUserPropertiesToSystemTest() {
		assertNull(System.getProperty("test.value"));
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("test.key", "testValue");
		
		propertiesManager.addUserPropertiesToSystem(map);
		
		assertEquals("test.key property should have the value testValue", "testValue", System.getProperty("test.key"));
	}
	
	@Test
	public void storeAndRestoreTest() {
		Map<String,String> map;
		map = new HashMap<String,String>();
		map.put("test.key.2", "testValue2");
		
		propertiesManager.addUserPropertiesToSystem(map);
		
		assertEquals("test.key.2 property should have the value testValue2", "testValue2", System.getProperty("test.key.2"));
		
		propertiesManager.storeInitialSystemProperties();
		
		map = new HashMap<String,String>();
		map.put("test.key.2", "testValue2Changed");
		
		propertiesManager.addUserPropertiesToSystem(map);
		
		assertEquals("test.key.2 property should now have the value testValue2Changed", "testValue2Changed", System.getProperty("test.key.2"));
		
		propertiesManager.restoreInitialSystemProperties();
		
		assertEquals("test.key.2 property should now have changed back to testValue2", "testValue2", System.getProperty("test.key.2"));
	}
	
	
}
