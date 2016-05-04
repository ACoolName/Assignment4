/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment4;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * @author raul
 */
public class Reservation implements ReservationInterface {

    private static Reservation instance;
    private static Connection conn = null;
    private static PreparedStatement stmt;
    private String URL = "datdb.cphbusiness.dk";
    private String user;
    private String pw;

    public Reservation(String user, String pw) throws SQLException {
        this.user = user;
        this.pw = pw;
        getConnection();
    }

    private Connection getConnection() throws SQLException {
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
    
    public void closeConnection() throws SQLException{
        conn.close();
    }

    @Override
    public int reserve(String plane_no, long id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
