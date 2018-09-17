package Dao;

import Dto.LocationTimeSlotDto;
import Entity.LocationAvailabilityRule;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LocationDao extends SuperDao implements Serializable {
    public static final String LOCATION_AVAIL_SQL = "SELECT      id, sfid, name, duration__c duration, isDeleted, starttime__c startTime, \n" +
            "                                        enddate__c endDate, location__c location, repeaton__c repeatOn,\n" +
            "                                        currencyisocode, locationsplitpercentage__c locationsplitpercentage,\n" +
            "                                        repeateveryxperiod__c repeateveryxperiod, availableforcompetitionteam__c\n" +
            "                                         availableforcompetitionteam, availablefororganisation__c \n" +
            "                                        availablefororganisation, repeatperiodtype__c repeatperiodtype,\n" +
            "                                        locationsplitname__c locationsplitname, availableforcompetition__c\n" +
            "                                        availableforcompetition, endafterxoccurance__c  endafterxoccurance,\n" +
            "                                        locationtimezone__c resourcetimezone,\n" +
            "                                        CASE WHEN lar.availableforcompetitionteam__c IS NOT NULL THEN teamhomegamelocationpreferenceranking__c\n" +
            "                                        WHEN lar.availableforcompetition__c IS NOT NULL THEN 11 \n" +
            "                                        WHEN lar.availablefororganisation__c IS NOT NULL THEN 12 \n" +
            "                                        ELSE 13 END AS availabilityranking \n" +
            "   FROM        heroku.v_locationavailabilityrule lar \n" +
            "   WHERE       1=1" +
            "   AND     (lar.availablefororganisation__c =?" +
            "   OR      lar.availableforcompetition__c =?" +
            "   OR      lar.availableforcompetitionteam__c IN (?))" +
            "   AND     isdeleted =? ";
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


    public Date getLastPlayedDate(String compId) throws SQLException {
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
            dbConnection.rollback();;
            LOG.error(e);
            return null;
        } finally {
            dbConnection.close();
        }
        return playedDate;
    }


    public List<LocationAvailabilityRule> getLocationAvailabilityRules(boolean isDeleted, String organisation, String compId, List<String> compTeamIds) throws SQLException {
        List<LocationAvailabilityRule> locationAvailabilityRuleList = new ArrayList<>();
        String selectSql= LOCATION_AVAIL_SQL;
        Connection dbConnection = getConnection();
        try {
            dbConnection.setAutoCommit(false);
            PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSql);
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()){
                LocationAvailabilityRule locationAvailabilityRule = locationeAvailabilityItemRowMapper(result);
                locationAvailabilityRuleList.add(locationAvailabilityRule);

            }
        } catch (SQLException e){
            dbConnection.rollback();
            LOG.error(e);
            return null;
        } finally {
            dbConnection.close();
        }

        return locationAvailabilityRuleList;
    }

    public List<LocationTimeSlotDto> getLocationTimeSlotList(List<String> locationSfIds) throws SQLException {
        List<LocationTimeSlotDto> locationTimeSlotDtos = new ArrayList<>();
        Connection dbConnection = getConnection();
        String selectSql = " SELECT\n" +
                "                        CONCAT(th.name, ' vs ', ta.name) AS eventTitle,\n" +
                "                        th.name AS homeTeamName,\n" +
                "                        th.sfid AS homeTeamId,\n" +
                "                        ta.name AS awayTeamName,\n" +
                "                        ta.sfid AS awayTeamId,\n" +
                "                        f.id AS fixtureId,\n" +
                "                        f.status AS fixtureStatus,\n" +
                "                        c.name AS competitionName,\n" +
                "                        c.sfid AS competitionId,\n" +
                "                        l.sfid AS resourceId,\n" +
                "                        l.name AS resourceTitle,\n" +
                "                        l.locationtimezone__c AS resourceTimezone,\n" +
                "                        lt.id AS locationTimeslotId,\n" +
                "                        lt.start_datetime AS startDateTime,\n" +
                "                        (lt.start_datetime + INTERVAL '1 MINUTE' * lt.duration) AS endDateTime,\n" +
                "                        lt.availability_rule AS availabilityRule,\n" +
                "                        lt.duration AS duration\n" +
                "                    FROM salesforce.location__c AS l\n" +
                "                    INNER JOIN heroku.locationtimeslot AS lt ON l.sfid = lt.location\n" +
                "                    INNER JOIN heroku.fixture AS f ON lt.id = f.location_timeslot\n" +
                "                    INNER JOIN heroku.round AS r ON f.round = r.id AND r.is_deleted = FALSE and r.is_archived = FALSE\n" +
                "                    INNER JOIN salesforce.competition__c AS c ON f.competition = c.sfid\n" +
                "                    INNER JOIN heroku.fixtureteam AS fth ON f.id = fth.fixture\n" +
                "                    INNER JOIN heroku.fixtureteam AS fta ON f.id = fta.fixture\n" +
                "                    INNER JOIN salesforce.team__c AS th ON (fth.team = th.sfid AND fth.home_away = 'Home')\n" +
                "                    INNER JOIN salesforce.team__c AS ta ON (fta.team = ta.sfid AND fta.home_away = 'Away')\n" +
                "                    WHERE 1=1" +
                "                    AND l.sfid = ANY(?)  ;";


        try {
            dbConnection.setAutoCommit(false);
            PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSql);
            Array array =  dbConnection.createArrayOf("VARCHAR", locationSfIds.toArray());

            preparedStatement.setArray(1, array);
            ResultSet result = preparedStatement.executeQuery();
            while (result.next() ){
                LocationTimeSlotDto locationTimeSlotDto = locationTimeSlotDtoItemRowMapper(result);

                locationTimeSlotDtos.add(locationTimeSlotDto);

            }
        } catch (SQLException e){
            dbConnection.rollback();
            LOG.error(e);
            return null;
        } finally {
            dbConnection.close();
        }

        return locationTimeSlotDtos;
    }

    private LocationTimeSlotDto locationTimeSlotDtoItemRowMapper(ResultSet result) throws SQLException {
        LocationTimeSlotDto locationTimeSlotDto = new LocationTimeSlotDto();
        locationTimeSlotDto.setEventTitle(result.getString("eventTitle"));
        locationTimeSlotDto.setHomeTeamName(result.getString("homeTeamName"));
        locationTimeSlotDto.setHomeTeamId(result.getString("homeTeamId"));
        locationTimeSlotDto.setAwayTeamName(result.getString("awayTeamName"));
        locationTimeSlotDto.setAwayTeamId(result.getString("awayTeamId"));

        locationTimeSlotDto.setFixtureId(result.getString("fixtureId"));
        locationTimeSlotDto.setFixtureStatus(result.getString("fixtureStatus"));
        locationTimeSlotDto.setCompetitionName(result.getString("competitionName"));
        locationTimeSlotDto.setCompetitionId(result.getString("competitionId"));

        locationTimeSlotDto.setResourceId(result.getString("resourceId"));
        locationTimeSlotDto.setResourceTitle(result.getString("resourceTitle"));
        locationTimeSlotDto.setResourceTimeZone(result.getString("resourceTimezone"));
        locationTimeSlotDto.setLocationTimeslotId(result.getString("locationTimeslotId"));
        locationTimeSlotDto.setStartDateTime(result.getDate("startDateTime"));
        locationTimeSlotDto.setEndDateTime(result.getDate("endDateTime"));

        locationTimeSlotDto.setAvailabilityRule(result.getString("availabilityRule"));
        locationTimeSlotDto.setDuration(result.getDouble("duration"));
        return locationTimeSlotDto;
    }

    private LocationAvailabilityRule locationeAvailabilityItemRowMapper(ResultSet result) throws SQLException {
        LocationAvailabilityRule locationAvailabilityRule = new LocationAvailabilityRule();
        locationAvailabilityRule.setId(result.getInt("id"));
        locationAvailabilityRule.setSfid(result.getString("sfid"));
        locationAvailabilityRule.setName(result.getString("name"));
        locationAvailabilityRule.setDurationC(result.getDouble("duration"));
        locationAvailabilityRule.setIsdeleted(result.getString("isDeleted"));
        locationAvailabilityRule.setStarttimeC(result.getTimestamp("startTime"));
        locationAvailabilityRule.setEnddateC(result.getDate("endDate"));
        locationAvailabilityRule.setLocationC(result.getString("location"));
        locationAvailabilityRule.setRepeatonC(result.getString("repeatOn"));
        locationAvailabilityRule.setCurrencyisocode(result.getString("currencyisocode"));
        locationAvailabilityRule.setLocationsplitpercentageC(result.getDouble("locationsplitpercentage"));
        locationAvailabilityRule.setRepeateveryxperiodC(result.getDouble("repeateveryxperiod"));
        locationAvailabilityRule.setAvailableforcompetitionteamC(result.getString("availableforcompetitionteam"));
        locationAvailabilityRule.setAvailablefororganisationC(result.getString("availablefororganisation"));
        locationAvailabilityRule.setRepeatperiodtypeC(result.getString("repeatperiodtype"));
        locationAvailabilityRule.setLocationsplitnameC(result.getString("locationsplitname"));
        locationAvailabilityRule.setAvailableforcompetitionC(result.getString("availableforcompetition"));
        locationAvailabilityRule.setEndafterxoccuranceC(result.getDouble("endafterxoccurance"));
        locationAvailabilityRule.setLocationTimeZone(result.getString("resourcetimezone"));
        locationAvailabilityRule.setAvailabilityRanking(result.getInt("availabilityranking"));
        return locationAvailabilityRule;
    }


}
