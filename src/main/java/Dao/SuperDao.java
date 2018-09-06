package Dao;

import JDBC.JDBCConnection;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SuperDao implements Serializable {
    Logger LOG = Logger.getLogger(FixtureDao.class);
    public Connection getConnection() {
        JDBCConnection jdbcConnection = new JDBCConnection();
        Connection dbConnection = null;
        dbConnection = jdbcConnection.getDbConnection();
        return dbConnection;
    }

    public void jdbcExecuteUpdateWithOneParameter(String compId, String updateTableSQL) throws SQLException {
        Connection dbConnection = getConnection();

        try{
            dbConnection.setAutoCommit(false);
            PreparedStatement preparedStatement = dbConnection.prepareStatement(updateTableSQL);
            preparedStatement.setString(1,compId);
            preparedStatement.executeUpdate();
            dbConnection.commit();
        } catch (SQLException e){
            dbConnection.rollback();
            LOG.error(e);
        } finally {
            dbConnection.close();
        }
    }


    public void jdbcExecuteUpdateTwoParameter(String compId, String updateTableSQL) throws SQLException {
        Connection dbConnection = getConnection();
        try{
            dbConnection.setAutoCommit(false);
            PreparedStatement preparedStatement = dbConnection.prepareStatement(updateTableSQL);
            preparedStatement.setString(1,compId);
            preparedStatement.setString(2,compId);
            preparedStatement.executeUpdate();
            dbConnection.commit();
        } catch (SQLException e){
            dbConnection.rollback();
            LOG.error(e);
        } finally {
            dbConnection.close();
        }
    }
}
