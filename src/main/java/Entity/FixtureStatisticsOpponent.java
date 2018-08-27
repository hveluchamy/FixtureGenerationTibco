package Entity;

import java.io.Serializable;

public class FixtureStatisticsOpponent implements Serializable {
    private String opponentSfId;
    private FixtureResultStatistics fixtureResultStatistics;

    public FixtureStatisticsOpponent(){

    }

    public FixtureStatisticsOpponent (String opponentSfId){
        //FixtureStatisticsOpponent fs = new FixtureStatisticsOpponent();
        FixtureResultStatistics fixtureResultStatistics = new FixtureResultStatistics(false);
        this.opponentSfId = opponentSfId;
        this.fixtureResultStatistics = fixtureResultStatistics;
    }

    public String getOpponentSfId() {
        return opponentSfId;
    }

    public void setOpponentSfId(String opponentSfId) {
        this.opponentSfId = opponentSfId;
    }

    public FixtureResultStatistics getFixtureResultStatistics() {
        return fixtureResultStatistics;
    }

    public void setFixtureResultStatistics(FixtureResultStatistics fixtureResultStatistics) {
        this.fixtureResultStatistics = fixtureResultStatistics;
    }
}
