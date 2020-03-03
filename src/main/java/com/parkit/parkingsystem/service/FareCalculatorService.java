package com.parkit.parkingsystem.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.temporal.ChronoUnit;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {
	BigDecimal price = new BigDecimal(0);
	BigDecimal discount = new BigDecimal(0);
	BigDecimal duration = new BigDecimal(0);
	MathContext mathContext = new MathContext(3, RoundingMode.HALF_UP);

	public void calculateFare(Ticket ticket) {
		if ((ticket.getOutTime() == null) || (ticket.getOutTime().isBefore(ticket.getInTime()))) {
			throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
		}

		duration = new BigDecimal(ticket.getInTime().until(ticket.getOutTime(), ChronoUnit.HOURS));// outHour - inHour;

		if (duration.equals(BigDecimal.ZERO)) {
			duration = new BigDecimal((ticket.getInTime().until(ticket.getOutTime(), ChronoUnit.MINUTES)));
			duration = duration.divide(BigDecimal.valueOf(60L), mathContext);
		}
		if (ticket.isRecurrentUser()) {
			discount = new BigDecimal(Fare.REGULAR_CUSTOMER_DISCOUNT);
		} else {
			discount = new BigDecimal(0);
		}

		switch (ticket.getParkingSpot().getParkingType()) {
		case CAR: {
			price = calculate(Fare.CAR_RATE_PER_HOUR);
			ticket.setPrice(price.doubleValue());
			break;
		}
		case BIKE: {
			price = calculate(Fare.BIKE_RATE_PER_HOUR);
			ticket.setPrice(price.doubleValue());
			break;
		}
		default:
			throw new IllegalArgumentException("Unkown Parking Type");
		}
	}

	private BigDecimal calculate(Double fare) {
		BigDecimal price = new BigDecimal(0);
		BigDecimal roundedPrice = new BigDecimal(0);
		if (duration.multiply(BigDecimal.valueOf(60L)).compareTo(BigDecimal.valueOf(Fare.FREE_PARKING_DURATION)) == 1) {
			price = BigDecimal.ONE.subtract(discount).multiply(duration).multiply(BigDecimal.valueOf(fare));
			roundedPrice = price.setScale(2, RoundingMode.HALF_UP);
		}
		return roundedPrice;
	}
}