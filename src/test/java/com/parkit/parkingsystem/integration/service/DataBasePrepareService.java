package com.parkit.parkingsystem.integration.service;

import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;

import java.sql.Connection;
import java.sql.ResultSet;

/**
 * This Class is used to reset the test DataBase before each test, and to update
 * existing inTime values of ticket records during test.
 * 
 * @author docky
 */
public class DataBasePrepareService {

    /**
     * Create a DataBaseTestConfig used to deal with test DataBase.
     */
    DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    /**
     * Time delay used for inTime value update, to simulate an earlier entry.
     */
    private static final int TIME_DELAY_IN_SECONDS = 3601;

    /**
     * Reset the tests DataBase before tests.
     */
    public void clearDataBaseEntries() {
        Connection connection = null;
        try {
            connection = dataBaseTestConfig.getConnection();

            // set parking entries to available
            connection.prepareStatement("update parking set available = true")
                    .execute();

            // clear ticket entries;
            connection.prepareStatement("truncate table ticket").execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dataBaseTestConfig.closeConnection(connection);
        }
    }

    /**
     * Update an existing ticket inTime value to create a
     */
    public void updateInTime() {
        Connection connection = null;
        try {
            connection = dataBaseTestConfig.getConnection();

            // set ticket InTime
            connection.prepareStatement(
                    "update test.ticket set IN_TIME = date_sub(IN_TIME, interval "
                            + TIME_DELAY_IN_SECONDS + " SECOND) where ID=1;")
                    .executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dataBaseTestConfig.closeConnection(connection);
        }
    }

    public ResultSet getResultSet() {
        Connection connection = null;
        ResultSet rs = null;
        try {
            connection = dataBaseTestConfig.getConnection();
            rs = connection.prepareStatement(
                    "select * from test.ticket;").executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //dataBaseTestConfig.closeConnection(connection);
        }
        return rs;
    }
}
