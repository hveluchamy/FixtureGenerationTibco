package Dto;

import Entity.Fixture;

import java.io.Serializable;
import java.util.Date;

public class FixtureTeamRoundDto implements Serializable {
    private Fixture fixture;
    private String fixtureId;
    private String homeTeamId;
    private String awayTeamId;
    private Date roundStartDate;
    private Date roundEnDate;
    private String roundId;
    private String roundName;

    public FixtureTeamRoundDto(Fixture fixture, String homeTeamId, String awayTeamId, Date roundStartDate, Date roundEnDate, String roundId, String roundName) {
        this.fixture = fixture;
        this.homeTeamId = homeTeamId;
        this.awayTeamId = awayTeamId;
        this.roundStartDate = roundStartDate;
        this.roundEnDate = roundEnDate;
        this.roundId = roundId;
        this.roundName = roundName;
    }

    public FixtureTeamRoundDto() {
    }

    public Fixture getFixture() {
        return fixture;
    }

    public void setFixture(Fixture fixture) {
        this.fixture = fixture;
    }

    public String getHomeTeamId() {
        return homeTeamId;
    }

    public void setHomeTeamId(String homeTeamId) {
        this.homeTeamId = homeTeamId;
    }

    public String getAwayTeamId() {
        return awayTeamId;
    }

    public void setAwayTeamId(String awayTeamId) {
        this.awayTeamId = awayTeamId;
    }

    public Date getRoundStartDate() {
        return roundStartDate;
    }

    public void setRoundStartDate(Date roundStartDate) {
        this.roundStartDate = roundStartDate;
    }

    public Date getRoundEnDate() {
        return roundEnDate;
    }

    public void setRoundEnDate(Date roundEnDate) {
        this.roundEnDate = roundEnDate;
    }

    public String getRoundId() {
        return roundId;
    }

    public void setRoundId(String roundId) {
        this.roundId = roundId;
    }

    public String getRoundName() {
        return roundName;
    }

    public void setRoundName(String roundName) {
        this.roundName = roundName;
    }

    public String getFixtureId() {
        return fixtureId;
    }

    public void setFixtureId(String fixtureId) {
        this.fixtureId = fixtureId;
    }
}
