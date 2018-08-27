package Entity;

public class FixtureTeam {
    private long id;
    private String status;
    private String homeAway;
    private long fixture;
    private long competitionTeam;
    private String team;
    private String externalId;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getHomeAway() {
        return homeAway;
    }

    public void setHomeAway(String homeAway) {
        this.homeAway = homeAway;
    }


    public long getFixture() {
        return fixture;
    }

    public void setFixture(long fixture) {
        this.fixture = fixture;
    }


    public long getCompetitionTeam() {
        return competitionTeam;
    }

    public void setCompetitionTeam(long competitionTeam) {
        this.competitionTeam = competitionTeam;
    }


    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }


    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }
}
