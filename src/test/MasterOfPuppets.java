/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author teo
 */
public class MasterOfPuppets {

    public static void main(String[] args) {
        try {
            MasterThread master = new MasterThread();
            master.mainMethod();
        } catch (SQLException ex) {
            Logger.getLogger(MasterOfPuppets.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
