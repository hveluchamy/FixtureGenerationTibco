import CustomComparator.OpponentComparator;
import Dao.LocationDao;
import Dao.RoundsDao;
import Dto.TibcoFixtureGenerationDto;
import Entity.*;
import Manager.FixtureManager;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

public class TibcoFirstIntegrationPoint implements Serializable {

    public static void main(String[] args) throws SQLException {
        final Logger LOG = Logger.getLogger(TibcoFirstIntegrationPoint.class);



        LocalDate date = LocalDate.of(2018, 12, 10); // 2014-06-15
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        int dayOfWeekIntValue = dayOfWeek.getValue(); // 6
        String dayOfWeekName = dayOfWeek.name(); // SATURDAY

        System.out.println(dayOfWeekIntValue);
        System.out.println(dayOfWeekName);

        generateRoundFixturesTest();

        //locationTimeSlotTest();

        Date currentdate = new Date();

        SimpleDateFormat simpleDateformat = new SimpleDateFormat("EEEE"); // the day of the week spelled out completely
        System.out.println(simpleDateformat.format(currentdate));

        Date nextDate;
        nextDate = DateUtils.addDays(currentdate, 4);

        System.out.println("current date: " + currentdate);

        System.out.println("next date: " + nextDate);



        TibcoFixtureGenerationDto tbdto = setupTibcoFixtureGenerationDto();

        FixtureManager fm = new FixtureManager();
        try {
            fm.startFixtureGeneration(tbdto);
        } catch (Exception e) {
            e.printStackTrace();
        }


        // JDBCConnection sc = new JDBCConnection();
      //  sc.TestPrintBlogs();
        //TestPrintBlogs();
    }

    private static void generateRoundFixturesTest() throws SQLException {
        RoundsDao roundsDao= new RoundsDao();
        List<Integer> fixtureIds = new ArrayList<>();
        fixtureIds.add(53384);
        fixtureIds.add(53595);

        fixtureIds.add(53600);
        fixtureIds.add(53610);
        fixtureIds.add(53616);

        roundsDao.getRoundFixturesByFixtureIdList(fixtureIds);
    }

    private static void locationTimeSlotTest() throws SQLException {
        List<String> fixtureSfIds = new ArrayList<>();
        fixtureSfIds.add("a0N2800000evxSJEAY");
        fixtureSfIds.add("a0N2800000evxSGEAY");
        fixtureSfIds.add("a0N2800000evxTaEAI");

        LocationDao locationDao = new LocationDao();
        locationDao.getLocationTimeSlotList(fixtureSfIds);
    }

    private static TibcoFixtureGenerationDto setupTibcoFixtureGenerationDto() {
        Competition competition = new Competition();
        competition.setSfId("a0A0k000007zmDTEAY");

        List<Team> teams = new ArrayList<>();

        Team t01 = new Team();
        t01.setSfid("a0R28000006t3eNEAQ");
        t01.setName("Aussie Cheetahs");

        teams.add(0, t01);

        Team t02 = new Team();
        t02.setSfid("a0R28000006t3d1EAA");
        t02.setName("Aussie Bobcats");
        teams.add(1, t02);

        Team t03 = new Team();
        t03.setSfid("a0R0k000000yEM8EAM");
        t03.setName("U17 Diamond Mine");
        teams.add(2, t03);

        Team t04 = new Team();
        t04.setSfid("a0R0k000000yEMIEA2");
        t04.setName("U17 AB team-Vikings");
        teams.add(3, t04);

        Team t05 = new Team();
        t05.setSfid("a0R0k000000vzADEAY");
        t05.setName("TN91");
        teams.add(4, t05);

        Team t06 = new Team();
        t06.setSfid("a0R0k000000vycGEAQ");
        t06.setName("team123");
        teams.add(5, t06);

        Team t07 = new Team();
        t07.setSfid("a0R0k00000149aQEAQ");
        t07.setName("TB 2");
        teams.add(6, t07);

        Team t08 = new Team();
        t08.setSfid("a0R0k00000149aLEAQ");
        t08.setName("Team Bendigo");
        teams.add(7, t08);

        Team t09 = new Team();
        t09.setSfid("a0R0k000000vzHAEAY");
        t09.setName("Testing 990");
        teams.add(8, t09);

        Team t10 = new Team();
        t10.setSfid("a0R0k000000zoYtEAI");
        t10.setName("Team3");
        teams.add(9, t10);

        Team t11 = new Team();
        t11.setSfid("a0R280000092JS4EAM");
        t11.setName("U17 Cashmore-Vikings");
        teams.add(10, t11);

        TibcoFixtureGenerationDto tbdto = new TibcoFixtureGenerationDto();
        tbdto.setCompetition(competition);
        tbdto.setMaxRounds(12);
        tbdto.setTeams(teams);
        return tbdto;
    }

