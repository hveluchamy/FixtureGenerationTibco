package Dao;

import Dto.FixtureTeamRoundDto;
import Entity.Fixture;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FixtureDao extends SuperDao implements Serializable {
    public static final String CLEAR_TIMESLOT_UPDATE_SQL = " UPDATE heroku.fixture \n" +
            "                    SET location_timeslot = null \n" +
            "                    WHERE \n" +
            "                      id IN (SELECT f.id FROM salesforce.competition__c c \n" +
            "                        INNER JOIN heroku.round r on r.competition=c.sfid \n" +
            "                        INNER JOIN heroku.fixture f on f.round = r.id \n" +
            "                        WHERE c.sfid = ?\n" +
            "                        AND r.is_deleted = TRUE \n" +
            "                      );";
    Logger LOG = Logger.getLogger(FixtureDao.class);

    public static final String GET_PLAYED_FIXTURE_SQL = "SELECT f.*, ht.id as homeTeamId, at.id as awayTeamId, r.name as roundName, r.id as roundId, r.start_date as roundStartDate, r.end_date as roundEndDate\n" +
            "                    FROM heroku.fixture f\n" +
            "                    JOIN heroku.round r on f.round = r.id AND r.is_deleted = FALSE and r.is_archived = FALSE\n" +
            "                    LEFT JOIN heroku.competitionteam ht on f.home_team_id = ht.team and f.competition = ht.competition\n" +
            "                    LEFT JOIN heroku.competitionteam at on f.away_team_id = at.team and f.competition = at.competition\n" +
            "                    WHERE f.competition =?\n" +
            "                    AND f.status NOT IN ('Draft', 'Published')\n" +
            "                    ORDER BY f.round, f.id ASC;";


    public static final String GET_ALL_FIXTURE_SQL = "SELECT f.*, ht.id as homeTeamId, at.id as awayTeamId, r.name as roundName, r.id as roundId, r.start_date as roundStartDate, r.end_date as roundEndDate\n" +
            "                    FROM heroku.fixture f\n" +
            "                    JOIN heroku.round r on f.round = r.id AND r.is_deleted = FALSE and r.is_archived = FALSE\n" +
            "                    LEFT JOIN heroku.competitionteam ht on f.home_team_id = ht.team and f.competition = ht.competition\n" +
            "                    LEFT JOIN heroku.competitionteam at on f.away_team_id = at.team and f.competition = at.competition\n" +
            "                    WHERE f.competition =?\n" +
            "                    AND f.status NOT IN ('Draft', 'Published')\n" +
            "                    ORDER BY f.round, f.id ASC;";


    public void clearLocationTimeSlotFromFixture(String compId) throws SQLException {
        String updateTableSQL = CLEAR_TIMESLOT_UPDATE_SQL;
        jdbcExecuteUpdateWithOneParameter(compId, updateTableSQL);
    }

    public List<FixtureTeamRoundDto> getPlayedFixtures(String compId) throws SQLException {
        String selectSql = GET_PLAYED_FIXTURE_SQL;
        Connection dbConnection = getConnection();
        try{
            dbConnection.setAutoCommit(false);
            PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSql);
            preparedStatement.setString(1, compId);
            ResultSet result = preparedStatement.executeQuery();
            List<FixtureTeamRoundDto> fixtureTeamRoundDtos = new ArrayList<FixtureTeamRoundDto>();
            while (result.next() )
            {
                processFixtureTeamRoundDto(result, fixtureTeamRoundDtos);
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

    private void processFixtureTeamRoundDto(ResultSet result, List<FixtureTeamRoundDto> fixtureTeamRoundDtos) throws SQLException {
        FixtureTeamRoundDto fixtureTeamRoundDto = new FixtureTeamRoundDto();
        Fixture fixture = initFixture(result);
        fixtureTeamRoundDtoRowMapper(result, fixtureTeamRoundDto, fixture);
        fixtureTeamRoundDtos.add(fixtureTeamRoundDto);
    }

    public List<FixtureTeamRoundDto> getAllFixturesByCompId(String compId) throws SQLException {
        List<FixtureTeamRoundDto> fixtureTeamRoundDtoList = new ArrayList<>();

        String selectSql = GET_ALL_FIXTURE_SQL;
        Connection dbConnection = getConnection();
        try {
            dbConnection.setAutoCommit(false);
            PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSql);
            preparedStatement.setString(1, compId);
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()){
                processFixtureTeamRoundDto(result, fixtureTeamRoundDtoList);
            }

            return fixtureTeamRoundDtoList;

        } catch (SQLException e){
            dbConnection.rollback();
            LOG.error(e);
            return null;
        } finally {
            dbConnection.close();
        }

    }


    private Fixture initFixture(ResultSet result) throws SQLException {
        return new Fixture(result.getLong("id"), result.getLong("round"), result.getString("competition"), result.getLong("location_timeslot"),
                            result.getString("home_team_name"), result.getString("homeTeamId"), result.getString("away_team_name"), result.getString("awayTeamId"),
                            result.getString("status"),result.getString("result_type"),
                            result.getString("external_id"));
    }


    private void fixtureTeamRoundDtoRowMapper(ResultSet result, FixtureTeamRoundDto fixtureTeamRoundDto, Fixture fixture) throws SQLException {
        fixtureTeamRoundDto.setFixture(fixture);
        fixtureTeamRoundDto.setAwayTeamId(result.getString("awayTeamId"));
        fixtureTeamRoundDto.setHomeTeamId(result.getString("homeTeamId"));
        fixtureTeamRoundDto.setRoundStartDate(result.getDate("roundStartDate"));
        fixtureTeamRoundDto.setRoundEnDate(result.getDate("roundEndDate"));
        fixtureTeamRoundDto.setRoundId(result.getString("roundId"));
        fixtureTeamRoundDto.setRoundName(result.getString("roundName"));
    }


}
