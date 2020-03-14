package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.service.InteractiveShell;
import com.parkit.parkingsystem.util.InputReaderUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.BufferedReader;
//import static org.assertj.core.api.Assertions.assertThat;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This Class manages the End To End test of this application.
 *
 * @author docky
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Parkit_E2E {

    /**
     * Initialize a Logger used to send messages to the console.
     */
    private static final Logger LOGGER = LogManager
            .getLogger("InputReaderUtil");
    /**
     * Declare a DataBasePrepareService instance that will be used to deal with
     * test DataBase.
     */
    private static DataBasePrepareService dataBasePrepareService;
    /**
     * Declare an InputStream instance that will be used to redirect file
     * content too System.in, in order to replace user keyboard entries.
     */
    public InputStream stream;

    /**
     * Exectuted before the test serie, this method calls an external method to
     * reset the test DataBase and tells DataBaseConfig class that the run is in
     * E2E test mode so its BufferedReader has to read a file stream instead of
     * keyboard entries.
     *
     * @throws Exception
     */
    @BeforeAll
    private static void setUp() throws Exception {
        dataBasePrepareService = new DataBasePrepareService();
        dataBasePrepareService.clearDataBaseEntries();
        DataBaseConfig.setE2ETest(true);
    }

    /**
     * Nothing to do before each test.
     * 
     * @throws Exception
     */
    @BeforeEach
    private void setUpPerTest() throws Exception {
    }

    @AfterAll
    private static void tearDown() {
    }

    /**
     * First part of E2E test. The goal his to execute parking entries and exit
     * from E2E.txt file. Assertions are used to verify that moves are well
     * recorded in DataBase. No assertions about rate calculation, because all
     * real IN_TIME & OUT_TIME are about in the same second (This first test
     * take about 2" on my computer).
     *
     * @throws SQLException
     */
    @Test
    @Tag("E2E_PartA")
    @DisplayName("Given testE2E.txt file, when call LoadInterface,"
            + " then Application works alone")
    public void a_givenTestE2EtxtFile_whenCallLoadInterface_thenApplicationWorksAlone()
            throws SQLException {
        // GIVEN
        String filepath = "src/test/resources/testE2E.txt";
        InputStream stdin = System.in;
        try {
            FileInputStream in = new FileInputStream(filepath);
            System.setIn(in);
        } catch (FileNotFoundException e) {
            LOGGER.info("And now, where is testE2E.txt file?");
            LOGGER.error("And now, where is testE2E.txt file?",e);
        }

        // WHEN
        InteractiveShell.loadInterface();
        System.setIn(stdin);
        try {
            stdin.close();
        } catch (IOException e) {
            LOGGER.error("Cannot close InputStream!");
        }
        ResultSet rS = dataBasePrepareService.getResultSet();

        // THEN
        rS.first();
        assertThat(rS.getString("VEHICLE_REG_NUMBER")).isEqualTo("CAR-001");
        rS.next();
        assertThat(rS.getString("VEHICLE_REG_NUMBER")).isEqualTo("YAMAMOTO");
        rS.next();
        assertThat(rS.getString("VEHICLE_REG_NUMBER")).isEqualTo("CAR-002");
        assertThat(rS.getDouble("PRICE")).isEqualTo(0);
        assertThat(rS.getTimestamp("OUT_TIME")).isNull();
        rS.next();
        assertThat(rS.getString("VEHICLE_REG_NUMBER")).isEqualTo("CAR-001");
        assertThat(rS.getInt("PARKING_NUMBER")).isEqualTo(2);
        assertThat(rS.getBoolean("IS_RECURRENT_USER")).isEqualTo(true);
        rS.next();
        assertThat(rS.getString("VEHICLE_REG_NUMBER")).isEqualTo("YAMAMOTO");
        assertThat(rS.getInt("PARKING_NUMBER")).isEqualTo(4);
        assertThat(rS.getBoolean("IS_RECURRENT_USER")).isEqualTo(true);
        rS.next();
        assertThat(rS.getString("VEHICLE_REG_NUMBER")).isEqualTo("CAR-003");
        assertThat(rS.getInt("PARKING_NUMBER")).isEqualTo(3);
        assertThat(rS.getBoolean("IS_RECURRENT_USER")).isEqualTo(false);
        rS.close();
    }

    /**
     * Second part of E2E test. Firstable, the method calls a sub methode to
     * uptade IN_TIME & OUT_TIME of test ticket table records. Then the test
     * makes parking exit for all parked vehicule. Assertions allows us to check
     * fare calculation.
     *
     * @throws SQLException
     * @throws IOException
     */
    @Test
    @Tag("E2E_PartB")
    @DisplayName("Given some changes in Database & testE2Ebis.txt file, when call LoadInterface, then Application completes its work")
    public void B_givenChangesInDBAndAndTestE2EbisTxtFile_whenCallLoadInterface_thenApplicationCompletesItsWork()
            throws SQLException, IOException {
        // GIVEN
        updateInDateOutDate();
        String filepath = "src/test/resources/testE2Ebis.txt";
        InputStream stdin = System.in;
        try {
            FileInputStream in = new FileInputStream(filepath);
            System.setIn(in); // (new FileInputStream(filepath));
        } catch (FileNotFoundException e) {
            LOGGER.error("And now, where is testE2E.txt file?");
        }

        // WHEN
        InputReaderUtil.setBuffReader(
                new BufferedReader(new InputStreamReader(System.in)));
        InteractiveShell.loadInterface();
        System.setIn(stdin);
        try {
            stdin.close();
        } catch (IOException e) {
            LOGGER.error("Cannot close InputStream!");
        }
        ResultSet rS = dataBasePrepareService.getResultSet();

        // THEN
        rS.first();
        assertThat(rS.getString("VEHICLE_REG_NUMBER")).isEqualTo("CAR-001");
        assertThat(rS.getInt("PARKING_NUMBER")).isEqualTo(1);
        assertThat(rS.getTimestamp("OUT_TIME")).isNotNull();
        rS.next();
        assertThat(rS.getString("VEHICLE_REG_NUMBER")).isEqualTo("YAMAMOTO");
        assertThat(rS.getInt("PARKING_NUMBER")).isEqualTo(4);
        assertThat(rS.getTimestamp("OUT_TIME")).isNotNull();
        rS.next();
        assertThat(rS.getString("VEHICLE_REG_NUMBER")).isEqualTo("CAR-002");
        assertThat(rS.getInt("PARKING_NUMBER")).isEqualTo(1);
        assertThat(rS.getTimestamp("OUT_TIME")).isNotNull();
        rS.next();
        assertThat(rS.getString("VEHICLE_REG_NUMBER")).isEqualTo("CAR-001");
        assertThat(rS.getDouble("PRICE")).isEqualTo(1.43d);
        rS.next();
        assertThat(rS.getString("VEHICLE_REG_NUMBER")).isEqualTo("YAMAMOTO");
        assertThat(rS.getDouble("PRICE")).isEqualTo(0.95d);
        rS.next();
        assertThat(rS.getString("VEHICLE_REG_NUMBER")).isEqualTo("CAR-003");
        assertThat(rS.getDouble("PRICE")).isEqualTo(1.00d);
        rS.close();
        LOGGER.info("Closing DB connection");
    }

    /**
     * Sub method of E2E_PartB, used to update test DataBase ticket records with
     * arguments that were read in the testE2EUpdateInDateOutDate.txt file.
     *
     * @throws IOException
     */
    private void updateInDateOutDate() throws IOException {
        String filepath = "src/test/resources/testE2EUpdateInDateOutDate.txt";
        InputStream stdin = System.in;
        try {
            FileInputStream in = new FileInputStream(filepath);
            System.setIn(in);
        } catch (FileNotFoundException e) {
            LOGGER.error("And now, where is testE2EUpdateInDateOutDate.txt file?");
        }
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(System.in));
        for (int i = 0; i < 6; i++) {
            int id = parseIntMyString(bufferedReader.readLine());
            int delay = parseIntMyString(bufferedReader.readLine());
            String unit = bufferedReader.readLine();
            dataBasePrepareService.updateInTimeWithArguments(id, delay, unit);
        }
        bufferedReader.close();
        System.setIn(stdin);
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
            LOGGER.error("Cannot parseInt(tempString)"
                    + " if tempString is not a Number!", e);
        }
        return tempInt;
    }

}
