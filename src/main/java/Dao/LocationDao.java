package Dao;

import JDBC.JDBCConnection;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LocationDao implements Serializable {
    Logger LOG = Logger.getLogger(LocationDao.class);
    public void deleteLocationTimeSlots(String compId) throws SQLException {
        String updateTableSQL = "DELETE FROM heroku.locationtimeslot \n" +
                "                    WHERE \n" +
                "                      id IN (SELECT lt.id FROM salesforce.competition__c c \n" +
                "                        INNER JOIN heroku.round r on r.competition=c.sfid \n" +
                "                        INNER JOIN heroku.fixture f on f.round = r.id \n" +
                "                        INNER JOIN heroku.locationtimeslot lt on (f.location_timeslot=lt.id or lt.related_fixture= f.id) \n" +
                "                        WHERE c.sfid = ?\n" +
                "                        AND r.is_deleted = TRUE \n" +
                "                      );";

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
