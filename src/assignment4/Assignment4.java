/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment4;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author raul
 */
public class Assignment4 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // TODO code application logic here
            Reservation res = new Reservation("db_020", "db2016");
//            res.bookAll("CR9");
            res.clearAllBookings("CR9");
//            String a = res.reserve("CR9", 1);
//            System.out.println(a);
            
        } catch (SQLException ex) {
            Logger.getLogger(Assignment4.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
