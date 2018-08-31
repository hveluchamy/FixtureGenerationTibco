package Manager;

import Entity.AvailableOpponent;

import java.util.Comparator;

public class RankedOpponentComparator implements Comparator<AvailableOpponent> {
    @Override
    public int compare(AvailableOpponent o1, AvailableOpponent o2) {
          /*// Out of our optimal set of opponents, find the opponent with:
            // 1. The most available bad options (to weed these out)
            // 2. The fewest available unplayed options (leave them for other teams)
            // 3. The fewest available good options (leave as many good posibilities open)
            // 4. The fewest played matches between the two teams
            // 5. The greatest difference in home and away average

            rankedOpponents = FixtureGenerationEngine.objectToArray(bestOpponents).sort(function (a, b) {
              return Math.sign(availableOpponents[a.key]['option_count'] - availableOpponents[b.key]['option_count'])
              || Math.sign(availableOpponents[b.key]['bad_options_count'] - availableOpponents[a.key]['bad_options_count'])
              || Math.sign(availableOpponents[a.key]['unplayed_options_count'] - availableOpponents[b.key]['unplayed_options_count'])
              || Math.sign(availableOpponents[a.key]['good_options_count'] - availableOpponents[b.key]['good_options_count'])
              || Math.sign(availableOpponents[team.key]['opponents'][a.key]['matches_played']) - Math.sign(availableOpponents[team.key]['opponents'][b.key]['matches_played'])
              || Math.sign(availableOpponents[team.key]['opponents'][b.key]['home_away_diff']) - Math.sign(availableOpponents[team.key]['opponents'][a.key]['home_away_diff'])
            });*/

        if(o1.getOptionCount() != null && o2.getOptionCount() != null){
            return Integer.valueOf(o1.getOptionCount().compareTo(o2.getOptionCount()));
        }

        if(o1.getGoodOptionsCount() != null && o2.getGoodOptionsCount() != null){
            return Integer.valueOf(o2.getBadOptionsCount().compareTo(o1.getBadOptionsCount()));
        }

        if(o1.getUnplayedOptionsCount() != null && o2.getUnplayedOptionsCount() != null){
            return Integer.valueOf(o1.getUnplayedOptionsCount().compareTo(o2.getUnplayedOptionsCount()));
        }

        if(o1.getBadOptionsCount() != null && o2.getBadOptionsCount() != null){
            return Integer.valueOf(o1.getGoodOptionsCount().compareTo(o2.getGoodOptionsCount()));
        }

        if(o1.getOpponents().get(o1.getTeamSfId()).getFixtureResultStatistics().getMatchesPlayed() != null
                && o2.getOpponents().get(o2.getTeamSfId()).getFixtureResultStatistics().getMatchesPlayed() !=null){
            return  Integer.valueOf(o1.getOpponents().get(o1.getTeamSfId()).getFixtureResultStatistics().getMatchesPlayed()
                    .compareTo(o2.getOpponents().get(o2.getTeamSfId()).getFixtureResultStatistics().getMatchesPlayed()));
        }

        if(o2.getOpponents().get(o2.getTeamSfId()).getFixtureResultStatistics().getHomeAwayDiff() !=null
                && o1.getOpponents().get(o1.getTeamSfId()).getFixtureResultStatistics().getHomeAwayDiff()!=null){
            return Integer.valueOf(o2.getOpponents().get(o2.getTeamSfId()).getFixtureResultStatistics().getHomeAwayDiff()
                    .compareTo(o1.getOpponents().get(o1.getTeamSfId()).getFixtureResultStatistics().getHomeAwayDiff()));
        }

       //TODO do that last two tomorrow
        return 0;
    }
}
