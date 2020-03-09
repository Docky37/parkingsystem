package com.parkit.parkingsystem.util;

import java.io.IOException;

public interface IImputReaderUtil {

	int readSelection();

	String readVehicleRegistrationNumber() throws IOException;

}