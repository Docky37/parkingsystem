package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.model.ParkingSpot;

/**
 * This interface contains 3 methods used to manage parking entry and exit
 * process.
 *
 * @author docky
 */
public interface IParkingService {

    /**
     * Method used to manage the parking entry process.
     */
    void processIncomingVehicle();

    /**
     * Method used to find a available parking spot for the incoming vehicule.
     *
     * @return a ParkingSpot object
     */
    ParkingSpot getNextParkingNumberIfAvailable();

    /**
     * Method used to manage the parking exit process.
     */
    void processExitingVehicle();

}
