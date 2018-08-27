package Entity;

import java.io.Serializable;

public class Match implements Serializable {
    AvailableOpponent homeTeam;
    AvailableOpponent awayTeam;

    public AvailableOpponent getHomeTeam() {
        return homeTeam;
    }

    public AvailableOpponent getAwayTeam() {
        return awayTeam;
    }

    public void setHomeTeam(AvailableOpponent homeTeam) {
        this.homeTeam = homeTeam;
    }

    public void setAwayTeam(AvailableOpponent awayTeam) {
        this.awayTeam = awayTeam;
    }


}
