package Manager;

import Entity.Team;

import java.io.Serializable;
import java.util.List;

public class TeamManager implements Serializable {

    public static final String BYE = "Bye";
    public static final String BYE_NAME = "Bye";

    public void assignByeTeam(List<Team> teams){
        if(teams.size()!=0 && teams.size() % 2 != 0){
            Team team = new Team();
            team.setSfid(BYE);
            team.setName(BYE_NAME);
            teams.add(team);

        }
    }
}
