package com.parkit.parkingsystem;

import com.parkit.parkingsystem.service.InteractiveShell;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Launch Park'it Application.
 *
 * @author Tek
 */
final class App {
    /**
     * LOGGER initialized to send console message.
     */
    private static final Logger LOGGER = LogManager.getLogger("App");

    /**
     * The method main start Park'it application.
     *
     * @param args no argument
     */
    public static void main(final String[] args) {
        LOGGER.info("Initializing Parking System");
        InteractiveShell.loadInterface();
    }

    private App() {
    }
}
