package com.parkit.parkingsystem;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.config.DataBaseConfig;

public class DataBaseConfigTest {

	private static DataBaseConfig dataBaseConfig;

	@BeforeAll
	private static void setUp() {
		dataBaseConfig = new DataBaseConfig();
	}

	@Test
	@DisplayName("Given a connection was declared, when DataBaseConfig.GetConnection is called, then a Connexion should be created.")
	public void givenDeclaredConnectionVariable_whenCallGetConnection_thenAConnexionExists() throws ClassNotFoundException, SQLException {
		// GIVEN
		Connection connection;
		// WHEN
		connection=dataBaseConfig.getConnection();
		// THEN
		assertThat(connection).isNotNull();
	}
	
}
