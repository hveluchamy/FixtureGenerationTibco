package Dao;

import org.apache.log4j.Logger;

import java.io.Serializable;
import java.sql.*;

public class LocationDao extends SuperDao implements Serializable {
    Logger LOG = Logger.getLogger(LocationDao.class);

    public static final String DELETE_LOCATION_SQL = "DELETE FROM heroku.locationtimeslot \n" +
            "                    WHERE \n" +
            "                      id IN (SELECT lt.id FROM salesforce.competition__c c \n" +
            "                        INNER JOIN heroku.round r on r.competition=c.sfid \n" +
            "                        INNER JOIN heroku.fixture f on f.round = r.id \n" +
            "                        INNER JOIN heroku.locationtimeslot lt on (f.location_timeslot=lt.id or lt.related_fixture= f.id) \n" +
            "                        WHERE c.sfid = ?\n" +
            "                        AND r.is_deleted = TRUE \n" +
            "                      );";

    public static final String GET_PLAYED_DATE_SQL = "SELECT lt.start_datetime\n" +
            "        FROM heroku.fixture f\n" +
            "        INNER JOIN heroku.round r ON f.round = r.id AND r.is_deleted = false AND r.is_archived = false\n" +
            "        LEFT JOIN heroku.locationtimeslot lt ON f.location_timeslot = lt.id\n" +
            "        WHERE\n" +
            "        f.competition = ?\n" +
            "        AND r.id in (SELECT DISTINCT round\n" +
            "        FROM heroku.fixture\n" +
            "        WHERE competition = ?\n" +
            "        AND status NOT IN ('Draft', 'Published')\n" +
            "        AND round IS NOT NULL)\n" +
            "        ORDER BY lt.start_datetime DESC LIMIT 1;";



    public void deleteLocationTimeSlots(String compId) throws SQLException {
        String updateTableSQL = DELETE_LOCATION_SQL;
        jdbcExecuteUpdateWithOneParameter(compId, updateTableSQL);
    }


    public Date getPlayedDate(String compId) throws SQLException {
        Date playedDate = null;
        String selectSql = GET_PLAYED_DATE_SQL;

        Connection dbConnection = getConnection();
        try {
            dbConnection.setAutoCommit(false);
            PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSql);
            preparedStatement.setString(1, compId);
            preparedStatement.setString(2, compId);
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()){
                playedDate = result.getDate(0);
            }
        } catch (SQLException e){
            dbConnection.rollback();
            LOG.error(e);
            return null;
        } finally {
            dbConnection.close();
        }
        return playedDate;
    }



}
