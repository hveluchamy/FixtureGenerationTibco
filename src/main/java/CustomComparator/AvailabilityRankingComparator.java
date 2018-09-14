package CustomComparator;

import Entity.LocationAvailabilityRule;

import java.util.Comparator;

public class AvailabilityRankingComparator implements Comparator<LocationAvailabilityRule> {
    @Override
    public int compare(LocationAvailabilityRule o1, LocationAvailabilityRule o2) {
        return Integer.valueOf(o1.getAvailabilityRanking().compareTo(o2.getAvailabilityRanking()));
    }
}
