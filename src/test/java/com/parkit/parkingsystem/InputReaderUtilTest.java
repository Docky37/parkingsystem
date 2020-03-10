package com.parkit.parkingsystem;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.util.IImputReaderUtil;
import com.parkit.parkingsystem.util.InputReaderUtil;

public class InputReaderUtilTest {
	private static IImputReaderUtil inputReaderUtil;

	@BeforeAll
	private static void setUp() {
		inputReaderUtil = new InputReaderUtil();
	}

	@Nested
	@Tag("MenuItemTests")
	class MenuItemTests {
		@Test
		@Tag("ValidInput")
		@DisplayName("Given a keyboard input process, when input=1, then read 1")
		public void givenAKeyboardInputProcess_whenEntry1_thenRead1() throws IOException {
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
		@Tag("Number12Input")
		@DisplayName("Given a keyboard input process, when input=12, then read -1")
		public void givenAKeyboardInputProcess_whenEntry12_thenReadMoins1() throws IOException {
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
		@Tag("OneLetterInput")
		@DisplayName("Given a keyboard input process, when input one letter, then read -1")
		public void givenAKeyboardInputProcess_whenEntryOneLetter_thenReadMoins1() throws IOException {
			// GIVEN
			int option;
			String data = "X";
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
		@Tag("BigStringInput")
		@DisplayName("Given a keyboard input process, when input is too long, then read -1")
		public void givengivenAKeyboardInputProcess_whenInputIsABigString_thenReadMoins1() throws IOException {
			// GIVEN
			int option;
			String data = "A computer pirate (hacker) or a malicious person try to attack our pretty job!?!?!?";
			InputStream stream = new ByteArrayInputStream((data + "\n").getBytes(StandardCharsets.UTF_8));
			InputStream stdin = System.in;
			// WHEN
			System.setIn(stream);
			option = inputReaderUtil.readSelection();
			System.setIn(stdin);
			// THEN
			assertThat(option).isEqualTo(-1);
		}
	}

	@Nested
	@Tag("RegNumberTests")
	class RegNumberTests {
		@Test
		@Tag("ValidRegNumber")
		@DisplayName("Given a keyboard input process, when input='AB125XY', then reading 'AB125XY'")
		public void givenAKeyboardInputProcess_whenEntryAB125XY_thenReadAB125XY() throws Exception {
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
		@Tag("BigStringInput")
		@DisplayName("Given a keyboard input process, when input is too long, then reading 'ILLEGAL ARGUMENT'")
		public void givenAKeyboardInputProcess_whenTooLongEntry_thenIllegalArgument() throws Exception {
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
		@Tag("EmptyInput")
		@DisplayName("Given a keyboard input process, when input is empty, then reading 'ILLEGAL ARGUMENT'")
		public void givenAKeyboardInputProcess_whenEmptyInput_thenIllegalArgument() throws Exception {
			// GIVEN
			String regNumb;
			String data = "";
			InputStream stream = new ByteArrayInputStream((data + "\n").getBytes(StandardCharsets.UTF_8));
			InputStream stdin = System.in;
			// WHEN
			System.setIn(stream);
			regNumb = inputReaderUtil.readVehicleRegistrationNumber();
			System.setIn(stdin);
			// THEN
			assertThat(regNumb).isEqualTo("ILLEGAL ARGUMENT");
		}
	}
}
