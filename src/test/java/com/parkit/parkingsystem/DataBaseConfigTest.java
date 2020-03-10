package com.parkit.parkingsystem;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;

public class DataBaseConfigTest {

	private static DataBaseConfig dataBaseConfig;

	@BeforeAll
	private static void setUp() {
		dataBaseConfig = new DataBaseConfig();
	}

	@Test
	@Tag("GetConnection")
	@DisplayName("Given a connection was declared, when DataBaseConfig.GetConnection is called, then a Connexion should be created.")
	public void givenDeclaredConnectionVariable_whenCallGetConnection_thenAConnexionExists()
			throws ClassNotFoundException, SQLException {
		// GIVEN
		Connection connection;
		// WHEN
		connection = dataBaseConfig.getConnection();
		// THEN
		assertThat(connection).isNotNull();
	}

	@Test
	@Tag("CloseConnection")
	@DisplayName("Given a connection exists, when DataBaseConfig.CloseConnection is called, then Connexion should be closed.")
	public void givenAnExistingConnection_whenCallCloseConnection_thenAConnexionIsClosed()
			throws ClassNotFoundException, SQLException {
		// GIVEN
		Connection connection;
		connection = dataBaseConfig.getConnection();
		// WHEN
		dataBaseConfig.closeConnection(connection);
		// THEN
		assertThat(connection.isClosed()).isTrue();
	}

	@Test
	@Tag("closePreparedStatement")
	@DisplayName("Given a PreparedStatement exists, when DataBaseConfig.closePreparedStatement is called, then PreparedStatement should be closed.")
	public void givenAnExistingPS_whenCallClosePreparedStatement_thenPreparedStatementIsClosed()
			throws ClassNotFoundException, SQLException {
		// GIVEN
		Connection connection;
		connection = dataBaseConfig.getConnection();
		PreparedStatement ps = null;
		ps = connection.prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT);
		// WHEN
		dataBaseConfig.closePreparedStatement(ps);
		dataBaseConfig.closeConnection(connection);
		// THEN
		assertThat(ps.isClosed()).isTrue();
	}

	@Test
	@Tag("closeResultSet")
	@DisplayName("Given a ResultSet exists, when DataBaseConfig.closeResultSet is called, then ResultSet should be closed.")
	public void givenAnExistingResultSet_whenCallCloseResultSet_thenResultSetIsClosed()
			throws ClassNotFoundException, SQLException {
		// GIVEN
		Connection connection;
		connection = dataBaseConfig.getConnection();
		ResultSet rs = null;
		PreparedStatement ps = null;
		ps = connection.prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT);
		ps.setString(1, "CAR");
		rs = ps.executeQuery();
		// WHEN
		dataBaseConfig.closeResultSet(rs);
		dataBaseConfig.closePreparedStatement(ps);
		dataBaseConfig.closeConnection(connection);
		// THEN
		assertThat(rs.isClosed()).isTrue();
	}

}
