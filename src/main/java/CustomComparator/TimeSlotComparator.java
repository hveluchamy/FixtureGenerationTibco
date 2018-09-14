package CustomComparator;

import Dto.LocationTimeSlotDto;
import Entity.LocationAvailabilityRule;

import java.util.Comparator;

public class TimeSlotComparator implements Comparator<LocationTimeSlotDto> {
    @Override
    public int compare(LocationTimeSlotDto o1, LocationTimeSlotDto o2) {
        return Integer.valueOf(o1.getEndDateTime().compareTo(o2.getEndDateTime()));
    }
}
