/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battlecity.cliente;

import javax.swing.JFrame;

/**
 *
 * @author Victor
 */
public class Marco extends JFrame implements InicioPanel.EventListener{
    public static int ALTURA = 500;
    public static int ANCHO = 500;
    public static String TITULO = "Battle City";
    
    public Marco(){
        setSize(ANCHO, ALTURA);
        setTitle(TITULO);
        
        InicioPanel inicio = new InicioPanel(this);
        setContentPane(inicio);
    }

    @Override
    public void jugar(String host, int puerto) {
        System.out.println("Host: "+host+"\nPuerto: "+puerto);
        
        JuegoPanel juego = new JuegoPanel(host,puerto,this);
        setContentPane(juego);
        revalidate();
        requestFocusInWindow();
        juego.startGame();
    }
}
