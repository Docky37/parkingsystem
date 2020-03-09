package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import com.parkit.parkingsystem.service.IFareCalculatorService;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

public class FareCalculatorServiceTest {

	private static IFareCalculatorService fareCalculatorService;
	private Ticket ticket;

	@BeforeAll
	private static void setUp() {
		fareCalculatorService = new FareCalculatorService();
	}

	@BeforeEach
	private void setUpPerTest() {
		ticket = new Ticket();
	}

	@Nested
	@Tag("NormalRates")
	class NormalRates {
		@Test
		@DisplayName("For a car parked one hour, the price should be equal to the car rate per hour.")
		public void givenACarParkedOneHour_whenCalculate_thenPriceShouldBeCarRatePerHour() {
			// GIVEN
			LocalDateTime inTime = LocalDateTime.now();
			inTime = inTime.minusHours(1);
			LocalDateTime outTime = LocalDateTime.now();
			ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
			ticket.setInTime(inTime);
			ticket.setOutTime(outTime);
			ticket.setParkingSpot(parkingSpot);
			// WHEN
			fareCalculatorService.calculateFare(ticket);
			// THEN
			assertThat(ticket.getPrice()).isEqualTo(Fare.CAR_RATE_PER_HOUR);
		}

		@Test
		@DisplayName("For a bike parked one hour, the price should be equal to the bike rate per hour.")
		public void givenABikeParkedOneHour_whenCalculate_thenPriceShouldBeBikeRatePerHour() {
			// GIVEN
			LocalDateTime inTime = LocalDateTime.now();
			inTime = inTime.minusHours(1);
			LocalDateTime outTime = LocalDateTime.now();
			ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
			ticket.setInTime(inTime);
			ticket.setOutTime(outTime);
			ticket.setParkingSpot(parkingSpot);
			// WHEN
			fareCalculatorService.calculateFare(ticket);
			// THEN
			assertThat(ticket.getPrice()).isEqualTo(Fare.BIKE_RATE_PER_HOUR);
		}

		@Test
		@DisplayName("For a bike parked less than one hour, price should be equal duration multiply bike rate per hour.")
		public void givenABikeParkedLessThanOneHour_whenCalculate_thenPriceShouldBeDurationMultiplyBikeRatePerHour() {
			// GIVEN
			LocalDateTime inTime = LocalDateTime.now();
			inTime = inTime.minusMinutes(Fare.FREE_PARKING_DURATION + 1);// 30min Free Parking + 1 minute give 31/60th
																			// parking fare
			LocalDateTime outTime = LocalDateTime.now();
			ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
			ticket.setInTime(inTime);
			ticket.setOutTime(outTime);
			ticket.setParkingSpot(parkingSpot);
			BigDecimal price = new BigDecimal(0);
			price = BigDecimal.valueOf(Fare.FREE_PARKING_DURATION + 1).divide(BigDecimal.valueOf(60), 10,
					RoundingMode.HALF_UP);
			price = price.multiply(BigDecimal.valueOf(Fare.BIKE_RATE_PER_HOUR));
			BigDecimal roundedPrice = new BigDecimal(0);
			roundedPrice = price.setScale(2, RoundingMode.HALF_UP);
			// WHEN
			fareCalculatorService.calculateFare(ticket);
			// THEN
			assertThat(ticket.getPrice()).isEqualTo(roundedPrice.doubleValue());
		}

		@Test
		@DisplayName("For a car parked less than one hour, price should be equal duration multiply car rate per hour.")
		public void givenACarParkedLessThanOneHour_whenCalculate_thenPriceShouldBeDurationMultiplyCarRatePerHour() {
			// GIVEN
			LocalDateTime inTime = LocalDateTime.now();
			inTime = inTime.minusMinutes(Fare.FREE_PARKING_DURATION + 1);// 30min Free Parking + 1 minute give 31/60th
																			// parking fare
			LocalDateTime outTime = LocalDateTime.now();
			ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
			ticket.setInTime(inTime);
			ticket.setOutTime(outTime);
			ticket.setParkingSpot(parkingSpot);
			BigDecimal price = new BigDecimal(0);
			price = BigDecimal.valueOf(Fare.FREE_PARKING_DURATION + 1).divide(BigDecimal.valueOf(60), 10,
					RoundingMode.HALF_UP);
			price = price.multiply(BigDecimal.valueOf(Fare.CAR_RATE_PER_HOUR));
			BigDecimal roundedPrice = new BigDecimal(0);
			roundedPrice = price.setScale(2, RoundingMode.HALF_UP);
			// WHEN
			fareCalculatorService.calculateFare(ticket);
			// THEN
			assertThat(ticket.getPrice()).isEqualTo(roundedPrice.doubleValue());
		}

