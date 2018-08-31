package Dto;

import Entity.AvailableOpponent;

import java.io.Serializable;
import java.util.Map;

public class RoundDto implements Serializable {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(double orderNumber) {
        this.orderNumber = orderNumber;
    }

    private String name;
    private double orderNumber;

    public Map<String, String> getHomeAndAwayTeam() {
        return homeAndAwayTeam;
    }

    public void setHomeAndAwayTeam(Map<String, String> homeAndAwayTeam) {
        this.homeAndAwayTeam = homeAndAwayTeam;
    }

    private Map<String, String> homeAndAwayTeam;


}
