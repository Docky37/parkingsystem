package com.parkit.parkingsystem.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

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
     * Boolean used to switch BufferredReader for E2E test.
     */
    private static boolean isE2ETest = true;

    /**
     * Class BufferedReader only used for E2E test.
     */
    private static BufferedReader buffReader = new BufferedReader(
            new InputStreamReader(System.in, Charset.forName("UTF-8"))) ;

    /**
     * Method used to read int keyboard inputs.
     *
     * @return a int - valid answer or -1 if non valid input
     */
    @Override
    public int readSelection() {
        int tempInt = -1;
        try {
            String tempString;
            if (isE2ETest) {
                tempString = buffReader.readLine();
            } else {
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(System.in, Charset.forName("UTF-8")));
                tempString = bufferedReader.readLine();
            }
            if (tempString != null && tempString.length() == 1) {
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
        try {
            String vehicleRegNumber;
            if (isE2ETest) {
                vehicleRegNumber = buffReader.readLine();
            } else {
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(System.in, Charset.forName("UTF-8")));
                vehicleRegNumber = bufferedReader.readLine();
            }
            if (vehicleRegNumber != null && vehicleRegNumber.trim().length() > 0
                    && vehicleRegNumber.trim().length() < MAX_CHAR_FOR_REGNUMBER
                            + 1) {
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
                    + " if tempString is not a Number!", e);
        }
        return tempInt;
    }

    /**
     * Setter du boolean isE2ETest.
     *
     * @return the isE2ETest
     */
    public static boolean isE2ETest() {
        return isE2ETest;
    }

    /**
     * Setter du boolean isE2ETest.
     *
     * @param e2ETest the isE2ETest to set
     */
    public static void setE2ETest(final boolean e2ETest) {
        isE2ETest = e2ETest;
    }

    /**
     * @return the buffReader
     */
    public static BufferedReader getBuffReader() {
        return buffReader;
    }

    /**
     * @param bufferReader the buffReader to set
     */
    public static void setBuffReader(final BufferedReader bufferReader) {
        InputReaderUtil.buffReader = bufferReader;
    }

}
