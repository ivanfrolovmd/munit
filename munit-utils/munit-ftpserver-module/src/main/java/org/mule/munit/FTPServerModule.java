/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.munit;

import org.mule.api.annotations.Category;
import org.mule.api.annotations.Configurable;
import org.mule.api.annotations.Module;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.param.Default;
import org.mule.api.annotations.param.Optional;
import org.mule.api.lifecycle.InitialisationException;

import javax.annotation.PostConstruct;

/**
 * <p>FTP server for Integration tests.</p>
 * <p/>
 * <p>With this module you can start a FTP server on your local machine </p>
 *
 * @author Mulesoft Inc.
 */
@Module(name = "ftpserver", schemaVersion = "1.0", minMuleVersion = "3.4.0", friendlyName = "FTP Server")
@Category(name = "org.mule.tooling.category.munit.utils", description = "Munit tools")
public class FTPServerModule
{

    /**
     * <p>FTP/SFTP server port.</p>
     * <p>If is not defined, the default value is 22.</p>
     */
    @Configurable
    @Optional
    @Default("22")
    private int port;

    /**
     * <p>Defines if is FTP over ssh(SFTP).</p>
     * <p>If is not defined,the default value is false.</p>
     */
    @Configurable
    @Optional
    @Default("false")
    private boolean secure;

    private Server server;


    /**
     * <p>Starts the server</p>
     * <p/>
     * {@sample.xml ../../../doc/SFTPServer-connector.xml.sample sftpserver:startServer}
     */
    @Processor
    public void startServer()
    {
        server.start();
    }

    /**
     * <p>check if a file exists.</p
     * <p/>
     * {@sample.xml ../../../doc/SFTPServer-connector.xml.sample sftpserver:containsFiles}
     *
     * @param file The file name that you want to check if is in the ftp.
     * @param path path where is going to be the file.
     */
    @Processor
    public void containsFiles(String file, String path)
    {
        server.containsFiles(file, path);
    }

    /**
     * <p>Stops the server</p>
     * <p/>
     * {@sample.xml ../../../doc/SFTPServer-connector.xml.sample sftpserver:stopServer}
     */
    @Processor
    public void stopServer()
    {
        try
        {
            server.stop();
        }
        catch (Throwable t)
        {

        }
    }


    /**
     * <p>Remove created files</p>
     * <p/>
     * {@sample.xml ../../../doc/SFTPServer-connector.xml.sample sftpserver:remove}
     *
     * @param path Path to be removed.
     */
    @Processor
    public void remove(String path)
    {
        server.remove(path);
    }

    public void setPort(int port)
    {
        this.port = port;
    }

    public void setSecure(boolean secure)
    {
        this.secure = secure;
    }

    @PostConstruct
    public void buildServer() throws InitialisationException
    {
        if (secure)
        {
            server = new SFTPServer();
        }
        else
        {
            server = new FTPServer();
        }


        server.initialize(port);

    }

    public int getPort()
    {
        return port;
    }

    public boolean isSecure()
    {
        return secure;
    }
}
