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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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


    public void findLocationTimeSlots(List<Fixture> fixtureList, List<Round> roundList, List<FixtureTeamRoundDto> fixtureTeamRoundDtoList, List<LocationAvailabilityRule> locationAvailabilityRuleList,
                                      Competition competition, List<LocationTimeSlotDto> locationTimeSlotDtoList, List<ExceptionDateDto> exceptionDateDtoList, List<FixtureTeamRoundDto> skippedFixtureList,
                                      Date lastPlayedDate ) throws Exception {

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

       /* //TODO : need to change this and deal with fixtureTeamRoundDtoList
        List<Long> roundNumberList = fixtureList.stream().map(item-> item.getRound()).collect(Collectors.toList());
        if(roundNumberList.size() ==0) throw new Exception("0 rounds");
        roundNumberList.sort(Comparator.comparing(item -> item));
       */
       //list.sort(Comparator.comparing(AnObject::getAttr));

        if(fixtureTeamRoundDtoList.size() ==0) throw new Exception("0 rounds");

        //TODO verify if it sorts by round name
       fixtureTeamRoundDtoList.sort(Comparator.comparing(FixtureTeamRoundDto::getRoundName));

       int roundIndex = 0;
       //loop each round
       for (FixtureTeamRoundDto f: fixtureTeamRoundDtoList
             ) {
          LocationTimeSlotDto locationTimeSlotDto = locationTimeSlotDtoList.remove(0);
          if(roundIndex >0){
              roundStartDate = DateUtils.addDays(roundEndDate, 1);
              roundEndDate =DateUtils.addDays(roundStartDate, daysBetweenRounds-1);
          }

          boolean exceptedFound = false;

           for (int dayIndex = 0; dayIndex < daysBetweenRounds; dayIndex++) {
               if(exceptedFound){
                   exceptedFound = false;
                   dayIndex=0;
               }


            Date SimpleDateFormat = DateUtils.addDays(roundStartDate, dayIndex);
            SimpleDateFormat simpleDateformat = new SimpleDateFormat("EEEE"); // the day of the week spelled out completely
            System.out.println(simpleDateformat.format(SimpleDateFormat));
               //simpleDateformat.format(

           }

          /*
          *  roundDayLoop: for (var dayIndex=0; dayIndex < daysBetweenRounds; dayIndex++) {



            //get the day of the week of this round day
            let thisDayOfWeek = thisDate.day();

            if (competitionDays.indexOf(thisDayOfWeek) != -1) {

                this.logger.info(thisDate.format('YYYY-MM-DD') + ' is a competition day of week');
                this.logger.info('check if ' + thisDate.format('YYYY-MM-DD') + ' is a competition exception day');
                let thisDateOnly = thisDate.format('YYYY-MM-DD');
                //check exception date
                let todayExceptionDates = competitionExceptionDates.filter(ed => {
                    let startDate = Moment(ed.start_date).format('YYYY-MM-DD');
                    let endDate = Moment(ed.end_date).format('YYYY-MM-DD');
                    return (thisDateOnly >= startDate && thisDateOnly <= endDate);
                });

                if (todayExceptionDates.length) {
                    this.logger.info(thisDate.format('YYYY-MM-DD') + ' is a competition exception day');
                    this.logger.info('skipping ' + thisDate.format('YYYY-MM-DD') + ', exception date found');
                    this.logger.info('reschedule the round to ' + daysBetweenRounds + ' days after');

                    exceptedFound = true;
                    roundStartDate = Moment(roundStartDate).add(daysBetweenRounds, 'days');
                    roundEndDate = Moment(roundEndDate).add(daysBetweenRounds, 'days');
                    this.logger.info('round start date rescheduled: ' + roundStartDate.format('YYYY-MM-DD'));
                    this.logger.info('round end date rescheduled: ' + roundEndDate.format('YYYY-MM-DD'));
                    continue roundDayLoop;
                }
                else {
                    this.logger.info(thisDate.format('YYYY-MM-DD') + ' is a competition day');
                }
            }
        }*/

           roundIndex++;

       }






    }

}
