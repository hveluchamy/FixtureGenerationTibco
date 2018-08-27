package Dao;

import Dto.FixtureTeamRoundDto;
import Entity.Fixture;
import JDBC.JDBCConnection;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FixtureDao implements Serializable {
    Logger LOG = Logger.getLogger(FixtureDao.class);
    public void clearLocationTimeSlotFromFixture(String compId) throws SQLException {
        String updateTableSQL = " UPDATE heroku.fixture \n" +
                "                    SET location_timeslot = null \n" +
                "                    WHERE \n" +
                "                      id IN (SELECT f.id FROM salesforce.competition__c c \n" +
                "                        INNER JOIN heroku.round r on r.competition=c.sfid \n" +
                "                        INNER JOIN heroku.fixture f on f.round = r.id \n" +
                "                        WHERE c.sfid = ?1\n" +
                "                        AND r.is_deleted = TRUE \n" +
                "                      );";

        jdbcExecuteUpdate(compId, updateTableSQL);

    }

    public List<FixtureTeamRoundDto> getPlayedFixtures(String compId) throws SQLException {
        String selectSql = "SELECT f.*, ht.id as homeTeamId, at.id as awayTeamId, r.name as roundName, r.id as roundId, r.start_date as roundStartDate, r.end_date as roundEndDate\n" +
                "                    FROM heroku.fixture f\n" +
                "                    JOIN heroku.round r on f.round = r.id AND r.is_deleted = FALSE and r.is_archived = FALSE\n" +
                "                    LEFT JOIN heroku.competitionteam ht on f.home_team_id = ht.team and f.competition = ht.competition\n" +
                "                    LEFT JOIN heroku.competitionteam at on f.away_team_id = at.team and f.competition = at.competition\n" +
                "                    WHERE f.competition =?\n" +
                "                    AND f.status NOT IN ('Draft', 'Published')\n" +
                "                    ORDER BY f.round, f.id ASC;";

        JDBCConnection jdbcConnection = new JDBCConnection();
        Connection dbConnection = null;
        dbConnection = jdbcConnection.getDbConnection();
        try{
            dbConnection.setAutoCommit(false);
            PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSql);
            preparedStatement.setString(1, compId);
            ResultSet result = preparedStatement.executeQuery();
            List<FixtureTeamRoundDto> fixtureTeamRoundDtos = new ArrayList<FixtureTeamRoundDto>();
            while (result.next() )
            {
                FixtureTeamRoundDto fixtureTeamRoundDto = new FixtureTeamRoundDto();
                Fixture fixture = new Fixture(result.getLong("id"), result.getLong("round"), result.getString("competition"), result.getLong("locationTimeslot"),
                        result.getString("homeTeamName"), result.getString("homeTeamId"), result.getString("awayTeamName"), result.getString("awayTeamId"),
                        result.getString("status"),result.getString("resultType"),
                        result.getString("externalId"));
                fixtureTeamRoundDto.setFixture(fixture);
                fixtureTeamRoundDto.setAwayTeamId(result.getString("awayTeamId"));
                fixtureTeamRoundDto.setHomeTeamId(result.getString("homeTeamId"));
                fixtureTeamRoundDto.setRoundStartDate(result.getDate("roundStartDate"));
                fixtureTeamRoundDto.setRoundEnDate(result.getDate("roundEndDate"));
                fixtureTeamRoundDto.setRoundId(result.getString("roundId"));
                fixtureTeamRoundDto.setRoundName(result.getString("roundName"));
                fixtureTeamRoundDtos.add(fixtureTeamRoundDto);
            }

            return fixtureTeamRoundDtos;

        } catch (SQLException e){
            dbConnection.rollback();
            LOG.error(e);
            return null;
        } finally {
            dbConnection.close();
        }

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
