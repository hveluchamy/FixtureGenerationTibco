package Entity;

import Enums.CompetitionStatus;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class Competition implements Serializable {
    private Integer Id;
    private String name;
    private CompetitionStatus competitionStatus;
    private String daysOfWeek;
    private String organisationOwner;
    private String parentCompetition;
    private Double percentageOfLocationRequired;
    private String competitionTemplate;
    private Boolean allowFixtureOutsideScheduledTime;

    private String season;
    private java.sql.Timestamp startDate;
    private java.sql.Timestamp endDate;
    private String sfId;
    private double maxNumberOfTeams;
    private double gameTimeSlotLength;
    private String repeatSchedule;
    private Integer daysBetweenRounds;
    //Transient
    private Date lastPlayedDate;



    public Integer getDaysBetweenRounds() {
        return daysBetweenRounds;
    }

    public void setDaysBetweenRounds(Integer daysBetweenRounds) {
        this.daysBetweenRounds = daysBetweenRounds;
    }


    public Date getLastPlayedDate() {
        return lastPlayedDate;
    }

    public void setLastPlayedDate(Date lastPlayedDate) {
        this.lastPlayedDate = lastPlayedDate;
    }



    public Competition(){

    }

    public Competition(Integer id, String name, CompetitionStatus competitionStatus, String daysOfWeek, String organisationOwner,
                       String parentCompetition, Timestamp startDate, Timestamp endDate, String sfId, double maxNumberOfTeams, double gameTimeSlotLength, String repeatSchedule, String season) {
        Id = id;
        this.name = name;
        this.competitionStatus = competitionStatus;
        this.daysOfWeek = daysOfWeek;
        this.organisationOwner = organisationOwner;
        this.parentCompetition = parentCompetition;
        this.startDate = startDate;
        this.endDate = endDate;
        this.sfId = sfId;
        this.maxNumberOfTeams = maxNumberOfTeams;
        this.gameTimeSlotLength = gameTimeSlotLength;
        this.repeatSchedule = repeatSchedule;
        this.season = season;
    }

    public Double getPercentageOfLocationRequired() {
        return percentageOfLocationRequired;
    }

    public void setPercentageOfLocationRequired(Double percentageOfLocationRequired) {
        this.percentageOfLocationRequired = percentageOfLocationRequired;
    }

    public String getCompetitionTemplate() {
        return competitionTemplate;
    }

    public void setCompetitionTemplate(String competitionTemplate) {
        this.competitionTemplate = competitionTemplate;
    }

    public Boolean getAllowFixtureOutsideScheduledTime() {
        return allowFixtureOutsideScheduledTime;
    }

    public void setAllowFixtureOutsideScheduledTime(Boolean allowFixtureOutsideScheduledTime) {
        this.allowFixtureOutsideScheduledTime = allowFixtureOutsideScheduledTime;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CompetitionStatus getCompetitionStatus() {
        return competitionStatus;
    }

    public void setCompetitionStatus(CompetitionStatus competitionStatus) {
        this.competitionStatus = competitionStatus;
    }

    public String getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(String daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public String getOrganisationOwner() {
        return organisationOwner;
    }

    public void setOrganisationOwner(String organisationOwner) {
        this.organisationOwner = organisationOwner;
    }

    public String getParentCompetition() {
        return parentCompetition;
    }

    public void setParentCompetition(String parentCompetition) {
        this.parentCompetition = parentCompetition;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public String getSfId() {
        return sfId;
    }

    public void setSfId(String sfId) {
        this.sfId = sfId;
    }

    public double getMaxNumberOfTeams() {
        return maxNumberOfTeams;
    }

    public void setMaxNumberOfTeams(double maxNumberOfTeams) {
        this.maxNumberOfTeams = maxNumberOfTeams;
    }

    public double getGameTimeSlotLength() {
        return gameTimeSlotLength;
    }

    public void setGameTimeSlotLength(double gameTimeSlotLength) {
        this.gameTimeSlotLength = gameTimeSlotLength;
    }

    public String getRepeatSchedule() {
        return repeatSchedule;
    }

    public void setRepeatSchedule(String repeatSchedule) {
        this.repeatSchedule = repeatSchedule;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }
}
