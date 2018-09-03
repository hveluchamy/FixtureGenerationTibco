package Manager;

import Dao.RoundsDao;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.sql.SQLException;

public class RoundsManager implements Serializable {
    Logger LOG = Logger.getLogger(RoundsManager.class);

    public void deleteUnplayedRounds(String compId) throws SQLException {
        RoundsDao roundsDao = new RoundsDao();
        roundsDao.deleteUnplayedRounds(compId);
    }


}