		@Test
		@DisplayName("For a car parked 24 hours, price should be equal 24 multiply car rate per hour.")
		public void givenACarParked24Hours_whenCalculate_thenPriceShouldBe24xCarRatePerHour() {
			// GIVEN
			LocalDateTime inTime = LocalDateTime.now();
			inTime = inTime.minusHours(24);
			LocalDateTime outTime = LocalDateTime.now();
			ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
			ticket.setInTime(inTime);
			ticket.setOutTime(outTime);
			ticket.setParkingSpot(parkingSpot);
			// WHEN
			fareCalculatorService.calculateFare(ticket);
			// THEN
			assertThat(ticket.getPrice()).isEqualTo(24 * Fare.CAR_RATE_PER_HOUR);
		}

	}

	@Nested
	@Tag("Exceptions")
	class Exceptions {
		@Test
		@DisplayName("If parking spot type is undefined, calculatorFare raise an NullPointerException.")
		public void givenUnkownTypeSpot_whencalculate_thenNullPointerException() {
			// GIVEN
			LocalDateTime inTime = LocalDateTime.now();
			inTime = inTime.minusHours(1);
			LocalDateTime outTime = LocalDateTime.now();
			ParkingSpot parkingSpot = new ParkingSpot(1, null, false);
			// WHEN
			ticket.setInTime(inTime);
			ticket.setOutTime(outTime);
			ticket.setParkingSpot(parkingSpot);
			// ASSERT
			assertThatThrownBy(() -> fareCalculatorService.calculateFare(ticket))
					.isInstanceOf(NullPointerException.class);
		}

		@Test
		@DisplayName("If parking exit time is prior to entry, calculatorFare raise an IllegalArgumentException.")
		public void givenNegativeParkingDuration_whenCalculate_thenIllegalArgumentException() {
			// GIVEN
			LocalDateTime inTime = LocalDateTime.now();
			inTime = inTime.plusHours(1);
			LocalDateTime outTime = LocalDateTime.now();
			ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
			// WHEN
			ticket.setInTime(inTime);
			ticket.setOutTime(outTime);
			ticket.setParkingSpot(parkingSpot);
			// THEN
			assertThatThrownBy(() -> fareCalculatorService.calculateFare(ticket))
					.isInstanceOf(IllegalArgumentException.class);
		}

	}

	@Nested
	@Tag("PreferentialRates")
	class PreferentialRates {
		@Test
		@DisplayName("For a reccuring user, a discount is applied.")
		public void givenARecurringUser_whenCalculate_thenDiscountIsApplied() {
			// GIVEN
			LocalDateTime inTime = LocalDateTime.now();
			inTime = inTime.minusHours(1);
			LocalDateTime outTime = LocalDateTime.now();
			ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
			ticket.setInTime(inTime);
			ticket.setOutTime(outTime);
			ticket.setParkingSpot(parkingSpot);
			ticket.setRecurrentUser(true);
			BigDecimal price = new BigDecimal(0);
			price = BigDecimal.ONE.subtract(BigDecimal.valueOf(Fare.REGULAR_CUSTOMER_DISCOUNT))
					.multiply(BigDecimal.valueOf(Fare.CAR_RATE_PER_HOUR));
			BigDecimal roundedPrice = new BigDecimal(0);
			roundedPrice = price.setScale(2, RoundingMode.HALF_UP);
			// ACT
			fareCalculatorService.calculateFare(ticket);
			// THEN
			assertThat(ticket.getPrice()).isEqualTo(roundedPrice.doubleValue());
		}

		@Test
		@DisplayName("For a car parked less than 30 minutes, parking is free.")
		public void givenACarParkedLessThanThirtyMinutes_whenCalculate_thenFreeParking() {
			// GIVEN
			LocalDateTime inTime = LocalDateTime.now();
			inTime = inTime.minusMinutes(20);
			LocalDateTime outTime = LocalDateTime.now();
			ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
			ticket.setInTime(inTime);
			ticket.setOutTime(outTime);
			ticket.setParkingSpot(parkingSpot);
			ticket.setRecurrentUser(true);
			// ACT
			fareCalculatorService.calculateFare(ticket);
			// THEN
			assertThat(ticket.getPrice()).isEqualTo(0);
		}
	}

}
