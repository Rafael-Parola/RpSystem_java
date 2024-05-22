/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.rpsystem.funcoes;

import br.com.rpsystem.dal.ModuloConexao;
import br.com.rpsystem.telas.TelaLancamentoFinanceiro;
import static br.com.rpsystem.telas.TelaLancamentoFinanceiro.txtCodFornecedor;
import static br.com.rpsystem.telas.TelaLancamentoFinanceiro.txtDescricao;
import br.com.rpsystem.telas.TelaPrincipal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author Rafael Veiga
 */
public class LancamentoFinanceiro {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public int adicionado;

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    private String tipo;
    private String descricao;
    private String dtemissao;
    private String dtvencimento;

    private double valor;

    private String formaspagamento;
    private int classificacaofinanceira;

    private String pessoas;
    private int excluido;
    private String observacao;

    private String dtpagamento;

    private int codConta;
  
    

    public LancamentoFinanceiro(
            String tipo,
            String descricao,
            String dtemissao,
            String dtvencimento,
            double valor,
            String formaspagamento,
            int classificacaofinanceira,
            String pessoas,
            String observacao,
            String dtpagamento,
            int codConta
          
    ) {

        this.tipo = tipo;
        this.descricao = descricao;
        this.dtemissao = dtemissao;
        this.dtvencimento = dtvencimento;
        this.valor = valor;
        this.formaspagamento = formaspagamento;
        this.classificacaofinanceira = classificacaofinanceira;
        this.pessoas = pessoas;
        this.observacao = observacao;
        this.dtpagamento = dtpagamento;
        this.codConta = codConta;
       
        
    }

    public void insere_lancamento() {
        conexao = ModuloConexao.conector();
        int verpagamento = 0;

        if (conexao == null) {
            JOptionPane.showMessageDialog(null, "Conexão não estabelecida.");
            return;
        }
        Date dtemi = null;
        Date dtpgto = null;
        Date dtvenc = null;
        java.sql.Date dtpgtof = null;
        try {
            dtemi = sdf.parse(dtemissao);

            if (dtpagamento.equals("  /  /    ")) {
                verpagamento = 1;
            } else {
                dtpgto = sdf.parse(dtpagamento);
            }

            dtvenc = sdf.parse(dtvencimento);
        } catch (ParseException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao converter datas.");
            return;
        }
        java.sql.Date dtemif = new java.sql.Date(dtemi.getTime());
        java.sql.Date dtvencf = new java.sql.Date(dtvenc.getTime());

        if (verpagamento == 1) {
            dtpgtof = null;
        } else {
            dtpgtof = new java.sql.Date(dtpgto.getTime());
        }

        //**********************************************************************
        String sql = "INSERT INTO `lancamentofinanceiro`\n"
                + "(`cod`,`tipo`,`descricao`,`dtemissao`,`dtvencimento`,`valor`,`formaspagamento_codigo`,\n"
                + "`classificacaofinanceira_cod`,`pessoas_id`,`usuarios_id`,`empresa_codempresa`,excluido,observacao,dtpagamento,codConta)\n"
                + "select 1+coalesce ((select max(cod) from lancamentofinanceiro),1),?,?,?,?,?,?,?,?,?,?,?,?,?,?";

        try {
            pst = conexao.prepareStatement(sql);

            pst.setString(1, tipo);
            pst.setString(2, descricao);
            pst.setDate(3, dtemif);
            pst.setDate(4, dtvencf);
            pst.setDouble(5, valor);
            pst.setString(6, formaspagamento);
            pst.setInt(7, classificacaofinanceira);
            pst.setString(8, pessoas);
            pst.setString(9, TelaPrincipal.lblCodUsoPrincipal.getText());
            pst.setString(10, TelaPrincipal.lblcodEmpresa.getText());
            pst.setString(11, "0");
            pst.setString(12, observacao);
            pst.setDate(13, dtpgtof);
            pst.setInt(14, codConta);

            int adiciona = pst.executeUpdate();
            if (adiciona > 0) {
                JOptionPane.showMessageDialog(null, "Inserido com sucesso");
                TelaLancamentoFinanceiro.btnIncluir.requestFocus();
                AtualizaSaldoContas atu = new AtualizaSaldoContas(tipo, dtpagamento, valor, codConta);
                atu.atualiza_saldo();
                
                
            }

        } catch (Exception e) {
            //System.out.println(e);
            JOptionPane.showMessageDialog(null, e);
        } finally {

            adicionado = 1;
            //  System.out.println(adicionado);
        }
    }

}
