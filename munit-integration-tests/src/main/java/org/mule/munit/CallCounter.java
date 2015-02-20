/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.munit;

public class CallCounter {
	private int count = 0;

	public int count() {
		count = count + 1;
//		System.out.println("************** lalalalalalalalal ******************");
		return count;
	}

	public int getCount() {
		return count;
	}

	public static CallCounter generate() {
		return new CallCounter();
	}

}
