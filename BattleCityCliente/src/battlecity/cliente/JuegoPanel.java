/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battlecity.cliente;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 *
 * @author Victor
 */
public class JuegoPanel extends JPanel implements SocketThread.EventListener{
    private String addr;
    private int port;
    private JTextArea areaJuego;    //Aqui imprimira la matriz
    private JFrame parent;
    private SocketThread socketThread;
    private static String prueba = 
            "################################\n" +
            "#      ***                     #\n" +
            "###*####  ##**##               #\n" +
            "#                              #\n" +
            "#                              #\n" +
            "#                              #\n" +
            "#                              #\n" +
            "#                              #\n" +
            "#                              #\n" +
            "#                              #\n" +
            "#                              #\n" +
            "#                              #\n" +
            "#                              #\n" +
            "#                              #\n" +
            "#                              #\n" +
            "################################";
    
    public JuegoPanel(String addr,int port,JFrame parent){
        this.addr = addr;
        this.port = port;
        super.setLayout(new BorderLayout());
        areaJuego = new JTextArea(16, 32);
        areaJuego.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 20));
        areaJuego.setEditable(false);
        areaJuego.setFocusable(false);
        super.add(areaJuego,BorderLayout.CENTER);
        
        JLabel lbl = new JLabel("Jugando en: "+addr+":"+port);
        super.add(lbl,BorderLayout.NORTH);
        
        parent.addKeyListener(keyListener);
        
        //areaJuego.setText(prueba);
        
    }
    
    public void startGame(){
        //Conectamos al server y repinta
        try {
            socketThread = new SocketThread(addr, port, this);
            socketThread.start();
        } catch (IOException ex) {
            notificarError( "Error en conexión");
            System.exit(0);
        }
    }
    
    //Captura las ordenes por teclado
    private final KeyListener keyListener = new KeyListener() {
        @Override
        public void keyTyped(KeyEvent e) {}
        @Override
        public void keyPressed(KeyEvent e) {}
        @Override
        public void keyReleased(KeyEvent e) {
            //System.out.println("Se presiono algo");
            mandarOrden(e.getKeyCode());
        }
    };
    
    private void mandarOrden(int key){
        if(socketThread!=null){
            switch(key){
                case KeyEvent.VK_W:   //Arriba
                case KeyEvent.VK_UP:
                    System.out.println("Arriba");
                    socketThread.enviarOrden('w');
                    break;
                case KeyEvent.VK_S:   //Abajo
                case KeyEvent.VK_DOWN:
                    System.out.println("Abajo");
                    socketThread.enviarOrden('s');
                    break;
                case KeyEvent.VK_A:   //Izquierda
                case KeyEvent.VK_LEFT:
                    System.out.println("Izquierda");
                    socketThread.enviarOrden('a');
                    break;
                case KeyEvent.VK_D:   //Derecha
                case KeyEvent.VK_RIGHT:
                    System.out.println("Derecha");
                    socketThread.enviarOrden('d');
                    break;
                case KeyEvent.VK_SPACE:   //Dispara
                    System.out.println("Dispara");
                    socketThread.enviarOrden('x');
                    break;
                default:
                    break;
            }
        }
    }
    
    private void notificarError(String msg){
        JOptionPane.showMessageDialog(null,msg);
    }

    @Override
    public void repintar(String juego) {
        areaJuego.setText(juego);
    }

    @Override
    public void notificaPerdio() {
        JOptionPane.showMessageDialog(this, "Eliminado!");
        System.exit(0);
    }
    
    
}
