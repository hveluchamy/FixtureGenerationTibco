package Entity;

import Enums.CompetitionStatus;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

public class Competition implements Serializable {
    private Long Id;
    private String name;
    private CompetitionStatus competitionStatus;
    private String daysOfWeek;
    private String organisationOwner;
    private String parentCompetition;
    private java.sql.Timestamp startDate;
    private java.sql.Date endDate;
    private String sfId;
    private double maxNumberOfTeams;
    private double gameTimeSlotLength;
    private String repeateSchedule;

    public Competition(){

    }

    public Competition(Long id, String name, CompetitionStatus competitionStatus, String daysOfWeek, String organisationOwner,
                       String parentCompetition, Timestamp startDate, Date endDate, String sfId, double maxNumberOfTeams, double gameTimeSlotLength, String repeateSchedule) {
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
        this.repeateSchedule = repeateSchedule;
    }


    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
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

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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

    public String getRepeateSchedule() {
        return repeateSchedule;
    }

    public void setRepeateSchedule(String repeateSchedule) {
        this.repeateSchedule = repeateSchedule;
    }
}
