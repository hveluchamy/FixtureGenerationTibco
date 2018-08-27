package Manager;

import Dao.FixtureDao;
import Dto.FixtureTeamRoundDto;
import Entity.*;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class FixtureManager implements Serializable {
    Logger LOG = Logger.getLogger(FixtureManager.class);

    public void startFixtureGeneration(Long maxRounds, List<Team> teams){


        List<FixtureTeamRoundDto> fixtureTeamRoundDtos = new ArrayList<>();
        List<String> playedRounds = new ArrayList<>();
        Long roundsToGenerate;
        try {
            //TODO pass compid
            fixtureTeamRoundDtos = getPlayedFixturesAndRounds();
            Set<String> rounds =  fixtureTeamRoundDtos.stream().map(f->{
                String round;
                round = f.getRoundId();
                return round;
            }).collect(Collectors.toSet());
            playedRounds = (List<String>) rounds;
            roundsToGenerate = maxRounds - playedRounds.size();
            //TODO get bye team if odd number of teams
            if(roundsToGenerate>0){
                List<FixtureStatistics> fixtureStatisticsList = getInitFixtureStatistics(teams);
                List<Round> roundList = addExistingFixtures(fixtureTeamRoundDtos, fixtureStatisticsList, playedRounds, teams);

                generateUnplayedRounds(roundsToGenerate, roundList,teams, fixtureStatisticsList);

                List<Round> newRounds = roundList.stream().filter(r->r.getNew().equals(true)).collect(Collectors.toList());



                /* local.rounds = FixtureGenerationEngine.addExistingFixtures(local.playedFixtures, local.fixtureStatistics);
                    //add unplayed rounds
                    local.rounds = FixtureGenerationEngine.generateUnplayedRounds(local.roundsToGenerate, local.rounds, local.teams, local.fixtureStatistics);*/
            }
        } catch (SQLException e) {
           LOG.error("Error getting played fixtures", e);
        }
    }

    private List<FixtureStatistics> getInitFixtureStatistics(List<Team> teams) {
        return teams.stream().map(team -> getInitFixtureStatisticsPerTeam(team.getSfid())).collect(Collectors.toList());
    }

    private FixtureStatistics getInitFixtureStatisticsPerTeam(String teamSfid) {
        FixtureResultStatistics fixtureResultStatistics = new FixtureResultStatistics(false);
        FixtureStatistics fixtureStatistics = new FixtureStatistics(teamSfid, fixtureResultStatistics);
        return fixtureStatistics;
    }

    private FixtureStatistics getInitFixtureStatisticsOpponent(String opponentTeamSfid, FixtureStatistics fixtureStatistics){
        FixtureResultStatistics fixtureResultStatistics = new FixtureResultStatistics(true);
        FixtureStatisticsOpponent fixtureStatisticsOpponent = new FixtureStatisticsOpponent();
        fixtureStatisticsOpponent.setOpponentSfId(opponentTeamSfid);
        fixtureStatisticsOpponent.setFixtureResultStatistics(fixtureResultStatistics);
        fixtureStatistics.setFixtureStatisticsOpponent(fixtureStatisticsOpponent);
        return fixtureStatistics;
    }

    //get played fixtures
    //TODO pass compid
    private List<FixtureTeamRoundDto> getPlayedFixturesAndRounds() throws SQLException {
        List<FixtureTeamRoundDto> fixtureTeamRoundDtoList = new ArrayList<>();
        FixtureDao fixtureDao = new FixtureDao();
        //TODO - send proper comp id ofcourse
        fixtureTeamRoundDtoList =  fixtureDao.getPlayedFixtures("a0A2800000Ri0adEAB");

        return fixtureTeamRoundDtoList;
    }


    private List<Round> addExistingFixtures(List<FixtureTeamRoundDto> fixtureTeamRoundDtoList, List<FixtureStatistics> fixtureStatisticsList, List<String> roundIdListStr, List<Team> teams){
        int index = 1;
        List<Round> rounds = new ArrayList<>();
        for (String roundId : roundIdListStr) {
            Round round = new Round();
            round.setOrderNumber(index);
            round.setNew(false);
            index = index + 1;
            List<FixtureTeamRoundDto> fixtureTeamRoundDtos = fixtureTeamRoundDtoList.stream().filter(f -> f.getRoundId().equals(roundId)).collect(Collectors.toList());
            round.setName(fixtureTeamRoundDtos.get(0).getRoundName());


            for (FixtureTeamRoundDto fixtureTeamRoundDto:fixtureTeamRoundDtos
                 ) {
                 addGameToFixture(fixtureTeamRoundDto, fixtureStatisticsList, round);
            }


           rounds.add(round);


        }
        return rounds;
    }


    private List<FixtureStatistics> addGameToFixture(FixtureTeamRoundDto fixtureTeamRoundDto, List<FixtureStatistics> fixtureStatistics, Round round){
        //TODO - add bye team if teamid is null
        String homeTeamId = fixtureTeamRoundDto.getFixture().getHomeTeamId();
        String awayTeamId = fixtureTeamRoundDto.getFixture().getAwayTeamId();
        return calculateStatistics(fixtureStatistics, round, awayTeamId, homeTeamId);
    }

    private List<FixtureStatistics> calculateStatistics(List<FixtureStatistics> fixtureStatisticsList, Round  round,  String opponentTeamSfid, String homeTeamSfid){
        fixtureStatisticsList = calculateTeamStatistics(fixtureStatisticsList, round, homeTeamSfid, opponentTeamSfid, homeTeamSfid);
        fixtureStatisticsList = calculateTeamStatistics(fixtureStatisticsList, round, opponentTeamSfid, homeTeamSfid, homeTeamSfid);
        return fixtureStatisticsList;
    }

    private List<FixtureStatistics> calculateTeamStatistics(List<FixtureStatistics> fixtureStatisticsList, Round round, String teamSfid, String opponentTeamSfid, String homeTeamSfid){
        Map<String, FixtureStatistics> fixtureStatisticsMap =  fixtureStatisticsList.stream().collect(
                Collectors.toMap(FixtureStatistics::getTeamSfId, item ->item));

        //TODO verify if this below is required as its been created already above and has team sfid
        //FixtureStatistics fixtureStatisticsTeam = getInitFixtureStatisticsPerTeam(team);

        FixtureStatistics f = fixtureStatisticsMap.get(teamSfid);


        FixtureResultStatistics fr = f.getFixtureResultStatistics();
        fr.setMatchesPlayed(increamentPropertyValueByOne(fr.getMatchesPlayed()));
        f.setFixtureResultStatistics(fr);
        //create team opponent team if not defined
        f = getInitFixtureStatisticsOpponent(opponentTeamSfid, f);

        //add this fixture to opponents matches_played
        FixtureStatisticsOpponent fo = f.getFixtureStatisticsOpponent();
        FixtureResultStatistics frsOpponent = fo.getFixtureResultStatistics();
        frsOpponent.setMatchesPlayed(increamentPropertyValueByOne(frsOpponent.getMatchesPlayed()));


        //calc rounds between last H2H between the teams
        if(frsOpponent.getLastPlayed()>0){
            frsOpponent.setDistribution((long) (round.getOrderNumber() - frsOpponent.getLastPlayed()));
        }
        //update last played opponent
        frsOpponent.setLastPlayed((long) round.getOrderNumber());
        Long currentStreak;
        //home game stats
        if(teamSfid.equals(homeTeamSfid)){
            //TODO - verify why to increament opponents home game
            frsOpponent.setHomeGames(increamentPropertyValueByOne(frsOpponent.getHomeGames()));
            fr.setHomeGames(increamentPropertyValueByOne(fr.getHomeGames()));
            //TODO - verify whey set this to 1 for home game in opponent
            frsOpponent.setLastHome(1L);
            fr.setLastHome(increamentPropertyValueByOne(fr.getLastHome()));
            currentStreak = fr.getLastHome();
            if(fr.getLastHome()>1){
                //remove streak
                fr.setHomeDistribution(currentStreak-1);
            }
            fr.setHomeDistribution(increamentPropertyValueByOne(currentStreak));
        } else {
            frsOpponent.setLastHome(0L);
            fr.setLastHome(0L);
            fr.setLastAway(increamentPropertyValueByOne(fr.getLastAway()));
            currentStreak = fr.getLastAway();
            if(fr.getLastAway()>1){
                fr.setAwayDistribution(currentStreak-1);
            }
            fr.setAwayDistribution(increamentPropertyValueByOne(currentStreak));
        }
        //ratios
        fr.setHomeRatio(fr.getHomeGames()!=null ? fr.getHomeGames():0/fr.getMatchesPlayed());

        fixtureStatisticsMap.put(teamSfid, f);
        List<FixtureStatistics> fixtureStatisticsListUpdated = new ArrayList<>(fixtureStatisticsMap.values());
        return  fixtureStatisticsListUpdated;
    }

    private long increamentPropertyValueByOne(Long increamentValue) {
        return increamentValue+1;
    }

    private List<Round> generateUnplayedRounds(Long roundsToGenerate, List<Round> rounds, List<Team> teams, List<FixtureStatistics> fixtureStatistics){
        for(int i =0; i<roundsToGenerate; i++){
            int currentRound = rounds.size();

            //TODO - need to bubble this up
            try {
               Match match = generateRound(teams, fixtureStatistics, currentRound);
               Round round = new Round();
               round.setOrderNumber(currentRound);
               round.setName("Round " + currentRound);
               round.setNew(true);
               round.setMatch(match);
               rounds.add(round);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return rounds;
    }

    private Match generateRound(List<Team> teams, List<FixtureStatistics> fixtureStatistics, int currentRound) throws Exception {


        Map<String, FixtureStatistics> fixtureStatisticsMap =  fixtureStatistics.stream().collect(
                Collectors.toMap(FixtureStatistics::getTeamSfId, item ->item));

        Map<String, AvailableOpponent> availableOpponentMap = getAvailableOpponents(teams, fixtureStatisticsMap, currentRound);

        List<AvailableOpponent> availableOpponentListForSort = (List<AvailableOpponent>) availableOpponentMap.values();
        List<AvailableOpponent> availableOpponentListOriginal = availableOpponentListForSort;

        //TODO - need to veirfy whcih list to use for looping(js uses the one where it removes avaopponent)
        for (AvailableOpponent availableOpponent: availableOpponentListOriginal
             ) {

            Map<String, FixtureStatisticsOpponent> bestOpponents;

            Map<String, FixtureStatisticsOpponent> rankedOpponents;

            Collections.sort(availableOpponentListForSort, new OpponentComparator());

            Map<String, AvailableOpponent> finalAvailableOpponentSorted = new LinkedHashMap<>();
            availableOpponentListForSort.forEach(a-> finalAvailableOpponentSorted.put(a.getTeamSfId(), a));

            Map<String, AvailableOpponent> availableOpponentMapSorted;
            availableOpponentMapSorted = finalAvailableOpponentSorted;
            //TODO - need to verify
            AvailableOpponent currentTeam = availableOpponentListForSort.get(0);

            if (currentTeam.getGoodOptionsCount()>0){
                bestOpponents = currentTeam.getGoodOptions();
            } else if(currentTeam.getUnplayedOptionsCount()>0){
                bestOpponents = currentTeam.getUnplayedOptions();
            } else {
                bestOpponents = currentTeam.getBadOptions();
            }

            if(bestOpponents == null){
                throw new Exception("No Oppopnents available. Please contact administrator");
            }

            Map<String, AvailableOpponent> bestAvailableOpponentMap = new LinkedHashMap<>();
            for (String key: bestOpponents.keySet())
                bestAvailableOpponentMap.put(key, availableOpponentMapSorted.get(key));

            List<AvailableOpponent> rankedBestOpponentListForSort = (List<AvailableOpponent>) bestAvailableOpponentMap.values();

            availableOpponentMapSorted = deleteOpponentOption(availableOpponentMapSorted, teams, currentTeam);

            Collections.sort(rankedBestOpponentListForSort, new RankedOpponentComparator());

            // Work out who should have the home game

            // There are a few rules we are trying to stick to
            //
            // 1. If Team A and Team B have already played, then they should alternate home and away matches
            // 2. If Team A and Team B have not played, then priority is given to the team with the fewest
            //       average home games
            // 3. If Team A and Team B have not played, but both average the same number of home games,
            //       then priority is given to the team with the lowest running current home game streak
            // 3. If Team has played games, but the other hasnt,

            // Set these as defaults

            AvailableOpponent currentOpponent = rankedBestOpponentListForSort.get(0);

            AvailableOpponent homeTeam = currentTeam;
            AvailableOpponent awayTeam = currentOpponent;

            if(fixtureStatisticsMap.get(homeTeam.getTeamSfId()).getFixtureStatisticsOpponent().getOpponentSfId().equals(currentOpponent.getTeamSfId())){
                homeTeam = currentOpponent;
                awayTeam = currentTeam;
            }
            // If the two teams havent played eachother
            else {
                //need to see if one team has had more home games on average...

                //work out home team ave
                FixtureResultStatistics fixtureResultStatisticsHome = fixtureStatisticsMap.get(homeTeam.getTeamSfId()).getFixtureResultStatistics();
                Integer homeTeamToalGames = Math.toIntExact(fixtureResultStatisticsHome.getMatchesPlayed());
                Integer homeTeamHOmeGames = Math.toIntExact(fixtureResultStatisticsHome.getHomeGames());
                Integer homeTeamAverage = (homeTeamToalGames !=null) ? homeTeamHOmeGames/homeTeamToalGames : 0;

                //work out away team ave
                FixtureResultStatistics fixtureResultStatisticsAway = fixtureStatisticsMap.get(awayTeam.getTeamSfId()).getFixtureResultStatistics();
                Integer awayTeamTotalGames = Math.toIntExact(fixtureResultStatisticsAway.getMatchesPlayed());
                Integer awayTeamHomeGames = Math.toIntExact(fixtureResultStatisticsAway.getHomeGames());
                Integer awayTeamAverage = (awayTeamTotalGames !=null)? awayTeamHomeGames/awayTeamTotalGames:0;

                // If one teams opponent hasn't played any games, but the other team has
                // then work out if they are above or below their average home games
                if(homeTeamToalGames ==0 && awayTeamTotalGames > 0){
                    if(homeTeamAverage<0.5){
                        homeTeam = currentOpponent;
                        awayTeam = currentTeam;
                    }
                } else if(awayTeamTotalGames ==0 && homeTeamToalGames > 0){
                    if(homeTeamAverage >=0.5){
                        homeTeam = currentOpponent;
                        awayTeam = currentTeam;
                    }
                }
                // If teams have both played games, then we need to work out which one
                // has had the most home games on average...
                else if(homeTeamAverage>awayTeamAverage){
                    homeTeam = currentOpponent;
                    awayTeam = currentTeam;
                } else if(homeTeamHOmeGames !=null && awayTeamHomeGames !=null && homeTeamAverage>awayTeamAverage){
                    if(fixtureResultStatisticsHome.getLastHome()>fixtureResultStatisticsAway.getLastHome()){
                        homeTeam = currentOpponent;
                        awayTeam = currentTeam;
                    }
                }
            }
            //Remove these teams from the avaiable hash
            modifyOption(availableOpponentMapSorted, homeTeam, awayTeam);

            Match match = new Match();
            match.setHomeTeam(homeTeam);
            match.setAwayTeam(awayTeam);

            Round round = new Round();
            round.setOrderNumber(currentRound);
            //List<FixtureStatistics> fixtureStatisticsList = (List<FixtureStatistics>) fixtureStatisticsMap.values();
            calculateStatistics(fixtureStatistics, round, currentOpponent.getTeamSfId(), currentTeam.getTeamSfId() );
            return match;
        }
        return null;
    }

    private Map<String, AvailableOpponent> getAvailableOpponents(List<Team> teams, Map<String, FixtureStatistics> fixtureStatisticsMap, Integer roundNumber){
        /*Map<String, FixtureStatistics> fixtureStatisticsMap =  fixtureStatistics.stream().collect(
                Collectors.toMap(FixtureStatistics::getTeamSfId, item ->item));*/
        Integer minRoundsBetweenEncounters = teams.size()/2;
        Map<String, AvailableOpponent> availableOpponentMap = new HashMap<>();
        for (Team team: teams) {
            for (Team opponent:teams) {

                if(!opponent.getSfid().equals(team.getSfid())){

                    FixtureStatistics fs = fixtureStatisticsMap.get(team.getSfid());
                    FixtureResultStatistics fixtureResultStatisticsTeam = fs.getFixtureResultStatistics();
                    FixtureStatisticsOpponent fixtureStatisticsOpponent = fs.getFixtureStatisticsOpponent();

                    Integer matchesPlayed=0;
                    Integer lastPlayed = 0;
                    Integer teamTotalGames = Math.toIntExact(fixtureResultStatisticsTeam.getMatchesPlayed());
                    Integer teamHomeGames = Math.toIntExact(fixtureResultStatisticsTeam.getHomeGames());
                    Integer teamHomeAverage = 0;
                    Integer opponentTotalGames = Math.toIntExact(fixtureStatisticsOpponent.getFixtureResultStatistics().getMatchesPlayed());
                    Integer opponentHomeGames = Math.toIntExact(fixtureStatisticsOpponent.getFixtureResultStatistics().getHomeGames());
                    Integer opponentHomeAverage = 0;

                    //how many times has the team played the opponent?
                    if(fs.getFixtureStatisticsOpponent().getOpponentSfId().equals(opponent.getSfid())){
                        FixtureResultStatistics fixtureResultStatisticsOpponent = fixtureStatisticsOpponent.getFixtureResultStatistics();
                        matchesPlayed = Math.toIntExact((fixtureResultStatisticsOpponent.getMatchesPlayed() != 0) ? fixtureResultStatisticsOpponent.getMatchesPlayed() : 0);
                        lastPlayed = Math.toIntExact((fixtureResultStatisticsOpponent.getLastPlayed() != 0) ? fixtureResultStatisticsOpponent.getLastPlayed() : 0);
                    }

                    //if played each other before they're suppose to, skip to next opponent
                    if ((lastPlayed == 1 && minRoundsBetweenEncounters > roundNumber - lastPlayed)){
                        continue;
                    }

                    //init the avaiable team opponents
                    AvailableOpponent availableOpponent;
                    availableOpponent = availableOpponentMap.get(team.getSfid());
                    if(availableOpponent == null){
                        availableOpponent = new AvailableOpponent();
                        availableOpponent.setTeamSfId(team.getSfid());
                    }


                    //If the number of opponents is greater than the number of rounds between playing this opponent
                    //then mark them as having played recently so that we don't prioritise them
                    if(lastPlayed==0){
                        FixtureStatisticsOpponent unPlayedOpponent;
                        if( availableOpponent.getUnplayedOptions().get(opponent.getSfid()) ==null ){
                            unPlayedOpponent = new FixtureStatisticsOpponent(opponent.getSfid());
                        } else {
                            unPlayedOpponent = availableOpponent.getUnplayedOptions().get(opponent.getSfid());
                        }
                        unPlayedOpponent.getFixtureResultStatistics().setMatchesPlayed(Long.valueOf(matchesPlayed));
                        availableOpponent.getUnplayedOptions().put(opponent.getSfid(), unPlayedOpponent);
                    } else if(teams.size() - 1 <= roundNumber - lastPlayed){
                        FixtureStatisticsOpponent goodOptionOpponent;
                        if(availableOpponent.getGoodOptions().get(opponent.getSfid()) == null){
                            goodOptionOpponent = new FixtureStatisticsOpponent(opponent.getSfid());
                        } else {
                            goodOptionOpponent = availableOpponent.getGoodOptions().get(opponent.getSfid());
                        }
                        goodOptionOpponent.getFixtureResultStatistics().setMatchesPlayed(Long.valueOf(matchesPlayed));
                        availableOpponent.getGoodOptions().put(opponent.getSfid(), goodOptionOpponent);
                    } else {
                        FixtureStatisticsOpponent badOptionOpponent;
                        if(availableOpponent.getBadOptions().get(opponent.getSfid()) == null){
                            badOptionOpponent = new FixtureStatisticsOpponent(opponent.getSfid());
                        } else {
                            badOptionOpponent = availableOpponent.getBadOptions().get(opponent.getSfid());
                        }
                        badOptionOpponent.getFixtureResultStatistics().setMatchesPlayed(Long.valueOf(matchesPlayed));
                        availableOpponent.getBadOptions().put(opponent.getSfid(), badOptionOpponent);
                    }



                    teamHomeAverage = (teamTotalGames !=null) ? teamHomeGames/teamTotalGames : 0;
                    opponentHomeAverage = (opponentTotalGames != null) ? opponentHomeGames/opponentTotalGames : 0;
                    availableOpponent.getOpponents().get(opponent.getSfid()).getFixtureResultStatistics().setHomeAwayDiff(Long.valueOf(Math.abs(teamHomeAverage - opponentHomeAverage)));

                    availableOpponent.setBadOptionsCount(availableOpponent.getBadOptions().size());
                    availableOpponent.setUnplayedOptionsCount(availableOpponent.getUnplayedOptions().size());
                    availableOpponent.setGoodOptionsCount(availableOpponent.getGoodOptions().size());
                    availableOpponent.setOptionCount(availableOpponent.getOpponents().size());

                    availableOpponentMap.put(team.getSfid(), availableOpponent);
                }
            }
        }
        return availableOpponentMap;
    }

    public void modifyOption(Map<String, AvailableOpponent> availableOpponentsList, AvailableOpponent homeTeam, AvailableOpponent awayTeam){
        availableOpponentsList.forEach((k,availableOpponent)->{
            availableOpponent.getOpponents().remove(homeTeam.getTeamSfId());
            availableOpponent.getUnplayedOptions().remove(homeTeam.getTeamSfId());
            availableOpponent.getGoodOptions().remove(homeTeam.getTeamSfId());
            availableOpponent.getBadOptions().remove(homeTeam.getTeamSfId());

            availableOpponent.getOpponents().remove(awayTeam.getTeamSfId());
            availableOpponent.getUnplayedOptions().remove(awayTeam.getTeamSfId());
            availableOpponent.getGoodOptions().remove(awayTeam.getTeamSfId());
            availableOpponent.getBadOptions().remove(awayTeam.getTeamSfId());

            availableOpponent.setBadOptionsCount(availableOpponent.getBadOptions().size());
            availableOpponent.setUnplayedOptionsCount(availableOpponent.getUnplayedOptions().size());
            availableOpponent.setGoodOptionsCount(availableOpponent.getGoodOptions().size());
            availableOpponent.setOptionCount(availableOpponent.getOpponents().size());
        });

    }

    public Map<String, AvailableOpponent> deleteOpponentOption(Map<String, AvailableOpponent> availableOpponentsList, List<Team> teams, AvailableOpponent selectedOpponent){
        availableOpponentsList.forEach((k,availableOpponent)->{
            availableOpponent.getOpponents().remove(selectedOpponent.getTeamSfId());
            availableOpponent.getUnplayedOptions().remove(selectedOpponent.getTeamSfId());
            availableOpponent.getGoodOptions().remove(selectedOpponent.getTeamSfId());
            availableOpponent.getBadOptions().remove(selectedOpponent.getTeamSfId());

            availableOpponent.setBadOptionsCount(availableOpponent.getBadOptions().size());
            availableOpponent.setUnplayedOptionsCount(availableOpponent.getUnplayedOptions().size());
            availableOpponent.setGoodOptionsCount(availableOpponent.getGoodOptions().size());
            availableOpponent.setOptionCount(availableOpponent.getOpponents().size());

        });
        return availableOpponentsList;
    }
}
