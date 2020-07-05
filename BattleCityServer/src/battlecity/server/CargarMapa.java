/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package battlecity.server;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author AsistenteBI
 */
public class CargarMapa {
    
    char[][] MatrizMapa = new char [16][32];

    public char[][] getMatrizMapa() {
        return MatrizMapa;
    }
    
    /*
    public void evaluarJuego(int j_posX,int j_posY){
        
    } */
    
    
    CargarMapa(){
        //File file = new File("E:\\concurrentes\\mapa.txt");
        File file = new File("mapa.txt");
        try {
            Scanner sc = new Scanner(file);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String st; // lectura cadena por cadena del file
            int index=0;
            
            try {
                while ((st = br.readLine()) != null){
                    for (int j=0;j<st.length();j++){
                        MatrizMapa[index][j]=st.charAt(j);
                    }
                    index++;
                }
            } catch (IOException ex) {
                
                System.out.println("Problema leyendo la cadena: "+index+" del texto");
                Logger.getLogger(CargarMapa.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } catch (FileNotFoundException ex) {
            System.out.println("No se encuentra archivo a cargar");
            Logger.getLogger(CargarMapa.class.getName()).log(Level.SEVERE, null, ex);
        }
        

    }
    
    
}
