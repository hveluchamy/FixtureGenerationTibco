package Manager;

import Entity.AvailableOpponent;

import java.util.Comparator;

public class OpponentComparator implements Comparator<AvailableOpponent> {
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
