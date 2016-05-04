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
        return conn;
    }

    public static PreparedStatement prepare(String SQLString) {
        try {
            stmt = conn.prepareStatement(SQLString);
        } catch (SQLException e) {
            System.out.println("Error in DB.prepare()" + e);
        }
        return stmt;
    }

    public void closeConnection() throws SQLException {
        conn.close();
    }

    @Override
    public String reserve(String plane_no, long id) {
        int timeStamp = (int) (System.currentTimeMillis() / 1000L);
        int rowsInserted = 0;
        String availableSeat = null;
        System.out.println(timeStamp);
        String availableQuery = "SELECT seat_no FROM seat WHERE booked is null and rownum=1";
        String reserveQuery = "UPDATE seat SET reserved= ? , booking_time= ? WHERE plane_no = ?";
        System.out.println("test " + availableQuery);
        try {
            stmt = prepare(availableQuery);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                availableSeat = rs.getString(1);
//                reserveQuery += " and seat_no='" + availableSeat + "'";
                System.out.println("Here ");
//                System.out.println(availableSeat);
//                System.out.println(reserveQuery);
            }
            if (availableSeat != null) {
                stmt = prepare(reserveQuery);
                stmt.setLong(1, id);
                stmt.setInt(2, timeStamp);
                stmt.setString(3, plane_no);
                rowsInserted = stmt.executeUpdate();
                if (rowsInserted > 0) {
                    return availableSeat;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Reservation.class.getName()).log(Level.SEVERE, null, ex);
        }
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return null;
    }

    @Override
    public int book(String plane_no, String seat_no, long id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isAllReserved(String plane_no) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
