package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.model.Ticket;

public interface IFareCalculatorService {

	void calculateFare(Ticket ticket);

	void calculateAndSetPrice(Double fare, Ticket ticket);

}