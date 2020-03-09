package com.parkit.parkingsystem.util;

import java.io.IOException;

public interface I_ImputReaderUtil {

	int readSelection();

	String readVehicleRegistrationNumber() throws IOException;

}