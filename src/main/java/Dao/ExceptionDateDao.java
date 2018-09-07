package Dao;

import Dto.ExceptionDateDto;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExceptionDateDao extends SuperDao implements Serializable {
    public static final String EXCEPTION_DATES_SQL = "SELECT      ed.id id, ed.end_date endDate, ed.description description, ed.start_date startDate, ed.exception_days exceptionDays, eed.related_season relatedSeason, \n" +
            "                                        eed.related_competition relatedCompetition, eed.related_sp_member_availablity rspmAvailablity, eed.related_availbility_rule relatedAvailbilityRule \n" +
            " FROM        heroku.exceptiondates ed \n" +
            "        INNER JOIN heroku.entityexceptiondates eed ON eed.related_exception_date = ed.id \n" +
            "        WHERE 1 = 1 \n" +
            " AND     (eed.related_season =?" +
            " OR      eed.related_competition =?)";

    Logger LOG = Logger.getLogger(ExceptionDateDao.class);

    public List<ExceptionDateDto> getExceptionDates(Integer exceptionDateId, Date startDate, Date endDate, String seasonId, String competitionId) throws SQLException {
        List<ExceptionDateDto> exceptionDateDtoList = new ArrayList<>();
        Connection dbConnection = getConnection();
        String selectSql = EXCEPTION_DATES_SQL;
        try {
            dbConnection.setAutoCommit(false);
            PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSql);
            preparedStatement.setString(1,seasonId);
            preparedStatement.setString(2,competitionId);
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()){
                ExceptionDateDto exceptionDateDto = exceptionDateItemRowMapper(result);
                exceptionDateDtoList.add(exceptionDateDto);
            }
        } catch (SQLException e){
            dbConnection.rollback();
            LOG.error(e);
        } finally {
            dbConnection.close();
        }
        return exceptionDateDtoList;
    }

    private ExceptionDateDto exceptionDateItemRowMapper(ResultSet result) throws SQLException {
        ExceptionDateDto exceptionDateDto = new ExceptionDateDto();
        exceptionDateDto.setId(result.getInt("id"));
        exceptionDateDto.setDescription(result.getString("description"));
        exceptionDateDto.setStartDate(result.getDate("startDate"));
        exceptionDateDto.setEndDate(result.getDate("endDate"));
        exceptionDateDto.setExceptionDays(result.getString("exceptionDays"));
        exceptionDateDto.setRelatedSeason(result.getString("relatedSeason"));
        exceptionDateDto.setRelatedCompetition(result.getString("relatedCompetition"));
        exceptionDateDto.setRelatedSpMemberAvailability(result.getInt("rspmAvailablity"));
        exceptionDateDto.setRelatedAvailabilityRule(result.getString("relatedAvailbilityRule"));
        return exceptionDateDto;
    }


}
