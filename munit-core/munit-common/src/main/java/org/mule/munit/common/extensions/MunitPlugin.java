/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.munit.common.extensions;

import org.mule.api.lifecycle.Lifecycle;


/**
 * <p>
 * Marker interface to determine that the extension is an Munit plugin. Each plugin must implement the interface
 * {@link Lifecycle} and can implement (if needed) {@link org.mule.api.context.MuleContextAware} interface
 * </p>
 *
 * @author Mulesoft Inc.
 * @since 3.4
 */
public interface MunitPlugin extends Lifecycle
{

}
