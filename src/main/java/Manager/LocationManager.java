package Manager;

import Dao.ExceptionDateDao;
import Dao.FixtureDao;
import Dao.LocationDao;
import Dao.SeasonDao;
import Dto.DateRangeDto;
import Dto.ExceptionDateDto;
import Dto.FixtureTeamRoundDto;
import Dto.RoundDto;
import Entity.Competition;
import Entity.CompetitionTeam;
import Entity.LocationAvailabilityRule;
import Entity.Season;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class LocationManager implements Serializable {
    Logger LOG = Logger.getLogger(LocationManager.class);
    public void deleteLocationTimeSlots(String compId) throws SQLException {
        FixtureDao fixtureDao = new FixtureDao();
        LocationDao locationDao = new LocationDao();

        //Delete location timeslot
        locationDao.deleteLocationTimeSlots(compId);

        //update fixture timeslot to null
        fixtureDao.clearLocationTimeSlotFromFixture(compId);


    }

    public void allocationFixtures(String compId, List<Integer>fixtureIds, boolean isFinal) throws Exception {
        //setup relevant managers and daos to get values
        CompetitionManager competitionManager = new CompetitionManager();
        RoundsManager roundsManager = new RoundsManager();
        FixtureManager fixtureManager = new FixtureManager();
        ExceptionDateDao exceptionDateDao = new ExceptionDateDao();

        //get detail about the competition this relates to
        Competition competition = competitionManager.getCompetitionById(compId);
        //get the cometition teams
        List<CompetitionTeam> competitionTeams = competitionManager.getActiveCompetitionTeamsByCompId(compId);
        //get fixture details //TODO - verify if this one needs Fixture details for processing- I think it needs. Then we have to update the query to do initFixtures like in FixtureDao
        List<FixtureTeamRoundDto> roundsFixtures = roundsManager.getRoundFixtureByFixtureIds(fixtureIds);
        //get competition rounds
        List<RoundDto> rounds = roundsManager.getRounds(compId);
        //get played rounds/fixtures
        List<FixtureTeamRoundDto> playedRoundFixtures = fixtureManager.getPlayedFixturesAndRounds(compId);
        //get last played date
        Date lastPlayedDate = getLastPlayedDate(compId);
        //get all rounds/fixtures
        List<FixtureTeamRoundDto> allFixtures = fixtureManager.getAllFixtures(compId);

        SeasonDao seasonDao = new SeasonDao();
        Season season = seasonDao.getSeasonById(competition.getSeason());

        DateRangeDto dateRangeDto = new DateRangeDto();
        dateRangeDto.setStartDate(competition.getStartDate());
        if(competition.getEndDate()!=null){
            dateRangeDto.setEndDate((java.sql.Date) DateUtils.addMonths(competition.getEndDate(), 6));
        } else {
            dateRangeDto.setEndDate((java.sql.Date) DateUtils.addMonths(season.getEnddateC(), 6));
        }
        dateRangeDto.setSeasonStartDate(season.getStartdateC());
        dateRangeDto.setSeasonEndDate(season.getEnddateC());

        inputDataValidation(compId, competition, competitionTeams, roundsFixtures, rounds, allFixtures, season);

        //TODO verfiy the season query logic
        //Followup - got season above. remove comment above after debugging

        String seasonId = competition.getSeason();

        //get competition exception dates
        List<ExceptionDateDto> competitionExceptionDates = new ArrayList<>();
        competitionExceptionDates = exceptionDateDao.getExceptionDates(seasonId, compId);

        //get location availability rules for the returned locations
        String organisationOwnerValue = competition.getAllowFixtureOutsideScheduledTime()==true? competition.getOrganisationOwner():null;
        //TODO verify this below
        List<String>competitionTeamIds = competitionTeams.stream().map(item->item.getTeam()).collect(Collectors.toList());
        List<LocationAvailabilityRule> locationAvailabilityRules = getLocationAvailabilityRules(organisationOwnerValue, compId,competitionTeamIds);












    }

    private List<LocationAvailabilityRule> getLocationAvailabilityRules(String organisationId, String compId, List<String> compTeamIds ) throws SQLException {
        LocationDao locationDao = new LocationDao();
        return locationDao.getLocationAvailabilityRules(false, organisationId, compId, compTeamIds);
    }

    private void inputDataValidation(String compId, Competition competition, List<CompetitionTeam> competitionTeams,
                                     List<FixtureTeamRoundDto> roundsFixtures, List<RoundDto> rounds,
                                     List<FixtureTeamRoundDto> allFixtures, Season season) throws Exception {
        if(competition==null) throw new Exception("No Competition exist for compid: " + compId);
        if(competition.getSeason()==null) throw new Exception("The competition season is undefined for compid: " + compId);
        if (competitionTeams ==null) throw new Exception("No competition teams exist for compid: " + compId);
        if(roundsFixtures==null) throw new Exception("No fixtures exist for compid: " + compId);
        if(rounds==null) throw new Exception("No rounds exist for compid: " + compId);
        if(allFixtures==null) throw new Exception("No fixtures exist for compid: " + compId);
        if(season==null) throw new Exception("No season exists for comp id: " + compId);
    }

    public Date getLastPlayedDate(String compId) throws SQLException {
        LocationDao locationDao = new LocationDao();
        return locationDao.getLastPlayedDate(compId);
    }


}
