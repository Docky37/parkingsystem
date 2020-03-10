package com.parkit.parkingsystem.model;

import java.time.LocalDateTime;

/**
 * This class is used to store the variants of the 7 fields of a ticket table
 * record.
 *
 * @author Tek
 */
public class Ticket {
    /**
     * The unique identifier of a ticket.
     */
    private int id;
    /**
     * The number of the parling spot allocated to the vehicle.
     */
    private ParkingSpot parkingSpot;
    /**
     * The vehicleRegNumber of the vehicle.
     */
    private String vehicleRegNumber;
    /**
     * The price to pay when exit from parking.
     */
    private double price;
    /**
     * The LocalDateTime of parking entry.
     */
    private LocalDateTime inTime;
    /**
     * The LocalDateTime of parking exit.
     */
    private LocalDateTime outTime;
    /**
     * This boolean is setted with true
     * if a previous ticket exits for this vehicule.
     */
    private boolean isRecurrentUser = false;


    /**
     * Getter of Ticket.id.
     * @return int the id of the ticket
     */
    public int getId() {
        return id;
    }

    /**
     * Setter of Ticket.id.
     * @param identifier the id of the ticket
     */
    public void setId(final int identifier) {
        this.id = identifier;
    }

    /**
     * Getter of Ticket.parkingSpot.
     * @return a ParkingSpot object
     */
    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    /**
     * Setter of Ticket.parkingSpot.
     * @param pkSpot
     */
    public void setParkingSpot(final ParkingSpot pkSpot) {
        this.parkingSpot = pkSpot;
    }

    /**
     * Getter of Ticket.parkingSpot.
     * @return a String - the vehicule registration number
     */
    public String getVehicleRegNumber() {
        return vehicleRegNumber;
    }

    /**
     * Setter of Ticket.parkingSpot.
     * @param vRegNumber - the vehicule registration number to set
     */
    public void setVehicleRegNumber(final String vRegNumber) {
        this.vehicleRegNumber = vRegNumber;
    }

    /**
     * Getter of Ticket.price.
     * @return a double - the price to pay when exit parking
     */
    public double getPrice() {
        return price;
    }

    /**
     * Setter of Ticket.price.
     * @param priceToPay
     */
    public void setPrice(final double priceToPay) {
        this.price = priceToPay;
    }

    /**
     * Getter of Ticket.inTime.
     * @return the LocalDateTime of parking entry
     */
    public LocalDateTime getInTime() {
        return inTime;
    }

    /**
     * Setter of Ticket.inTime.
     * @param entryTime - the LocalDateTime of entry
     */
    public void setInTime(final LocalDateTime entryTime) {
        this.inTime = entryTime;
    }

    /**
     * Getter of Ticket.outTime.
     * @return the LocalDateTime of parking exit
     */
    public LocalDateTime getOutTime() {
        return outTime;
    }

    /**
     * Setter of Ticket.ouTime.
     * @param exitTime - the LocalDateTime of exit
     */
    public void setOutTime(final LocalDateTime exitTime) {
        this.outTime = exitTime;
    }

    /**
     * Getter of Ticket.isRecurrentUser.
     * @return a boolean (true if previous ticket exists in DB)
     */
    public boolean isRecurrentUser() {
        return isRecurrentUser;
    }

    /**
     * Setter of Ticket.isRegularCustomer.
     * @param isRecurrent a boolean (true if previous ticket exists in DB)
     */
    public void setRecurrentUser(final boolean isRecurrent) {
        this.isRecurrentUser = isRecurrent;
    }

}
