package Dao;

import Dto.FixtureTeamRoundDto;
import Entity.Round;
import JDBC.JDBCConnection;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.sql.*;
import java.util.List;
import java.util.stream.Collectors;

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


    public List<FixtureTeamRoundDto> getRoundFixtures(List<Integer> fixtureIds) throws SQLException {
        FixtureTeamRoundDto fixtureTeamRoundDto = new FixtureTeamRoundDto();

        /*fixture.round, fixture.id, hometeam.competition_team as "hometeam_id",
                awayteam.competition_team as "awayteam_id*/

        JDBCConnection jdbcConnection = new JDBCConnection();
        Connection dbConnection = null;
        dbConnection = jdbcConnection.getDbConnection();
      /*  String selectSql = "SELECT fixture.round, fixture.id, hometeam.competition_team homeTeamId," +
                "  awayteam.competition_team  awayTeamId" +
                "                     FROM        heroku.fixture fixture\n" +
                "                  JOIN        heroku.round as  round \n"+
        " ON fixture.round = round.id and round.is_deleted = FALSE and round.is_archived = FALSE \n" +
        " LEFT JOIN heroku.fixtureteam hometeam ON fixture.id = hometeam.fixture and hometeam.home_away ='Home'\n" +
        " LEFT JOIN heroku.fixtureteam awayteam ON fixture.id = awayteam.fixture and awayteam.home_away = 'Away'\n" +
        " WHERE 1 = 1 " +
                "                    AND fixture.id = ANY(?)  ;";*/


        String selectSql = " select *\n" +
                " from heroku.fixture where id = ANY (?)  ;";

        try {
            dbConnection.setAutoCommit(false);
            PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSql);
            Integer[] fistIdStringAray = fixtureIds.toArray(new Integer[0]);

            //preparedStatement.setArray(1, conn.createArrayOf("INTEGER", some_ids.toArray()));
            Array array =  dbConnection.createArrayOf("INTEGER", fixtureIds.toArray());

            preparedStatement.setArray(1, array);
            ResultSet result = preparedStatement.executeQuery();
            while (result.next() ){
               System.out.println(result.getString("competition"));
            }
        } catch (SQLException e){
            dbConnection.rollback();
            LOG.error(e);
            return null;
        } finally {
            dbConnection.close();
        }

        return null;
    }
}
