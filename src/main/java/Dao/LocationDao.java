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

    public List<LocationTimeSlotDto> getLocationTimeSlotList(){
        List<LocationTimeSlotDto> locationTimeSlotDtos = new ArrayList<>();

        return locationTimeSlotDtos;
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
        locationAvailabilityRule.setTeamhomegamelocationpreferencerankingC(result.getDouble("availabilityranking"));
        return locationAvailabilityRule;
    }


}
