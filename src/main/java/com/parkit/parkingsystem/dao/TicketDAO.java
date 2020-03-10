package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

/**
 * This class defines methods used to deal with ticket table of prod DB.
 *
 * @author Tek
 */
public class TicketDAO implements ITicketDAO {
    /**
     * Initialise a Logger used to send messages to the console.
     */
    private static final Logger LOGGER = LogManager.getLogger("TicketDAO");
    /**
     * Create a DataBasObject used to make a connection with the Prod DataBase.
     */
    private DataBaseConfig dataBaseConfig = new DataBaseConfig();
    /**
     * A static final int to avoid Magic Number.
     */
    private static final int SEPT = 7;

    /**
     * Used to check if there is a existing ticket for the given
     * vehicleRegNumber.
     *
     * @param vehicleRegNumber the unique identifer of a vehicle
     * @return true if a previous ticket exist
     */
    @Override
    public boolean checkExistingTicket(final String vehicleRegNumber) {
        boolean existTicket = false;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getDataBaseConfig().getConnection();
            ps = con.prepareStatement(DBConstants.SEARCH_TICKET);
            ps.setString(1, vehicleRegNumber);
            rs = ps.executeQuery();
            if (rs.next()) {
                existTicket = true;
            }
        } catch (Exception ex) {
            LOGGER.error("Error checkExistingTicket(vehicleRegNumber)", ex);
        } finally {
            getDataBaseConfig().closeConnection(con);
            getDataBaseConfig().closePreparedStatement(ps);
            getDataBaseConfig().closeResultSet(rs);
        }
        return existTicket;
    }

    /**
     * Used to save the given ticket in the ticket table of prod DB.
     *
     * @param ticket the Ticket to save
     * @return true if job is well done
     */
    @Override
    public boolean saveTicket(final Ticket ticket) {
        Connection con = null;
        PreparedStatement ps = null;
        int i = 1;
        try {
            con = getDataBaseConfig().getConnection();
            ps = con.prepareStatement(DBConstants.SAVE_TICKET);
            // ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME,
            // IS_RECURRENT_USER)
            ps.setInt(i, ticket.getParkingSpot().getId());
            i++;
            ps.setString(i, ticket.getVehicleRegNumber());
            i++;
            ps.setDouble(i, ticket.getPrice());
            i++;
            ps.setTimestamp(i, Timestamp.valueOf(ticket.getInTime()));
            i++;
            ps.setTimestamp(i, (ticket.getOutTime() == null) ? null
                    : (Timestamp.valueOf(ticket.getOutTime())));
            i++;
            ps.setBoolean(i, ticket.isRecurrentUser());
            return ps.execute();
        } catch (Exception ex) {
            LOGGER.error("Error fetching next available slot", ex);
            return false;
        } finally {
            getDataBaseConfig().closeConnection(con);
            getDataBaseConfig().closePreparedStatement(ps);
        }
    }

    /**
     * Used to get the latest Ticket of the given vehicleRegNumber.
     *
     * @param vehicleRegNumber the unique identifer of a vehicle
     * @return the latest Ticket for the given vehicleRegNumber
     */
    @Override
    public Ticket getTicket(final String vehicleRegNumber) {
        Connection con = null;
        PreparedStatement ps = null;
        Ticket ticket = null;
        ResultSet rs = null;
        int i = 2;
        try {
            con = getDataBaseConfig().getConnection();
            ps = con.prepareStatement(DBConstants.GET_TICKET);
            // ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
            ps.setString(1, vehicleRegNumber);
            rs = ps.executeQuery();
            if (rs.next()) {
                ticket = new Ticket();
                ParkingSpot parkingSpot = new ParkingSpot(rs.getInt(1),
                        ParkingType.valueOf(rs.getString(SEPT)), false);
                ticket.setParkingSpot(parkingSpot);
                ticket.setId(rs.getInt(i));
                ticket.setVehicleRegNumber(vehicleRegNumber);
                i++;
                ticket.setPrice(rs.getDouble(i));
                i++;
                ticket.setInTime(rs.getTimestamp(i).toLocalDateTime());
                i++;
                ticket.setOutTime((rs.getTimestamp(i) == null) ? null
                        : rs.getTimestamp(i).toLocalDateTime());
                i++;
                ticket.setRecurrentUser(rs.getBoolean(i));
            }
            getDataBaseConfig().closeResultSet(rs);
            getDataBaseConfig().closePreparedStatement(ps);
        } catch (Exception ex) {
            LOGGER.error("Error fetching next available slot", ex);
        } finally {
            getDataBaseConfig().closeConnection(con);
            getDataBaseConfig().closePreparedStatement(ps);
            getDataBaseConfig().closeResultSet(rs);
        }
        return ticket;
    }

    /**
     * Used to update the latest Ticket of the given vehicleRegNumber.
     *
     * @param ticket the Ticket to update
     * @return true if job is well done
     */
    @Override
    public boolean updateTicket(final Ticket ticket) {
        Connection con = null;
        PreparedStatement ps = null;
        int i = 1;
        try {
            con = getDataBaseConfig().getConnection();
            ps = con.prepareStatement(DBConstants.UPDATE_TICKET);
            ps.setDouble(i, ticket.getPrice());
            i++;
            ps.setTimestamp(i, Timestamp.valueOf(ticket.getOutTime()));
            i++;
            ps.setInt(i, ticket.getId());
            ps.execute();
            return true;
        } catch (Exception ex) {
            LOGGER.error("Error saving ticket info", ex);
            return false;
        } finally {
            getDataBaseConfig().closeConnection(con);
            getDataBaseConfig().closePreparedStatement(ps);
        }
    }

    /**
     * Setter of a DataBaseConfig object.
     *
     * @param dBConfig the DataBaseConfig to set
     */
    @Override
    public void setDataBaseConfig(final DataBaseConfig dBConfig) {
        this.dataBaseConfig = dBConfig;
    }

    /**
     * Getter of the object DataBaseConfig.
     *
     * @return the DataBaseConfig object
     */
    public DataBaseConfig getDataBaseConfig() {
        return dataBaseConfig;
    }

}
