/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battlecity.cliente;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
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
    private Canvas canvas;
    private int width,height;
    private BufferStrategy bs;
    private Graphics g;
    //private JTextArea areaJuego;    //Aqui imprimira la matriz
    private JFrame parent;
    private static int W = 30;
    private static int H = 30;
    private BufferedImage wall,bricks,tankUp,tankDown,tankLeft,tankRight;
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
    
    public JuegoPanel(String addr,int port, int width, int height,JFrame parent){
        this.addr = addr;
        this.port = port;
        this.width = width;
        this.height = height;
        canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(width, height));
        canvas.setMaximumSize(new Dimension(width, height));
        canvas.setMinimumSize(new Dimension(width, height));
        super.add(canvas);
        
        parent.setTitle("Jugando en: "+addr+":"+port);
        
        canvas.addKeyListener(keyListener);
        
        try{
            //wall = ImageIO.read(new File("E:\\concurrentes\\CC462_Battle-City\\assets\\wall.png"));
            wall = ImageIO.read(new File("assets/wall.png"));
            //bricks = ImageIO.read(new File("E:\\concurrentes\\CC462_Battle-City\\assets\\brick.png"));
            bricks = ImageIO.read(new File("assets/brick.png"));
            //tankUp = ImageIO.read(new File("E:\\concurrentes\\CC462_Battle-City\\assets\\tank_up.png"));
            tankUp = ImageIO.read(new File("assets/tank_up.png"));
            //tankDown = ImageIO.read(new File("E:\\concurrentes\\CC462_Battle-City\\assets\\tank_down.png"));
            tankDown = ImageIO.read(new File("assets/tank_down.png"));
            //tankLeft = ImageIO.read(new File("E:\\concurrentes\\CC462_Battle-City\\assets\\tank_left.png"));
            tankLeft = ImageIO.read(new File("assets/tank_left.png"));
            //tankRight = ImageIO.read(new File("E:\\concurrentes\\CC462_Battle-City\\assets\\tank_right.png"));
            tankRight = ImageIO.read(new File("assets/tank_right.png"));
        }catch(IOException e){
            e.printStackTrace();
            System.out.println("Image could not be read");
            System.exit(1);
        };
        //repintar(prueba);
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
    public synchronized void repintar(String juego) {
        bs = canvas.getBufferStrategy();
        if(bs==null){
            canvas.createBufferStrategy(3);
            return;
        }
        g = bs.getDrawGraphics();
        
        int posX = 0,posY = 0;
        
        //Clear
        g.clearRect(0, 0, width, height);
        

        //Draw here
        
        for(char c : juego.toCharArray()){
            switch(c){
                case '#':
                    drawWall(posX, posY);
                    break;
                case '*':
                    drawDestruc(posX, posY);
                    break;
                case '↑':
                    drawPlayerUp(posX, posY);
                    break;
                case '↓':
                    drawPlayerDown(posX, posY);
                    break;
                case '→':
                    drawPlayerRight(posX, posY);
                    break;
                case '←':
                    drawPlayerLeft(posX, posY);
                    break;
                case '°':
                    drawBullet(posX, posY);
                    break;
                case '\n':
                    posX = -1;
                    posY++;
                default:
                    break;
            }
            posX++;
        }
        
        //End Drawing
        bs.show();
        g.dispose();
    }
    
    private void drawWall(int posX,int posY){
        //g.setColor(Color.RED);
        //g.fillRect(posX*W, posY*H, W, H);
        g.drawImage(wall, posX*W,posY*H, W, H, null);
    }
    
    private void drawDestruc(int posX,int posY){
        //g.setColor(Color.YELLOW);
        //g.fillRect(posX*W, posY*H, W, H);
        g.drawImage(bricks, posX*W,posY*H, W, H, null);
    }
    
    private void drawBullet(int posX,int posY){
        g.setColor(Color.BLACK);
        g.fillOval(posX*W+W/4, posY*H+H/4, W/4, H/4);
    }
    
    private void drawPlayerUp(int posX,int posY){
        //g.setColor(Color.BLUE);
        //int x = posX*W, y=posY*H;
        //int xPoints[] = {x+W/2,  x+W, x};
        //int yPoints[] = {y, y+H, y+H};
        //g.fillPolygon(xPoints,yPoints,3);
        g.drawImage(tankUp, posX*W,posY*H, W, H, null);
    }

    private void drawPlayerDown(int posX,int posY){
        //g.setColor(Color.BLUE);
        //int x = posX*W, y=posY*H;
        //int xPoints[] = {x,  x+W, x+W/2};
        //int yPoints[] = {y, y, y+H};
        //g.fillPolygon(xPoints,yPoints,3);
        g.drawImage(tankDown, posX*W,posY*H, W, H, null);
    }
    
    private void drawPlayerRight(int posX,int posY){
        //g.setColor(Color.BLUE);
        //int x = posX*W, y=posY*H;
        //int xPoints[] = {x,  x+W, x};
        //int yPoints[] = {y, y+H/2, y+H};
        //g.fillPolygon(xPoints,yPoints,3);
        g.drawImage(tankRight, posX*W,posY*H, W, H, null);
    }
    
    private void drawPlayerLeft(int posX,int posY){
        //g.setColor(Color.BLUE);
        //int x = posX*W, y=posY*H;
        //int xPoints[] = {x,  x+W, x+W};
        //int yPoints[] = {y+H/2, y, y+H};
        //g.fillPolygon(xPoints,yPoints,3);
        g.drawImage(tankLeft, posX*W,posY*H, W, H, null);
    }
    
    @Override
    public void notificaPerdio() {
        JOptionPane.showMessageDialog(this, "Eliminado!");
        System.exit(0);
    }
    
    
}
