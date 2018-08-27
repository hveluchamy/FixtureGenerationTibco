package Dto;

import Entity.Competition;
import Entity.Location;
import Entity.Team;

import java.util.List;

public class TibcoFixtureGenerationInputDto {
    public Competition competition;
    public List<Team> teams;
    public List<Location> locations;
}
