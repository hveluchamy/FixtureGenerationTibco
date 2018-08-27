package Manager;

import Dao.FixtureDao;
import Dao.LocationDao;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.sql.SQLException;

public class LocationManager implements Serializable {
    Logger LOG = Logger.getLogger(LocationManager.class);
    public void deleteLocationTimeSlots(String compId) throws SQLException {
        FixtureDao fixtureDao = new FixtureDao();
        LocationDao locationDao = new LocationDao();

        //Delete location timeslot
        locationDao.deleteLocationTimeSlots(compId);

        //update fixture timeslot to null
        fixtureDao.clearLocationTimeSlotFromFixture(compId);


    }
}
