package battlecity.server;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Victor
 */
public class ServerJugador extends Thread{
    Socket socket;
    DataInputStream in;
    DataOutputStream out;
    
    //Codigo agregado para obtener el estado de los
    boolean running = true;
    boolean state_jugador;
    boolean disparo=false;
    List<bala> balas;
    int postX=1;
    int postY=1;
    char movimiento;
    char orientacion='→';
    
    public void setMovimiento(char movimiento) {
        this.movimiento = movimiento;
    }
    
    
    public void setState_jugador(boolean state_jugador) {
        this.state_jugador = state_jugador;
    }

    public void setPostX(int postX) {
        this.postX = postX;
    }

    public void setPostY(int postY) {
        this.postY = postY;
    }

    public void setOrientacion(char orientacion){
        this.orientacion=orientacion;
    }
    
    
    
    public boolean isState_jugador() {
        return state_jugador;
    }

    public int getPostX() {
        return postX;
    }

    public int getPostY() {
        return postY;
    }

    public char getMovimiento() {
        return movimiento;
    }
    
    public char getOrientacion(){
        return orientacion;
    }

    public void notificaPerdio(){
        try{
            out.writeUTF("pierdes");    //Notifica al jugador
        }catch(IOException ex){}
        running = false;    //cierra el loop principal, por ende desconecta
    }
    
    public void setSpawn(){
        int spawnPos[] = ServerThread.getSpawnPos();
        postX = spawnPos[0];
        postY = spawnPos[1];
    }
    
    public ServerJugador(Socket socket){
        this.socket = socket;
        this.balas = new ArrayList<>();
        setSpawn();
    }
    
    @Override
    public void run(){
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            
            while(running){
                char key = in.readChar();
                System.out.println(key);
                this.setMovimiento(key);
                if(key=='x') {
                    bala disparo = new bala();
                    disparo.direccion=this.orientacion;
                    disparo.setBala_postX(this.postX);
                    disparo.setBala_postY(this.postY);
                    disparo.setEstado_bala(true);
                    balas.add(disparo);
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public void enviar(String msg) throws IOException{
        out.writeUTF(msg);
    }
}


class bala{
    
    int bala_postX;
    int bala_postY;
    char direccion;
    boolean estado_bala;
   

    public void setBala_postX(int bala_postX) {
        this.bala_postX = bala_postX;
    }

    public void setBala_postY(int bala_postY) {
        this.bala_postY = bala_postY;
    }

    public void setEstado_bala(boolean estado_bala) {
        this.estado_bala = estado_bala;
    }

    public int getBala_postX() {
        return bala_postX;
    }

    public int getBala_postY() {
        return bala_postY;
    }

    public boolean isEstado_bala() {
        return estado_bala;
    }

    public char getDireccion() {
        return direccion;
    }    

    
}