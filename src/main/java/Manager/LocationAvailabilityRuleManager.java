package Manager;

import Dto.ExceptionDateDto;
import Dto.FixtureTeamRoundDto;
import Dto.LocationTimeSlotDto;
import Entity.Competition;
import Entity.Fixture;
import Entity.LocationAvailabilityRule;
import Entity.Round;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class LocationAvailabilityRuleManager implements Serializable {
    Logger LOG = Logger.getLogger(LocationAvailabilityRuleManager.class);

    public List<LocationAvailabilityRule> reduceLocationAvailabilityRules(List<LocationAvailabilityRule> locationAvailabilityRuleList, Competition competition) throws Exception {
        Double duration = 0.0;
        duration = competition.getGameTimeSlotLength();
        if(duration.equals(0.0)){
            throw new Exception("Fixutre duration equals 0");
        }

        for (LocationAvailabilityRule lar: locationAvailabilityRuleList
             ) {
            if(lar.getStarttimeC() == null || lar.getEnddateC() ==null|| lar.getDurationC() < duration){
                locationAvailabilityRuleList.remove(lar);
            } else {




                //TODO : need to see why its transposed like this in nodejs
                /*..
                *  let sfMomentMap = {
                  daily: 'days',
                  weekly: 'weeks',
                  monthly: 'months',
                  yearly: 'years'
              }*/



            }
        }

        return locationAvailabilityRuleList;
    }


    public void findLocationTimeSlots(List<Fixture> fixtureList, List<Round> roundList, List<LocationAvailabilityRule> locationAvailabilityRuleList, Competition competition,
                                      List<LocationTimeSlotDto> locationTimeSlotDtoList, List<ExceptionDateDto> exceptionDateDtoList, List<FixtureTeamRoundDto> skippedFixtureList, Date lastPlayedDate ) throws Exception {

        List<Date> skippedDateList = skippedFixtureList.stream().filter(fixture -> fixture.getRoundEnDate()!=null).map(item -> item.getRoundEnDate()).sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        Date lastSkippedRoundDate = skippedDateList.get(0)!= null?skippedDateList.get(0):null;
        if(lastSkippedRoundDate==null){
            lastSkippedRoundDate = lastPlayedDate;
        }

        Integer daysBetweenRounds = competition.getDaysBetweenRounds()!=null? competition.getDaysBetweenRounds() : 7;

        if(daysBetweenRounds ==0) throw new Exception("0 days between rounds");

        Date currentDate = new Date();

        Date roundStartDate = null;
        Date roundEndDate = null;

        // Workout the default round start date
        if(lastSkippedRoundDate !=null){
            roundStartDate = lastSkippedRoundDate;
            roundStartDate= DateUtils.addDays(roundStartDate, 1);
        } else {
            if(currentDate.before(competition.getStartDate())){
                roundStartDate = currentDate;
            }

        }

        // Workout the default round end date from the round start date

        roundEndDate = DateUtils.addDays(roundStartDate, daysBetweenRounds-1);

        Double duration = 0.0;
        duration = competition.getGameTimeSlotLength();

        if(duration==0.0) throw new Exception("Fixture duraiton equals 0");



        /*  // Workout the default round start date
        //group the rounds we'll be usings
        let groupedRounds = LocationTimeslotAvailability._groupBy(fixtures, 'round');
        this.logger.info('groupedRounds', groupedRounds);
        this.logger.info(groupedRounds.length + ' grouped rounds retrieved.');
        if (groupedRounds.length == 0) throw new Error('Warning: 0 rounds.');*/
    }

}
