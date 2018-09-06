package Dao;

import Dto.FixtureTeamRoundDto;
import Dto.RoundDto;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoundsDao extends SuperDao implements Serializable{
    public static final String GET_ROUND_FIXTURE_BY_FIXTURE_ID_SQL = "SELECT fixture.round round, fixture.id fixtureId, hometeam.competition_team homeTeamId," +
            "  awayteam.competition_team  awayTeamId" +
            "                     FROM        heroku.fixture fixture\n" +
            "                  JOIN        heroku.round as  round \n" +
            " ON fixture.round = round.id and round.is_deleted = FALSE and round.is_archived = FALSE \n" +
            " LEFT JOIN heroku.fixtureteam hometeam ON fixture.id = hometeam.fixture and hometeam.home_away ='Home'\n" +
            " LEFT JOIN heroku.fixtureteam awayteam ON fixture.id = awayteam.fixture and awayteam.home_away = 'Away'\n" +
            " WHERE 1 = 1 " +
            "                    AND fixture.id = ANY(?)  ;";
    public static final String DELETE_UNPLAYED_ROUNDS_SQL = "UPDATE heroku.round r\n" +
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
    Logger LOG = Logger.getLogger(RoundsDao.class);



    public void deleteUnplayedRounds(String compId) throws SQLException {
        String updateTableSQL = DELETE_UNPLAYED_ROUNDS_SQL;
        jdbcExecuteUpdateTwoParameter(compId, updateTableSQL);
    }




    public List<RoundDto> getRoundListByCompetitionId(String compId) throws SQLException {
        List<RoundDto> roundDtoList = new ArrayList<>();
        Connection dbConnection = getConnection();
        String selectSql = "SELECT name, order_number FROM heroku.round \n" +
                "        WHERE competition = ? AND is_deleted = FALSE AND is_archived = FALSE AND status <> 'Not published'";
        try {
            dbConnection.setAutoCommit(false);
            PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSql);
            preparedStatement.setString(1,compId);
            ResultSet result = preparedStatement.executeQuery();
            while (result.next() ){
                RoundDto roundDto = RoundDtoRowMapper(result);
                roundDtoList.add(roundDto);
            }
        } catch (SQLException e){
            dbConnection.rollback();
            LOG.error(e);
        } finally {
            dbConnection.close();
        }
        return roundDtoList;
    }

    private RoundDto RoundDtoRowMapper(ResultSet result) throws SQLException {
        RoundDto roundDto = new RoundDto();
        roundDto.setName(result.getString("name"));
        roundDto.setNumber(result.getDouble("order_number"));
        roundDto.setOrder_number(result.getDouble("order_number"));
        return roundDto;
    }


    public List<FixtureTeamRoundDto> getRoundFixturesByFixtureIdList(List<Integer> fixtureIds) throws SQLException {
        List<FixtureTeamRoundDto> fixtureTeamRoundDtoList = new ArrayList<>();
        Connection dbConnection = getConnection();
        String selectSql = GET_ROUND_FIXTURE_BY_FIXTURE_ID_SQL;


        try {
            dbConnection.setAutoCommit(false);
            PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSql);
            Array array =  dbConnection.createArrayOf("INTEGER", fixtureIds.toArray());

            preparedStatement.setArray(1, array);
            ResultSet result = preparedStatement.executeQuery();
            while (result.next() ){
                FixtureTeamRoundDto fixtureTeamRoundDto = fixtureTeamRoundDtoRowMapper(result);

                fixtureTeamRoundDtoList.add(fixtureTeamRoundDto);

            }
        } catch (SQLException e){
            dbConnection.rollback();
            LOG.error(e);
            return null;
        } finally {
            dbConnection.close();
        }

        return fixtureTeamRoundDtoList;
    }

    private FixtureTeamRoundDto fixtureTeamRoundDtoRowMapper(ResultSet result) throws SQLException {
        FixtureTeamRoundDto fixtureTeamRoundDto = new FixtureTeamRoundDto();
        fixtureTeamRoundDto.setRoundId(result.getString("round"));
        fixtureTeamRoundDto.setFixtureId(result.getString("fixtureId"));
        fixtureTeamRoundDto.setHomeTeamId(result.getString("homeTeamId"));
        fixtureTeamRoundDto.setAwayTeamId(result.getString("awayTeamId"));
        return fixtureTeamRoundDto;
    }
}
