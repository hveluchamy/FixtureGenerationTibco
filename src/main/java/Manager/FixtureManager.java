package Manager;

import Dao.FixtureDao;
import Dto.MatchDto;
import Dto.FixtureTeamRoundDto;
import Dto.RoundDto;
import Dto.TibcoFixtureGenerationDto;
import Entity.*;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import CustomComparator.*;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class FixtureManager implements Serializable {
    Logger LOG = Logger.getLogger(FixtureManager.class);

    public void startFixtureGeneration( TibcoFixtureGenerationDto tibcoFixtureGenerationDto) throws Exception {

        List<Team> teams = tibcoFixtureGenerationDto.getTeams();

        Map<String, Team> teamMap = teams.stream().collect(Collectors.toMap(Team::getSfid, item->item));
        List<FixtureTeamRoundDto> fixtureTeamRoundDtos;
        List<String> playedRounds;
        Integer roundsToGenerate;
        String competitionId = tibcoFixtureGenerationDto.getCompetition().getSfId();

        TeamManager teamManager = new TeamManager();

        LocationManager locationManager = new LocationManager();
        RoundsManager roundsManager = new RoundsManager();
        //TODO delete lcoationtimeslot and delete unplayed rounds - lines commented below for test purpose
        locationManager.deleteLocationTimeSlots(competitionId);
        roundsManager.deleteUnplayedRounds(competitionId);

        try {

            fixtureTeamRoundDtos = getPlayedFixturesAndRounds(competitionId);
            Set<String> rounds =  fixtureTeamRoundDtos.stream().map(f->{
                String round;
                round = f.getRoundId();
                return round;
            }).collect(Collectors.toSet());
            //playedRounds = (List<String>) rounds;
            playedRounds = rounds.stream().collect(Collectors.toList());
            roundsToGenerate = tibcoFixtureGenerationDto.getMaxRounds() - playedRounds.size();

            teamManager.assignByeTeam(teams);
            if(roundsToGenerate>0){
                List<FixtureStatistics> fixtureStatisticsList = getInitFixtureStatistics(teams);
                List<Round> roundList = addExistingFixtures(fixtureTeamRoundDtos, fixtureStatisticsList, playedRounds, teams);
                generateUnplayedRounds(roundsToGenerate, roundList,teams, fixtureStatisticsList);
                List<Round> newRounds = roundList.stream().filter(r->r.getNew().equals(true)).collect(Collectors.toList());
                List<RoundDto> newRoundDto = newRounds.stream().map(round ->
                                                                    {
                                                                        RoundDto dto = new RoundDto();
                                                                        dto.setName(round.getName());
                                                                        dto.setOrder_number(round.getOrderNumber());
                                                                        dto.setNumber(round.getOrderNumber());
                                                                        List<MatchDto> matchDtos = new ArrayList<>();
                                                                        for (Match matc: round.getMatch()
                                                                             ) {
                                                                            MatchDto matchDto = new MatchDto();

                                                                            String homeTeamSfId = matc.getHomeTeam().getTeamSfId();
                                                                            String awayTeamSfId = matc.getAwayTeam().getTeamSfId();
                                                                            if(homeTeamSfId.equals("Bye")){
                                                                                matchDto.setHome("Bye");
                                                                            } else {
                                                                                matchDto.setHome(homeTeamSfId  + (teamMap.get(homeTeamSfId).getExternalidC() == 0? "" : ";" + teamMap.get(homeTeamSfId).getExternalidC())
                                                                                        + (teamMap.get(homeTeamSfId).getName() ==null? "" : ";" + teamMap.get(homeTeamSfId).getName())
                                                                                );
                                                                            }

                                                                            if(awayTeamSfId.equals("Bye")){
                                                                                matchDto.setAway("Bye");
                                                                            } else {
                                                                                matchDto.setAway(awayTeamSfId + ";" + (teamMap.get(awayTeamSfId).getExternalidC()== 0? "" : ";" + teamMap.get(awayTeamSfId).getExternalidC())
                                                                                        + (teamMap.get(awayTeamSfId).getName() ==null? "" : ";" + teamMap.get(awayTeamSfId).getName()));
                                                                            }

                                                                            matchDtos.add(matchDto);
                                                                        }
                                                                        dto.setMatches(matchDtos);
                                                                        return dto;
                                                                    }).collect(Collectors.toList());
                String jsonStr = new Gson().toJson(newRoundDto);
                tibcoFixtureGenerationDto.setRounds(newRoundDto);
                String tibcoDtroJson = new Gson().toJson(tibcoFixtureGenerationDto);

                System.out.println("Finished genearting rounds " + jsonStr );

                System.out.println("Finished genearting rounds " + tibcoDtroJson );

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
        fixtureStatistics.addFixtureStatisticsOpponent(fixtureStatisticsOpponent);
        return fixtureStatistics;
    }

    //get played fixtures
    private List<FixtureTeamRoundDto> getPlayedFixturesAndRounds(String competitionId) throws SQLException {
        List<FixtureTeamRoundDto> fixtureTeamRoundDtoList = new ArrayList<>();
        FixtureDao fixtureDao = new FixtureDao();
        fixtureTeamRoundDtoList =  fixtureDao.getPlayedFixtures(competitionId);
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

                 List<Match> matches = new ArrayList<>();
                 addGameToFixture(fixtureTeamRoundDto, fixtureStatisticsList, round, matches);
            }


           rounds.add(round);


        }
        return rounds;
    }


    private List<FixtureStatistics> addGameToFixture(FixtureTeamRoundDto fixtureTeamRoundDto, List<FixtureStatistics> fixtureStatistics, Round round, List<Match> matches){
        //TODO - add bye team if teamid is null - check after debugging
        String homeTeamId = fixtureTeamRoundDto.getFixture().getHomeTeamId();
        String awayTeamId = fixtureTeamRoundDto.getFixture().getAwayTeamId();

        Match match = new Match();
        AvailableOpponent homeTeam = new AvailableOpponent();
        homeTeam.setTeamSfId(homeTeamId);

        AvailableOpponent awayTeam = new AvailableOpponent();
        awayTeam.setTeamSfId(awayTeamId);

        match.setHomeTeam(homeTeam);
        match.setAwayTeam(awayTeam);

        matches.add(match);

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
        if(fixtureStatisticsMap.get(teamSfid)==null){
            FixtureStatistics fixtureStatisticsTeam = getInitFixtureStatisticsPerTeam(teamSfid);
            fixtureStatisticsMap.put(teamSfid, fixtureStatisticsTeam);
        }


        FixtureStatistics f = fixtureStatisticsMap.get(teamSfid);


        FixtureResultStatistics fr = f.getFixtureResultStatistics();
        fr.setMatchesPlayed(increamentPropertyValueByOne(fr.getMatchesPlayed()));
        f.setFixtureResultStatistics(fr);

        //create team opponent team if not defined
        if(f.getFixtureStatisticsOpponent(opponentTeamSfid) == null)
        f = getInitFixtureStatisticsOpponent(opponentTeamSfid, f);

        //add this fixture to opponents matches_played
        FixtureStatisticsOpponent fo = f.getFixtureStatisticsOpponent(opponentTeamSfid);
        FixtureResultStatistics frsOpponent = fo.getFixtureResultStatistics();
        frsOpponent.setMatchesPlayed(increamentPropertyValueByOne(frsOpponent.getMatchesPlayed()));


        //calc rounds between last H2H between the teams
        if(frsOpponent.getLastPlayed()>0){
            frsOpponent.setDistribution((long) (round.getOrderNumber() - frsOpponent.getLastPlayed()));
        }
        //update last played opponent - This is used to calculate when they can play again after a minimum gap
        frsOpponent.setLastPlayed((long) round.getOrderNumber());
        Long currentStreak;
        //home game stats

      /* if(teamSfid.equals(homeTeamSfid)){

           //frsOpponent.setHomeGames(increamentPropertyValueByOne(frsOpponent.getHomeGames()));
           fr.setHomeGames(increamentPropertyValueByOne(fr.getHomeGames()));
           //frsOpponent.setLastHome(1L);
           frsOpponent.setLastAway(1L);
           fr.setLastHome(increamentPropertyValueByOne(fr.getLastHome()));
           fr.setLastAway(0L);
           currentStreak = fr.getLastHome();
           if(fr.getLastHome()>1){
               //remove streak
               fr.setHomeDistribution(currentStreak-1);
           }
           fr.setHomeDistribution(increamentPropertyValueByOne(currentStreak));
       } else {
           frsOpponent.setLastAway(0L);
           frsOpponent.setLastHome(1L);
           fr.setLastHome(0L);
           fr.setLastAway(increamentPropertyValueByOne(fr.getLastAway()));
           currentStreak = fr.getLastAway();
           if(fr.getLastAway()>1){
               fr.setAwayDistribution(currentStreak-1);
           }
           fr.setAwayDistribution(increamentPropertyValueByOne(currentStreak));
       }*/

       /*//The one below is translated from nodejs. aboce is the modification
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
        }*/

       //The one below is translated from nodejs. aboce is the modification
       if(teamSfid.equals(homeTeamSfid)){
           //TODO - verify why to increament opponents home game
           frsOpponent.setHomeGames(increamentPropertyValueByOne(frsOpponent.getHomeGames()));
           fr.setHomeGames(increamentPropertyValueByOne(fr.getHomeGames()));
           //TODO - verify whey set this to 1 for home game in opponent
           frsOpponent.setLastHome(1L);
           fr.setLastHome(increamentPropertyValueByOne(fr.getLastHome()));
           currentStreak = fr.getLastHome();
           fr.setLastAway(0L);
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

        fo.setFixtureResultStatistics(frsOpponent);
        f.addFixtureStatisticsOpponent(fo);
        //ratios
        fr.setHomeRatio(fr.getHomeGames()!=null ? fr.getHomeGames():0/fr.getMatchesPlayed());
        f.setFixtureResultStatistics(fr);

        fixtureStatisticsMap.put(teamSfid, f);
        List<FixtureStatistics> fixtureStatisticsListUpdated = new ArrayList<>(fixtureStatisticsMap.values());
        return  fixtureStatisticsListUpdated;
    }

    private long increamentPropertyValueByOne(Long increamentValue) {
        return increamentValue+1;
    }

    private List<Round> generateUnplayedRounds(Integer roundsToGenerate, List<Round> rounds, List<Team> teams, List<FixtureStatistics> fixtureStatistics) throws Exception {
        for(int i =0; i<roundsToGenerate; i++){
            int currentRound = rounds.size()+1;

            Round round = new Round();
            round.setOrderNumber(currentRound);
            round.setName("Round " + currentRound);
            round.setNew(true);

            List<Match> matches = generateRoundMatches(teams, fixtureStatistics, currentRound, round);

            round.setMatches(matches);
            rounds.add(round);

        }

        return rounds;
    }

    private List<Match> generateRoundMatches(List<Team> teams, List<FixtureStatistics> fixtureStatistics, int currentRound, Round round) throws Exception {


        Map<String, FixtureStatistics> fixtureStatisticsMap =  fixtureStatistics.stream().collect(
                Collectors.toMap(FixtureStatistics::getTeamSfId, item ->item));

        Map<String, AvailableOpponent> availableOpponentMap = getAvailableOpponents(teams, fixtureStatisticsMap, currentRound);

        List<AvailableOpponent> availableOpponentListForSort = availableOpponentMap.values().stream().collect(Collectors.toList());
        //List<AvailableOpponent> availableOpponentListOriginal = availableOpponentListForSort;

        List<Match> matchList = new ArrayList<>();

        while (availableOpponentListForSort.size()>0)
        {

            Map<String, FixtureStatisticsOpponent> bestOpponents;

            Collections.sort(availableOpponentListForSort, new OpponentComparator());

            Map<String, AvailableOpponent> finalAvailableOpponentSorted = new LinkedHashMap<>();
            availableOpponentListForSort.forEach(a-> finalAvailableOpponentSorted.put(a.getTeamSfId(), a));

            Map<String, AvailableOpponent> availableOpponentMapSorted;
            availableOpponentMapSorted = finalAvailableOpponentSorted;

            //TODO - need to verify during debugging
            AvailableOpponent currentTeam = availableOpponentListForSort.get(0);

            if (currentTeam.getUnplayedOptionsCount()>0){
                bestOpponents = currentTeam.getUnplayedOptions();
            } else if(currentTeam.getGoodOptionsCount()>0){
                bestOpponents = currentTeam.getGoodOptions();
            } else {
                bestOpponents = currentTeam.getBadOptions();
            }

            if(bestOpponents == null || bestOpponents.size()==0){
                throw new Exception("No Oppopnents available. Please contact administrator");
            }

            Map<String, AvailableOpponent> bestAvailableOpponentMap = new LinkedHashMap<>();
            for (String key: bestOpponents.keySet())
                bestAvailableOpponentMap.put(key, availableOpponentMapSorted.get(key));

            List<AvailableOpponent> rankedBestOpponentListForSort =  bestAvailableOpponentMap.values().stream().collect(Collectors.toList());

            availableOpponentMapSorted = deleteOpponentOption(availableOpponentMapSorted, teams, currentTeam);

            Collections.sort(rankedBestOpponentListForSort, new RankedOpponentComparator());

            //rankedBestOpponentListForSort.stream().forEach(availableOpponent -> System.out.println(availableOpponent.getTeamSfId()));

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

            /*// If our current home team had the last home game against this opponent, then swap
            if (fixtureStatistics[homeTeam.sfid]['opponents'].hasOwnProperty(awayTeam.sfid)
            && fixtureStatistics[homeTeam.sfid]['opponents'][awayTeam.sfid]['last_home']) {
            */
            // If our current home team had the last home game against this opponent, then swap
            if(fixtureStatisticsMap.get(homeTeam.getTeamSfId()).getFixtureStatisticsOpponent(awayTeam.getTeamSfId())!=null
                    && fixtureStatisticsMap.get(homeTeam.getTeamSfId()).getFixtureStatisticsOpponent(awayTeam.getTeamSfId()).getFixtureResultStatistics().getLastHome()>0){
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
                Double homeTeamAverage = (homeTeamToalGames !=null && homeTeamToalGames!=0) ? Double.valueOf(homeTeamHOmeGames)/Double.valueOf(homeTeamToalGames) : 0;

                //work out away team ave
                FixtureResultStatistics fixtureResultStatisticsAway = fixtureStatisticsMap.get(awayTeam.getTeamSfId()).getFixtureResultStatistics();
                Integer awayTeamTotalGames = Math.toIntExact(fixtureResultStatisticsAway.getMatchesPlayed());
                Integer awayTeamHomeGames = Math.toIntExact(fixtureResultStatisticsAway.getHomeGames());
                Double awayTeamAverage = (awayTeamTotalGames !=null && awayTeamTotalGames!=0)? Double.valueOf(awayTeamHomeGames)/Double.valueOf(awayTeamTotalGames):0;

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
                } else if(homeTeamHOmeGames !=null && awayTeamHomeGames !=null && homeTeamAverage.equals(awayTeamAverage)){
                    if(fixtureResultStatisticsHome.getLastHome()>fixtureResultStatisticsAway.getLastHome()){
                        homeTeam = currentOpponent;
                        awayTeam = currentTeam;
                    }
                }
            }
            //Remove these teams from the avaiable hash
            modifyOption(availableOpponentMapSorted, homeTeam, awayTeam);
            availableOpponentListForSort.clear();
            availableOpponentListForSort = availableOpponentMapSorted.values().stream().collect(Collectors.toList());

            Match match = new Match();
            match.setHomeTeam(homeTeam);
            match.setAwayTeam(awayTeam);


            matchList.add(match);
            //List<FixtureStatistics> fixtureStatisticsList = (List<FixtureStatistics>) fixtureStatisticsMap.values();
            fixtureStatistics= calculateStatistics(fixtureStatistics, round, awayTeam.getTeamSfId(), homeTeam.getTeamSfId() );
            fixtureStatisticsMap.clear();
            fixtureStatisticsMap =  fixtureStatistics.stream().collect(
                    Collectors.toMap(FixtureStatistics::getTeamSfId, item ->item));

        }
        return matchList;
    }

    private Map<String, AvailableOpponent> getAvailableOpponents(List<Team> teams, Map<String, FixtureStatistics> fixtureStatisticsMap, Integer roundNumber){
        /*Map<String, FixtureStatistics> fixtureStatisticsMap =  fixtureStatistics.stream().collect(
                Collectors.toMap(FixtureStatistics::getTeamSfId, item ->item));*/
        Integer minRoundsBetweenEncounters = teams.size()/2;
        Map<String, AvailableOpponent> availableOpponentMap = new HashMap<>();
        Integer teamIndex = 0;
        Integer opponentIndex;
        while (teamIndex < teams.size()) {
            Team team = teams.get(teamIndex);
            teamIndex = teamIndex + 1;
            opponentIndex = 0;
            while (opponentIndex < teams.size()) {
                Team opponent = teams.get(opponentIndex);
                opponentIndex = opponentIndex+1;
                if(!opponent.getSfid().equals(team.getSfid())){

                    FixtureStatistics fs = fixtureStatisticsMap.get(team.getSfid());
                    FixtureResultStatistics fixtureResultStatisticsTeam = fs.getFixtureResultStatistics();
                    FixtureStatisticsOpponent fixtureStatisticsOpponent = null;

                    try {
                        fixtureStatisticsOpponent = fs.getFixtureStatisticsOpponent(opponent.getSfid());
                    } catch (NullPointerException e){
                        fixtureStatisticsOpponent = null;
                    }

                    Integer matchesPlayed=0;
                    Integer lastPlayed = 0;
                    Integer teamTotalGames = Math.toIntExact(fixtureResultStatisticsTeam.getMatchesPlayed());
                    Integer teamHomeGames = Math.toIntExact(fixtureResultStatisticsTeam.getHomeGames());
                    Double teamHomeAverage;
                    Integer opponentTotalGames = 0;
                    Integer opponentHomeGames = 0;
                    Double opponentHomeAverage;

                    if(fixtureStatisticsOpponent!=null){
                         opponentTotalGames = Math.toIntExact(fixtureStatisticsOpponent.getFixtureResultStatistics().getMatchesPlayed()!=null? fixtureStatisticsOpponent.getFixtureResultStatistics().getMatchesPlayed() : 0);
                         opponentHomeGames = Math.toIntExact(fixtureStatisticsOpponent.getFixtureResultStatistics().getHomeGames());
                        //how many times has the team played the opponent?
                        if(fixtureStatisticsOpponent.getOpponentSfId().equals(opponent.getSfid())){
                            FixtureResultStatistics fixtureResultStatisticsOpponent = fixtureStatisticsOpponent.getFixtureResultStatistics();
                            matchesPlayed = Math.toIntExact((fixtureResultStatisticsOpponent.getMatchesPlayed() != 0) ? fixtureResultStatisticsOpponent.getMatchesPlayed() : 0);
                            lastPlayed = Math.toIntExact((fixtureResultStatisticsOpponent.getLastPlayed() != 0) ? fixtureResultStatisticsOpponent.getLastPlayed() : 0);
                        }
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
                        if( availableOpponent.getUnplayedOptions() ==null || availableOpponent.getUnplayedOptions().get(opponent.getSfid()) ==null ){
                            unPlayedOpponent = new FixtureStatisticsOpponent(opponent.getSfid());
                        } else {
                            unPlayedOpponent = availableOpponent.getUnplayedOptions().get(opponent.getSfid());
                        }
                        unPlayedOpponent.getFixtureResultStatistics().setMatchesPlayed(Long.valueOf(matchesPlayed));
                        availableOpponent.getUnplayedOptions().put(opponent.getSfid(), unPlayedOpponent);
                    } else if(teams.size() - 1 <= roundNumber - lastPlayed){
                        FixtureStatisticsOpponent goodOptionOpponent;
                        if(availableOpponent.getGoodOptions()==null || availableOpponent.getGoodOptions().get(opponent.getSfid()) == null){
                            goodOptionOpponent = new FixtureStatisticsOpponent(opponent.getSfid());
                        } else {
                            goodOptionOpponent = availableOpponent.getGoodOptions().get(opponent.getSfid());
                        }
                        goodOptionOpponent.getFixtureResultStatistics().setMatchesPlayed(Long.valueOf(matchesPlayed));
                        availableOpponent.getGoodOptions().put(opponent.getSfid(), goodOptionOpponent);
                    } else {
                        FixtureStatisticsOpponent badOptionOpponent;
                        if(availableOpponent.getBadOptions()==null || availableOpponent.getBadOptions().get(opponent.getSfid()) == null){
                            badOptionOpponent = new FixtureStatisticsOpponent(opponent.getSfid());
                        } else {
                            badOptionOpponent = availableOpponent.getBadOptions().get(opponent.getSfid());
                        }
                        badOptionOpponent.getFixtureResultStatistics().setMatchesPlayed(Long.valueOf(matchesPlayed));
                        availableOpponent.getBadOptions().put(opponent.getSfid(), badOptionOpponent);
                    }

                    if(availableOpponent.getOpponents().get(opponent.getSfid()) ==null){
                        FixtureStatisticsOpponent fso = new FixtureStatisticsOpponent(opponent.getSfid());
                        availableOpponent.getOpponents().put(opponent.getSfid(), fso);
                    }

                    teamHomeAverage = (teamTotalGames !=null && teamTotalGames !=0) ? Double.valueOf(teamHomeGames)/Double.valueOf(teamTotalGames) : 0;
                    opponentHomeAverage = (opponentTotalGames != null && opponentTotalGames != 0) ? Double.valueOf(opponentHomeGames)/Double.valueOf(opponentTotalGames) : 0;
                    availableOpponent.getOpponents().get(opponent.getSfid()).getFixtureResultStatistics().setHomeAwayDiff(Long.valueOf((long) Math.abs(teamHomeAverage - opponentHomeAverage)));

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
        availableOpponentsList.remove(homeTeam.getTeamSfId());
        availableOpponentsList.remove(awayTeam.getTeamSfId());
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
