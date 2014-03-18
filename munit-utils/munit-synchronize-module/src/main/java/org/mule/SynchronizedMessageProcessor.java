/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule;

/**
 * <p>
 * The Message processor it has to be waited
 * </p>
 *
 * @author Mulesoft Inc.
 * @since 3.4
 */
public class SynchronizedMessageProcessor
{

    private String name;
    private int times;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setMessageProcessor(String name)
    {
        this.name = name;
    }

    public int getTimes()
    {
        return times;
    }

    public void setTimes(int times)
    {
        this.times = times;
    }
}
