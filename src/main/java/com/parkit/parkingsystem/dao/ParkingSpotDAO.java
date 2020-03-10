package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * This class defines methods used to deal with parking table of prod DB.
 *
 * @author Tek
 */
public class ParkingSpotDAO implements IParkingSpotDAO {
    /**
     * Initialise a Logger used to send messages to the console.
     */
    private static final Logger LOGGER = LogManager.
            getLogger("ParkingSpotDAO");
    /**
     * Create a DataBasObject used to make a connection with the Prod DataBase.
     */
    private DataBaseConfig dataBaseConfig = new DataBaseConfig();

    /**
     * Used to get a available ParkingSpot for a type of vehicle.
     *
     * @param parkingType the type of vehicle (CAR / BIKE)
     * @return an int, the ParkingSpot id
     */
    @Override
    public int getNextAvailableSlot(final ParkingType parkingType) {
        int result = -1;
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            con = getDataBaseConfig().getConnection();
            ps = con.prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT);
            ps.setString(1, parkingType.toString());
            rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getInt(1);
            }
            getDataBaseConfig().closeResultSet(rs);
            getDataBaseConfig().closePreparedStatement(ps);
        } catch (Exception ex) {
            LOGGER.error("Error fetching next available slot", ex);
        } finally {
            getDataBaseConfig().closeConnection(con);
            getDataBaseConfig().closePreparedStatement(ps);
            getDataBaseConfig().closeResultSet(rs);
        }
        return result;
    }

    /**
     * Used to update availability of the given ParkingSpot.
     *
     * @param parkingSpot the ParkingSpot to update
     * @return the DataBaseConfig object
     */
    @Override
    public boolean updateParking(final ParkingSpot parkingSpot) {
        // update the availability fo that parking slot
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = getDataBaseConfig().getConnection();
            ps = con.prepareStatement(DBConstants.UPDATE_PARKING_SPOT);
            ps.setBoolean(1, parkingSpot.isAvailable());
            ps.setInt(2, parkingSpot.getId());
            int updateRowCount = ps.executeUpdate();
            getDataBaseConfig().closePreparedStatement(ps);
            return (updateRowCount == 1);
        } catch (Exception ex) {
            LOGGER.error("Error updating parking info", ex);
            return false;
        } finally {
            getDataBaseConfig().closeConnection(con);
            getDataBaseConfig().closePreparedStatement(ps);
        }
    }

    /**
     * Getter of the object DataBaseConfig.
     *
     * @return the DataBaseConfig object
     */
    @Override
    public DataBaseConfig getDataBaseConfig() {
        return dataBaseConfig;
    }

    /**
     * Setter of a DataBaseConfig object.
     *
     * @param dbConfig the DataBaseConfig to set
     */
    @Override
    public void setDataBaseConfig(final DataBaseConfig dbConfig) {
        this.dataBaseConfig = dbConfig;
    }

}
