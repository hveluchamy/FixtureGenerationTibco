package Dao;

import JDBC.JDBCConnection;

import java.io.Serializable;
import java.sql.Connection;

public class SuperDao implements Serializable {
    public Connection getConnection() {
        JDBCConnection jdbcConnection = new JDBCConnection();
        Connection dbConnection = null;
        dbConnection = jdbcConnection.getDbConnection();
        return dbConnection;
    }
}
