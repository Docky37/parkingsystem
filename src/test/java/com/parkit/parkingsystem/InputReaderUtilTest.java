package com.parkit.parkingsystem;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.util.InputReaderUtil;

public class InputReaderUtilTest {
	private static InputReaderUtil inputReaderUtil;

	@BeforeAll
	private static void setUp() {
		inputReaderUtil = new InputReaderUtil();
	}

	@Test
	public void givenAKeyboardEntryAsk_whenEntry1_thenRead1() throws IOException {
		// GIVEN
		int option;
		String data = "1";
		InputStream stream = new ByteArrayInputStream((data + "\n").getBytes(StandardCharsets.UTF_8));
		InputStream stdin = System.in;
		// WHEN
		System.setIn(stream);
		option = inputReaderUtil.readSelection();
		System.setIn(stdin);
		// THEN
		assertThat(option).isEqualTo(1);
	}

	@Test
	public void givenAKeyboardEntryAsk_whenEntry12_thenReadMoins1() throws IOException {
		// GIVEN
		int option;
		String data = "12";
		InputStream stream = new ByteArrayInputStream((data + "\n").getBytes(StandardCharsets.UTF_8));
		InputStream stdin = System.in;
		// WHEN
		System.setIn(stream);
		option = inputReaderUtil.readSelection();
		System.setIn(stdin);
		// THEN
		assertThat(option).isEqualTo(-1);
	}

	@Test
	public void givenAKeyboardEntryAsk_whenEntryAB125XY_thenReadAB125XY() throws Exception {
		// GIVEN
		String regNumb;
		String data = "AB125XY";
		InputStream stream = new ByteArrayInputStream((data + "\n").getBytes(StandardCharsets.UTF_8));
		InputStream stdin = System.in;
		// WHEN
		System.setIn(stream);
		regNumb = inputReaderUtil.readVehicleRegistrationNumber();
		System.setIn(stdin);
		// THEN
		assertThat(regNumb).isEqualTo("AB125XY");
	}

	@Test
	public void givenAKeyboardEntryAsk_whenTooLongEntry_thenIllegalArgument() throws Exception {
		// GIVEN
		String regNumb;
		String data = "A computer pirate (hacker) or a malicious person try to attack our pretty job!?!?!?";
		InputStream stream = new ByteArrayInputStream((data + "\n").getBytes(StandardCharsets.UTF_8));
		InputStream stdin = System.in;
		// WHEN
		System.setIn(stream);
		regNumb = inputReaderUtil.readVehicleRegistrationNumber();
		System.setIn(stdin);
		// THEN
		assertThat(regNumb).isEqualTo("ILLEGAL ARGUMENT");
	}

	@Test
	public void givenAKeyboardEntryAsk_whenNullEntry_thenIllegalArgument() throws Exception {
		// GIVEN
		String regNumb;
		String data = null;
		InputStream stream = new ByteArrayInputStream((data + "\n").getBytes(StandardCharsets.UTF_8));
		InputStream stdin = System.in;
		// WHEN
		System.setIn(stream);
		regNumb = inputReaderUtil.readVehicleRegistrationNumber();
		System.setIn(stdin);
		// THEN
		assertThat(regNumb).isEqualTo("null");
	}

}
