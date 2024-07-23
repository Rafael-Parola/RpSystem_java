/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package br.com.rpsystem.telas;

import br.com.rpsystem.dal.ModuloConexao;
import br.com.rpsystem.funcoes.AtualizaSaldoContas;
import br.com.rpsystem.funcoes.CentralizaForm;
import br.com.rpsystem.funcoes.Funcoes;
import br.com.rpsystem.funcoes.GeraLog;
import br.com.rpsystem.funcoes.LancamentoFinanceiro;
import java.awt.event.ActionEvent;

import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author Rafael Veiga
 */
public class TelaLancamentoFinanceiro extends javax.swing.JInternalFrame {

    private TelaPesquisaFinanceira telaPesquisa;

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    Object[] options = {"Sim", "Não"};

    String pgtocomp;
    int pgtoc;

    String classificacaocompleta;
    int codclassificacao;

    String contacompleta;
    String codconta;

    java.sql.Date dtpgto = null;
    Date datapagamento = null;

    int codcartao = 0;
    Date vencimento = null;

    boolean vaiatualizar = false;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sdfSaida = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * Creates new form TelaLancamentoFinanceiro
     */
    public TelaLancamentoFinanceiro() {
        initComponents();
        conexao = ModuloConexao.conector();

        Funcoes cla = new Funcoes(cboClassificacao);
        cla.f_classificacao(cboClassificacao);

        Funcoes conta = new Funcoes(cboConta);
        conta.f_carregaconta(cboConta);

        conta.f_carregaconta(cboContaDestino);

        // Inicialize a tela de pesquisa
        telaPesquisa = new TelaPesquisaFinanceira();

        // Adicione a tela de pesquisa ao desktop pane
        TelaPrincipal.Desktop.add(telaPesquisa);

        CentralizaForm c = new CentralizaForm(telaPesquisa);
        c.centralizaForm();

        // Mapeie a tecla F3
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();

        // Adicione o KeyStroke e a ação correspondente
        inputMap.put(KeyStroke.getKeyStroke("F3"), "abrirPesquisa");
        actionMap.put("abrirPesquisa", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirTelaPesquisa();
            }
        });
    }

    private void abrirTelaPesquisa() {
        if (!telaPesquisa.isVisible()) {
            telaPesquisa.setVisible(true);
        }
        try {
            telaPesquisa.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
            e.printStackTrace();
        }
    }

    public void preenche_fornecedor() {
        String sql = "select  nome from pessoas where id = ? and fornecedor = '1' and empresa_codempresa = " + TelaPrincipal.lblcodEmpresa.getText();
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtCodFornecedor.getText());
            rs = pst.executeQuery();
            if (rs.next()) {
                txtNomeFornecedor.setText(rs.getString(1));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void liberacampos() {
        txtCodFornecedor.setEnabled(true);
        txtDescricao.setEnabled(true);
        txtData.setEnabled(true);
        txtVencimento.setEnabled(true);
        txtdtPgto.setEnabled(true);
        txtValor.setEnabled(true);
        cboClassificacao.setEnabled(true);
        cboTipo.setEnabled(true);
        //  cboFormaPgto.setEnabled(true);
        btnAlterar.setEnabled(false);
        btnIncluir.setEnabled(false);
        btnExcluir.setEnabled(false);
        jTextObs.setEnabled(true);
        chkBoxPagoRecebido.requestFocus();
        cboConta.setEnabled(true);
        chkBoxPagoRecebido.setEnabled(true);
        chkBoxPagoRecebido.setSelected(true);

    }

    private void liberacampos_alteracao() {
        txtCodFornecedor.setEnabled(true);
        txtDescricao.setEnabled(true);
        if (txtdtPgto.getText().equals("  /  /    ")) {
            txtdtPgto.setEnabled(true);
            chkBoxPagoRecebido.setEnabled(true);
        } else {
            txtdtPgto.setEnabled(false);
        }
        cboClassificacao.setEnabled(true);
        btnAlterar.setEnabled(false);
        btnIncluir.setEnabled(false);
        btnExcluir.setEnabled(false);
        jTextObs.setEnabled(true);
        cboConta.requestFocus();

    }

    private void bloqueiacampos() {
        txtCodFornecedor.setEnabled(false);
        txtDescricao.setEnabled(false);
        txtData.setEnabled(false);
        txtVencimento.setEnabled(false);
        txtdtPgto.setEnabled(false);
        txtValor.setEnabled(false);
        cboClassificacao.setEnabled(false);
        cboTipo.setEnabled(false);

        btnAlterar.setEnabled(true);
        btnIncluir.setEnabled(true);
        btnExcluir.setEnabled(true);
        jTextObs.setEnabled(false);
        cboConta.setEnabled(false);
        cboContaDestino.setEnabled(false);
        chkBoxPagoRecebido.setEnabled(false);

    }

    private void limpa_campos() {
        txtCodFin.setText(null);
        txtCodFornecedor.setText(null);
        txtNomeFornecedor.setText("Opicional");
        txtData.setText(null);
        txtDescricao.setText(null);
        txtVencimento.setText(null);
        txtdtPgto.setText(null);
        txtValor.setText("0");

        jTextObs.setText(null);
        lblAviso.setVisible(false);
        //cboTipo.setSelectedItem("Despesas");

        txtData.setEnabled(false);
        txtVencimento.setEnabled(false);
        txtdtPgto.setEnabled(false);
        txtdtPgto.setText(TelaPrincipal.lblData.getText().toString());
        btnIncluir.requestFocus();
    }

    private void insere_lancamento() throws ParseException {
        //captura o codigo do cboforma de pagamento
        pgtocomp = null;
        //= cboFormaPgto.getSelectedItem().toString();
        //  pgtoc = Integer.parseInt(pgtocomp.substring(0, 2).replace(" ", "").replace("-", ""));
        //captura o codigo do cboclassificacao
        classificacaocompleta = cboClassificacao.getSelectedItem().toString();
        codclassificacao = Integer.parseInt(classificacaocompleta.substring(0, 2).replace(" ", "").replace("-", ""));
        // capura o codigo da conta do combo 
        contacompleta = cboConta.getSelectedItem().toString();
        codconta = contacompleta.substring(0, 2).replace(" ", "").replace("-", "");

        String tipo = cboTipo.getSelectedItem().toString();
        String descricao = txtDescricao.getText();
        double vvalor = Double.parseDouble(txtValor.getText().replace(",", "."));

        String pessoa = null;
        if (txtCodFornecedor.getText().equals("")) {
            pessoa = null;
        } else {
            pessoa = txtCodFornecedor.getText();
        }
        //Integer.parseInt(txtCodFornecedor.getText());

        String obs = jTextObs.getText();
        int cconta = Integer.parseInt(codconta);
        String dtemissao = txtData.getText();
        String dtvenci = txtVencimento.getText();
        String dtpg = txtdtPgto.getText();

        // if (txtValor == null || txtValor.getText().equals("0") || txtValor.getText().equals(""))
        if (txtValor.getText().equals("0") || txtValor.getText().equals("")) {
            //JOptionPane.showMessageDialog(null, "Informe o valor!");

            JOptionPane.showMessageDialog(null, "Informe o valor!", "Atenção", JOptionPane.INFORMATION_MESSAGE);

            txtValor.requestFocus();
        } else {

            LancamentoFinanceiro lan = new LancamentoFinanceiro(tipo, descricao, dtemissao, dtvenci, vvalor, pgtocomp, codclassificacao, pessoa, obs, dtpg, cconta);
            lan.insere_lancamento();

            if (lan.adicionado == 1) {
                cboTipo.setSelectedIndex(0);
                bloqueiacampos();
                limpa_campos();
                //btnIncluir.setSelected(true);
                btnIncluir.requestFocus();
                chkBoxPagoRecebido.setSelected(true);
            }

        }
    }

    public void seta_item_alteracao() {

        String sql = "select * from lancamentofinanceiro where cod = ? and empresa_codempresa = " + TelaPrincipal.lblcodEmpresa.getText();

//        int setar = tblPesquisa.getSelectedRow();
//        txtCodFin.setText(tblPesquisa.getModel().getValueAt(setar, 0).toString());
        String cod = txtCodFin.getText();
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, cod);
            rs = pst.executeQuery();
            if (rs.next()) {
                cboTipo.setSelectedItem(rs.getString(2).toString());
                txtDescricao.setText(rs.getString(3));

                java.util.Date dtemi = sdf.parse(rs.getString(4));
                // Formata a data para exibição no JTextField
                String dtemiformatada = sdfSaida.format(dtemi);
                // Define o texto no JTextField com a data formatada
                //    jTextFieldData.setText(dataFormatada);
                txtData.setText(dtemiformatada);

                java.util.Date dtvenc = sdf.parse(rs.getString(5));
                vencimento = rs.getDate(5);
                //System.out.println("Vencimento setado " + vencimento);
                String dtvencformatada = sdfSaida.format(dtvenc);
                txtVencimento.setText(dtvencformatada);

                // Converter o número para uma string
                String numeroString = String.valueOf(rs.getString(6));

                txtValor.setText(rs.getString(6).replace(".", ","));
                //  txtValor.setText(rs.getString(6).replace(".", ","));

                String classificacao = rs.getString(9);
                for (int i = 0; i < cboClassificacao.getItemCount(); i++) {
                    String item = cboClassificacao.getItemAt(i);
                    if (item.startsWith(classificacao)) {
                        cboClassificacao.setSelectedIndex(i);
                        break;
                    }
                }
                txtCodFornecedor.setText(rs.getString(10));

                jTextObs.setText(rs.getString(14));

                java.util.Date data = rs.getDate(15);
                // System.out.println(data);
                codcartao = rs.getInt(17);
                if (data == null) {
                    txtdtPgto.setText("");
                    // System.out.println("Caiu no null");
                    chkBoxPagoRecebido.setSelected(false);
                } else {
                    //  System.out.println("Caiu no else");
                    String dtpgtoformat = sdfSaida.format(data);
                    txtdtPgto.setText(dtpgtoformat);
                    chkBoxPagoRecebido.setSelected(true);

                }
                String conta = rs.getString(18);
                //System.out.println(conta);
                if (conta != null) {
                    for (int i = 0; i < cboConta.getItemCount(); i++) {
                        String item = cboConta.getItemAt(i);
                        if (item.startsWith(conta)) {
                            cboConta.setSelectedIndex(i);
                            break;
                        }
                    }
                }

                lblAviso.setVisible(true);

                // usado para verificar se deve atualizar o saldo após a alteração da conta
                // caso seja true ao gravar a alteração sera chamado o metodo atualiza saldo 
                if (txtdtPgto.getText().equals("  /  /    ")) {
                    vaiatualizar = true;
                    // System.out.println(vaiatualizar);

                } else {
                    vaiatualizar = false;
                    //   System.out.println(vaiatualizar);
                }
                chkBoxPagoRecebido.setEnabled(false);

                String busca = "select nome from pessoas where id = ?";
                try {
                    pst = conexao.prepareStatement(busca);
                    pst.setString(1, txtCodFornecedor.getText());
                    rs = pst.executeQuery();
                    if (rs.next()) {
                        txtNomeFornecedor.setText(rs.getString(1));
                    } else {
                        txtNomeFornecedor.setText("Opicional");
                    }
                } catch (Exception e) {
                    System.out.println("Fornecedor");
                    JOptionPane.showMessageDialog(null, e);

                }

            }
            contarCaracteres();
        } catch (Exception e) {
            System.out.println("Errrrrroooooooooo");
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void alterar() {
        pgtocomp = null;

        //= cboFormaPgto.getSelectedItem().toString();
        //  pgtoc = Integer.parseInt(pgtocomp.substring(0, 2).replace(" ", "").replace("-", ""));
        //captura o codigo do cboclassificacao
        classificacaocompleta = cboClassificacao.getSelectedItem().toString();
        codclassificacao = Integer.parseInt(classificacaocompleta.substring(0, 2).replace(" ", "").replace("-", ""));

        contacompleta = cboConta.getSelectedItem().toString();
        codconta = contacompleta.substring(0, 2).replace(" ", "").replace("-", "");
        String sql = "UPDATE `lancamentofinanceiro`\n"
                + "SET`tipo` = ?,`descricao` = ?,`dtemissao` = ?,`dtvencimento` = ?,`valor` = ?,`formaspagamento_codigo` = ?,\n"
                + "`classificacaofinanceira_cod` = ?,`pessoas_id` = ?,`usuarios_id` = ?,`empresa_codempresa` = ?,excluido = ?,"
                + "observacao = ?,dtpagamento = ?,codConta = ?\n"
                + "WHERE `cod` = ? and empresa_codempresa = ?";
        try {
            // Cria um SimpleDateFormat.
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            // Converte o conteúdo do JTextField para um objeto Date.
            Date dataemissao = sdf.parse(txtData.getText());
            // Converte o objeto Date para um objeto SQL Date.
            java.sql.Date dtemi = new java.sql.Date(dataemissao.getTime());
            Date datavencimento = sdf.parse(txtVencimento.getText());
            // Converte o objeto Date para um objeto SQL Date.
            java.sql.Date dtvenc = new java.sql.Date(datavencimento.getTime());

            pst = conexao.prepareStatement(sql);
            pst.setString(1, cboTipo.getSelectedItem().toString());
            pst.setString(2, txtDescricao.getText());
            pst.setDate(3, dtemi);
            pst.setDate(4, dtvenc);

            pst.setDouble(5, Double.parseDouble(txtValor.getText().replace(",", ".")));
            pst.setString(6, pgtocomp);
            //pgtoc
            pst.setInt(7, codclassificacao);

            String pessoa = null;
            if (txtCodFornecedor.getText().equals("")) {
                pessoa = null;
            } else {
                pessoa = txtCodFornecedor.getText();
            }

            pst.setString(8, pessoa);
            pst.setString(9, TelaPrincipal.lblCodUsoPrincipal.getText());
            pst.setString(10, TelaPrincipal.lblcodEmpresa.getText());
            pst.setString(11, "0");

            pst.setString(12, jTextObs.getText().toString());

            // Converte o objeto Date para um objeto SQL Date.
            if (txtdtPgto.getText().equals("  /  /    ")) {
                datapagamento = null;
                dtpgto = (datapagamento != null) ? new java.sql.Date(datapagamento.getTime()) : null;

                //System.out.println("Aqui");
            } else {
                //System.out.println("Aqui 2");
                datapagamento = sdf.parse(txtdtPgto.getText());
                dtpgto = new java.sql.Date(datapagamento.getTime());

            }
            pst.setDate(13, dtpgto);
            pst.setInt(14, Integer.parseInt(codconta));

            // Parametros do where
            pst.setString(15, txtCodFin.getText());
            pst.setString(16, TelaPrincipal.lblcodEmpresa.getText());

            // checando campos obrigatórios
            if (txtDescricao.getText().isEmpty() || txtData.getText().isEmpty()
                    || txtDescricao.getText().isEmpty() || txtValor.getText().isEmpty()
                    || txtVencimento.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Preencha a descrição.");

            } else {

                int adiciona = pst.executeUpdate();
                if (adiciona > 0) {
                    JOptionPane.showMessageDialog(rootPane, "Alterado com sucesso");

                    if (vaiatualizar == true) {
                        String tipo = cboTipo.getSelectedItem().toString();
                        int conta = Integer.parseInt(codconta);
                        double vl = Double.parseDouble(txtValor.getText().replace(",", "."));
                        String dtpg = txtdtPgto.getText();

                        AtualizaSaldoContas atu = new AtualizaSaldoContas(tipo, dtpg, vl, conta);
                        atu.atualiza_saldo();
                    } else {

                    }

                    GeraLog bkp = new GeraLog("Tela Lançamento financeiro", TelaPrincipal.lblCodUsoPrincipal.getText().concat(" - ").concat(TelaPrincipal.lblUsuario.getText()),
                            "Alterado lançamento ", TelaPrincipal.lblcodEmpresa.getText(), txtCodFin.getText());
                    bkp.gravaBackup();

                    bloqueiacampos();
                    limpa_campos();
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            System.out.println(jTextObs.getText());
            System.out.println(e);

        }

    }

    private void excluir() {

        String sql = "update lancamentofinanceiro set excluido = 1 where cod = ?";
        String exluiFaturacartao = "update movimentocartoes set pago = 0 where datavencimento in (\n"
                + "select dtvencimento from lancamentofinanceiro where dtvencimento = ? and cartao = ?"
                + " and empresa_codempresa = " + TelaPrincipal.lblcodEmpresa.getText() + " );";
        try {
            int exclui = JOptionPane.showConfirmDialog(null, "Tem Certeza que deseja excluir?", "Alerta", JOptionPane.YES_NO_OPTION);
            if (exclui == JOptionPane.YES_OPTION) {

                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtCodFin.getText());
                int excluilogico = pst.executeUpdate();

                if (excluilogico > 0) {

                    if (codcartao > 0) {

                        try {
                            pst = conexao.prepareStatement(exluiFaturacartao);
                            pst.setDate(1, (java.sql.Date) vencimento);
                            pst.setInt(2, codcartao);
                            int estornafatura = pst.executeUpdate();
                            if (estornafatura > 0) {
                                JOptionPane.showMessageDialog(null, "A fatura foi estornada\n Verificar tela de Faturas");
                            }

                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, e);
                        }

                    }

                    JOptionPane.showMessageDialog(null, "Excluído com sucesso");

                    String tipo = cboTipo.getSelectedItem().toString();
                    int conta = Integer.parseInt(codconta);
                    double vl = Double.parseDouble(txtValor.getText().replace(",", "."));
                    String dtpg = txtdtPgto.getText();

                    AtualizaSaldoContas atu = new AtualizaSaldoContas(tipo, dtpg, vl, conta);
                    atu.atualiza_saldo_remocao();

                    GeraLog bkp = new GeraLog("Tela Lançamento financeiro", TelaPrincipal.lblCodUsoPrincipal.getText().concat(" - ").concat(TelaPrincipal.lblUsuario.getText()),
                            "Excluiu lançamento financeiro", TelaPrincipal.lblcodEmpresa.getText(), txtCodFin.getText());

                    bkp.gravaBackup();
                    limpa_campos();
                    bloqueiacampos();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void campos_transferencia() {
        if (cboTipo.getSelectedItem().toString().equals("Transferencia")) {
            txtData.setText(TelaPrincipal.lblData.getText());
            // txtData.setEnabled(false);
            txtVencimento.setText(TelaPrincipal.lblData.getText());
            //txtVencimento.setEnabled(false);
            txtdtPgto.setText(TelaPrincipal.lblData.getText());
            //txtdtPgto.setEnabled(false);
            lblContaDest.setVisible(true);
            cboContaDestino.setVisible(true);
            cboContaDestino.setEnabled(true);

        } else {
            txtData.setText(null);
            //txtData.setEnabled(true);
            txtVencimento.setText(null);
            //xtVencimento.setEnabled(true);
            //txtdtPgto.setText(null);
            //txtdtPgto.setEnabled(true);
            lblContaDest.setVisible(false);
            cboContaDestino.setVisible(false);
            cboContaDestino.setEnabled(false);
            //chkBoxPagoRecebido.setEnabled(true);

        }
    }

    public void efetua_transferencia() {
        pgtocomp = null;
        String tipo_saida = "Despesas";
        String tipo_entrada = "Receita";
        String conta = cboConta.getSelectedItem().toString();
        String conta_destino = cboContaDestino.getSelectedItem().toString();
        String descricao = "Trasnferencia";
        double valor = Double.parseDouble(txtValor.getText().replace(",", "."));
        String obs_saida = "Debito de : " + valor + " com destino a conta: " + conta_destino;
        String obs_entrada = "crédito de : " + valor + " com origem da conta: " + conta;

        String data_emissao = txtData.getText();
        String data_vencimento = txtVencimento.getText();
        String data_pagamento = txtdtPgto.getText();
        String pessoa = null;
        classificacaocompleta = cboClassificacao.getSelectedItem().toString();
        codclassificacao = Integer.parseInt(classificacaocompleta.substring(0, 2).replace(" ", "").replace("-", ""));
        // capura o codigo da conta do combo 

        String contasaidacompleta = null;
        int codcontasaida;
        contasaidacompleta = cboConta.getSelectedItem().toString();
        codcontasaida = Integer.parseInt(contasaidacompleta.substring(0, 2).replace(" ", "").replace("-", ""));

        String contaentracompleta = null;
        int codcontaentrada;
        contaentracompleta = cboContaDestino.getSelectedItem().toString();
        codcontaentrada = Integer.parseInt(contaentracompleta.substring(0, 2).replace(" ", "").replace("-", ""));

        //Aqui dou a saida da conta
        LancamentoFinanceiro lan = new LancamentoFinanceiro(tipo_saida, descricao, data_emissao, data_vencimento, valor,
                pgtocomp, codclassificacao, pessoa, obs_saida, data_pagamento, codcontasaida);
        lan.insere_lancamento();

        if (lan.adicionado == 1) {
            //Aqui dou a entrada na conta
            lan = new LancamentoFinanceiro(tipo_entrada, descricao, data_emissao, data_vencimento, valor,
                    pgtocomp, codclassificacao, pessoa, obs_entrada, data_pagamento, codcontaentrada);
            lan.insere_lancamento();

            if (lan.adicionado == 1) {
                bloqueiacampos();
                limpa_campos();
            }
        }

    }

    private void contarCaracteres() {
        String texto = jTextObs.getText();
        int numeroCaracteres = texto.length();
        if (numeroCaracteres > 3000) {
            jTextObs.setEditable(false); // Desabilita a edição
            JOptionPane.showMessageDialog(null, "Numero máximo de caracteres atingigo");
        } else {
            jTextObs.setEditable(true);  // Habilita a edição
        }
        lblNumeroCaracteres.setText(numeroCaracteres + " /3000");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txtCodFin = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtCodFornecedor = new javax.swing.JTextField();
        txtNomeFornecedor = new javax.swing.JTextField();
        cboTipo = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtDescricao = new javax.swing.JTextField();
        cboClassificacao = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtValor = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        btnIncluir = new javax.swing.JButton();
        btnAlterar = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        tnCancelaaar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        txtData = new javax.swing.JFormattedTextField();
        txtVencimento = new javax.swing.JFormattedTextField();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextObs = new javax.swing.JTextArea();
        jLabel12 = new javax.swing.JLabel();
        txtdtPgto = new javax.swing.JFormattedTextField();
        jLabel13 = new javax.swing.JLabel();
        cboConta = new javax.swing.JComboBox<>();
        lblAviso = new javax.swing.JLabel();
        lblContaDest = new javax.swing.JLabel();
        cboContaDestino = new javax.swing.JComboBox<>();
        chkBoxPagoRecebido = new javax.swing.JCheckBox();
        btnPesquisa = new javax.swing.JButton();
        lblNumeroCaracteres = new javax.swing.JLabel();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        setClosable(true);
        setIconifiable(true);
        setTitle("Lançamentos");
        setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/rpsystem/icones/iconelancamentofinanceiro.png"))); // NOI18N
        setMaximumSize(new java.awt.Dimension(858, 604));
        setMinimumSize(new java.awt.Dimension(858, 604));
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameActivated(evt);
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                formKeyReleased(evt);
            }
        });

        jLabel1.setVisible(false);
        jLabel1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel1.setText("Cod.:");
        jLabel1.setEnabled(false);

        txtCodFin.setVisible(false);
        txtCodFin.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtCodFin.setEnabled(false);

        jLabel2.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel2.setText("Fornecedor(F2:");
        jLabel2.setToolTipText("Pressione F3 para buscar pelo nome");

        txtCodFornecedor.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtCodFornecedor.setEnabled(false);
        txtCodFornecedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodFornecedorActionPerformed(evt);
            }
        });
        txtCodFornecedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCodFornecedorKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCodFornecedorKeyReleased(evt);
            }
        });

        txtNomeFornecedor.setEditable(false);
        txtNomeFornecedor.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtNomeFornecedor.setText("Opicional");
        txtNomeFornecedor.setEnabled(false);
        txtNomeFornecedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNomeFornecedorActionPerformed(evt);
            }
        });

        cboTipo.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        cboTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Despesas", "Receita", "Transferencia" }));
        cboTipo.setEnabled(false);
        cboTipo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cboTipoMouseClicked(evt);
            }
        });
        cboTipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboTipoActionPerformed(evt);
            }
        });
        cboTipo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cboTipoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cboTipoKeyReleased(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel3.setText("Tipo:");

        jLabel4.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel4.setText("Data emissão:");

        jLabel5.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel5.setText("Descrição:");

        txtDescricao.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtDescricao.setEnabled(false);
        txtDescricao.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDescricaoKeyPressed(evt);
            }
        });

        cboClassificacao.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        cboClassificacao.setEnabled(false);
        cboClassificacao.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cboClassificacaoKeyPressed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel6.setText("Classificação:");

        jLabel8.setText("Valor:");

        txtValor.setText("0");
        txtValor.setEnabled(false);
        txtValor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtValorKeyPressed(evt);
            }
        });

        jLabel9.setText("Venc.:");

        btnIncluir.setText("Incluir");
        btnIncluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIncluirActionPerformed(evt);
            }
        });

        btnAlterar.setText("Alterar");
        btnAlterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlterarActionPerformed(evt);
            }
        });

        btnSalvar.setText("Salvar");
        btnSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvarActionPerformed(evt);
            }
        });
        btnSalvar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnSalvarKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                btnSalvarKeyReleased(evt);
            }
        });

        tnCancelaaar.setText("Cancelar");
        tnCancelaaar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tnCancelaaarActionPerformed(evt);
            }
        });

        btnExcluir.setText("Excluir");
        btnExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcluirActionPerformed(evt);
            }
        });

        try {
            txtData.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtData.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtData.setEnabled(false);
        txtData.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDataKeyPressed(evt);
            }
        });

        try {
            txtVencimento.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtVencimento.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtVencimento.setEnabled(false);
        txtVencimento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtVencimentoActionPerformed(evt);
            }
        });
        txtVencimento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtVencimentoKeyPressed(evt);
            }
        });

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Observação:");

        jTextObs.setColumns(20);
        jTextObs.setRows(5);
        jTextObs.setEnabled(false);
        jTextObs.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextObsKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextObsKeyReleased(evt);
            }
        });
        jScrollPane3.setViewportView(jTextObs);

        jLabel12.setText("Pgto.:");

        try {
            txtdtPgto.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtdtPgto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtdtPgto.setEnabled(false);
        txtdtPgto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtdtPgtoActionPerformed(evt);
            }
        });
        txtdtPgto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtdtPgtoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtdtPgtoKeyReleased(evt);
            }
        });

        jLabel13.setText("Selecione a conta: ");

        cboConta.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));
        cboConta.setEnabled(false);
        cboConta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cboContaKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cboContaKeyReleased(evt);
            }
        });

        lblAviso.setVisible(false);
        lblAviso.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        lblAviso.setForeground(new java.awt.Color(255, 0, 0));
        lblAviso.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAviso.setText("*Não é possivel alterar todos os campos");

        lblContaDest.setVisible(false);
        lblContaDest.setText("Conta Desti.");

        cboContaDestino.setVisible(false);
        cboContaDestino.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));
        cboContaDestino.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboContaDestinoActionPerformed(evt);
            }
        });
        cboContaDestino.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cboContaDestinoKeyPressed(evt);
            }
        });

        chkBoxPagoRecebido.setSelected(true);
        chkBoxPagoRecebido.setText("Pago");
        chkBoxPagoRecebido.setEnabled(false);
        chkBoxPagoRecebido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkBoxPagoRecebidoActionPerformed(evt);
            }
        });
        chkBoxPagoRecebido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                chkBoxPagoRecebidoKeyPressed(evt);
            }
        });

        btnPesquisa.setText("Pesquisa(F3)");
        btnPesquisa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPesquisaActionPerformed(evt);
            }
        });

        lblNumeroCaracteres.setForeground(new java.awt.Color(51, 51, 51));
        lblNumeroCaracteres.setText("0/3000");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(txtCodFin, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                        .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING))
                                    .addComponent(lblNumeroCaracteres))
                                .addGap(34, 34, 34)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtCodFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtNomeFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, 611, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(txtData, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtVencimento, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtdtPgto, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblContaDest)
                                .addGap(18, 18, 18)
                                .addComponent(cboContaDestino, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(cboTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cboConta, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel6)
                                .addGap(18, 18, 18)
                                .addComponent(cboClassificacao, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(txtDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, 388, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel8)
                                .addGap(18, 18, 18)
                                .addComponent(txtValor))
                            .addComponent(lblAviso, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane3)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(676, 676, 676)))
                .addGap(0, 52, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(chkBoxPagoRecebido, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(539, 539, 539))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btnIncluir)
                        .addGap(6, 6, 6)
                        .addComponent(btnAlterar)
                        .addGap(6, 6, 6)
                        .addComponent(btnSalvar)
                        .addGap(6, 6, 6)
                        .addComponent(tnCancelaaar)
                        .addGap(6, 6, 6)
                        .addComponent(btnExcluir)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPesquisa)
                        .addGap(167, 167, 167))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkBoxPagoRecebido)
                    .addComponent(lblAviso))
                .addGap(60, 60, 60)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(cboTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel13)
                    .addComponent(cboClassificacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboConta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(txtData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtVencimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel12)
                    .addComponent(txtdtPgto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblContaDest)
                    .addComponent(jLabel9)
                    .addComponent(cboContaDestino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel5)
                    .addComponent(txtDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(txtValor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtCodFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNomeFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnIncluir)
                            .addComponent(btnAlterar)
                            .addComponent(btnSalvar)
                            .addComponent(tnCancelaaar)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btnExcluir)
                                .addComponent(btnPesquisa)))
                        .addGap(52, 52, 52))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblNumeroCaracteres)
                        .addGap(44, 44, 44)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCodFin, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        setBounds(0, 0, 858, 604);
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameActivated(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameActivated
        // TODO add your handling code here:
        seta_item_alteracao();

    }//GEN-LAST:event_formInternalFrameActivated

    private void txtCodFornecedorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodFornecedorKeyPressed
        // TODO add your handling code here:
        String codfornecedor = txtCodFornecedor.getText();
        if (codfornecedor.equals("")) {
            //System.out.println(codfornecedor);
            if (evt.getKeyCode() == KeyEvent.VK_F2) {
                TelaPesquisa pesquisa = new TelaPesquisa();
                TelaPrincipal.Desktop.add(pesquisa);
                pesquisa.setVisible(true);
                CentralizaForm c = new CentralizaForm(pesquisa);
                c.centralizaForm();
                pesquisa.toFront();
                pesquisa.setTitle("Lançamento Financeiro");
                pesquisa.lblOrigem.setText("CLASSFIN");
                //

            } 
        }
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jTextObs.requestFocus();
            preenche_fornecedor();
          
        }

    }//GEN-LAST:event_txtCodFornecedorKeyPressed

    private void btnIncluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIncluirActionPerformed
        // TODO add your handling code here:
        limpa_campos();
        liberacampos();
    }//GEN-LAST:event_btnIncluirActionPerformed

    private void tnCancelaaarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tnCancelaaarActionPerformed
        // TODO add your handling code here:
        bloqueiacampos();
        limpa_campos();
    }//GEN-LAST:event_tnCancelaaarActionPerformed

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
        // TODO add your handling code here:
        String vazio = txtCodFin.getText();

        if (vazio.equals("")) {

            String tipo = cboTipo.getSelectedItem().toString();

            if (tipo.equals("Transferencia")) {
                efetua_transferencia();
            } else {
                try {
                    insere_lancamento();
                } catch (ParseException ex) {
                    Logger.getLogger(TelaLancamentoFinanceiro.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        } else {
            alterar();
        }
    }//GEN-LAST:event_btnSalvarActionPerformed

    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarActionPerformed
        // TODO add your handling code here:
        String vazio = txtCodFin.getText();
        if (vazio.equals("")) {
            JOptionPane.showMessageDialog(null, "Selecione um lançamento para alteração!");
        } else {
            liberacampos_alteracao();

        }
    }//GEN-LAST:event_btnAlterarActionPerformed

    private void cboTipoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cboTipoKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_cboTipoKeyReleased

    private void cboTipoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cboTipoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cboConta.requestFocus();

        }
        campos_transferencia();
    }//GEN-LAST:event_cboTipoKeyPressed

    private void txtDataKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDataKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (txtData.getText().equals("  /  /    ")) {
                txtData.setText(TelaPrincipal.lblData.getText());
                txtVencimento.requestFocus();
                //  txtVencimento.setText(txtData.getText());
            } else {
                txtVencimento.requestFocus();
                //txtVencimento.setText(txtData.getText());
            }

        }
    }//GEN-LAST:event_txtDataKeyPressed

    private void txtDescricaoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDescricaoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER || evt.getKeyCode() == KeyEvent.VK_TAB) {
            if (txtValor.getText().equals("0")) {
                txtValor.setText("");
            }

            txtValor.requestFocus();

        }
    }//GEN-LAST:event_txtDescricaoKeyPressed

    private void cboClassificacaoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cboClassificacaoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtData.requestFocus();

        }

    }//GEN-LAST:event_cboClassificacaoKeyPressed

    private void txtValorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtValorKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtCodFornecedor.requestFocus();
        }
    }//GEN-LAST:event_txtValorKeyPressed

    private void txtVencimentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtVencimentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtVencimentoActionPerformed

    private void txtVencimentoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtVencimentoKeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (txtVencimento.getText().equals("  /  /    ")) {
                txtVencimento.setText(txtData.getText());
                txtdtPgto.requestFocus();
            } else {
                txtdtPgto.requestFocus();
            }
        }

    }//GEN-LAST:event_txtVencimentoKeyPressed

    private void txtCodFornecedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodFornecedorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodFornecedorActionPerformed

    private void txtCodFornecedorKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodFornecedorKeyReleased
        // TODO add your handling code here:

    }//GEN-LAST:event_txtCodFornecedorKeyReleased

    private void btnSalvarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnSalvarKeyPressed
        // TODO add your handling code here:
        String vazio = txtCodFin.getText();

        if (vazio.equals("")) {
            String tipo = cboTipo.getSelectedItem().toString();
            if (tipo.equals("Transferencia")) {
                efetua_transferencia();
            } else {
                try {
                    insere_lancamento();
                } catch (ParseException ex) {
                    Logger.getLogger(TelaLancamentoFinanceiro.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            alterar();
        }

    }//GEN-LAST:event_btnSalvarKeyPressed

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
        // TODO add your handling code here:
        String vazio = txtCodFin.getText();
        if (vazio.equals("")) {
            JOptionPane.showMessageDialog(null, "Selecione um lançamento");
        } else {
            excluir();
        }
    }//GEN-LAST:event_btnExcluirActionPerformed

    private void jTextObsKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextObsKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_TAB) {
            btnSalvar.requestFocus();
        }
       
    }//GEN-LAST:event_jTextObsKeyPressed

    private void txtdtPgtoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtdtPgtoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtdtPgtoActionPerformed

    private void txtdtPgtoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtdtPgtoKeyPressed
        // TODO add your handling code here:
        String tipo = cboTipo.getSelectedItem().toString();
        if (tipo.equals("Transferencia")) {
            if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                cboContaDestino.requestFocus();
            }
        } else {
            if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                txtDescricao.requestFocus();
            }
        }
    }//GEN-LAST:event_txtdtPgtoKeyPressed

    private void cboContaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cboContaKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_cboContaKeyReleased

    private void cboContaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cboContaKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cboClassificacao.requestFocus();
        }

    }//GEN-LAST:event_cboContaKeyPressed

    private void txtNomeFornecedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeFornecedorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNomeFornecedorActionPerformed

    private void cboTipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboTipoActionPerformed
        // TODO add your handling code here:
        campos_transferencia();
        String tipo2 = cboTipo.getSelectedItem().toString();
        //System.out.println(tipo);
        if (tipo2.equals("Despesas")) {
            chkBoxPagoRecebido.setText("Pago");
            chkBoxPagoRecebido.setEnabled(true);
        }
        if (tipo2.equals("Receita")) {
            chkBoxPagoRecebido.setText("Recebido");
            chkBoxPagoRecebido.setEnabled(true);
        }
        if (tipo2.equals("Transferencia")) {
            chkBoxPagoRecebido.setText("Transferencia");

        }
    }//GEN-LAST:event_cboTipoActionPerformed

    private void cboContaDestinoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboContaDestinoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboContaDestinoActionPerformed

    private void cboTipoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cboTipoMouseClicked
        // TODO add your handling code here:
        campos_transferencia();
        String tipo3 = cboTipo.getSelectedItem().toString();
        // System.out.println(tipo3);
        if (tipo3.equals("Despesas")) {
            chkBoxPagoRecebido.setText("Pago");
            chkBoxPagoRecebido.setEnabled(true);
        }
        if (tipo3.equals("Receita")) {
            chkBoxPagoRecebido.setText("Recebido");
            chkBoxPagoRecebido.setEnabled(true);
        }
        if (tipo3.equals("Transferencia")) {
            chkBoxPagoRecebido.setText("Transferencia");
            //chkBoxPagoRecebido.setEnabled(false);
        }
    }//GEN-LAST:event_cboTipoMouseClicked

    private void txtdtPgtoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtdtPgtoKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtdtPgtoKeyReleased

    private void cboContaDestinoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cboContaDestinoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtDescricao.requestFocus();
        }
    }//GEN-LAST:event_cboContaDestinoKeyPressed

    private void chkBoxPagoRecebidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkBoxPagoRecebidoActionPerformed
        // TODO add your handling code here:

        if (chkBoxPagoRecebido.isSelected()) {
            txtdtPgto.setText(TelaPrincipal.lblData.getText().toString());
        } else {
            txtdtPgto.setText(null);
        }
    }//GEN-LAST:event_chkBoxPagoRecebidoActionPerformed

    private void chkBoxPagoRecebidoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_chkBoxPagoRecebidoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cboTipo.requestFocus();
        }
    }//GEN-LAST:event_chkBoxPagoRecebidoKeyPressed

    private void btnPesquisaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPesquisaActionPerformed
        // TODO add your handling code here:
        TelaPesquisaFinanceira fin = new TelaPesquisaFinanceira();
        TelaPrincipal.Desktop.add(fin);
        fin.setVisible(true);
        CentralizaForm c = new CentralizaForm(fin);
        c.centralizaForm();
        fin.toFront();

    }//GEN-LAST:event_btnPesquisaActionPerformed

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_F3) {
            TelaPesquisaFinanceira fin = new TelaPesquisaFinanceira();
            TelaPrincipal.Desktop.add(fin);
            fin.setVisible(true);
            CentralizaForm c = new CentralizaForm(fin);
            c.centralizaForm();
            fin.toFront();

        }
    }//GEN-LAST:event_formKeyPressed

    private void formKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_F3) {
            TelaPesquisaFinanceira fin = new TelaPesquisaFinanceira();
            TelaPrincipal.Desktop.add(fin);
            fin.setVisible(true);
            CentralizaForm c = new CentralizaForm(fin);
            c.centralizaForm();
            fin.toFront();

        }
    }//GEN-LAST:event_formKeyReleased

    private void jTextObsKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextObsKeyReleased
        // TODO add your handling code here:
         contarCaracteres();
    }//GEN-LAST:event_jTextObsKeyReleased

    private void btnSalvarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnSalvarKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSalvarKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAlterar;
    private javax.swing.JButton btnExcluir;
    public static javax.swing.JButton btnIncluir;
    private javax.swing.JButton btnPesquisa;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JComboBox<String> cboClassificacao;
    private javax.swing.JComboBox<String> cboConta;
    private javax.swing.JComboBox<String> cboContaDestino;
    private javax.swing.JComboBox<String> cboTipo;
    private javax.swing.JCheckBox chkBoxPagoRecebido;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea jTextObs;
    private javax.swing.JLabel lblAviso;
    private javax.swing.JLabel lblContaDest;
    private javax.swing.JLabel lblNumeroCaracteres;
    private javax.swing.JButton tnCancelaaar;
    public static javax.swing.JTextField txtCodFin;
    public static javax.swing.JTextField txtCodFornecedor;
    private javax.swing.JFormattedTextField txtData;
    public static javax.swing.JTextField txtDescricao;
    public static javax.swing.JTextField txtNomeFornecedor;
    private javax.swing.JTextField txtValor;
    private javax.swing.JFormattedTextField txtVencimento;
    private javax.swing.JFormattedTextField txtdtPgto;
    // End of variables declaration//GEN-END:variables
}
