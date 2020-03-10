package com.parkit.parkingsystem.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.temporal.ChronoUnit;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

/**
 * This class is used to calculate the price to pay when exiting the parking.
 *
 * @author Tek
 */
public class FareCalculatorService implements IFareCalculatorService {
    /**
     * An int constant used to define MathContext precision.
     */
    private static final  int THREE = 3;
    /**
     * An int constant equal to the number of minutes in one hour.
     */
    private static final  int SIXTY = 60;
    /**
     * A BigDecimal variant initialized to store discount calculation.
     */
    private BigDecimal discount = new BigDecimal(0);
    /**
     * A BigDecimal variant initialized to store parking duration calculation.
     */
    private BigDecimal duration = new BigDecimal(0);
    /**
     * Define the MathContext to use for calculation accuracy.
     */
    private MathContext mathContext = new MathContext(THREE,
            RoundingMode.HALF_UP);

    /**
     * This method start the calculation of the price to pay when exiting
     * parking.
     *
     * @param ticket - the Ticket from witch calculation is done
     */
    @Override
    public void calculateFare(final Ticket ticket) {
        if ((ticket.getOutTime() == null)
                || (ticket.getOutTime().isBefore(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect:"
                    + ticket.getOutTime().toString());
        }

        duration = new BigDecimal(ticket.getInTime().until(ticket.getOutTime(),
                ChronoUnit.HOURS));

        if (duration.equals(BigDecimal.ZERO)) {
            duration = new BigDecimal((ticket.getInTime()
                    .until(ticket.getOutTime(), ChronoUnit.MINUTES)));
            duration = duration.divide(BigDecimal.valueOf(SIXTY), mathContext);
        }
        if (ticket.isRecurrentUser()) {
            discount = BigDecimal.valueOf(Fare.RECURRENT_USER_DISCOUNT);
        } else {
            discount = new BigDecimal(0);
        }

        switch (ticket.getParkingSpot().getParkingType()) {
        case CAR:
            calculateAndSetPrice(Fare.CAR_RATE_PER_HOUR, ticket);
            break;
        case BIKE:
            calculateAndSetPrice(Fare.BIKE_RATE_PER_HOUR, ticket);
            break;
        default:
            throw new IllegalArgumentException("Unkown Parking Type");
        }
    }

    /**
     * This sub-method of CalculateFare() finalize the calculation of the
     * price to pay and set the resulting price into the ticket.
     *
     * @param fare   - the fare per hour of the vehicule type (CAR or BIKE)
     * @param ticket - the Ticket from witch calculation is done
     */
    @Override
    public void calculateAndSetPrice(final Double fare, final Ticket ticket) {
        BigDecimal calculedPrice = BigDecimal.ZERO;
        BigDecimal roundedPrice;
        if (duration.multiply(BigDecimal.valueOf(SIXTY)).compareTo(
                BigDecimal.valueOf(Fare.FREE_PARKING_DURATION)) > 0) {
            calculedPrice = BigDecimal.ONE.subtract(discount).multiply(duration)
                    .multiply(BigDecimal.valueOf(fare));
            roundedPrice = calculedPrice.setScale(2, RoundingMode.HALF_UP);
        } else {
            roundedPrice = calculedPrice;
        }
        ticket.setPrice(roundedPrice.doubleValue());
    }
}
