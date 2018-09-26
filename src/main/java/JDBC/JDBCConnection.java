package JDBC;

import Entity.Competition;
import Entity.PropertyDetailsSingleton;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;

public class JDBCConnection implements Serializable {
    Logger LOG = Logger.getLogger(JDBCConnection.class);
    private static String FILENAME = "application.properties";

    public Connection getDbConnection()
    {
        Connection conn = null;
        Properties properties = PropertyDetailsSingleton.getPropertyInstance();
        try
        {
            Class.forName(String.valueOf(properties.get("postgres.driver")));
            String url = properties.getProperty("jdbcUrl");
            String user = properties.getProperty("dbuser");
            String password = properties.getProperty("dbpassword");
            conn = DriverManager.getConnection(url,user, password);
        }
        catch (ClassNotFoundException e)
        {
            LOG.error(e);
            System.exit(1);
        }
        catch (SQLException e)
        {
            LOG.error(e);
            System.exit(2);
        }
        return conn;
    }
}
