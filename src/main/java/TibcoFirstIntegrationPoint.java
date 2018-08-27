import Entity.AvailableOpponent;
import Entity.FixtureResultStatistics;
import Entity.FixtureStatisticsOpponent;
import JDBC.JDBCConnection;
import Manager.OpponentComparator;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class TibcoFirstIntegrationPoint implements Serializable {

    public static void main(String[] args) throws SQLException {
        final Logger LOG = Logger.getLogger(TibcoFirstIntegrationPoint.class);




       /* System.out.println("hello world");
        ThirdClassWithParam tc = new ThirdClassWithParam();
              System.out.print(tc.returnParamTest("value from main"));
        LOG.info("Imported log4j from maven");*/
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
        availableOpponent1.setOptionCount(3);

        AvailableOpponent availableOpponent2 = new AvailableOpponent();
        availableOpponent2.setGoodOptionsCount(3);

        AvailableOpponent availableOpponent3 = new AvailableOpponent();
        availableOpponent3.setGoodOptionsCount(2);

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
        availableOpponent12.setTeamHomeRatio(2L);

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

        Map<String, AvailableOpponent> sortedMap = new LinkedHashMap<>();

               /* availableOpponentListForSort.stream().collect(
                Collectors.toMap(AvailableOpponent::getTeamSfId, item->item)
        );*/
        System.out.println("map print");
        availableOpponentListForSort.forEach(a-> sortedMap.put(a.getTeamSfId(), a));
        sortedMap.values().stream().forEach(a-> System.out.println(a.getTeamSfId()));










       // JDBCConnection sc = new JDBCConnection();
      //  sc.TestPrintBlogs();
        //TestPrintBlogs();
    }




}
