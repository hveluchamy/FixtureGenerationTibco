package CustomComparator;

import Entity.AvailableOpponent;

import java.util.Comparator;

public class OpponentComparator implements Comparator<AvailableOpponent> {
    /*  // Find the team with:
            // 1. The fewest available options
            // 2. The fewest available good options
            // 3. The fewest available unplayed options
            // 4. The fewest available bad options
            // 5. The lowest home game raito
            avaiableTeams = FixtureGenerationEngine.objectToArray(availableOpponents).sort(function (a, b) {
              return Math.sign(availableOpponents[a.key]['option_count'] - availableOpponents[b.key]['option_count'])
              || Math.sign(availableOpponents[a.key]['good_options_count'] - availableOpponents[b.key]['good_options_count'])
              || Math.sign(availableOpponents[a.key]['unplayed_options_count'] - availableOpponents[b.key]['unplayed_options_count'])
              || Math.sign(availableOpponents[a.key]['bad_options_count'] - availableOpponents[b.key]['bad_options_count'])
              || Math.sign(fixtureStatistics[a.key]['home_ratio'] - fixtureStatistics[b.key]['home_ratio']);
            });*/
    @Override
    public int compare(AvailableOpponent o1, AvailableOpponent o2) {
        if(o1.getOptionCount() != null && o2.getOptionCount() != null){
            return Integer.valueOf(o1.getOptionCount().compareTo(o2.getOptionCount()));
        }

        if(o1.getGoodOptionsCount() != null && o2.getGoodOptionsCount() != null){
            return Integer.valueOf(o1.getGoodOptionsCount().compareTo(o2.getGoodOptionsCount()));
        }
        if(o1.getUnplayedOptionsCount() != null && o2.getUnplayedOptionsCount() != null){
            return Integer.valueOf(o1.getUnplayedOptionsCount().compareTo(o2.getUnplayedOptionsCount()));
        }
        if(o1.getBadOptionsCount() != null && o2.getBadOptionsCount() != null){
            return Integer.valueOf(o1.getBadOptionsCount().compareTo(o2.getBadOptionsCount()));
        }

        if(o1.getTeamHomeRatio() != null && o2.getTeamHomeRatio() != null){
            return Integer.valueOf(o1.getTeamHomeRatio().compareTo(o2.getTeamHomeRatio()));

        }
        return 0;
    }
}
