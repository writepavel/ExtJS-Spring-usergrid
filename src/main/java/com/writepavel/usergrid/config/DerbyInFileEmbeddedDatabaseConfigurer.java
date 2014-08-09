package com.writepavel.usergrid.config;

import org.apache.commons.logging.LogFactory;
import org.apache.derby.jdbc.EmbeddedDriver;
import org.springframework.jdbc.datasource.embedded.ConnectionProperties;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseConfigurer;
import org.springframework.jdbc.datasource.embedded.OutputStreamFactory;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by Pavel on 07.08.2014.
 * This class is copy of org.springframework.jdbc.datasource.embedded.DerbyEmbeddedDatabaseConfigurer,
 * adapted to use not "in-memory" Embedded Derby Database, but "in-file" Database.
 * Changed jdbc url for creating and shutdowning DB-connection.
 */
public class DerbyInFileEmbeddedDatabaseConfigurer implements EmbeddedDatabaseConfigurer {

    //private static final String URL_TEMPLATE = "jdbc:derby:memory:%s;%s";
    private static final String URL_TEMPLATE = "jdbc:derby:%s;%s";

    private static DerbyInFileEmbeddedDatabaseConfigurer instance;


    /**
     * Get the singleton {@link DerbyInFileEmbeddedDatabaseConfigurer} instance.
     * @return the configurer
     * @throws ClassNotFoundException if Derby is not on the classpath
     */
    public static synchronized DerbyInFileEmbeddedDatabaseConfigurer getInstance() throws ClassNotFoundException {
        if (instance == null) {
            // disable log file
            System.setProperty("derby.stream.error.method",
                    OutputStreamFactory.class.getName() + ".getNoopOutputStream");
            instance = new DerbyInFileEmbeddedDatabaseConfigurer();
        }
        return instance;
    }


    private DerbyInFileEmbeddedDatabaseConfigurer() {
    }

    @Override
    public void configureConnectionProperties(ConnectionProperties properties, String databaseName) {
        properties.setDriverClass(EmbeddedDriver.class);
        properties.setUrl(String.format(URL_TEMPLATE, databaseName, "create=true"));
        properties.setUsername("sa");
        properties.setPassword("");
    }

    @Override
    public void shutdown(DataSource dataSource, String databaseName) {
        try {
            System.out.println("DerbyInFileEmbeddedDatabaseConfigurer.shutdown");
            new EmbeddedDriver().connect(
                    String.format(URL_TEMPLATE, databaseName, "shutdown=true"), new Properties());
                    //String.format(URL_TEMPLATE, databaseName, "drop=true"), new Properties());
        }
        catch (SQLException ex) {
            // Error code that indicates successful shutdown
            if (!"08006".equals(ex.getSQLState())) {
                LogFactory.getLog(getClass()).warn("Could not shutdown in-memory Derby database", ex);
            }
        }
    }

}
