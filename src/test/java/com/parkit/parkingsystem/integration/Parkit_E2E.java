package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.service.InteractiveShell;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

//import static org.assertj.core.api.Assertions.assertThat;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This Class manages the End To End test of this application.
 * @author docky
 */
@ExtendWith(MockitoExtension.class)
public class Parkit_E2E {

    private static DataBasePrepareService dataBasePrepareService;
    public InputStream stream;

    @BeforeAll
    private static void setUp() throws Exception {
        dataBasePrepareService = new DataBasePrepareService();
        dataBasePrepareService.clearDataBaseEntries();
        DataBaseConfig.setE2ETest(true);

    }

    @BeforeEach
    private void setUpPerTest() throws Exception {
        dataBasePrepareService.clearDataBaseEntries();
    }

    @AfterAll
    private static void tearDown() {
        
    }

    @Test
    @Tag("E2E_#1")
    @DisplayName("Given testE2E.txt file, when call LoadInterface, then Application works")
    public void givenATestE2ETextFile_whenCallLoadInterface_thenApplicationWorks() throws SQLException {
        // GIVEN
        String filepath = "src/test/resources/testE2E.txt";
        InputStream stdin = System.in;

        try {
            FileInputStream in = new FileInputStream(filepath);
            System.setIn(in); // (new FileInputStream(filepath));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // WHEN
        InteractiveShell.loadInterface();
        System.setIn(stdin);
        System.setIn(stdin);
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

}