    private static void testComparatorSort() {
        AvailableOpponent availableOpponent = new AvailableOpponent();

        FixtureStatisticsOpponent unPlayedOpponent;
        unPlayedOpponent = new FixtureStatisticsOpponent("testOpponent");
        //unPlayedOpponent.init("testOpponent");

        Map<String, FixtureStatisticsOpponent> unPlayedMap = new HashMap<>();
        unPlayedMap.put("testOpponent", unPlayedOpponent);

        unPlayedMap.get("testOpponent").getFixtureResultStatistics().setMatchesPlayed(5L);

        availableOpponent.setUnplayedOptions(unPlayedMap);
        //System.out.println(unPlayedMap.get("testOpponent").getFixtureResultStatistics().getMatchesPlayed());

        //availableOpponent.getUnplayedOptions().put("testOpponent", unPlayedOpponent);

        unPlayedOpponent = availableOpponent.getUnplayedOptions().get("testOpponent");
        unPlayedOpponent.getFixtureResultStatistics().setMatchesPlayed(6L);

        unPlayedMap.put("testOpponent", unPlayedOpponent);

        availableOpponent.getUnplayedOptions().put("testOpponent", unPlayedOpponent);

        // System.out.println(availableOpponent.getUnplayedOptions().get("testOpponent").getFixtureResultStatistics().getMatchesPlayed());


        availableOpponent.setOptionCount(5);

        AvailableOpponent availableOpponent1 = new AvailableOpponent();
        availableOpponent1.setOptionCount(2);

        AvailableOpponent availableOpponent2 = new AvailableOpponent();
        availableOpponent2.setGoodOptionsCount(3);
        availableOpponent2.setOptionCount(1);

        AvailableOpponent availableOpponent3 = new AvailableOpponent();
        availableOpponent3.setGoodOptionsCount(2);
        availableOpponent3.setOptionCount(2);

        AvailableOpponent availableOpponent4 = new AvailableOpponent();
        availableOpponent4.setUnplayedOptionsCount(3);

        AvailableOpponent availableOpponent5 = new AvailableOpponent();
        availableOpponent5.setUnplayedOptionsCount(3);

        AvailableOpponent availableOpponent6 = new AvailableOpponent();
        availableOpponent6.setBadOptionsCount(3);

        AvailableOpponent availableOpponent7 = new AvailableOpponent();
        availableOpponent7.setBadOptionsCount(1);

        AvailableOpponent availableOpponent8 = new AvailableOpponent();
        availableOpponent8.setTeamHomeRatio(3L);

        AvailableOpponent availableOpponent9 = new AvailableOpponent();
        availableOpponent9.setTeamHomeRatio(4L);

        AvailableOpponent availableOpponent10 = new AvailableOpponent();
        availableOpponent10.setGoodOptionsCount(3);

        AvailableOpponent availableOpponent11 = new AvailableOpponent();
        availableOpponent11.setUnplayedOptionsCount(7);

        AvailableOpponent availableOpponent12 = new AvailableOpponent();
        //availableOpponent12.setTeamHomeRatio(2L);
        availableOpponent12.setOptionCount(1);

        availableOpponent.setTeamSfId("testAV");
        availableOpponent1.setTeamSfId("testAV1");
        availableOpponent2.setTeamSfId("testAV2");
        availableOpponent3.setTeamSfId("testAV3");
        availableOpponent4.setTeamSfId("testAV4");
        availableOpponent5.setTeamSfId("testAV5");
        availableOpponent6.setTeamSfId("testAV6");
        availableOpponent7.setTeamSfId("testAV7");
        availableOpponent8.setTeamSfId("testAV8");
        availableOpponent9.setTeamSfId("testAV9");
        availableOpponent10.setTeamSfId("testAV10");
        availableOpponent11.setTeamSfId("testAV11");
        availableOpponent12.setTeamSfId("testAV12");

        List<AvailableOpponent> availableOpponentListForSort = new ArrayList<>();
        availableOpponentListForSort.add(availableOpponent);
        availableOpponentListForSort.add(availableOpponent1);
        availableOpponentListForSort.add(availableOpponent2);
        availableOpponentListForSort.add(availableOpponent3);
        availableOpponentListForSort.add(availableOpponent4);
        availableOpponentListForSort.add(availableOpponent5);
        availableOpponentListForSort.add(availableOpponent6);
        availableOpponentListForSort.add(availableOpponent7);
        availableOpponentListForSort.add(availableOpponent8);
        availableOpponentListForSort.add(availableOpponent9);
        availableOpponentListForSort.add(availableOpponent10);
        availableOpponentListForSort.add(availableOpponent11);
        availableOpponentListForSort.add(availableOpponent12);

        Collections.sort(availableOpponentListForSort, new OpponentComparator());

        //availableOpponentListForSort.stream().map(a -> a.getTeamSfId()); 	items.forEach(item->System.out.println(item));
        availableOpponentListForSort.forEach(a-> System.out.println(a.getTeamSfId()));
    }


}
