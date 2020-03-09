package com.parkit.parkingsystem.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class InputReaderUtil implements IImputReaderUtil {
	private static final Logger logger = LogManager.getLogger("InputReaderUtil");

	@Override
	public int readSelection() {
		int tempInt = -1;
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		try {
			String tempString;
			tempString = bufferedReader.readLine();
			if (tempString.length() == 1) {
				tempInt = Integer.parseInt(tempString);
			}
		} catch (IOException e) {
			logger.error("Error while reading user input from Shell", e);
			logger.error("Error reading input. Please enter valid number for proceeding further");
			tempInt = -1;
		}
		return (tempInt);
	}

	@Override
	public String readVehicleRegistrationNumber() throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		try {
			String vehicleRegNumber = bufferedReader.readLine();
			if (vehicleRegNumber != null && vehicleRegNumber.trim().length() > 0
					&& vehicleRegNumber.trim().length() < 9) {
				return vehicleRegNumber;
			}
			return "ILLEGAL ARGUMENT";
		} catch (IOException ioe) {
			logger.error("Input error while reading", ioe);
			throw ioe;
		}
	}

}
