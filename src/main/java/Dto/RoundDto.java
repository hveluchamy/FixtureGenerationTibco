package Dto;

import java.io.Serializable;
import java.util.List;

public class RoundDto implements Serializable {


    public double number;
    private double order_number;
    private String name;
    private List<MatchDto> matches;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getOrder_number() {
        return order_number;
    }

    public void setOrder_number(double order_number) {
        this.order_number = order_number;
    }


    public double getNumber() {
        return number;
    }

    public void setNumber(double number) {
        this.number = number;
    }


    public List<MatchDto> getMatches() {
        return matches;
    }

    public void setMatches(List<MatchDto> matches) {
        this.matches = matches;
    }
}
