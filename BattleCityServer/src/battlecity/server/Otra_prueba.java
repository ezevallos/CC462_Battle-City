/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package battlecity.server;

import javax.swing.JFrame;

/**
 *
 * @author AsistenteBI
 */
public class Otra_prueba {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        ServerThread server = new ServerThread();
        server.start();
        server.enviarUpdates();
    }
  
    
}
