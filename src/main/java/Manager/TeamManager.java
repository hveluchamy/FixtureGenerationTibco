package Manager;

import Entity.Team;

import java.io.Serializable;
import java.util.List;

public class TeamManager implements Serializable {
    public void assignByeTeam(List<Team> teams){
        if(teams.size()!=0 && teams.size() % 2 != 0){
            Team team = new Team();
            team.setSfid("Bye");
            team.setName("Bye");
            teams.add(team);

        }
    }
}
