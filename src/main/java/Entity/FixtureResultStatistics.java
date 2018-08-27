package Entity;

public class FixtureResultStatistics {
    private Long lastHome;
    private Long matchesPlayed;
    private Long homeGames;
    private Long lastAway;
    private Long homeDistribution;
    private Long awayDistribution;
    private Long homeRatio;
    private Long lastPlayed;
    private Long distribution;
    private Long homeAwayDiff;

    public FixtureResultStatistics(Boolean opponent) {
        if(opponent ==null || (!opponent)){
            this.matchesPlayed=0L;
            this.homeGames = 0L;
            this.lastHome = 0L;
            this.lastAway = 0L;
            this.homeDistribution = 0L;
            this.awayDistribution = 0L;
            this.homeRatio = 0L;
            //fixtureStatistics[team.sfid] = fixtureStatistics[team.sfid] || {matches_played: 0, home_games: 0, last_home: 0, last_away: 0, home_distribution: 0, away_distribution: 0, opponents: {}, home_ratio: 0};
            //
        }

        if(opponent){
            this.matchesPlayed=0L;
            this.homeGames = 0L;
            this.lastHome = null;
            this.lastPlayed = 0L;
            this.distribution=null;
        }

        //fixtureStatistics[team.sfid]['opponents'][opponent.sfid] = fixtureStatistics[team.sfid]['opponents'][opponent.sfid] || {matches_played: 0, home_games: 0, last_home: null, last_played: 0, distribution: null};
        //

    }

    public Long getLastHome() {
        return lastHome;
    }

    public void setLastHome(Long lastHome) {
        this.lastHome = lastHome;
    }

    public Long getMatchesPlayed() {
        return matchesPlayed;
    }

    public void setMatchesPlayed(Long matchesPlayed) {
        this.matchesPlayed = matchesPlayed;
    }

    public Long getHomeGames() {
        return homeGames;
    }

    public void setHomeGames(Long homeGames) {
        this.homeGames = homeGames;
    }

    public Long getLastAway() {
        return lastAway;
    }

    public void setLastAway(Long lastAway) {
        this.lastAway = lastAway;
    }

    public Long getHomeDistribution() {
        return homeDistribution;
    }

    public void setHomeDistribution(Long homeDistribution) {
        this.homeDistribution = homeDistribution;
    }

    public Long getAwayDistribution() {
        return awayDistribution;
    }

    public void setAwayDistribution(Long awayDistribution) {
        this.awayDistribution = awayDistribution;
    }

    public Long getHomeRatio() {
        return homeRatio;
    }

    public void setHomeRatio(Long homeRatio) {
        this.homeRatio = homeRatio;
    }

    public Long getLastPlayed() {
        return lastPlayed;
    }

    public void setLastPlayed(Long lastPlayed) {
        this.lastPlayed = lastPlayed;
    }

    public Long getDistribution() {
        return distribution;
    }

    public void setDistribution(Long distribution) {
        this.distribution = distribution;
    }

    public Long getHomeAwayDiff() {
        return homeAwayDiff;
    }

    public void setHomeAwayDiff(Long homeAwayDiff) {
        this.homeAwayDiff = homeAwayDiff;
    }
}