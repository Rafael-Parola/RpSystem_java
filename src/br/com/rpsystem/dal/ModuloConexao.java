/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.rpsystem.dal;

import br.com.rpsystem.funcoes.Arquivo;
import br.com.rpsystem.funcoes.EnviarEmail;
import br.com.rpsystem.telas.SplashScreen;
import br.com.rpsystem.telas.TelaPrincipal;
import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import static org.mozilla.javascript.Context.exit;

/**
 *
 * @author Rafael Veiga
 */
public class ModuloConexao {

    // Metodo responsável por estabelecer a conexao com o banco
    public static Connection conector() {
        String caminhoConf = System.getProperty("user.dir");
        String nomebanco = "a";
        java.sql.Connection conexao = null;
        String ArqConfig = caminhoConf + "\\config.con";

        String conteudo = Arquivo.Read(ArqConfig);

        String caminho = conteudo.split(";")[0];
        String banco = conteudo.split(";")[1];
        String usuario = conteudo.split(";")[2];
        //System.out.println("Banco em uso " + banco);
//String usuario = "dba_adm" ;
        String senha = "dev_rp_sys";

        // Alinha abixo chama o driver de conecoa que foi importado
        String driver = "com.mysql.cj.jdbc.Driver";
        // Armazenando informações referente ao BD
        String url = "jdbc:mysql://" + caminho + ":3306/" + banco + "?characterEncoding=utf-8";
        //jdbc:mysql://localhost:3306/rpfinancas?characterEncoding=utf-8
        String user = usuario;
        String password = senha;

        // metodo conexao funcionando
        // Estabelencendo a Conexao com o banco; 
//        try {
//            Class.forName(driver);
//            conexao = DriverManager.getConnection(url, user, password);
//            return conexao;
//        } catch (Exception e) {
//            return null;
//            
//        }
///     Fim do metodo conexao funcionando não apagar 


        try {
            Class.forName(driver);
            conexao = DriverManager.getConnection(url, user, password);
            return conexao;
        } catch (ClassNotFoundException e) {
            // Se o driver não puder ser carregado
            System.err.println("Driver JDBC não encontrado: " + e.getMessage());
        } catch (SQLException e) {
            // Se houver um erro de SQL ao tentar se conectar ao banco
           // System.err.println("Erro ao conectar ao banco de dados: " + e.getMessage());
            
            JOptionPane.showMessageDialog(null, " Erro ao conectar ao banco de dados "
                    + "\n Entre em contato com o Suporte para "
                    + "\n verificar o nome do banco de dados no arquivo Conexao.con", "Erro", JOptionPane.ERROR_MESSAGE);
            
            exit();

        } catch (Exception e) {
            // Outras exceções não previstas
            System.err.println("Ocorreu um erro inesperado: " + e.getMessage());
        }

        // Se ocorrer algum erro, retorne null ou lançe uma exceção
        return null;
    }

    
}
