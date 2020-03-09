package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;

public interface IParkingSpotDAO {

	int getNextAvailableSlot(ParkingType parkingType);

	boolean updateParking(ParkingSpot parkingSpot);

	void setDataBaseConfig(DataBaseConfig dataBaseConfig);

}