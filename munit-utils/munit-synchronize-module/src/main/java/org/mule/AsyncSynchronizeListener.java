/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule;


import org.mule.api.MuleEvent;
import org.mule.api.context.notification.AsyncMessageNotificationListener;
import org.mule.context.notification.AsyncMessageNotification;

import java.util.concurrent.atomic.AtomicInteger;

public class AsyncSynchronizeListener implements AsyncMessageNotificationListener<AsyncMessageNotification>, Synchronize
{

    AtomicInteger asyncCount = new AtomicInteger(0);
    private String messageRootId;

    public AsyncSynchronizeListener(String messageRootId)
    {
        this.messageRootId = messageRootId;
    }

    @Override
    public void onNotification(AsyncMessageNotification notification)
    {
        String notificationRootId = ((MuleEvent) notification.getSource()).getMessage().getMessageRootId();
        
        
        if (notificationRootId.equals(messageRootId))
        {
            if (notification.getAction() == AsyncMessageNotification.PROCESS_ASYNC_SCHEDULED)
            {
                asyncCount.incrementAndGet();
            }
            else
            {
                asyncCount.decrementAndGet();
            }
        }
    }

    public AtomicInteger getAsyncCount()
    {
        return asyncCount;
    }

    @Override
    public synchronized boolean readyToContinue()
    {
        return asyncCount.get() <= 0;
    }
}
