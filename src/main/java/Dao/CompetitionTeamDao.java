package Dao;

import Entity.CompetitionTeam;
import JDBC.JDBCConnection;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CompetitionTeamDao extends SuperDao implements Serializable {
    public static final String GET_COMPETITION_TEAM_SQL = "SELECT *\n" +
            "                    FROM heroku.competitionteam\n" +
            "                    WHERE (competition = ?1)" +
            "                    AND is_archived=?    " +
            "                    AND status =?   ;";
    Logger LOG = Logger.getLogger(CompetitionTeamDao.class);

    public List<CompetitionTeam> getCompetitionTeams(String compId, boolean isArchived, String status) throws SQLException {
        List<CompetitionTeam> competitionTeams = new ArrayList<>();
        String selectSql = GET_COMPETITION_TEAM_SQL;
        Connection dbConnection = getConnection();

        try {
            dbConnection.setAutoCommit(false);
            PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSql);
            preparedStatement.setString(1, compId);
            preparedStatement.setBoolean(2, isArchived);
            preparedStatement.setString(3, status);
            ResultSet result = preparedStatement.executeQuery();
            while (result.next() ){
                CompetitionTeam competitionTeam = new CompetitionTeam();
                competitionTeamItemWrapper(compId, competitionTeam, result);
                competitionTeams.add(competitionTeam);

            }
        } catch (SQLException e){
            dbConnection.rollback();
            LOG.error(e);
            return null;
        } finally {
            dbConnection.close();
        }

        return competitionTeams;
    }

    private void competitionTeamItemWrapper(String compId, CompetitionTeam competitionTeam, ResultSet result) throws SQLException {
        competitionTeam.setId(result.getInt("id"));
        competitionTeam.setCompetition(compId);
        competitionTeam.setOrganisationOfTeam(result.getString("organisation_of_team"));
        competitionTeam.setStatus(result.getString("status"));
        competitionTeam.setPreferredDaysToPlay(result.getString("preferred_days_to_play"));
        competitionTeam.setPreferredTime(result.getString("preferred_time"));
        competitionTeam.setIsArchived(result.getBoolean("is_archived"));
        competitionTeam.setHasMoved(result.getBoolean("has_moved"));
        competitionTeam.setTimesMoved(result.getDouble("times_moved"));
        competitionTeam.setCompetitionOrganisationOwner(result.getString("competition_organisation_owner"));
        competitionTeam.setRanking(result.getDouble("ranking"));
    }
}
