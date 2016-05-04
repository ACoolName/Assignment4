/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import assignment4.Reservation;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author teo
 */
public class MasterThread {

    private static Reservation reservation;
    private static String plane_no = "CR9";
    private static HashMap<Integer, Integer> results;

    public static void main(String[] args) {
        try {
            reservation = new Reservation("db_020", "db2016");
        } catch (SQLException ex) {
            Logger.getLogger(MasterThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        int i = 0;
        while (!reservation.isAllBooked(plane_no)) {
            UserThread t = new UserThread(reservation, plane_no, i);
            i++;
            t.run();
        }
        while (i > results.size()) {

        }
        Iterator it = results.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            System.out.println("Thread " + pair.getKey() + " returned " + pair.getValue());
            it.remove();
        }

    }

    public void notify(int result, int id) {
        results.put(id, result);
    }
}
