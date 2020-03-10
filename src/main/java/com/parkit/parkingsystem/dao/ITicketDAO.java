package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.model.Ticket;

/**
 * This interface contains 5 methods used to deal with the ticket table of prod
 * DB.
 *
 * @author docky
 */
public interface ITicketDAO {

    /**
     * Used to check if there is a existing ticket for the given
     * vehicleRegNumber.
     *
     * @param vehicleRegNumber the unique identifer of a vehicle
     * @return true if a previous ticket exist
     */
    boolean checkExistingTicket(String vehicleRegNumber);

    /**
     * Used to save the given ticket in the ticket table of prod DB.
     *
     * @param ticket the Ticket to save
     * @return true if job is well done
     */
    boolean saveTicket(Ticket ticket);

    /**
     * Used to get the latest Ticket of the given vehicleRegNumber.
     *
     * @param vehicleRegNumber the unique identifer of a vehicle
     * @return the latest Ticket for the given vehicleRegNumber
     */
    Ticket getTicket(String vehicleRegNumber);

    /**
     * Used to update the latest Ticket of the given vehicleRegNumber.
     *
     * @param ticket the Ticket to update
     * @return true if job is well done
     */
    boolean updateTicket(Ticket ticket);

    /**
     * Setter of a DataBaseConfig object.
     *
     * @param dataBaseConfig the DataBaseConfig to set
     */
    void setDataBaseConfig(DataBaseConfig dataBaseConfig);

}
