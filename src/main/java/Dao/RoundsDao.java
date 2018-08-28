package Dao;

import Entity.Round;
import JDBC.JDBCConnection;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoundsDao implements Serializable {
    Logger LOG = Logger.getLogger(RoundsDao.class);

  /*  public String getRoundNameFromId(String roundId) throws SQLException {
        String selectTableSQL = "SELECT r.name from heroku.round r where r.Id = ?";
        JDBCConnection jdbcConnection = new JDBCConnection();
        Connection dbConnection = null;
        dbConnection = jdbcConnection.getDbConnection();
        try {
            PreparedStatement preparedStatement = dbConnection.prepareStatement(selectTableSQL);
            preparedStatement.setString(1,roundId);
            ResultSet rs = preparedStatement.executeQuery();

            String roundName = rs.getString(1);
            return roundName;
        } catch (SQLException e){
            LOG.error("Error getting round name", e);
            return null;
        } finally {
            dbConnection.close();

        }
    }*/

    public void deleteUnplayedRounds(String compId) throws SQLException {
        String updateTableSQL = "UPDATE heroku.round r\n" +
                "                     SET is_deleted = TRUE\n" +
                "                     WHERE r.competition = ?\n" +
                "                     AND (\n" +
                "                         is_archived IS NULL\n" +
                "                         OR\n" +
                "                         is_archived = FALSE\n" +
                "                     )\n" +
                "                     AND r.id NOT IN (\n" +
                "                        SELECT DISTINCT round\n" +
                "                        FROM heroku.fixture\n" +
                "                        WHERE competition = ?\n" +
                "                        AND status NOT IN ('Draft','Published')\n" +
                "                        AND round IS NOT NULL\n" +
                "                     );";

        jdbcExecuteUpdate(compId, updateTableSQL);
    }

    private void jdbcExecuteUpdate(String compId, String updateTableSQL) throws SQLException {
        JDBCConnection jdbcConnection = new JDBCConnection();
        Connection dbConnection = null;
        dbConnection = jdbcConnection.getDbConnection();

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
