/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.properties;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MUnitUserPropertiesManagerTest {
	private MUnitUserPropertiesManager propertiesManager;
	private Properties properties;
	
	@Before
	public void setUp(){
		propertiesManager = new MUnitUserPropertiesManager();
		properties = (Properties) System.getProperties().clone();
	}
	
	@Test
	public void addUserPropertiesToSystemTest() {
		assertNull(System.getProperty("test.value"));
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("test.key", "testValue");
		
		propertiesManager.addUserPropertiesToSystem(map);
		
		assertEquals("testValue", System.getProperty("test.key"));
	}
	
	@Test
	public void storeAndRestoreTest() {
		Map<String,String> map;
		map = new HashMap<String,String>();
		map.put("test.key.2", "testValue2");
		
		propertiesManager.addUserPropertiesToSystem(map);
		
		assertEquals("testValue2", System.getProperty("test.key.2"));
		
		propertiesManager.storeInitialSystemProperties();
		
		map = new HashMap<String,String>();
		map.put("test.key.2", "testValue2Changed");
		
		propertiesManager.addUserPropertiesToSystem(map);
		
		assertEquals("testValue2Changed", System.getProperty("test.key.2"));
		
		propertiesManager.restoreInitialSystemProperties();
		
		assertEquals("testValue2", System.getProperty("test.key.2"));
	}
	
	@After
	public void tearDown() {
		System.setProperties(properties);
	}
	
}
