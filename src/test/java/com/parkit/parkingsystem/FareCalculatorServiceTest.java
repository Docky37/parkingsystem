package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

public class FareCalculatorServiceTest {

	private static FareCalculatorService fareCalculatorService;
	private Ticket ticket;

	@BeforeAll
	private static void setUp() {
		fareCalculatorService = new FareCalculatorService();
	}

	@BeforeEach
	private void setUpPerTest() {
		ticket = new Ticket();
	}

	@Test
	public void calculateFareCar() {
		LocalDateTime inTime = LocalDateTime.now();
		inTime = inTime.minusHours(1);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket);
		assertEquals(Fare.CAR_RATE_PER_HOUR, ticket.getPrice());
	}

	@Test
	public void calculateFareBike() {
		LocalDateTime inTime = LocalDateTime.now();
		inTime = inTime.minusHours(1);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket);
		assertEquals(Fare.BIKE_RATE_PER_HOUR, ticket.getPrice());
	}

	@Test
	public void calculateFareUnkownType() {
		LocalDateTime inTime = LocalDateTime.now();
		inTime = inTime.minusHours(1);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, null, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
	}

	@Test
	public void calculateFareBikeWithFutureInTime() {
		LocalDateTime inTime = LocalDateTime.now();
		inTime = inTime.plusHours(1);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
	}

	@Test
	public void calculateFareBikeWithLessThanOneHourParkingTime() {
		LocalDateTime inTime = LocalDateTime.now();
		inTime = inTime.minusMinutes(Fare.FREE_PARKING_DURATION + 1);// 30min Free Parking + 1 minute give 31/60th
																		// parking fare
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket);
		BigDecimal price = new BigDecimal(0);
		price = BigDecimal.valueOf(Fare.FREE_PARKING_DURATION + 1).divide(BigDecimal.valueOf(60), 10,
				RoundingMode.HALF_UP);
		price = price.multiply(BigDecimal.valueOf(Fare.BIKE_RATE_PER_HOUR));
		BigDecimal roundedPrice = new BigDecimal(0);
		roundedPrice = price.setScale(2, RoundingMode.HALF_UP);
		assertEquals(ticket.getPrice(), roundedPrice.doubleValue());
	}

	@Test
	public void calculateFareCarWithLessThanOneHourParkingTime() {
		LocalDateTime inTime = LocalDateTime.now();
		inTime = inTime.minusMinutes(Fare.FREE_PARKING_DURATION + 1);// 30min Free Parking + 1 minute give 31/60th
																		// parking fare
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket);

		BigDecimal price = new BigDecimal(0);
		price = BigDecimal.valueOf(Fare.FREE_PARKING_DURATION + 1).divide(BigDecimal.valueOf(60), 10,
				RoundingMode.HALF_UP);
		price = price.multiply(BigDecimal.valueOf(Fare.CAR_RATE_PER_HOUR));
		BigDecimal roundedPrice = new BigDecimal(0);
		roundedPrice = price.setScale(2, RoundingMode.HALF_UP);
		assertEquals(ticket.getPrice(), roundedPrice.doubleValue());
	}

	@Test
	public void calculateFareCarWithMoreThanADayParkingTime() {// 24 hours parking time should give 24 * parking fare
																// per hour
		LocalDateTime inTime = LocalDateTime.now();
		inTime = inTime.minusHours(24);// 24 hours parking time should give 24 * parking fare per hour
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket);

		assertEquals((24 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void calculateFareCarForRecurringUser() {
		LocalDateTime inTime = LocalDateTime.now();
		inTime = inTime.minusHours(1);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		ticket.setRecurrentUser(true);
		fareCalculatorService.calculateFare(ticket);

		BigDecimal price = new BigDecimal(0);
		price = BigDecimal.ONE.subtract(BigDecimal.valueOf(Fare.REGULAR_CUSTOMER_DISCOUNT))
				.multiply(BigDecimal.valueOf(Fare.CAR_RATE_PER_HOUR));
		BigDecimal roundedPrice = new BigDecimal(0);
		roundedPrice = price.setScale(2, RoundingMode.HALF_UP);
		assertEquals(ticket.getPrice(), roundedPrice.doubleValue());
	}

	@Test
	public void calculateFareCarForLessThanThirtyMinutes() {
		LocalDateTime inTime = LocalDateTime.now();
		inTime = inTime.minusMinutes(20);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		ticket.setRecurrentUser(true);
		fareCalculatorService.calculateFare(ticket);

		assertEquals(0, ticket.getPrice());
	}

}
