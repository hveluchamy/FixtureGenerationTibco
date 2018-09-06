package Dao;

import Entity.Competition;
import JDBC.JDBCConnection;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CompetitionDao implements Serializable {
    Logger LOG = Logger.getLogger(CompetitionDao.class);
    public Competition getCompetitionById(String compId) throws SQLException {
        String selectSql = "SELECT *\n" +
                "                    FROM salesforce.competition__c\n" +
                "                    WHERE (sfid = ?);";

        JDBCConnection jdbcConnection = new JDBCConnection();
        Connection dbConnection = null;
        dbConnection = jdbcConnection.getDbConnection();
        try{
            dbConnection.setAutoCommit(false);
            PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSql);
            preparedStatement.setString(1,compId);
            ResultSet result = preparedStatement.executeQuery();
            Competition competition = new Competition();
            while (result.next() ){
                competition.setSfId(compId);
                competition.setName(result.getString("Name"));
                competition.setStartDate(result.getTimestamp("startdate__c"));
                competition.setEndDate(result.getTimestamp("enddate__c"));
                competition.setSeason(result.getString("season__c"));
                competition.setOrganisationOwner(result.getString("organisationowner__c"));
                competition.setParentCompetition(result.getString("parentcompetition__c"));
                competition.setPercentageOfLocationRequired(result.getDouble("percentageoflocationrequired__c"));
                competition.setCompetitionTemplate(result.getString("competitiontemplate__c"));
                competition.setDaysOfWeek(result.getString("daysofweek__c"));
                competition.setGameTimeSlotLength(result.getDouble("gametimeslotlength__c"));
                competition.setAllowFixtureOutsideScheduledTime(result.getBoolean("allowfixtureoutsidescheduledtime__c"));

            }

            return competition;
        } catch (SQLException e){
            dbConnection.rollback();
            LOG.error(e);
            return null;
        } finally {
            dbConnection.close();
        }
    }
}
