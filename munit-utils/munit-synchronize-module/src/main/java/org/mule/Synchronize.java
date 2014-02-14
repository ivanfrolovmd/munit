/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule;

/**
 * <p>
 * Defines if the flow test is ready to continue or not
 * </p>
 *
 * @author Mulesoft Inc.
 * @since 3.4
 */
public interface Synchronize
{
    boolean readyToContinue();
}
