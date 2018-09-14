package Manager;

import CustomComparator.AvailabilityRankingComparator;
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
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class LocationAvailabilityRuleManager implements Serializable {
    public static final String BYE = "BYE";
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

        Map<Long, Fixture> fixtureMap = fixtureList.stream().collect(
                Collectors.toMap(Fixture::getRound, item ->item));

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

        //TODO verify if its minute
        Double duration = 0.0;
        duration = competition.getGameTimeSlotLength();

        if(duration==0.0) throw new Exception("Fixture duraiton equals 0");
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
            if(competition.getDaysOfWeek().contains(simpleDateformat.format(SimpleDateFormat))){
                //exceptionDateDtoList.get(0).getExceptionDays()
                Date finalRoundStartDate = roundStartDate;
                //if start date falls between exception dates
                List<ExceptionDateDto> problemDates = exceptionDateDtoList.stream().filter(getExceptionDateOverlapFilterPredicate(finalRoundStartDate)).collect(Collectors.toList());
                if(problemDates.size()>0){
                    exceptedFound = true;
                    roundStartDate = DateUtils.addDays(roundStartDate, daysBetweenRounds);
                    roundEndDate = DateUtils.addDays(roundEndDate, daysBetweenRounds);
                }
            }
           }
           f.setRoundStartDate(roundStartDate);
           f.setRoundEnDate(roundEndDate);

           //now we have a round start and end date we can look at finding locations for the rounds

           //TODO need to check if we have to do this as I already have fixturerounddtor
           /*  //fixtures.
        let roundFixtures = fixtures.filter(f => f.round == round);
        this.logger.info('round fixture count: ' + roundFixtures.length);*/

           //find LARs within the round date range
           Date finalRoundStartDate1 = roundStartDate;
           Date finalRoundEndDate = roundEndDate;
           List<LocationAvailabilityRule> roundLars = locationAvailabilityRuleList.stream().filter(availableDateLARPredicate(finalRoundStartDate1, finalRoundEndDate)).collect(Collectors.toList());


           //loop each round fixture

           List<FixtureTeamRoundDto> roundFixture = fixtureTeamRoundDtoList.stream().filter(item-> item.getRoundId().equals(f.getRoundId())).collect(Collectors.toList());

           for (FixtureTeamRoundDto ftrd: roundFixture) {
               Fixture fixture = ftrd.getFixture();

               if(fixture.getHomeTeamId().equals(BYE) || fixture.getAwayTeamId().equals(BYE)) continue;
               //loop each day of round
               for (int dayIndex = 0; dayIndex < daysBetweenRounds; dayIndex++) {

                   Date thisDate = DateUtils.addDays(roundStartDate, daysBetweenRounds);

                   Date SimpleDateFormat = DateUtils.addDays(thisDate, dayIndex);
                   String thisDayOfWeek = getDayOfTheWeek(SimpleDateFormat);

                   //check if this is a competition day
                   if(competition.getDaysOfWeek().contains(thisDayOfWeek)){
                      List<LocationAvailabilityRule> dayLars = roundLars.stream().filter(getLocationAvailabilityRulePredicate(thisDate, fixture, competition)).collect(Collectors.toList());
                      // Collections.sort(availableOpponentListForSort, new OpponentComparator());
                      Collections.sort(dayLars, new AvailabilityRankingComparator());

                       //if a matching lar/s is found then check their availability
                       for (LocationAvailabilityRule lar:dayLars) {
                           Date larStartDate = lar.getStarttimeC();
                           //TODO verfiy this
                           Date todayStartDate =  thisDate;
                           todayStartDate.setTime(larStartDate.getTime());
                           Date todayEndDate = DateUtils.addMinutes(todayStartDate, duration.intValue());

                       }



                   }
               }
           }
           roundIndex++;
       }
    }

    private String getDayOfTheWeek(Date simpleDateFormat) {
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("EEEE");
        return simpleDateformat.format(simpleDateFormat);
    }

    private Predicate<LocationAvailabilityRule> getLocationAvailabilityRulePredicate(Date thisDate, Fixture fixture, Competition competition) {
        return locationAvailabilityRule -> {
            Timestamp starttimeC = locationAvailabilityRule.getStarttimeC();
            java.sql.Date enddateC = locationAvailabilityRule.getEnddateC();
            if(locationAvailabilityRule.getRepeatperiodtypeC()==null && starttimeC.equals(enddateC) && starttimeC == thisDate ) {
                return true;
            }

            if(availableEntityIsNotNullAndMatchesValidation(fixture, competition, locationAvailabilityRule)
                    ){
                String repeatPeriod = locationAvailabilityRule.getRepeatperiodtypeC().toLowerCase();
                //TODO - verfy if the vales below or rigth as it was transposed in nodejs and i didnt
                //TODO - followup of above todo..I changed it to values from db. need to check it while debugging
                if(repeatPeriod == "daily" || repeatPeriod == "weekly" || repeatPeriod == "monthly" || repeatPeriod == "yearly") {
                    if(dateAvailabilityValidation(thisDate, locationAvailabilityRule, starttimeC, enddateC)) {
                        String repeatonC = locationAvailabilityRule.getRepeatonC();
                        Date today = new Date();
                        Long differenceTime;
                        if(repeatonC.length()== 0){

                            differenceTime =  getDifferenceDays(starttimeC, today);
                            if (validateIfItsIntegerAndReturnTrue(locationAvailabilityRule, differenceTime)) return true;
                        }
                        if(repeatPeriod =="weekly" && repeatonC.length()>0){
                            String thisDayOfTheWeek = getDayOfTheWeek(thisDate);
                            if (competition.getDaysOfWeek().contains(thisDayOfTheWeek)){

                                //TODO verify if this is working during debugging. Translated the commented bit to the one below. IMPORTANT!!! please check
                                /*
                                 // Find the first forward occurence of this day of the week from the start date,
                                    // then divide by repeat value. If result is integer then the current fixture date is
                                    // is sequence to the rule.
                                    Number.isInteger(thisDate.diff(Moment(larStartDate).hour(0).minute(0).second(0).day(thisDate.day() <= thisDayOfWeek ?  thisDayOfWeek : thisDayOfWeek + 7), 'weeks') / l.repeatEvery)
                                * */
                                int larStartTimeDayofweek = DayOfWeek.valueOf(getDayOfTheWeek(starttimeC)).value;
                                int thisDateDayofweek = DayOfWeek.valueOf(thisDayOfTheWeek).value;
                                int daysOffset = 0;
                                if(thisDateDayofweek > larStartTimeDayofweek){
                                    larStartTimeDayofweek = larStartTimeDayofweek+7;
                                    daysOffset = larStartTimeDayofweek;
                                } else {
                                    daysOffset = thisDateDayofweek;
                                }

                                differenceTime = getDifferenceWeeks(DateUtils.addDays(starttimeC, daysOffset), today);
                                if (validateIfItsIntegerAndReturnTrue(locationAvailabilityRule, differenceTime)) return true;




                            }
                        }


                    }

                }

            }

            return false;
        };
    }

    //TODO: Verify this during debugging
    private boolean validateIfItsIntegerAndReturnTrue(LocationAvailabilityRule locationAvailabilityRule, Long differenceTime) {
        if (differenceTime/locationAvailabilityRule.getRepeateveryxperiodC()% 1 ==0 ){
            return true;
        }
        return false;
    }

    public enum DayOfWeek {
        Sunday(1),Monday(2),Tuesday(3),Wednesday(4),Thursday(5),Friday(6),Saturday(7);

        private final int value;

        DayOfWeek(int value) {

            this.value = value;
        }

        public int getValue() {

            return value;
        }

        @Override
        public String toString() {

            return value + "";
        }
    }


    public static long getDifferenceDays(Date d1, Date d2) {
        YearMonth m1 = YearMonth.from(d1.toInstant());
        YearMonth m2 = YearMonth.from(d2.toInstant());

        return m1.until(m2, ChronoUnit.DAYS) + 1;
    }


    public static long getDifferenceWeeks(Date d1, Date d2) {
        YearMonth m1 = YearMonth.from(d1.toInstant());
        YearMonth m2 = YearMonth.from(d2.toInstant());

        return m1.until(m2, ChronoUnit.WEEKS) + 1;
    }

    public static long getDifferenceMonths(Date d1, Date d2) {
        YearMonth m1 = YearMonth.from(d1.toInstant());
        YearMonth m2 = YearMonth.from(d2.toInstant());

        return m1.until(m2, ChronoUnit.MONTHS) + 1;
    }

    /* YearMonth m1 = YearMonth.from(date1.toInstant());
    YearMonth m2 = YearMonth.from(date2.toInstant());

    return m1.until(m2, ChronoUnit.MONTHS) + 1;*/



    private boolean dateAvailabilityValidation(Date thisDate, LocationAvailabilityRule locationAvailabilityRule, Timestamp starttimeC, java.sql.Date enddateC) {
        return thisDate.compareTo(starttimeC) >=0
                && thisDate.compareTo(enddateC) <=0
                &&  locationAvailabilityRule.getRepeateveryxperiodC() >0.0;
    }

    private boolean availableEntityIsNotNullAndMatchesValidation(Fixture fixture, Competition competition, LocationAvailabilityRule locationAvailabilityRule) {
        String availableforCompetitionteam = locationAvailabilityRule.getAvailableforcompetitionteamC();
        String availablForCompetition = locationAvailabilityRule.getAvailableforcompetitionC();
        String availableForOrganisation = locationAvailabilityRule.getAvailablefororganisationC();
        return (availableforCompetitionteam != null && availableforCompetitionteam.equals(fixture.getHomeTeamId()))
                || (availablForCompetition!=null && availablForCompetition.equals(competition.getSfId()))
                || (availableForOrganisation!=null && availableForOrganisation.equals(competition.getOrganisationOwner()));
    }


    private Predicate<LocationAvailabilityRule> availableDateLARPredicate(Date finalRoundStartDate1, Date finalRoundEndDate) {
        return item-> finalRoundStartDate1.compareTo(item.getStarttimeC())>=0 && finalRoundEndDate.compareTo(item.getEnddateC()) <=0;
    }

    private Predicate<ExceptionDateDto> getExceptionDateOverlapFilterPredicate(Date finalRoundStartDate) {
        return item->  finalRoundStartDate.compareTo(item.getStartDate()) >=0
                && finalRoundStartDate.compareTo(item.getEndDate()) <=0;
    }

}
