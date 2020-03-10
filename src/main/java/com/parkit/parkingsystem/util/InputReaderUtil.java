package com.parkit.parkingsystem.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * This class is used to read int keyboard inputs, and String registering number
 * inputs.
 *
 * @author Tek
 */
public class InputReaderUtil implements IInputReaderUtil {
    /**
     * Initialise a Logger used to send messages to the console.
     */
    private static final Logger LOGGER = LogManager
            .getLogger("InputReaderUtil");
    /**
     * Maximum number of characters allocated for a registring number.
     */
    private static final int MAX_CHAR_FOR_REGNUMBER = 8;

    /**
     * Method used to read int keyboard inputs.
     *
     * @return a int - valid answer or -1 if non valid input
     */
    @Override
    public int readSelection() {
        int tempInt = -1;
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(System.in));
        try {
            String tempString;
            tempString = bufferedReader.readLine();
            if (tempString.length() == 1) {
                tempInt = parseIntMyString(tempString);
            }
        } catch (IOException e) {
            LOGGER.error("Error reading input.", e);
        }
        return (tempInt);
    }

    /**
     * This method asks a user for his registering number and call an external
     * method that will read his keyboard input.
     *
     * @return a String - the registering number or "ILLEGAL ARGUMENT"
     * @throws IOException
     */
    @Override
    public String readVehicleRegistrationNumber() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(System.in));
        try {
            String vehicleRegNumber = bufferedReader.readLine();
            if (vehicleRegNumber.trim().length() > 0 && vehicleRegNumber.trim()
                    .length() < MAX_CHAR_FOR_REGNUMBER + 1) {
                return vehicleRegNumber;
            }
            return "ILLEGAL ARGUMENT";
        } catch (IOException ioe) {
            LOGGER.error("Input error while reading", ioe);
            throw ioe;
        }
    }

    /**
     * This method get the decimal digit from a String if possible.
     *
     * @param tempString the one character String to parseInt
     * @return int the decimal digit or -1
     */
    private int parseIntMyString(final String tempString) {
        int tempInt = -1;
        try {
            tempInt = Integer.parseInt(tempString);
        } catch (NumberFormatException e) {
            LOGGER.info("Please enter valid number for proceeding further");
            LOGGER.error("Cannot parseInt(tempString)"
                    + " if tempString is not a Number!",
                    e);
        }
        return tempInt;
    }

}
