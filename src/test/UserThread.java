/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import assignment4.Reservation;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author teo
 */
public class UserThread implements Runnable {

    private Reservation reservation;
    private String plane_no;
    private long customer_id;
    private Random rn = new Random();
    int result;
    private MasterThread master;
    
    public UserThread(Reservation reservation, String plane_no, long customer_id) {
        this.reservation = reservation;
        this.plane_no = plane_no;
        this.customer_id = customer_id;
    }
    
    @Override
    public void run() {
        makeBooking();
    }
    
    private void makeBooking() {
        try {
            String seat = reservation.reserve(plane_no, customer_id);
            long sleepTime = (long) rn.nextInt(10);
            this.wait(sleepTime);
            result = reservation.book(plane_no, seat, customer_id);
            master.notify(result, (int) customer_id);
        } catch (InterruptedException ex) {
            Logger.getLogger(UserThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
