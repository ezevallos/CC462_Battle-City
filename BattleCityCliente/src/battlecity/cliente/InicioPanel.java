/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battlecity.cliente;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Victor
 */
public class InicioPanel extends JPanel implements ActionListener{
    private JTextField host, puerto;
    private EventListener listener;
    
    public InicioPanel(EventListener listener){
        this.listener = listener;
        
        setLayout(new BorderLayout(24, 24));
        JLabel titulo = new JLabel(Marco.TITULO);
        titulo.setFont(new Font("TimesRoman",Font.BOLD,60));
        titulo.setHorizontalAlignment(JLabel.CENTER);
        add(titulo,BorderLayout.CENTER);
        
        JPanel panelSuperior = new JPanel();
        JLabel hostLbl = new JLabel("Host: ");
        panelSuperior.add(hostLbl);
        
        host = new JTextField(20);
        panelSuperior.add(host);
        
        JLabel puertoLbl = new JLabel("Puerto: ");
        panelSuperior.add(puertoLbl);
        
        puerto = new JTextField(5);
        panelSuperior.add(puerto);
        
        add(panelSuperior,BorderLayout.NORTH);
        
        JButton botonInicio = new JButton("Jugar!!");
        botonInicio.setFont(new Font("TimesRoman",Font.BOLD,30));
        botonInicio.addActionListener(this);
        add(botonInicio,BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int puertoNum;
        String hostAddr = host.getText();
        String puertoText = puerto.getText();
        if("".equals(hostAddr.trim()) || "".equals(puertoText.trim())){
            JOptionPane.showMessageDialog(null, "Ingrese un host y puerto para jugar");
            return;
        }
        try{
            puertoNum = Integer.parseInt(puertoText);
        }catch(NumberFormatException ex){
            JOptionPane.showMessageDialog(null, "Ingrese un número de puerto válido");
            return;
        }
        listener.jugar(hostAddr,puertoNum);
    }
    
    
    public interface EventListener{
        void jugar(String host,int puerto);
    }
}
