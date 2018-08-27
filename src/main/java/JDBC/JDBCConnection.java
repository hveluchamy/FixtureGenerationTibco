package JDBC;

import Entity.Competition;
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

    public void TestPrintBlogs() throws SQLException {
        Connection dbConnection = null;
        LinkedList listOfBlogs = new LinkedList();

        // connect to the database
        dbConnection = getDbConnection();

        try {
            dbConnection.setAutoCommit(false);
            populateListOfTopics(dbConnection, listOfBlogs);
            printTopics(listOfBlogs);
            dbConnection.commit();
        } catch (SQLException e) {
            dbConnection.rollback();
            LOG.error(e);
            e.printStackTrace();
        } finally {
            dbConnection.close();
        }


    }

    private void printTopics(LinkedList listOfBlogs)
    {
        Iterator it = listOfBlogs.iterator();
        while (it.hasNext())
        {
            Competition blog = (Competition) it.next();
            System.out.println("id: " + blog.getId() + ", name: " + blog.getName());
        }
    }

    private void populateListOfTopics(Connection conn, LinkedList listOfBlogs)
    {
        try
        {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("select id, competition_team_name\n" +
                    "from heroku.competitionteam limit 10;");
            while ( rs.next() )
            {
                Competition competition = new Competition();
                competition.setId(Long.valueOf(rs.getString ("id")));
                competition.setName(rs.getString ("competition_team_name"));
                listOfBlogs.add(competition);
            }
            rs.close();
            st.close();
        }
        catch (SQLException se) {
            LOG.error("Threw a SQLException creating the list of blogs.", se);

        }
    }

    public Connection getDbConnection()
    {
        Connection conn = null;
        Properties properties = getConnectionProperties();


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

    private Properties getConnectionProperties() {
        Properties prop = new Properties();
        InputStream input = null;

        try {


            input = JDBCConnection.class.getClassLoader().getResourceAsStream(FILENAME);
            if(input==null){
                System.out.println("Sorry, unable to find " + FILENAME);
                return null;
            }
            prop.load(input);
            return prop;


        } catch (IOException ex) {
            LOG.error(ex);
        } finally{
            if(input!=null){
                try {
                    input.close();
                } catch (IOException e) {
                    LOG.error(e);
                }
            }
        }
        return null;
    }
}
