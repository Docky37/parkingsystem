package com.parkit.parkingsystem.service;

import java.time.temporal.ChronoUnit;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {
	double price;
	public void calculateFare(Ticket ticket) {
		if ((ticket.getOutTime() == null) || (ticket.getOutTime().isBefore(ticket.getInTime()))) {
			throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
		}

		double duration = ticket.getInTime().until(ticket.getOutTime(), ChronoUnit.HOURS);// outHour - inHour;
		if (duration == 0) {
			duration = (ticket.getInTime().until(ticket.getOutTime(), ChronoUnit.MINUTES));
			duration = duration / 60;
		}
		double discount = 0;
		if (ticket.isRegularCustomer()) {
			discount = Fare.REGULAR_CUSTOMER_DISCOUNT;
		}
		
		switch (ticket.getParkingSpot().getParkingType()) {
		case CAR: {
			price = Math.round((1-discount) * duration * Fare.CAR_RATE_PER_HOUR *100);
			price /= 100; 
			ticket.setPrice(price);
			break;
		}
		case BIKE: {
			price = Math.round((1-discount) * duration * Fare.BIKE_RATE_PER_HOUR *100);
			price /= 100; 
			ticket.setPrice(price);
			break;
		}
		default:
			throw new IllegalArgumentException("Unkown Parking Type");
		}
	}
}