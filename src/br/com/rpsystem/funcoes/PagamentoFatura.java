/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.rpsystem.funcoes;

import br.com.rpsystem.dal.ModuloConexao;
import br.com.rpsystem.telas.TelaFechaFatura;
import br.com.rpsystem.telas.TelaPrincipal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import jxl.common.Logger;
import static mondrian.olap.Category.Level;

/**
 *
 * @author Rafael Veiga
 */
public class PagamentoFatura {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public int insere = 0;

    private String dtvencimento;
    private String dtpagamento;
    private int codfatura;
    private double valorpago;
    private String status;
    private String obs;

    public PagamentoFatura(int codfatura, double valorpago, String dtpagamento, String dtvencimento, String obs) {
        this.codfatura = codfatura;
        this.valorpago = valorpago;
        this.dtpagamento = dtpagamento;
        this.dtvencimento = dtvencimento;
        this.status = status;
        this.obs = obs;
    }

    public void pgfatura() {
        conexao = ModuloConexao.conector();
        if (conexao == null) {
            JOptionPane.showMessageDialog(null, "Conexão não estabelecida.");
            return;
        }
        Date dtpgto = null;
        Date dtvenc = null;
        try {
            dtpgto = sdf.parse(dtpagamento);

            dtvenc = sdf.parse(dtvencimento);
        } catch (ParseException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao converter datas.");
            return;
        }
        java.sql.Date dtvencf = new java.sql.Date(dtvenc.getTime());
        java.sql.Date dtpgtof = new java.sql.Date(dtpgto.getTime());

        String sql = "INSERT INTO `pagamentos_faturas`\n"
                + "(`codigo`,`cod_fatura`,`valorpago`,`dtpagamento`,`dtvencimento`,`status_pagamento`,`obs`,`codempresa`,`codusuario`)\n"
                + "SELECT 1 + COALESCE((SELECT MAX(codigo) FROM pagamentos_faturas), 1),?,?,?,?,?,?,?,?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setInt(1, codfatura);
            pst.setDouble(2, valorpago);
            pst.setDate(3, dtpgtof);
            pst.setDate(4, dtvencf);
            pst.setInt(5, 0);
            pst.setString(6, obs);
            pst.setInt(7, Integer.parseInt(TelaPrincipal.lblcodEmpresa.getText()));
            pst.setInt(8, Integer.parseInt(TelaPrincipal.lblCodUsoPrincipal.getText()));

            insere = pst.executeUpdate();
            if (insere > 0) {

//
//                pgtocomp = cboFormaPgto.getSelectedItem().toString();
//        pgtoc = Integer.parseInt(pgtocomp.substring(0, 2).replace(" ", "").replace("-", ""));
                String tipo = "Despesas";
                String descricao = "Pagamento de fatura";
                String dtemissao = TelaPrincipal.lblData.getText();
                String dtvencimento = TelaFechaFatura.txtVencimento.getText();

                int codclassificacao = Integer.parseInt(TelaPrincipal.lblClassificacaoFatura.getText());
                String pessoa = TelaFechaFatura.lblCodFornecedor.getText();
                String obs = "Pagamento fatura";
                String dtpagamento = TelaPrincipal.lblData.getText();

                String pagamento = null;
                
                //TelaFechaFatura.cboForma.getSelectedItem().toString();
                //int cpagamento = Integer.parseInt(pagamento.substring(0, 2).replace(" ", "").replace("-", ""));

                String conta = TelaFechaFatura.cboConta.getSelectedItem().toString();
                int cconta = Integer.parseInt(conta.substring(0, 2).replace(" ", "").replace("-", ""));

                LancamentoFinanceiro lan = new LancamentoFinanceiro(tipo, descricao, dtemissao, dtvencimento,
                        valorpago, pagamento, codclassificacao, pessoa, obs, dtpagamento, cconta);
                lan.insere_lancamento();

                JOptionPane.showMessageDialog(null, "Pagamento de fatura concluído!");

            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao inserir pagamento de fatura.");
        } finally {

        }
    }

}
