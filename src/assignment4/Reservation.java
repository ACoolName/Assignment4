/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment4;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author raul
 */
public class Reservation implements ReservationInterface {

    private static Connection conn = null;
    private static PreparedStatement stmt;
    private String URL = "jdbc:oracle:thin:@datdb.cphbusiness.dk:1521:dat";
    private String user;
    private String pw;
    private long cooldown = 10;

    public Reservation(String user, String pw) throws SQLException {
        this.user = user;
        this.pw = pw;
        try {
            getConnection();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Reservation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        conn = DriverManager.getConnection(URL, user, pw);
        System.out.println("Connected to database");
        conn.setAutoCommit(true);
        return conn;
    }

    public void closeConnection() throws SQLException {
        conn.close();
    }

    @Override
    public String reserve(String plane_no, long id) {
        long timeStamp = System.currentTimeMillis() / 1000L;
        int rowsInserted = 0;
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        String availableSeat = null;
        String availableQuery = "SELECT seat_no FROM seat WHERE booked is null and (? - booking_time > ? OR booking_time is null) and rownum=1";
        String reserveQuery = "UPDATE seat SET reserved= ? , booking_time= ? WHERE plane_no = ? and seat_no= ? ";
        try {
            stmt = conn.prepareStatement(availableQuery);
            stmt.setLong(1, timeStamp);
            stmt.setLong(2, cooldown);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                availableSeat = rs.getString(1);
            }
            rs.close();
            if (availableSeat != null) {
                stmt2 = conn.prepareStatement(reserveQuery);
                stmt2.setLong(1, id);
                stmt2.setLong(2, timeStamp);
                stmt2.setString(3, plane_no);
                stmt2.setString(4, availableSeat);
                rowsInserted = stmt2.executeUpdate();
//                System.out.println("Reserve: seat: " + availableSeat + " rows: " + rowsInserted);
                if (rowsInserted > 0) {
                    return availableSeat;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Reservation.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (stmt2 != null) {
                    stmt2.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(Reservation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    public int badBook(String plane_no, String seat_no, long id) {
        long timeStamp = System.currentTimeMillis() / 1000L;
        int rowsInserted = 0;
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        long customer_id = -1L;
        long booking_time = -1L;
        String reservationQuery = "SELECT reserved, booking_time FROM seat WHERE seat_no = ?";
        String bookQuery = "UPDATE seat SET booking_time = ?, booked = ? WHERE seat_no = ?";
        try {
            stmt = conn.prepareStatement(reservationQuery);
            stmt.setString(1, seat_no);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                customer_id = rs.getLong(1);
                booking_time = rs.getLong(2);
            }
            rs.close();
            if (customer_id == 0) {
                return -1; //seat not reserved
            }
            if (customer_id == id) {
                if (timeStamp - booking_time < cooldown) {
                    stmt2 = conn.prepareStatement(bookQuery);
                    stmt2.setLong(1, timeStamp);
                    stmt2.setLong(2, id);
                    stmt2.setString(3, seat_no);
                    rowsInserted = stmt2.executeUpdate();
//                    System.out.println("Book: seat: " + seat_no + " customer: " + customer_id);
                    if (rowsInserted > 0) {
                        return 0;   //success
                    } else {
                        return -5; //ghosts and stuff
                    }
                } else {
                    return -3; //timeout
                }
            } else {
                return -2; //reserved by someone else
            }
        } catch (Exception ex) {
//            System.out.println("Something wicked: rowInserted " + rowsInserted);
            ex.printStackTrace();
            return -5; //spooky error
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (stmt2 != null) {
                    stmt2.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(Reservation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public int book(String plane_no, String seat_no, long id) {
        long timeStamp = System.currentTimeMillis() / 1000L;
        int rowsInserted = 0;
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        long customer_id = -1L;
        long booking_time = -1L;
        String reservationQuery = "SELECT reserved, booking_time FROM seat WHERE seat_no = ? for update";
        String bookQuery = "UPDATE seat SET booking_time = ?, booked = ? WHERE seat_no = ?";
        try {
            stmt = conn.prepareStatement(reservationQuery);
            stmt.setString(1, seat_no);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                customer_id = rs.getLong(1);
                booking_time = rs.getLong(2);
            }
            rs.close();
            if (customer_id == 0) {
                return -1; //seat not reserved
            }
            if (customer_id == id) {
                if (timeStamp - booking_time < cooldown) {
                    stmt2 = conn.prepareStatement(bookQuery);
                    stmt2.setLong(1, timeStamp);
                    stmt2.setLong(2, id);
                    stmt2.setString(3, seat_no);
                    rowsInserted = stmt2.executeUpdate();
//                    System.out.println("Book: seat: " + seat_no + " customer: " + customer_id);
                    if (rowsInserted > 0) {
                        return 0;   //success
                    } else {
                        return -5; //ghosts and stuff
                    }
                } else {
                    return -3; //timeout
                }
            } else {
                return -2; //reserved by someone else
            }
        } catch (Exception ex) {
//            System.out.println("Something wicked: rowInserted " + rowsInserted);
            ex.printStackTrace();
            return -5; //spooky error
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (stmt2 != null) {
                    stmt2.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(Reservation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void bookAll(String plane_no) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void clearAllBookings(String plane_no) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isAllBooked(String plane_no) {
        PreparedStatement stmt = null;
        try {
            String isAllBookedQuery = "SELECT * FROM seat WHERE booked is null AND plane_no = ?";
            stmt = conn.prepareStatement(isAllBookedQuery);
            stmt.setString(1, plane_no);
            ResultSet result = stmt.executeQuery();
            return !result.next();
        } catch (SQLException ex) {
            Logger.getLogger(Reservation.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(Reservation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public boolean isAllReserved(String plane_no) {
        PreparedStatement stmt = null;
        try {
            long timeStamp = System.currentTimeMillis() / 1000L;
            String isAllBookedQuery = "SELECT * FROM seat WHERE (reserved is null OR ? - booking_time > ? or booking_time is null) AND plane_no = ?";
            stmt = conn.prepareStatement(isAllBookedQuery);
            stmt.setLong(1, timeStamp);
            stmt.setLong(2, cooldown);
            stmt.setString(3, plane_no);
            ResultSet result = stmt.executeQuery();
            return !result.next();
        } catch (SQLException ex) {
            Logger.getLogger(Reservation.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(Reservation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
