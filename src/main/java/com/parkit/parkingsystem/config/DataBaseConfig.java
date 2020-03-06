package com.parkit.parkingsystem.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DataBaseConfig {

	private static final Logger logger = LogManager.getLogger("DataBaseConfig");
	private String url;
    private String user;
    private String password;

	public Connection getConnection() throws ClassNotFoundException, SQLException {
		logger.info("Create DB connection");
		try(FileInputStream f = new FileInputStream("db.properties")) {
		    // load the properties file
		    Properties pros = new Properties();
		    pros.load(f);
		 
		    // assign db parameters
		    url       = pros.getProperty("url");
		    user      = pros.getProperty("user");
		    password  = pros.getProperty("password");
		    
		    // create a connection to the database
			Class.forName("com.mysql.cj.jdbc.Driver");
			return DriverManager.getConnection(url, user, password);
		}catch (IOException e){
			logger.error("Unable to read database properties file!",e);
		}
		return DriverManager.getConnection(url, user, password);
	}

	public void closeConnection(Connection con) {
		if (con != null) {
			try {
				con.close();
				logger.info("Closing DB connection");
			} catch (SQLException e) {
				logger.error("Error while closing connection", e);
			}
		}
	}

	public void closePreparedStatement(PreparedStatement ps) {
		if (ps != null) {
			try {
				ps.close();
				logger.info("Closing Prepared Statement");
			} catch (SQLException e) {
				logger.error("Error while closing prepared statement", e);
			}
		}
	}

	public void closeResultSet(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
				logger.info("Closing Result Set");
			} catch (SQLException e) {
				logger.error("Error while closing result set", e);
			}
		}
	}
}
