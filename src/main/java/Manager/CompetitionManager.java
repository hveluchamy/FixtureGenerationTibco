package Manager;

import Dao.CompetitionDao;
import Dao.CompetitionTeamDao;
import Entity.Competition;
import Entity.CompetitionTeam;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

public class CompetitionManager implements Serializable {
    Logger LOG = Logger.getLogger(CompetitionManager.class);

    public Competition getCompetitionById(String compId) throws SQLException {
        CompetitionDao competitionDao = new CompetitionDao();
        return competitionDao.getCompetitionById(compId);
    }

    public List<CompetitionTeam> getActiveCompetitionTeamsByCompId(String compId) throws SQLException {
        CompetitionTeamDao competitionTeamDao = new CompetitionTeamDao();
        return competitionTeamDao.getCompetitionTeams(compId, false, "Active");
    }




}
