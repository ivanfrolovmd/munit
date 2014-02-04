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

import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

/**
 * {@link MUnitUserPropertiesManager} allows to set the system properties
 * defined by the user for the execution of the MUnit tests. It also allows
 * to save and restore the state of the system properties on call.
 * 
 * @author damianpelaez
 */
public class MUnitUserPropertiesManager
{
	private static Set<String> notAllowedPropertyKeys = defineNotAllowedPropertyKeys();

	private Properties initialSystemProperties;
	
	public MUnitUserPropertiesManager()
	{
		storeInitialSystemProperties();
	}
	
	private static Set<String> defineNotAllowedPropertyKeys()
	{
		Set<String> propertyKeysSet = new HashSet<String>();
		
		propertyKeysSet.add("java.library.path");
		propertyKeysSet.add("file.encoding");
		propertyKeysSet.add("jdk.map.althashing.threshold");
		
		return propertyKeysSet;
	}

	public static Boolean isPropertyKeyAllowed(String key)
	{
		return !notAllowedPropertyKeys.contains(key);
	}

	
	public void storeInitialSystemProperties()
	{
		initialSystemProperties = (Properties) System.getProperties().clone();
	}
    
	public void addUserPropertiesToSystem(Map<String,String> userProperties)
	{
    	if(userProperties != null) 
    	{
			for(Entry<String, String> entry : userProperties.entrySet())
			{
				if(isPropertyKeyAllowed(entry.getKey()))
				{
					System.setProperty(entry.getKey(), entry.getValue());
				}
			}
		}
	}
    
	public void restoreInitialSystemProperties()
    {
		System.setProperties(initialSystemProperties);
	}

}
