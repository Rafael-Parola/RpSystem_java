/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.rpsystem.funcoes;

import br.com.rpsystem.funcoes.Arquivo;
/**
 *
 * @author Rafael Veiga
 */
public class LendoConfiguracao {
     public static void main(String[] args) {
        String arq = "c://config.con";
        String ArqConfig = "config.con";
        String conteudo = Arquivo.Read(ArqConfig);
        String c1 = conteudo.split(";")[0];
        String c2 = conteudo.split(";")[1];
        String c3 = conteudo.split(";")[2];
        
        
    }
    
}


