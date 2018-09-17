package Dto;

import Entity.Competition;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

public class DateRangeDto implements Serializable {
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

    public Date getSeasonStartDate() {
        return seasonStartDate;
    }

    public void setSeasonStartDate(Date seasonStartDate) {
        this.seasonStartDate = seasonStartDate;
    }

    public Date getSeasonEndDate() {
        return seasonEndDate;
    }

    public void setSeasonEndDate(Date seasonEndDate) {
        this.seasonEndDate = seasonEndDate;
    }

    Timestamp startDate;
    Date endDate;
    Date seasonStartDate;
    Date seasonEndDate;
}
