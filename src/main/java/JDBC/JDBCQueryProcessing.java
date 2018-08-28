package JDBC;

import org.apache.log4j.Logger;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JDBCQueryProcessing implements Serializable {
    static Logger LOG = Logger.getLogger(JDBCQueryProcessing.class);

    public static void jdbcExecuteUpdate(String compId, String updateTableSQL) throws SQLException {
        JDBCConnection jdbcConnection = new JDBCConnection();
        Connection dbConnection = null;
        dbConnection = jdbcConnection.getDbConnection();
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
}
