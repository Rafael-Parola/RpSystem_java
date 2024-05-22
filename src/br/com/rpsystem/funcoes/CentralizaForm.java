/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.rpsystem.funcoes;

import br.com.rpsystem.telas.TelaPrincipal;
import static br.com.rpsystem.telas.TelaPrincipal.Desktop;
import java.awt.Dimension;
import javax.swing.JInternalFrame;

/**
 *
 * @author Rafael Veiga
 */
public class CentralizaForm {
   private JInternalFrame  tela; 
    
    public CentralizaForm(JInternalFrame tela){
        this.tela = tela;
    }
    
    
    public void centralizaForm(){
    
    Dimension desktopSize = TelaPrincipal.Desktop.getSize();
        Dimension jInternalFrameSize = tela.getSize();
        tela.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
    }
    
    
}
