package Dto;

import java.io.Serializable;
import java.sql.Date;

public class LocationTimeSlotDto implements Serializable {
    private String eventTitle;
    private String homeTeamName;
    private String homeTeamId;
    private String awayTeamName;
    private String awayTeamId;
    private String fixtureId;
    private String fixtureStatus;
    private String competitionName;
    private String competitionId;
    private String resourceId;
    private String resourceTitle;
    private String resourceTimeZone;
    private String locationTimeslotId;
    private Date startDateTime;
    private Date endDateTime;
    private String availabilityRule;
    private double duration;

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getHomeTeamName() {
        return homeTeamName;
    }

    public void setHomeTeamName(String homeTeamName) {
        this.homeTeamName = homeTeamName;
    }

    public String getHomeTeamId() {
        return homeTeamId;
    }

    public void setHomeTeamId(String homeTeamId) {
        this.homeTeamId = homeTeamId;
    }

    public String getAwayTeamName() {
        return awayTeamName;
    }

    public void setAwayTeamName(String awayTeamName) {
        this.awayTeamName = awayTeamName;
    }

    public String getAwayTeamId() {
        return awayTeamId;
    }

    public void setAwayTeamId(String awayTeamId) {
        this.awayTeamId = awayTeamId;
    }

    public String getFixtureId() {
        return fixtureId;
    }

    public void setFixtureId(String fixtureId) {
        this.fixtureId = fixtureId;
    }

    public String getFixtureStatus() {
        return fixtureStatus;
    }

    public void setFixtureStatus(String fixtureStatus) {
        this.fixtureStatus = fixtureStatus;
    }

    public String getCompetitionName() {
        return competitionName;
    }

    public void setCompetitionName(String competitionName) {
        this.competitionName = competitionName;
    }

    public String getCompetitionId() {
        return competitionId;
    }

    public void setCompetitionId(String competitionId) {
        this.competitionId = competitionId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceTitle() {
        return resourceTitle;
    }

    public void setResourceTitle(String resourceTitle) {
        this.resourceTitle = resourceTitle;
    }

    public String getResourceTimeZone() {
        return resourceTimeZone;
    }

    public void setResourceTimeZone(String resourceTimeZone) {
        this.resourceTimeZone = resourceTimeZone;
    }

    public String getLocationTimeslotId() {
        return locationTimeslotId;
    }

    public void setLocationTimeslotId(String locationTimeslotId) {
        this.locationTimeslotId = locationTimeslotId;
    }

    public Date getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Date getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Date endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getAvailabilityRule() {
        return availabilityRule;
    }

    public void setAvailabilityRule(String availabilityRule) {
        this.availabilityRule = availabilityRule;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }
}
