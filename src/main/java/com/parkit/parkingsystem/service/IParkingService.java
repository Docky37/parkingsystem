package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.model.ParkingSpot;

public interface IParkingService {

	void processIncomingVehicle();

	ParkingSpot getNextParkingNumberIfAvailable();

	void processExitingVehicle();

}