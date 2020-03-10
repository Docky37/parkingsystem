package com.parkit.parkingsystem.model;

import com.parkit.parkingsystem.constants.ParkingType;

/**
 * This class is used to store the variants of the 3 fields of a parking table
 * record.
 *
 * @author Tek
 */
public class ParkingSpot {
    /**
     * The unique identifier of a parking spot.
     */
    private int number;
    /**
     * The parking type (CAR or BIKE).
     */
    private ParkingType parkingType;
    /**
     * Tell if parking spot is used or free.
     */
    private boolean isAvailable;

    /**
     * Class constructor.
     *
     * @param numb the unique identifier of a parking spot
     * @param pkType the parking type (CAR or BIKE)
     * @param available that tell if parking spot is used or free
     */
    public ParkingSpot(final int numb, final ParkingType pkType,
            final boolean available) {
        this.number = numb;
        this.parkingType = pkType;
        this.isAvailable = available;
    }

    /**
     * Getter of ParkingSpot.number.
     *
     * @return an int the id the parking spot.
     */
    public int getId() {
        return number;
    }

    /**
     * Setter of ParkingSpot.number.
     *
     * @param id the parking spot identifier
     */
    public void setId(final int id) {
        number = id;
    }

    /**
     * Getter of ParkingSpot.ParkingType.
     *
     * @return a ParkingType (CAR or BIKE)
     */
    public ParkingType getParkingType() {
        return parkingType;
    }

     /**
     * Setter of ParkingSpot.ParkingType.
     *
     * @param parkType the parking spot type (CAR or BIKE)
     */
   public void setParkingType(final ParkingType parkType) {
        parkingType = parkType;
    }

    /**
     * Getter of ParkingSpot.isAvailable.
     *
     * @return boolean that tell if spot is used or free.
     */
    public boolean isAvailable() {
        return isAvailable;
    }

    /**
     * Setter of ParkingSpot.isAvailable.
     *
     * @param available (used or free)
     */
    public void setAvailable(final boolean available) {
        isAvailable = available;
    }

}
