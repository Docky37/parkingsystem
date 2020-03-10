package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;

/**
 * This interface contains 3 methods used to deal with parking table of prod DB.
 *
 * @author Tek
 */
public interface IParkingSpotDAO {

    /**
     * Used to get a available ParkingSpot for a type of vehicle.
     *
     * @param parkingType the type of vehicle (CAR / BIKE)
     * @return an int, the ParkingSpot id
     */
    int getNextAvailableSlot(ParkingType parkingType);

    /**
     * Used to update availability of the given ParkingSpot.
     *
     * @param parkingSpot
     * @return a boolean (true if job is done)
     */
    boolean updateParking(ParkingSpot parkingSpot);

    /**
     * Setter of a DataBaseConfig object.
     *
     * @param dataBaseConfig the DataBaseConfig to set
     */
    void setDataBaseConfig(DataBaseConfig dataBaseConfig);

    /**
     * Getter of a DataBaseConfig object.
     * @return the DataBaseConfig
     */
    DataBaseConfig getDataBaseConfig();

}
