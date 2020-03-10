package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.dao.ITicketDAO;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.dao.IParkingSpotDAO;
import com.parkit.parkingsystem.util.IInputReaderUtil;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class is used to display the main menu of the application.
 *
 * @author Tek
 */
public final class InteractiveShell {

    /**
     * An int constant used for the switch().
     */
    private static final int THREE = 3;
    /**
     * LOGGER initialized to send console message.
     */
    private static final Logger LOGGER = LogManager
            .getLogger("InteractiveShell");

    /**
     * Empty constructor.
     */
    private InteractiveShell() {
    }

    /**
     * This method display manage the main menu of the application. Call
     * loadMenu() to display menu items and after keyboard entry call
     * corresponding parkingService method or Quit application.
     */
    public static void loadInterface() {
        LOGGER.info("App initialized!!!");
        LOGGER.info("Welcome to Parking System!");

        boolean continueApp = true;
        IInputReaderUtil inputReaderUtil = new InputReaderUtil();
        IParkingSpotDAO parkingSpotDAO = new ParkingSpotDAO();
        ITicketDAO ticketDAO = new TicketDAO();
        IParkingService parkingService = new ParkingService(inputReaderUtil,
                parkingSpotDAO, ticketDAO);

        while (continueApp) {
            loadMenu();
            int option = inputReaderUtil.readSelection();
            switch (option) {
            case 1:
                parkingService.processIncomingVehicle();
                break;
            case 2:
                parkingService.processExitingVehicle();
                break;
            case THREE:
                LOGGER.info("Exiting from the system!");
                continueApp = false;
                break;
            default:
                LOGGER.info("Unsupported option. Please enter a number"
                        + " corresponding to the provided menu.");
            }
        }
    }

    /**
     * This method display the main menu.
     */
    private static void loadMenu() {
        LOGGER.info("Please select an option. Simply enter the number"
                + " to choose an action");
        LOGGER.info("1 New Vehicle Entering - Allocate Parking Space");
        LOGGER.info("2 Vehicle Exiting - Generate Ticket Price");
        LOGGER.info("3 Shutdown System");
    }

}
