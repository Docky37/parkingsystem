package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.model.Ticket;

/**
 * This interface contains 2 methods used to calculate the price to pay when
 * exiting the parking.
 *
 * @author docky
 */
public interface IFareCalculatorService {

    /**
     * This method start the calculation of the price to pay when exiting
     * parking.
     *
     * @param ticket - the Ticket from witch calculation is done
     */
    void calculateFare(Ticket ticket);

    /**
     * This sub-method of CalculateFare() finalize the calculation of the price
     * to pay and set the resulting price into the ticket.
     *
     * @param fare   - the fare per hour of the vehicule type (CAR or BIKE)
     * @param ticket - the Ticket from witch calculation is done
     */
    void calculateAndSetPrice(Double fare, Ticket ticket);

}
