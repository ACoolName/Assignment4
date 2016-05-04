/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment4;

/**
 *
 * @author raul
 */
public interface ReservationInterface {
    
    // plane_no -> the plane number
    // id -> id of the customer
    int reserve(String plane_no, long id);
    
    // plane_no -> plane number
    // seat_no -> seat number
    // id -> customer id
    int book(String plane_no, String seat_no, long id);
    
    // Books all seats in a plane
    void bookAll(String plane_no);
    
    // Resets all bookings and reservations for a plane_no
    void clearAllBookings(String plane_no);
    
    // Returns true if all seats are booked, false otherwise
    boolean isAllBooked(String plane_no);
    
    // Returns true if all seats are reserved, false otherwise
    boolean isAllReserved(String plane_no);
}
