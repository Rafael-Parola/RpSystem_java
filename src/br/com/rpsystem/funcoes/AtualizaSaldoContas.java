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
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author Rafael Veiga
 */
public class AtualizaSaldoContas {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    String tipo;
    String dtpgto;
    Double valor;
    int codConta;

    public AtualizaSaldoContas(String tipo, String dtpgto, Double valor, int codConta) {

        this.tipo = tipo;
        this.dtpgto = dtpgto;
        this.valor = valor;
        this.codConta = codConta;
    }

    public void atualiza_saldo() {
        conexao = ModuloConexao.conector();
        if (dtpgto.equals("  /  /    ")) {

        } else {

            String sql = "UPDATE cadastrocontas SET saldo = saldo "
                    + (tipo.equalsIgnoreCase("Despesas") ? "-" : "+") + valor + " WHERE codigo = " + codConta;
            try {
                pst = conexao.prepareStatement(sql);
                int atualiza = pst.executeUpdate();
                if (atualiza > 0) {
                    CarregaSaldoContas carrega = new CarregaSaldoContas(TelaPrincipal.tblSaldo);
                    carrega.exibesaldo();
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    public void atualiza_saldo_remocao() {
        conexao = ModuloConexao.conector();
        if (dtpgto.equals("  /  /    ")) {
            JOptionPane.showMessageDialog(null, "Não fez nada");
        } else {
            String sql = "UPDATE cadastrocontas SET saldo = saldo "
                    + (tipo.equalsIgnoreCase("Despesas") ? "+" : "-") + valor + " WHERE codigo = " + codConta;
            try {
                pst = conexao.prepareStatement(sql);
                int atualiza = pst.executeUpdate();
                if (atualiza > 0) {
                    CarregaSaldoContas carrega = new CarregaSaldoContas(TelaPrincipal.tblSaldo);
                    carrega.exibesaldo();
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);

            }
        }
    }

}
