package com.parkit.parkingsystem.service;

import java.time.temporal.ChronoUnit;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {
	double price = 0;
	double discount = 0;
	double duration;
	
	public void calculateFare(Ticket ticket) {
		if ((ticket.getOutTime() == null) || (ticket.getOutTime().isBefore(ticket.getInTime()))) {
			throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
		}

		duration = ticket.getInTime().until(ticket.getOutTime(), ChronoUnit.HOURS);// outHour - inHour;
		if (duration == 0) {
			duration = (ticket.getInTime().until(ticket.getOutTime(), ChronoUnit.MINUTES));
			duration = duration / 60;
		}
		if (ticket.isRecurrentUser()) {
			discount = Fare.REGULAR_CUSTOMER_DISCOUNT;
		}else {
			discount = 0;
		}

		switch (ticket.getParkingSpot().getParkingType()) {
		case CAR: {
			price = calculate(Fare.CAR_RATE_PER_HOUR);
			ticket.setPrice(price);
			break;
		}
		case BIKE: {
			price = calculate(Fare.BIKE_RATE_PER_HOUR);
			ticket.setPrice(price);
			break;
		}
		default:
			throw new IllegalArgumentException("Unkown Parking Type");
		}
	}
	
	private double calculate(Double fare) {
		double price=0;
		if (60*duration>Fare.FREE_PARKING_DURATION) {
			price = Math.round((1 - discount) * duration * fare * 100);
			price /= 100;
		}

		return price;
	}
}