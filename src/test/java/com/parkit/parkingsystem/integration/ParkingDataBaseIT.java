package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.ITicketDAO;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.dao.IParkingSpotDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.IParkingService;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.I_ImputReaderUtil;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

	private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
	private static IParkingSpotDAO parkingSpotDAO;
	private static ITicketDAO ticketDAO;
	private static DataBasePrepareService dataBasePrepareService;

	@Mock
	private static I_ImputReaderUtil inputReaderUtil;

	@BeforeAll
	private static void setUp() throws Exception {
		dataBasePrepareService = new DataBasePrepareService();
		parkingSpotDAO = new ParkingSpotDAO();
		parkingSpotDAO.setDataBaseConfig(dataBaseTestConfig);
		ticketDAO = new TicketDAO();
		ticketDAO.setDataBaseConfig(dataBaseTestConfig);
	}

	@BeforeEach
	private void setUpPerTest() throws Exception {
		when(inputReaderUtil.readSelection()).thenReturn(1);
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
		dataBasePrepareService.clearDataBaseEntries();
	}

	@AfterAll
	private static void tearDown() {

	}

	@Test
	@Tag("ParkingACar")
	@DisplayName("At vehicle entry, after incoming process, DB tables should be updated (parking.AVAILABLE, new record in ticket).")
	public void givenVehicleEntry_whenIncomingProcessIsCompleted_thenDBShouldBeUpdated() {
		// GIVEN
		IParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		parkingService.processIncomingVehicle();
		// WHEN
		ParkingSpot parkingSpot = parkingService.getNextParkingNumberIfAvailable();
		boolean ticketExist = ticketDAO.checkExistingTicket("ABCDEF");
		// THEN
		assertThat(parkingSpot.getId()).isEqualTo(2); //Check the ParkingSpot Availability update in test DB
		assertThat(ticketExist).isEqualTo(true); //Check the creation of a ticket in test DB
	}

	@Test
	@Tag("ParkingExit")
	@DisplayName("At vehicle exit, after exit process, DB tables should be updated (OUT_TIME & PRICE).")
	public void givenVehicleExit_whenExitProcessIsCompleted_thenDBShouldBeUpdated() {
		// GIVEN
		IParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		parkingService.processIncomingVehicle();
		dataBasePrepareService.updateInTime(); // Data update used to generate one hour parking duration
		// WHEN
		parkingService.processExitingVehicle();
		Ticket ticket = ticketDAO.getTicket("ABCDEF");
		// THEN
		assertThat(ticket.getOutTime()).isNotNull();// Check that OUT_TIME field of Ticket table contains a value
		assertThat(ticket.getPrice()).isEqualTo(Fare.CAR_RATE_PER_HOUR); // Check that PRICE field contains one hour parking fare	
	}

}
