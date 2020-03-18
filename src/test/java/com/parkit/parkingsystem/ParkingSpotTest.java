package com.parkit.parkingsystem;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;

public class ParkingSpotTest {

    ParkingSpot parkingSpot = null;
    
    @Test
    public void test1() {
        
        parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
        //when
        parkingSpot.setId(4);
        parkingSpot.setParkingType(ParkingType.BIKE);
       
        
        assertThat(parkingSpot.getId()).isEqualTo(4);
        assertThat(parkingSpot.getParkingType()).isEqualByComparingTo(ParkingType.BIKE);
    }
    
}
