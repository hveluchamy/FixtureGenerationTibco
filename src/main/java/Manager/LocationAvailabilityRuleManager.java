package Manager;

import Entity.Competition;
import Entity.LocationAvailabilityRule;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.List;

public class LocationAvailabilityRuleManager implements Serializable {
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
                /*
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

}
