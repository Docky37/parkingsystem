package com.parkit.parkingsystem.constants;

/**
 * This class contains parking rates per hour, the rate of discount and the free
 * parking duration.
 *
 * @author Tek & Docky
 */
public final class Fare {
    /**
     * Parking rate per hour for car.
     */
    public static final double BIKE_RATE_PER_HOUR = 1.0;
    /**
     * Parking rate per hour for bike.
     */
    public static final double CAR_RATE_PER_HOUR = 1.5;
    /**
     * Rate of discount for recurrent user.
     */
    public static final double RECURRENT_USER_DISCOUNT = 0.05;
    /**
     * Free parking duration.
     */
    public static final int FREE_PARKING_DURATION = 30;

    /**
     * Empty constructor.
     */
    private Fare() {

    }

}
