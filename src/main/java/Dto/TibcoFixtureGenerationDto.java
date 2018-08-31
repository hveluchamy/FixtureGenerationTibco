package Dto;

import Entity.Competition;
import Entity.Location;
import Entity.Team;

import java.io.Serializable;
import java.util.List;

public class TibcoFixtureGenerationDto implements Serializable {
    public Competition competition;
    public List<Team> teams;
    public List<Location> locations;
    public Integer maxRounds;

    public Competition getCompetition() {
        return competition;
    }

    public void setCompetition(Competition competition) {
        this.competition = competition;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public Integer getMaxRounds() {
        return maxRounds;
    }

    public void setMaxRounds(Integer maxRounds) {
        this.maxRounds = maxRounds;
    }


}
