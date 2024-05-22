/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.rpsystem.funcoes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;
import br.com.rpsystem.dal.ModuloConexao;

/**
 *
 * @author Rafael Veiga
 */
public class GeraLog {

    // ResultSet rs = null;
    private final String tela;
    private String usuario;
    private String acao;
    private String empresa;
    private String registro;

        public GeraLog(String tela, String usuario, String acao, String empresa,String registro) {
        this.tela = tela;
        this.usuario = usuario;
        this.acao = acao;
        this.empresa = empresa;
        this.registro = registro;

    }

    public void gravaBackup() {
        Connection conexao = null;
        PreparedStatement pst = null;
        conexao = ModuloConexao.conector();
        
        String sql = "insert into log(id,telaAlterada,obs,user,empresa_codempresa,registro)"
                + "select 1 + coalesce ((select max(id)from log),0),'" + tela + "','" + acao + "','" + usuario + "'," + empresa + ","+registro+";";
        try {
            pst = conexao.prepareStatement(sql);
            pst.execute();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            System.out.println(e);
        }

    }

}
