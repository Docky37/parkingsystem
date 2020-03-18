package com.parkit.parkingsystem;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.IParkingSpotDAO;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;


@ExtendWith(MockitoExtension.class)
public class ParkingSpotDAOTest {

    private static DataBasePrepareService dataBasePrepareService = new DataBasePrepareService();
    
    IParkingSpotDAO parkingSpotDAO = null;


    @BeforeEach
    private void setUpPerTest() throws Exception {
        dataBasePrepareService.clearDataBaseEntries();
        DataBaseConfig.setE2ETest(true);
    }
    @AfterEach
    private void tearDrop() throws Exception {
        DataBaseConfig.setE2ETest(false);
    }

    
    @Test
    @DisplayName("Given a ParkingType when getNextAvailableSpot then returns -1")
    public void givenParkingType_when_getNextAvailableSlot_thenReturnsMoins1() {
        //GIVEN
        parkingSpotDAO = new ParkingSpotDAO();
        ParkingType parkingType = null;
        //WHEN
        int ret = parkingSpotDAO.getNextAvailableSlot(parkingType);
        //THEN
        assertThat(ret).isEqualTo(-1);
    }

    @Test
    @DisplayName("Given No available Spot whenGetNextAvailableSpot then returns 0")
    public void givenNoAvailableSpot_whenGetNextAvailableSpotThenReturn0() {
        //GIVEN
        dataBasePrepareService.updateParkingSoptAvailable();
        parkingSpotDAO = new ParkingSpotDAO();
         //WHEN
        int ret = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);
        //THEN
        assertThat(ret).isEqualTo(0);
    }
}
