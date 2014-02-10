/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.munit;

import org.mule.api.annotations.Category;
import org.mule.api.annotations.Module;
import org.mule.api.annotations.Processor;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.mail.internet.MimeMessage;

/**
 * <p>Module for mail integration testing.</p>
 *
 * @author Mulesoft Inc.
 */
@Module(name = "mail-server", schemaVersion = "1.0")
@Category(name = "org.mule.tooling.category.munit.utils", description = "Munit tools")
public class MailServerModule
{

    private MailServer mailServer;


    @PostConstruct
    public void createServer()
    {
        mailServer = new MailServer();
    }

    /**
     * <p>Starts the mail server</p>
     * <p/>
     * {@sample.xml ../../../doc/MailServer-connector.xml.sample mail-server:start}
     */
    @Processor
    public void startServer()
    {
        mailServer.start();
    }

    /**
     * <p>Stops the mail server</p>
     * <p/>
     * {@sample.xml ../../../doc/MailServer-connector.xml.sample mail-server:stop}
     */
    @Processor
    public void stopServer()
    {
        try
        {
            mailServer.stop();
        }
        catch (Throwable e)
        {
            // Do nothing
        }
    }

    /**
     * <p>Gets the messages from the server</p>
     * <p/>
     * {@sample.xml ../../../doc/MailServer-connector.xml.sample mail-server:getMessages}
     *
     * @return The list of MimeMessages
     */
    @Processor
    public List<MimeMessage> getReceivedMessages()
    {
        return mailServer.getReceivedMessages();
    }

}
