package com.parkit.parkingsystem.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.temporal.ChronoUnit;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService implements IFareCalculatorService {
	BigDecimal price = new BigDecimal(0);
	BigDecimal discount = new BigDecimal(0);
	BigDecimal duration = new BigDecimal(0);
	MathContext mathContext = new MathContext(3, RoundingMode.HALF_UP);

	@Override
	public void calculateFare(Ticket ticket) {
		if ((ticket.getOutTime() == null) || (ticket.getOutTime().isBefore(ticket.getInTime()))) {
			throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
		}

		duration = new BigDecimal(ticket.getInTime().until(ticket.getOutTime(), ChronoUnit.HOURS));

		if (duration.equals(BigDecimal.ZERO)) {
			duration = new BigDecimal((ticket.getInTime().until(ticket.getOutTime(), ChronoUnit.MINUTES)));
			duration = duration.divide(BigDecimal.valueOf(60L), mathContext);
		}
		if (ticket.isRecurrentUser()) {
			discount = BigDecimal.valueOf(Fare.REGULAR_CUSTOMER_DISCOUNT);
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

	@Override
	public void calculateAndSetPrice(Double fare, Ticket ticket) {
		BigDecimal calculedPrice = BigDecimal.ZERO;
		BigDecimal roundedPrice;
		if (duration.multiply(BigDecimal.valueOf(60L)).compareTo(BigDecimal.valueOf(Fare.FREE_PARKING_DURATION)) > 0) {
			calculedPrice = BigDecimal.ONE.subtract(discount).multiply(duration).multiply(BigDecimal.valueOf(fare));
			roundedPrice = calculedPrice.setScale(2, RoundingMode.HALF_UP);
		} else {
			roundedPrice = calculedPrice;
		}
		ticket.setPrice(roundedPrice.doubleValue());
	}
}