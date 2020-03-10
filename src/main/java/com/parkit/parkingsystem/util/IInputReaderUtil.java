package com.parkit.parkingsystem.util;

import java.io.IOException;

/**
 * This interface contains one method to read int keyboard inputs,
 * and a second one to read String registering number inputs.
 *
 * @author docky
 */
public interface IInputReaderUtil {

    /**
     * Method used to read int keyboard inputs.
     * @return a int - valid answer or -1 if non valid input
     */
    int readSelection();

    /**
     * This method asks a user for his registering number and call
     * an external method that will read his keyboard input.
     *
     * @return a String - the registering number or "ILLEGAL ARGUMENT"
     * @throws IOException
     */
    String readVehicleRegistrationNumber() throws IOException;

}
