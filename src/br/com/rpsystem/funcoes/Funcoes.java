/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.rpsystem.funcoes;

import br.com.rpsystem.dal.ModuloConexao;
import br.com.rpsystem.telas.TelaPrincipal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

/**
 *
 * @author Rafael Veiga
 */
public class Funcoes {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    private JComboBox<String> comboBox;

    public Funcoes(JComboBox comboBox) {
        this.comboBox = comboBox;
    }

    public void f_carregaconta(JComboBox<String> comboBox) {
        String conta = "select codigo,nome from cadastrocontas "
                + "where status =0 and codempresa = ? ";
        conexao = ModuloConexao.conector();
        try {
            pst = conexao.prepareStatement(conta);
            pst.setString(1, TelaPrincipal.lblcodEmpresa.getText());
            rs = pst.executeQuery();
            if (rs.next()) {
                do {
                    comboBox.addItem(rs.getString("codigo").concat(" - ").concat(rs.getString("nome")));
                } while (rs.next());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void f_classificacao(JComboBox<String> comboBox) {

        var classificacao = "select cod,descricao from classificacaofinanceira where excluido = 0 and empresa_codempresa = ? ";
        conexao = ModuloConexao.conector();
        try {
            pst = conexao.prepareStatement(classificacao);
            pst.setString(1, TelaPrincipal.lblcodEmpresa.getText());
            rs = pst.executeQuery();
            if (rs.next()) {
                do {
                    comboBox.addItem(rs.getString("cod").concat(" - ").concat(rs.getString("descricao")));
                } while (rs.next());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    public void f_formapgto(JComboBox<String> comboBox) {

        var formapgto = "select codigo,descricao from formaspagamento where status =0";
        conexao = ModuloConexao.conector();
        try {
            pst = conexao.prepareStatement(formapgto);
            rs = pst.executeQuery();
            if (rs.next()) {
                do {
                    comboBox.addItem(rs.getString("codigo").concat(" - ").concat(rs.getString("descricao")));
                } while (rs.next());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    public void f_situacaoOs(JComboBox<String> comboBox) {
        String status = "select descricao from statusos where excluido= 0";
        conexao = ModuloConexao.conector();
        try {
            pst = conexao.prepareStatement(status);
            rs = pst.executeQuery();
            if (rs.next()) {
                do {
                    comboBox.addItem(rs.getString("descricao"));
                } while (rs.next());

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }
    
    public void f_cartao(JComboBox<String> comboBox) {

        var forma = "select codigo,nomecartao from cadcartoes where excluido =0";
        conexao = ModuloConexao.conector();
        try {
            pst = conexao.prepareStatement(forma);
            rs = pst.executeQuery();
            if (rs.next()) {
                do {
                    comboBox.addItem(rs.getString("codigo").concat(" - ").concat(rs.getString("nomecartao")));
                } while (rs.next());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }
    
    public void f_preenchetecino(JComboBox<String> comboBox) {
        String vendedor = "select nomeVendedor from vendedores where demitido= 'Não'";
        conexao = ModuloConexao.conector();
        try {
            pst = conexao.prepareStatement(vendedor);
            rs = pst.executeQuery();
            if (rs.next()) {
                do {
                    comboBox.addItem(rs.getString("nomeVendedor"));
                } while (rs.next());

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

}
