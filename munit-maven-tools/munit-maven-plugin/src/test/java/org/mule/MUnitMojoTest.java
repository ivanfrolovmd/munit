/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.junit.Test;

public class MUnitMojoTest {

	@Test
	public void test() throws MojoExecutionException {
		MavenProject project = new MavenProject();
		MUnitMojo munitMojo = new MUnitMojo();
		Properties properties = (Properties) System.getProperties().clone();
		
		Map<String,String> map = new HashMap<String,String>(); 
		map.put("test.key", "testValue");
		map.put("test.key.2", "testValue2");
		
		munitMojo.systemPropertyVariables = map;
		munitMojo.project = project;
		munitMojo.classpathElements = new ArrayList<String>();
		
		munitMojo.execute();
		
		assertEquals("The system properties should be the same as the initial ones.", properties, System.getProperties());
	}

}
