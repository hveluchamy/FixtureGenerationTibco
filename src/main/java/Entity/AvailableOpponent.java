package Entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AvailableOpponent implements Serializable {
    private String teamSfId;
    private Map<String, FixtureStatisticsOpponent> opponents;
    private Map<String, FixtureStatisticsOpponent> unplayedOptions;
    private Map<String, FixtureStatisticsOpponent> goodOptions;
    private Map<String, FixtureStatisticsOpponent> badOptions;
    private Integer badOptionsCount;
    private Integer unplayedOptionsCount;
    private Integer goodOptionsCount;
    private Integer optionCount;
    private Long teamHomeRatio;

    public AvailableOpponent(){
        this.opponents = new HashMap<>();
        this.unplayedOptions = new HashMap<>();
        this.goodOptions = new HashMap<>();
        this.badOptions = new HashMap<>();
    }

    public String getTeamSfId() {
        return teamSfId;
    }

    public void setTeamSfId(String teamSfId) {
        this.teamSfId = teamSfId;
    }

    public Map<String, FixtureStatisticsOpponent> getOpponents() {
        return opponents;
    }

    public void setOpponents(Map<String, FixtureStatisticsOpponent> opponents) {
        this.opponents = opponents;
    }

    public Map<String, FixtureStatisticsOpponent> getUnplayedOptions() {
        return unplayedOptions;
    }

    public void setUnplayedOptions(Map<String, FixtureStatisticsOpponent> unplayedOptions) {
        this.unplayedOptions = unplayedOptions;
    }

    public Map<String, FixtureStatisticsOpponent> getGoodOptions() {
        return goodOptions;
    }

    public void setGoodOptions(Map<String, FixtureStatisticsOpponent> goodOptions) {
        this.goodOptions = goodOptions;
    }

    public Map<String, FixtureStatisticsOpponent> getBadOptions() {
        return badOptions;
    }

    public void setBadOptions(Map<String, FixtureStatisticsOpponent> badOptions) {
        this.badOptions = badOptions;
    }

    public Integer getBadOptionsCount() {
        return badOptionsCount;
    }

    public void setBadOptionsCount(Integer badOptionsCount) {
        this.badOptionsCount = badOptionsCount;
    }

    public Integer getUnplayedOptionsCount() {
        return unplayedOptionsCount;
    }

    public void setUnplayedOptionsCount(Integer unplayedOptionsCount) {
        this.unplayedOptionsCount = unplayedOptionsCount;
    }

    public Integer getGoodOptionsCount() {
        return goodOptionsCount;
    }

    public void setGoodOptionsCount(Integer goodOptionsCount) {
        this.goodOptionsCount = goodOptionsCount;
    }

    public Integer getOptionCount() {
        return optionCount;
    }

    public void setOptionCount(Integer optionCount) {
        this.optionCount = optionCount;
    }

    public Long getTeamHomeRatio() {
        return teamHomeRatio;
    }

    public void setTeamHomeRatio(Long teamHomeRatio) {
        this.teamHomeRatio = teamHomeRatio;
    }
}
