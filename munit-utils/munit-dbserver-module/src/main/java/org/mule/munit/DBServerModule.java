/**
 * Mule Development Kit
 * Copyright 2010-2011 (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * This file was automatically generated by the Mule Development Kit
 */
package org.mule.munit;

import org.mule.api.annotations.Configurable;
import org.mule.api.annotations.Module;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.param.Optional;

/**
 * <p>Module to test database connections</p>
 *
 * @author Mulesoft Inc.
 * @author Casal, Javier
 */
@Module(name = "dbserver", schemaVersion = "1.0")
public class DBServerModule
{

    /**
     * <p>H2 Database name</p>
     */
    @Configurable
    private String database;

    /**
     * <p>Name of (or path to) the SQL file whose statements will be executed when the database is started</p>
     */
    @Configurable
    @Optional
    private String sqlFile;

    /**
     * <p>CSV files (separated by semicolon) that creates tables in the database using the file name (without the
     * termination, ".csv") as the table name and its columns as the table columns</p>
     */
    @Configurable
    @Optional
    private String csv;


    private DatabaseServer server;


    /**
     * <p>Starts the server</p>
     * <p>Executes the correspondent queries if an SQL file has been included in the dbserver configuration</p>
     * <p>Creates the correspondent tables in the database if a CSV file has been included in the dbserver
     * configuration</p>
     * <p/>
     * {@sample.xml ../../../doc/DBServer-connector.xml.sample dbserver:start}
     */
    @Processor
    public void startDbServer()
    {
        server = new DatabaseServer(database, sqlFile, csv);
        server.start();
    }

    /**
     * <p>Executes the SQL query received as parameter</p>
     * <p/>
     * {@sample.xml ../../../doc/DBServer-connector.xml.sample dbserver:execute}
     *
     * @param sql query to be executed
     * @return result of the SQL query received
     */
    @Processor
    public Object execute(String sql)
    {
        return server.execute(sql);
    }


    /**
     * <p>Executes a SQL query</p>
     * <p/>
     * {@sample.xml ../../../doc/DBServer-connector.xml.sample dbserver:executeQuery}
     *
     * @param sql query to be executed
     * @return result of the SQL query in a JSON format.
     */
    @Processor
    public Object executeQuery(String sql)
    {
        return server.executeQuery(sql);
    }

    /**
     * <p>Executes a SQL query</p>
     * <p/>
     * {@sample.xml ../../../doc/DBServer-connector.xml.sample dbserver:validateThat}
     *
     * @param query   query to be executed
     * @param returns Expected value
     */
    @Processor
    public void validateThat(String query, String returns)
    {
        server.validateThat(query, returns);
    }

    /**
     * <p>Stops the server.</p>
     * <p/>
     * {@sample.xml ../../../doc/DBServer-connector.xml.sample dbserver:stop}
     */
    @Processor
    public void stopDbServer()
    {
        server.stop();
    }


    public void setDatabase(String database)
    {
        this.database = database;
    }

    public void setSqlFile(String sqlFile)
    {
        this.sqlFile = sqlFile;
    }

    public void setCsv(String csv)
    {
        this.csv = csv;
    }

    public String getDatabase()
    {
        return database;
    }

    public String getSqlFile()
    {
        return sqlFile;
    }

    public String getCsv()
    {
        return csv;
    }
}
