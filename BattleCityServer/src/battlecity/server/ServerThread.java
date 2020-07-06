package battlecity.server;




import java.io.IOException;
import java.net.ServerSocket;
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
public class ServerThread extends Thread{
    
    //verifica el lugar de una colision
    //se inicia en 0,0 porque nunca se podra realizar una colision ahi
    int colision_X=0;
    int colision_Y=0;
    private List<ServerJugador> jugadores;
    private static int[][] spawningArea = {{1,1},{1,30},{14,1},{14,30}};
    
    private CargarMapa mapaInicial = new CargarMapa();
    char[][] mapaCaracter = mapaInicial.getMatrizMapa();
    
    
    String transformarCadena(char[][] array){
        String cadena="";
        
        for (int i=0;i<16;i++){
            for(int j=0;j<32;j++){
                cadena=cadena+array[i][j];
            }
            cadena=cadena+"\n";
        }
        
        return cadena;
    }
    
    
    public ServerThread(){
        jugadores = new ArrayList<>();
    }

    @Override
    public void run() {
        try {
            ServerSocket server = new ServerSocket(5555);
            
            while(true){
                Socket socket = server.accept();
                System.out.println("Nueva conexion!");
                ServerJugador jugador = new ServerJugador(socket);
                jugadores.add(jugador);
                jugador.start();
            }
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
    }
    
    public void enviarUpdates(){
        Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                int total;
                while(true){
                    total = jugadores.size();
                    List<ServerJugador> gamer_eliminado=new ArrayList<>();
                    
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {}
                    total = jugadores.size();
                    System.out.println("total: "+total);
                    System.out.println("colision X:"+colision_X+" Y:"+colision_Y);
                    for(int i = 0; i < total; i++){
                        try {
                            CoreJuego(jugadores.get(i));
                            //System.out.println("hilo:"+i +" X:"+" Y:"+colision_Y);
                            if((colision_X==jugadores.get(i).getPostX()) &&
                                    (colision_Y==jugadores.get(i).getPostY())){
                                System.out.println("murio");
                                gamer_eliminado.add(jugadores.get(i));
                                colision_X=0;
                                colision_Y=0;        
                            }
                            jugadores.get(i).enviar(transformarCadena(mapaCaracter));
                            
                        } catch (IOException ex) {
                            //ex.printStackTrace();
                        }
                    }
                    
                    for(ServerJugador eliminado : gamer_eliminado)
                        eliminado.notificaPerdio();
                    
                    jugadores.removeAll(gamer_eliminado);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {}
                }
            }
        });
        hilo.start();
    }
    
    public static int[] getSpawnPos(){
        int i = (int) (Math.random() * 4);
        return spawningArea[i];
    }
    
    public void CoreJuego(ServerJugador gamer){
        char direccion = gamer.movimiento;
        int total_balas = gamer.balas.size();
        
        System.out.println("Balas: "+total_balas);
        if(direccion=='w'){
            //System.out.println("Entro pe");
            if(gamer.orientacion=='↑'){
             if(mapaCaracter[gamer.getPostX()-1][gamer.getPostY()]==' '){
                 mapaCaracter[gamer.getPostX()][gamer.getPostY()]=' ';
                 mapaCaracter[gamer.getPostX()-1][gamer.getPostY()]='↑';
                 gamer.setPostX(gamer.getPostX()-1);
                 gamer.movimiento =' ';
             }   
            }
            else{
               mapaCaracter[gamer.getPostX()][gamer.getPostY()]='↑';
               gamer.orientacion='↑';
            } 
        }
        
        else if(direccion=='s'){
            if(gamer.orientacion=='↓'){
                if(mapaCaracter[gamer.getPostX()+1][gamer.getPostY()]==' '){
                    mapaCaracter[gamer.getPostX()][gamer.getPostY()]=' ';
                    mapaCaracter[gamer.getPostX()+1][gamer.getPostY()]='↓';
                    gamer.setPostX(gamer.getPostX()+1);
                    gamer.movimiento =' ';
                }
            }
            
            else{
                mapaCaracter[gamer.getPostX()][gamer.getPostY()]='↓';
                gamer.orientacion='↓';
            }
        }
        
        else if(direccion=='a'){
            
            if(gamer.orientacion=='←'){
                if(mapaCaracter[gamer.getPostX()][gamer.getPostY()-1]==' '){
                    mapaCaracter[gamer.getPostX()][gamer.getPostY()]=' ';
                    mapaCaracter[gamer.getPostX()][gamer.getPostY()-1]='←';
                    gamer.setPostY(gamer.getPostY()-1);
                    gamer.movimiento =' ';
                }
            }
            
            else{
                mapaCaracter[gamer.getPostX()][gamer.getPostY()]='←';
                gamer.orientacion='←';
            }
            
        }
        
        else if(direccion=='d'){
            if(gamer.orientacion=='→'){
                if(mapaCaracter[gamer.getPostX()][gamer.getPostY()+1]==' '){
                    mapaCaracter[gamer.getPostX()][gamer.getPostY()]=' ';
                    mapaCaracter[gamer.getPostX()][gamer.getPostY()+1]='→';
                    gamer.setPostY(gamer.getPostY()+1);
                    gamer.movimiento =' ';
                }
                
            }
            
            else{
                mapaCaracter[gamer.getPostX()][gamer.getPostY()]='→';
                gamer.orientacion='→';
            }
                        
        }
        
        else if(gamer.disparo==true){
            
        }
        
        //leo todas las balas
        else if(total_balas>0){
            
            List <bala> a_remover= new  ArrayList<bala>(); //nos sirve para eliminar balas 
            for(int i=0;i<total_balas;i++){
                char direct=gamer.balas.get(i).direccion;
                int b_postX=gamer.balas.get(i).getBala_postX();
                int b_postY=gamer.balas.get(i).getBala_postY();
                
                if(mapaCaracter[b_postX][b_postY]==' '){ //Comprueba si la bala fue destruida anteriormente
                    gamer.balas.get(i).setEstado_bala(false);
                    a_remover.add(gamer.balas.get(i));  //La elimina
                    continue;
                }
                
                //La bala es lanzada hacia arriba
                if(direct=='↑'){
                    switch (mapaCaracter[b_postX-1][b_postY]) {
                        case ' ':
                            //if( b_postX!=gamer.postX && b_postY!=gamer.postY)
                            mapaCaracter[b_postX][b_postY]=' ';
                            mapaCaracter[b_postX-1][b_postY]='°';
                            gamer.balas.get(i).setBala_postX(b_postX-1);
                            break;
                        case '*':
                            mapaCaracter[b_postX][b_postY]=' ';
                            mapaCaracter[b_postX-1][b_postY]=' ';
                            gamer.balas.get(i).setEstado_bala(false);
                            a_remover.add(gamer.balas.get(i));
                            break;
                        case '#':
                            mapaCaracter[b_postX][b_postY]=' ';
                            //mapaCaracter[b_postX-1][b_postY]=' ';
                            gamer.balas.get(i).setEstado_bala(false);
                            a_remover.add(gamer.balas.get(i));
                            break;
                        case '↑':
                        case '↓':
                        case '→':
                        case '←':
                            mapaCaracter[b_postX][b_postY]=' ';
                            mapaCaracter[b_postX-1][b_postY]=' ';
                            gamer.balas.get(i).setEstado_bala(false);
                            a_remover.add(gamer.balas.get(i));
                            this.colision_X=b_postX-1;
                            this.colision_Y=b_postY;
                            break;
                        case '°':   //Cuando choca con otra bala
                            mapaCaracter[b_postX][b_postY]=' ';
                            mapaCaracter[b_postX-1][b_postY]=' ';
                            gamer.balas.get(i).setEstado_bala(false);
                            a_remover.add(gamer.balas.get(i));
                            break;
                        default:
                            break;
                    }
                    
                }
                
                //La bala es lanzada hacia abajo
                if(direct=='↓'){
                    switch (mapaCaracter[b_postX+1][b_postY]) {
                        case ' ':
                            mapaCaracter[b_postX][b_postY]=' ';
                            mapaCaracter[b_postX+1][b_postY]='°';
                            gamer.balas.get(i).setBala_postX(b_postX+1);
                            break;
                        case '*':
                            mapaCaracter[b_postX][b_postY]=' ';
                            mapaCaracter[b_postX+1][b_postY]=' ';
                            gamer.balas.get(i).setEstado_bala(false);
                            a_remover.add(gamer.balas.get(i));
                            break;
                        case '#':
                            mapaCaracter[b_postX][b_postY]=' ';
                            gamer.balas.get(i).setEstado_bala(false);
                            a_remover.add(gamer.balas.get(i));
                            break;
                        case '↑':
                        case '↓':
                        case '→':
                        case '←':
                            mapaCaracter[b_postX][b_postY]=' ';
                            mapaCaracter[b_postX+1][b_postY]=' ';
                            gamer.balas.get(i).setEstado_bala(false);
                            a_remover.add(gamer.balas.get(i));
                            this.colision_X=b_postX+1;
                            this.colision_Y=b_postY;
                            break;
                        case '°':   //Cuando choca con otra bala
                            mapaCaracter[b_postX][b_postY]=' ';
                            mapaCaracter[b_postX+1][b_postY]=' ';
                            gamer.balas.get(i).setEstado_bala(false);
                            a_remover.add(gamer.balas.get(i));
                            break;
                        default:
                            break;
                    }
                    
                }
                
                //La bala es lanzada hacia la izquierda
                if(direct=='←'){
                    switch (mapaCaracter[b_postX][b_postY-1]) {
                        case ' ':
                            mapaCaracter[b_postX][b_postY]=' ';
                            mapaCaracter[b_postX][b_postY-1]='°';
                            gamer.balas.get(i).setBala_postY(b_postY-1);
                            break;
                        case '*':
                            mapaCaracter[b_postX][b_postY]=' ';
                            mapaCaracter[b_postX][b_postY-1]=' ';
                            gamer.balas.get(i).setEstado_bala(false);
                            a_remover.add(gamer.balas.get(i));
                            break;
                        case '#':
                            mapaCaracter[b_postX][b_postY]=' ';
                            gamer.balas.get(i).setEstado_bala(false);
                            a_remover.add(gamer.balas.get(i));
                            break;
                        case '↑':
                        case '↓':
                        case '→':
                        case '←':
                            mapaCaracter[b_postX][b_postY]=' ';
                            mapaCaracter[b_postX][b_postY-1]=' ';
                            gamer.balas.get(i).setEstado_bala(false);
                            a_remover.add(gamer.balas.get(i));
                            this.colision_X=b_postX;
                            this.colision_Y=b_postY-1;
                            break;
                        case '°':   //Cuando choca con otra bala
                            mapaCaracter[b_postX][b_postY]=' ';
                            mapaCaracter[b_postX][b_postY-1]=' ';
                            gamer.balas.get(i).setEstado_bala(false);
                            a_remover.add(gamer.balas.get(i));
                            break;
                        default:
                            break;
                    }
                    
                }
                
                
                //lanzado bala hacia la derecha
                if(direct=='→'){
                    switch (mapaCaracter[b_postX][b_postY+1]) {
                        case ' ':
                            mapaCaracter[b_postX][b_postY]=' ';
                            mapaCaracter[b_postX][b_postY+1]='°';
                            gamer.balas.get(i).setBala_postY(b_postY+1);
                            break;
                        case '*':
                            mapaCaracter[b_postX][b_postY]=' ';
                            mapaCaracter[b_postX][b_postY+1]=' ';
                            gamer.balas.get(i).setEstado_bala(false);
                            a_remover.add(gamer.balas.get(i));
                            break;
                        case '#':
                            mapaCaracter[b_postX][b_postY]=' ';
                            gamer.balas.get(i).setEstado_bala(false);
                            a_remover.add(gamer.balas.get(i));
                            break;
                        case '↑':
                        case '↓':
                        case '→':
                        case '←':
                            mapaCaracter[b_postX][b_postY]=' ';
                            mapaCaracter[b_postX][b_postY+1]=' ';
                            gamer.balas.get(i).setEstado_bala(false);
                            a_remover.add(gamer.balas.get(i));
                            this.colision_X=b_postX;
                            this.colision_Y=b_postY+1;
                            break;
                        case '°':   //Cuando choca con otra bala
                            mapaCaracter[b_postX][b_postY]=' ';
                            mapaCaracter[b_postX][b_postY+1]=' ';
                            gamer.balas.get(i).setEstado_bala(false);
                            a_remover.add(gamer.balas.get(i));
                            break;
                        default:
                            break;
                    }
                    
                }
                
                
                //para que no se me desaparesca el tanque cuando lanzo la bala
                mapaCaracter[gamer.getPostX()][gamer.getPostY()]=gamer.orientacion;
            }
            
            gamer.balas.removeAll(a_remover);//eliminando balas
            
        }
        else {
            System.out.println("Espera movimiento");
        }
    }
    
}
