package Entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FixtureStatistics {
    private Long id;
    private double value;
    private String confirmed;
    private Long statistics;
    private Long fixtureEntity;
    private String externalId;
    private Long fixture;
    private Long fixtureTeam;
    private Long fixtureParticipant;
    private String matchTypePeriod;
    private java.sql.Timestamp createddatetime;
    private java.sql.Timestamp updateddatetime;

    //Transient fields for calculation purposes

    private String teamSfId;
    private FixtureResultStatistics fixtureResultStatistics;
    private Map<String, FixtureStatisticsOpponent>  fixtureStatisticsOpponentMap;



    //fixtureStatistics[team.sfid] = fixtureStatistics[team.sfid] || {matches_played: 0, home_games: 0, last_home: 0, last_away: 0, home_distribution: 0, away_distribution: 0, opponents: {}, home_ratio: 0};
    //

    public FixtureStatistics(String teamSfId, FixtureResultStatistics fixtureResultStatistics) {
        this.teamSfId = teamSfId;
        this.fixtureResultStatistics = fixtureResultStatistics;
        this.fixtureStatisticsOpponentMap = new HashMap<>();
    }

    public FixtureStatistics(String teamSfId, FixtureStatisticsOpponent fixtureStatisticsOpponent){
        this.teamSfId = teamSfId;
        this.fixtureStatisticsOpponentMap.put(fixtureStatisticsOpponent.getOpponentSfId(), fixtureStatisticsOpponent);
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }


    public String getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(String confirmed) {
        this.confirmed = confirmed;
    }


    public long getStatistics() {
        return statistics;
    }

    public void setStatistics(Long statistics) {
        this.statistics = statistics;
    }


    public long getFixtureEntity() {
        return fixtureEntity;
    }

    public void setFixtureEntity(Long fixtureEntity) {
        this.fixtureEntity = fixtureEntity;
    }


    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }


    public long getFixture() {
        return fixture;
    }

    public void setFixture(long fixture) {
        this.fixture = fixture;
    }


    public long getFixtureTeam() {
        return fixtureTeam;
    }

    public void setFixtureTeam(long fixtureTeam) {
        this.fixtureTeam = fixtureTeam;
    }


    public long getFixtureParticipant() {
        return fixtureParticipant;
    }

    public void setFixtureParticipant(long fixtureParticipant) {
        this.fixtureParticipant = fixtureParticipant;
    }


    public String getMatchTypePeriod() {
        return matchTypePeriod;
    }

    public void setMatchTypePeriod(String matchTypePeriod) {
        this.matchTypePeriod = matchTypePeriod;
    }


    public java.sql.Timestamp getCreateddatetime() {
        return createddatetime;
    }

    public void setCreateddatetime(java.sql.Timestamp createddatetime) {
        this.createddatetime = createddatetime;
    }


    public java.sql.Timestamp getUpdateddatetime() {
        return updateddatetime;
    }

    public void setUpdateddatetime(java.sql.Timestamp updateddatetime) {
        this.updateddatetime = updateddatetime;
    }

    public String getTeamSfId() {
        return teamSfId;
    }

    public void setTeamSfId(String teamSfId) {
        this.teamSfId = teamSfId;
    }

    public FixtureStatisticsOpponent getFixtureStatisticsOpponent(String opponentSfId) {
        try {
            return this.fixtureStatisticsOpponentMap.get(opponentSfId);
        } catch (NullPointerException e){
            return null;
        }

    }

    public void addFixtureStatisticsOpponent(FixtureStatisticsOpponent fixtureStatisticsOpponent) {
        this.fixtureStatisticsOpponentMap.put(fixtureStatisticsOpponent.getOpponentSfId(), fixtureStatisticsOpponent);
    }

    public FixtureResultStatistics getFixtureResultStatistics() {
        return fixtureResultStatistics;
    }

    public void setFixtureResultStatistics(FixtureResultStatistics fixtureResultStatistics) {
        this.fixtureResultStatistics = fixtureResultStatistics;
    }
}
