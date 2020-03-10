package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ITicketDAO;
import com.parkit.parkingsystem.dao.IParkingSpotDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.IInputReaderUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * This class is used to manage parking entry and exit process.
 *
 * @author Tek
 */
public class ParkingService implements IParkingService {

    /**
     * Initialise a Logger used to send messages to the console.
     */
    private static final Logger LOGGER = LogManager.getLogger("ParkingService");
    /**
     * Create a FareCalculatorService object via the interface.
     */
    private static IFareCalculatorService fareCalculatorService
            = new FareCalculatorService();
    /**
     * Create a InputReaderUtil object via the interface.
     */
    private IInputReaderUtil inputReaderUtil;
    /**
     * Create a ParkingSpotDAO object via the interface.
     */
    private IParkingSpotDAO parkingSpotDAO;
    /**
     * Create a TicketDAO object via the interface.
     */
    private ITicketDAO ticketDAO;

    /**
     * The Class constructor.
     *
     * @param inReadUtil
     * @param pkSpotDAO
     * @param tktDAO
     */
    public ParkingService(final IInputReaderUtil inReadUtil,
            final IParkingSpotDAO pkSpotDAO, final ITicketDAO tktDAO) {
        this.inputReaderUtil = inReadUtil;
        this.parkingSpotDAO = pkSpotDAO;
        this.ticketDAO = tktDAO;
    }

    /**
     * This method manage the vehicule incoming process.
     */
    @Override
    public void processIncomingVehicle() {
        try {
            ParkingSpot parkingSpot = getNextParkingNumberIfAvailable();
            if (parkingSpot != null && parkingSpot.getId() > 0) {
                String vehicleRegNumber = getVehicleRegNumber();
                boolean ticketExist = ticketDAO
                        .checkExistingTicket(vehicleRegNumber);
                parkingSpot.setAvailable(false);
                parkingSpotDAO.updateParking(parkingSpot); // allot this parking
                                                           // spot and mark
                                                           // it's availability
                                                           // as false

                LocalDateTime inTime = LocalDateTime.now();
                Ticket ticket = new Ticket();
                // ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME,
                // OUT_TIME)
                ticket.setParkingSpot(parkingSpot);
                ticket.setVehicleRegNumber(vehicleRegNumber);
                ticket.setPrice(0);
                if (ticketExist) {
                    LOGGER.info("Welcome back! As a recurring user"
                            + " of our parking, you'll benefit"
                            + " from a {} discount.", "5%");
                    ticket.setRecurrentUser(true);
                }
                ticket.setInTime(inTime);
                ticket.setOutTime(null);
                ticketDAO.saveTicket(ticket);
                LOGGER.info("Generated Ticket and saved in DB");
                LOGGER.info("Please park your vehicle in spot number: {}",
                        parkingSpot.getId());
                LOGGER.info("Recorded in-time for vehicle number: {} is: {}",
                        vehicleRegNumber, inTime);
            }
        } catch (Exception e) {
            LOGGER.error("Unable to process incoming vehicle", e);
        }
    }

    /**
     * This method asks a user for his registering number and call the external
     * method that will read his keyboard input.
     *
     * @return a String - the registering number or "ILLEGAL ARGUMENT"
     * @throws IOException
     */
    private String getVehicleRegNumber() throws IOException {
        LOGGER.info("Please type the vehicle registration number"
                + " and press enter key");
        try {
            return inputReaderUtil.readVehicleRegistrationNumber();
        } catch (Exception e) {
            LOGGER.info("EXCEPTION");
            return "";
        }
    }

    /**
     * Method used to find a available parking spot for the incoming vehicule.
     *
     * @return a ParkingSpot object
     */
    @Override
    public ParkingSpot getNextParkingNumberIfAvailable() {
        int parkingNumber = 0;
        ParkingSpot parkingSpot = null;
        try {
            ParkingType parkingType = getVehicleType();
            parkingNumber = parkingSpotDAO.getNextAvailableSlot(parkingType);
            if (parkingNumber > 0) {
                parkingSpot = new ParkingSpot(parkingNumber, parkingType, true);
            } else {
                throw new SQLException("Error fetching parking number from DB."
                        + " Parking slots might be full");
            }
        } catch (IllegalArgumentException ie) {
            LOGGER.error("Error parsing user input for type of vehicle", ie);
        } catch (Exception e) {
            LOGGER.error("Error fetching next available parking slot", e);
        }
        return parkingSpot;
    }

    /**
     * This method ask the user for his vehicle type and call the external
     * method that will read his keyboard input.
     *
     * @return a ParkingType object
     */
    private ParkingType getVehicleType() {
        LOGGER.info("Please select vehicle type from menu");
        LOGGER.info("1 CAR");
        LOGGER.info("2 BIKE");
        int input = inputReaderUtil.readSelection();
        switch (input) {
        case 1:
            return ParkingType.CAR;

        case 2:
            return ParkingType.BIKE;

        default:
            LOGGER.info("Incorrect input provided!");
            throw new IllegalArgumentException("Entered input is invalid!");
        }
    }

    /**
     * This method manage the vehicule exit process.
     */
    @Override
    public void processExitingVehicle() {
        try {
            String vehicleRegNumber = getVehicleRegNumber();
            Ticket ticket = ticketDAO.getTicket(vehicleRegNumber);
            LocalDateTime outTime = LocalDateTime.now();
            LocalDateTime inTime = ticket.getInTime();
            ticket.setOutTime(outTime);
            fareCalculatorService.calculateFare(ticket);
            if (ticketDAO.updateTicket(ticket)) {
                ParkingSpot parkingSpot = ticket.getParkingSpot();
                parkingSpot.setAvailable(true);
                parkingSpotDAO.updateParking(parkingSpot);
                LOGGER.info("Recorded in-time for vehicle number: {} is: {}",
                        vehicleRegNumber, inTime);
                LOGGER.info("Recorded out-time for vehicle number: {} is: {}",
                        ticket.getVehicleRegNumber(), outTime);
                LOGGER.info("Please pay the parking fare: {}",
                        ticket.getPrice());
            } else {
                LOGGER.error(
                        "Unable to update ticket information. Error occurred");
            }
        } catch (Exception e) {
            LOGGER.error("Unable to process exiting vehicle", e);
        }
    }

}
