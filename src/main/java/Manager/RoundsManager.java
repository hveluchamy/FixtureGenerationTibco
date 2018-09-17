package Manager;

import Dao.RoundsDao;
import Dto.FixtureTeamRoundDto;
import Dto.RoundDto;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

public class RoundsManager implements Serializable {
    Logger LOG = Logger.getLogger(RoundsManager.class);

    public void deleteUnplayedRounds(String compId) throws SQLException {
        RoundsDao roundsDao = getRoundsDao();
        roundsDao.deleteUnplayedRounds(compId);
    }



    public List<FixtureTeamRoundDto> getRoundFixtureByFixtureIds(List<Integer> fixtureIds) throws SQLException {
        RoundsDao roundsDao = getRoundsDao();
        return roundsDao.getRoundFixturesByFixtureIdList(fixtureIds);
    }

    public List<RoundDto> getRounds(String compId) throws SQLException {
        RoundsDao roundsDao = getRoundsDao();
        return roundsDao.getRoundListByCompetitionId(compId);
    }

    private RoundsDao getRoundsDao() {
        return new RoundsDao();
    }


}
