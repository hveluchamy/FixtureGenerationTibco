package Dto;

import java.io.Serializable;
import java.sql.Date;

public class ExceptionDateDto implements Serializable {
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExceptionDays() {
        return exceptionDays;
    }

    public void setExceptionDays(String exceptionDays) {
        this.exceptionDays = exceptionDays;
    }

    public String getRelatedSeason() {
        return relatedSeason;
    }

    public void setRelatedSeason(String relatedSeason) {
        this.relatedSeason = relatedSeason;
    }

    public String getRelatedCompetition() {
        return relatedCompetition;
    }

    public void setRelatedCompetition(String relatedCompetition) {
        this.relatedCompetition = relatedCompetition;
    }

    public Integer getRelatedSpMemberAvailability() {
        return relatedSpMemberAvailability;
    }

    public void setRelatedSpMemberAvailability(Integer relatedSpMemberAvailability) {
        this.relatedSpMemberAvailability = relatedSpMemberAvailability;
    }

    public String getRelatedAvailabilityRule() {
        return relatedAvailabilityRule;
    }

    public void setRelatedAvailabilityRule(String relatedAvailabilityRule) {
        this.relatedAvailabilityRule = relatedAvailabilityRule;
    }

    private Integer id;
    private Date startDate;
    private Date endDate;
    private String description;
    private String exceptionDays;
    private String relatedSeason;
    private String relatedCompetition;
    private Integer relatedSpMemberAvailability;
    private String relatedAvailabilityRule;

}
