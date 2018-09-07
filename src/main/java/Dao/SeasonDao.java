package Dao;

import Entity.Season;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SeasonDao extends SuperDao implements Serializable {
    Logger LOG = Logger.getLogger(SeasonDao.class);

    public Season getSeasonById(String seasonId) throws SQLException {
        Season season = new Season();
        Connection dbConnection = getConnection();
        String selectSql = " SELECT * FROM salesforce.season__c WHERE(id = ?) ;";
        try {
            dbConnection.setAutoCommit(false);
            dbConnection.setAutoCommit(false);
            PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSql);
            preparedStatement.setString(1,seasonId);
            ResultSet result = preparedStatement.executeQuery();
            while (result.next() ){
                seasonItemRowMapper(seasonId, season, result);
            }
        } catch (SQLException e){
            dbConnection.rollback();
            LOG.error(e);
        } finally {
            dbConnection.close();
        }
        return season;
    }

    private void seasonItemRowMapper(String seasonId, Season season, ResultSet result) throws SQLException {
        season.setSfid(seasonId);
        season.setName(result.getString("name"));
        season.setStartdateC(result.getDate("startdate__c"));
        season.setEnddateC(result.getDate("enddate__c"));
    }


}
