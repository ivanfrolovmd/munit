/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule;

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
