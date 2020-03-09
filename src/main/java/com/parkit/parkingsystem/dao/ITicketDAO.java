package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.model.Ticket;

public interface ITicketDAO {

	boolean checkExistingTicket(String vehicleRegNumber);

	boolean saveTicket(Ticket ticket);

	Ticket getTicket(String vehicleRegNumber);

	boolean updateTicket(Ticket ticket);

	DataBaseConfig getDataBaseConfig();

	void setDataBaseConfig(DataBaseConfig dataBaseConfig);

}