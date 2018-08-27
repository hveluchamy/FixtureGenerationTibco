package Entity;


import java.io.Serializable;

public class Fixture implements Serializable {

    //TODO change status to enum
    private String status;

    private long id;
    private long round;
    private String competition;
    private long locationTimeslot;
    private String homeTeamName;
    private String homeTeamId;
    private String awayTeamName;
    private String awayTeamId;
    private String resultType;
    private String externalId;

    public Fixture() {
    }

    public Fixture(long id, long round, String competition,
                   long locationTimeslot, String homeTeamName, String homeTeamId, String awayTeamName, String awayTeamId,
                   String status, String resultType,
                   String externalId) {
        this.status = status;

        this.id = id;
        this.round = round;
        this.competition = competition;
        this.locationTimeslot = locationTimeslot;
        this.homeTeamName = homeTeamName;
        this.homeTeamId = homeTeamId;
        this.awayTeamName = awayTeamName;
        this.awayTeamId = awayTeamId;
        this.resultType = resultType;
        this.externalId = externalId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public long getRound() {
        return round;
    }

    public void setRound(long round) {
        this.round = round;
    }


    public String getCompetition() {
        return competition;
    }

    public void setCompetition(String competition) {
        this.competition = competition;
    }


    public long getLocationTimeslot() {
        return locationTimeslot;
    }

    public void setLocationTimeslot(long locationTimeslot) {
        this.locationTimeslot = locationTimeslot;
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


    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }


    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

}
