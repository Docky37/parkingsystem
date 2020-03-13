package com.parkit.parkingsystem.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * @author Tek
 *
 */
public class DataBaseConfig {

    /**
     * LOGGER initialized to send console message.
     */
    private static final Logger LOGGER = LogManager.getLogger("DataBaseConfig");
    /**
     * URL used to connect application to MySQL database.
     */
    private String url;
    /**
     * Path of the connexion properties file.
     */
    private static final String PROPERTIES_PATH = "src/main/resources/"
            + "db.properties";
    /**
     * Use to store the username of MySQL database.
     */
    private String user;
    /**
     * Use to store the password of MySQL database.
     */
    private String password;
    /**
     * Boolean used to switch BufferredReader for E2E test.
     */
    private static boolean isE2ETest = false;

    /**
     * Create a connection to MySQL database.
     *
     * @return a Connection instance
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public Connection getConnection()
            throws ClassNotFoundException, SQLException {
        LOGGER.info("Create DB connection");
        try (FileInputStream f = new FileInputStream(PROPERTIES_PATH)) {
            Properties pros = new Properties();
            pros.load(f);
            // assign url with test or prod DataBase
            if (isE2ETest) {
                url = pros.getProperty("url2");
            } else {
                url = pros.getProperty("url");
            }
            user = pros.getProperty("user");
            password = pros.getProperty("password");

            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (IOException e) {
            LOGGER.error("Unable to read database properties file!", e);
        }
        return DriverManager.getConnection(url, user, password);
    }

    /**
     * Close the given Connection.
     *
     * @param con the Connection to close
     */
    public void closeConnection(final Connection con) {
        if (con != null) {
            try {
                con.close();
                LOGGER.info("Closing DB connection");
            } catch (SQLException e) {
                LOGGER.error("Error while closing connection", e);
            }
        }
    }

    /**
     * Close the given PreparedStatement.
     *
     * @param ps the PreparedStatement to close
     */
    public void closePreparedStatement(final PreparedStatement ps) {
        if (ps != null) {
            try {
                ps.close();
                LOGGER.info("Closing Prepared Statement");
            } catch (SQLException e) {
                LOGGER.error("Error while closing prepared statement", e);
            }
        }
    }

    /**
     * Close the given ResultSet.
     *
     * @param rs the ResultSet to close
     */
    public void closeResultSet(final ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
                LOGGER.info("Closing Result Set");
            } catch (SQLException e) {
                LOGGER.error("Error while closing result set", e);
            }
        }
    }

    /**
     * Setter du boolean isE2ETest.
     *
     * @param e2ETest the isE2ETest to set
     */
    public static void setE2ETest(final boolean e2ETest) {
        isE2ETest = e2ETest;
    }

}
