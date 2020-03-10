package com.parkit.parkingsystem.constants;

/**
 * Affect all SQL queries in public static final Strings.
 *
 * @author Tek
 */
public final class DBConstants {

    /**
     * SQL query use to find an available parking spot.
     */
    public static final String GET_NEXT_PARKING_SPOT = "select"
            + " min(PARKING_NUMBER) from parking"
            + " where AVAILABLE = true and TYPE = ?";
    /**
     * SQL query use to update availability of a parking spot.
     */
    public static final String UPDATE_PARKING_SPOT = "update parking"
            + " set available = ? where PARKING_NUMBER = ?";

    /**
     * SQL query use to save a ticket in DB.
     */
    public static final String SAVE_TICKET = "insert into ticket"
            + "(PARKING_NUMBER, VEHICLE_REG_NUMBER,"
            + "PRICE, IN_TIME, OUT_TIME, IS_RECURRENT_USER)"
            + "values(?,?,?,?,?,?)";

    /**
     * SQL query use to update a ticket in DB.
     */
    public static final String UPDATE_TICKET = "update ticket"
            + " set PRICE=?, OUT_TIME=? where ID=?";

    /**
     * SQL query use to get a ticket of DB.
     */
    public static final String GET_TICKET = "select t.PARKING_NUMBER,"
            + " t.ID, t.PRICE, t.IN_TIME, t.OUT_TIME, t.IS_RECURRENT_USER,"
            + " p.TYPE from ticket t,parking p "
            + "where p.parking_number = t.parking_number"
            + " and t.VEHICLE_REG_NUMBER=? "
            + "order by t.IN_TIME DESC limit 1";

    /**
     * SQL query use to find a ticket of DB.
     */
    public static final String SEARCH_TICKET = "select t.ID, t.IN_TIME"
            + " from ticket t where t.VEHICLE_REG_NUMBER=?"
            + " order by t.IN_TIME DESC limit 1";

    /**
     * Empty constructor.
     */
    private DBConstants() {

    }
}
