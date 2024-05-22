/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package br.com.rpsystem.telas;

import java.sql.*;
import br.com.rpsystem.dal.ModuloConexao;
import br.com.rpsystem.funcoes.AtualizaSaldoContas;
import br.com.rpsystem.funcoes.CentralizaForm;
import br.com.rpsystem.funcoes.EnviarEmail;
import br.com.rpsystem.funcoes.Funcoes;
import br.com.rpsystem.funcoes.GeraLog;
import static br.com.rpsystem.telas.TelaPrincipal.Desktop;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Rafael Veiga
 */
public class TelaOsProdeServ extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    Object[] options = {"Sim", "Não"};

    String pgtocomp;
    String pgtoc;

    // armazenando o texto de acor do com radio butom
    public String tipo;

    /**
     * Creates new form TelaOs
     */
    public TelaOsProdeServ() {
        initComponents();
        btnIncluir.requestFocus();
        conexao = ModuloConexao.conector();
        if (TelaPrincipal.lblInsereFinAuto.getText().equals("true")) {
            lblAviso.setVisible(true);
           // lblAviso.setToolTipText("Caso seja selecionado o Status: ".concat(TelaPrincipal.lblSituacaoosfinaliza.getText().concat(" \nSerá inserido o lançamento financeiro automaticamente.")));
        }

    }

    private void consultaAvancada() {
        String sql = "select id as Codigo, nome as Nome, "
                + "telefone as Telefone from pessoas where nome like ? and empresa_codempresa = ? and cliente = 1";
        try {
            pst = conexao.prepareStatement(sql);
            // Passando o conteudo do campo pesquisar para o SQL
            pst.setString(1, "%" + txtPesquisaCliOS.getText() + "%");
            pst.setString(2, TelaPrincipal.lblcodEmpresa.getText());
            rs = pst.executeQuery();
            // A linha abixo usa a rs2xml.jar 
            tblListarCli.setModel(DbUtils.resultSetToTableModel(rs));
            tblListarCli.getColumnModel().getColumn(0).setPreferredWidth(1);
            tblListarCli.getColumnModel().getColumn(1).setPreferredWidth(100);
            tblListarCli.getColumnModel().getColumn(2).setPreferredWidth(20);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void consultaprod() {
        conexao = ModuloConexao.conector();
        String sql = "select codproduto as Cod, descricao as Descrição, "
                + "valorvenda as Valor from produtos where status <> 'Inativo' and descricao like ? and empresa_codempresa = ?";
        try {
            pst = conexao.prepareStatement(sql);
            // Passando o conteudo do campo pesquisar para o SQL
            pst.setString(1, "%" + txtPesquisaProd.getText() + "%");
            pst.setString(2, TelaPrincipal.lblcodEmpresa.getText());
            rs = pst.executeQuery();
            // A linha abixo usa a rs2xml.jar 
            tblPesquisaProd.setModel(DbUtils.resultSetToTableModel(rs));
            tblPesquisaProd.getColumnModel().getColumn(0).setPreferredWidth(1);  // Coluna "Valor" com largura 80 pixels
            tblPesquisaProd.getColumnModel().getColumn(1).setPreferredWidth(200);
            tblPesquisaProd.getColumnModel().getColumn(2).setPreferredWidth(2);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    public void setaprod() {

        int setar = tblPesquisaProd.getSelectedRow();
        txtCodProd.setText(tblPesquisaProd.getModel().getValueAt(setar, 0).toString());
        txtDescProd.setText(tblPesquisaProd.getModel().getValueAt(setar, 1).toString());
        txtValorUniProd.setText(tblPesquisaProd.getModel().getValueAt(setar, 2).toString().replace(".", ","));
    }

    public void seleciona_produto_removacao() {

        int setar = tblGridProd.getSelectedRow();
        txtSeqItem.setText(tblGridProd.getModel().getValueAt(setar, 0).toString());
        txtCodProd.setText(tblGridProd.getModel().getValueAt(setar, 1).toString());
        txtDescProd.setText(tblGridProd.getModel().getValueAt(setar, 2).toString());
        txtValorUniProd.setText(tblGridProd.getModel().getValueAt(setar, 3).toString().replace(".", ","));
        txtQtdeProd.setText(tblGridProd.getModel().getValueAt(setar, 4).toString().replace(".", ","));
        txtvlrFinal.setText(tblGridProd.getModel().getValueAt(setar, 5).toString().replace(".", ","));

        txtCodProd.setEnabled(false);
        txtDescProd.setEnabled(false);
        txtValorUniProd.setEnabled(false);
        txtQtdeProd.setEnabled(false);
        txtDesconto.setEnabled(false);
        txtvlrFinal.setEnabled(false);

    }

    public void setar_codCliente() {

        int setar = tblListarCli.getSelectedRow();
        txtCodCliOs.setText(tblListarCli.getModel().getValueAt(setar, 0).toString());
    }

    private void limparcampos() {
        txtNumOS.setText(null);
        txtDataOS.setText(null);
        txtServico.setText(null);
        txtCodCliOs.setText(null);
        txtPesquisaProd.setText(null);
        txtDescProd.setText(null);
        txtValorUniProd.setText(null);
        txtQtdeProd.setText("1");
        txtCodProd.setText(null);
        txtPesquisaCliOS.setText(null);
        txtSeqItem.setText("1");
        txtValorOrdemServico.setText("0");
        txtDesconto.setText("0");
        txtvlrFinal.setText("0");
        txtValorBruto.setText("0");
        txtValorDescontoOS.setText("0");
        ((DefaultTableModel) tblListarCli.getModel()).setRowCount(0);
        ((DefaultTableModel) tblGridProd.getModel()).setRowCount(0);
        ((DefaultTableModel) tblPesquisaProd.getModel()).setRowCount(0);

    }

    private void bloqueiacampos() {
        rbtOrdemDeServico.setEnabled(false);
        rbtOrcamento.setEnabled(false);
        cboSitacao.setEnabled(false);

        txtServico.setEnabled(false);
        //   txtTecnico.setEnabled(false);
        cboVendedor.setEnabled(false);
        txtValorOrdemServico.setEnabled(false);
        txtCodCliOs.setEnabled(false);
        txtPesquisaCliOS.setEnabled(false);
        //txtPesquisaCliOS.setText("Pesquisa cliente");
        txtPesquisaProd.setEnabled(false);
        txtDescProd.setEnabled(false);
        txtValorUniProd.setEnabled(false);
        txtQtdeProd.setEnabled(false);
        txtvlrFinal.setEnabled(false);
        btnIncluir.setEnabled(true);
        tblListarCli.setEnabled(false);
        txtCodProd.setEnabled(false);
        tblGridProd.setEnabled(false);
        btnAltera.setEnabled(true);
        btnPesquisaOs.setEnabled(true);
        btnExcluiOs.setEnabled(true);
        btnImprimeOs.setEnabled(true);
        btnFechar.setEnabled(true);
        txtDesconto.setEnabled(false);
        txtValorBruto.setEnabled(false);
        txtValorDescontoOS.setEnabled(false);
        cboFormaPgto.setEnabled(false);
        rbtVenda.setEnabled(false);

    }

    private void liberacampos() {
        rbtVenda.setEnabled(true);
        rbtOrdemDeServico.setEnabled(true);
        rbtOrcamento.setEnabled(true);
        txtServico.setEnabled(true);
        cboVendedor.setEnabled(true);
        cboSitacao.setEnabled(true);
        txtValorOrdemServico.setEnabled(true);
        txtCodCliOs.setEnabled(true);
        txtPesquisaCliOS.setEnabled(true);
        txtPesquisaProd.setEnabled(true);
        txtDescProd.setEnabled(true);
        txtValorUniProd.setEnabled(true);
        txtQtdeProd.setEnabled(true);
        txtvlrFinal.setEnabled(true);
        btnIncluir.setEnabled(false);
        tblListarCli.setEnabled(true);
        txtCodProd.setEnabled(true);
        tblGridProd.setEnabled(true);
        btnAltera.setEnabled(false);
        btnPesquisaOs.setEnabled(false);
        btnExcluiOs.setEnabled(false);
        btnImprimeOs.setEnabled(false);
        btnFechar.setEnabled(false);
        txtPesquisaCliOS.requestFocus();
        txtDesconto.setEnabled(true);
        txtValorBruto.setEnabled(true);
        txtValorDescontoOS.setEnabled(true);
        cboFormaPgto.setEnabled(true);
    }

    public void iniciaos() {

        String disp = TelaPrincipal.lblDispositivo.getText();
        String sql = " INSERT INTO ordemservico (os,tipo,situacao,dispositivo,selecionado,usuarios_id,empresa_codempresa,tecnico,formaspagamento_codigo)\n"
                + " SELECT 1 + coalesce((SELECT max(os) FROM ordemservico), 0), ?,?,?,?,?,?,?,?";

        /*"INSERT INTO `ordemservico`\n"
                        + "(os,tipo,situacao,dispositivo,selecionado,usuarios_id,empresa_codempresa)VALUES\n"
                        + "(?,?,?,?,?,?,?);";*/
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, tipo);
            pst.setString(2, cboSitacao.getSelectedItem().toString());
            pst.setString(3, disp);
            pst.setString(4, "X");
            pst.setString(5, TelaPrincipal.lblCodUsoPrincipal.getText());
            pst.setString(6, TelaPrincipal.lblcodEmpresa.getText());
            pst.setString(7, cboVendedor.getSelectedItem().toString());
            pst.setInt(8, 1);

            int adicionado = pst.executeUpdate();

            if (adicionado > 0) {
                String numOs = "select os from ordemservico where selecionado = 'X' and dispositivo = " + "'" + disp + "'";
                try {
                    pst = conexao.prepareStatement(numOs);
                    rs = pst.executeQuery();
                    if (rs.next()) {

                        String numeroos = rs.getString(1);
                        txtNumOS.setText(numeroos);
                        //System.out.println(numeroos);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
                }

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    private void gravaitens_banco() {
        String disp = TelaPrincipal.lblDispositivo.getText();
        String numOs = "select os from ordemservico where selecionado = 'X' and dispositivo = " + "'" + disp + "'";
        try {
            pst = conexao.prepareStatement(numOs);
            rs = pst.executeQuery();
            if (rs.next()) {

                String numeroos = rs.getString(1);
                txtNumOS.setText(numeroos);

                //  System.out.println(numeroos);
                //    int codigoproduto = Integer.parseInt(v.getText());
                String itens = "INSERT INTO `itensorcamento`\n"
                        + "(`seqItem`,`ordemservico_os`,`produtos_codproduto`,`usuarios_id`,`empresa_codempresa`,`qtde`,`vlunitario`,`vltotal`,vlrdesconto,vlrfinal)\n"
                        + "VALUES(?,?,?,?,?,?,?,?,?,?) ;";
                try {
                    String usuario = TelaPrincipal.lblCodUsoPrincipal.getText();
                    String emp = TelaPrincipal.lblcodEmpresa.getText();
                    String qtd = txtQtdeProd.getText();
                    Double vluni = Double.valueOf(txtValorUniProd.getText().replace(",", "."));
                    Double vltotal = Double.valueOf(lblTotalbrutoItem.getText().replace(",", "."));
                    Double desconto = Double.valueOf(txtDesconto.getText().replace(",", "."));
                    Double vlrfinal = Double.valueOf(txtvlrFinal.getText().replace(",", "."));

                    pst = conexao.prepareStatement(itens);
                    pst.setString(1, txtSeqItem.getText());
                    pst.setString(2, numeroos);
                    pst.setInt(3, Integer.parseInt(txtCodProd.getText()));
                    pst.setInt(4, Integer.parseInt(usuario));
                    pst.setInt(5, Integer.parseInt(emp));
                    pst.setInt(6, Integer.parseInt(qtd));
                    pst.setDouble(7, vluni);
                    pst.setDouble(8, vltotal);
                    pst.setDouble(9, desconto);
                    pst.setDouble(10, vlrfinal);
                    if ((txtQtdeProd.getText().isEmpty()) || (txtValorUniProd.getText().isEmpty())
                            || (txtvlrFinal.getText().isEmpty()) || txtCodProd.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Preencha corretamente os campos.");
                    } else {
                        int adicionado = pst.executeUpdate();
                        if (adicionado > 0) {
                            insereitens_tabela();
                            sequencia_itens();
                            calculatotal_os();

                        }

                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void excluios() {
        String excluios = "update ordemservico set excluido = 1 where os = ?";

        try {

            int exclui = JOptionPane.showConfirmDialog(null, "Tem Certeza que deseja excluir?", "Alerta", JOptionPane.YES_NO_OPTION);
            if (exclui == JOptionPane.YES_OPTION) {
                pst = conexao.prepareStatement(excluios);
                pst.setString(1, txtNumOS.getText());

                int excluilogico = pst.executeUpdate();
                if (excluilogico > 0) {

                    JOptionPane.showMessageDialog(null, "Excluido com sucesso");
                    GeraLog bkp = new GeraLog("Tela Os", TelaPrincipal.lblCodUsoPrincipal.getText().concat(" - ").concat(TelaPrincipal.lblUsuario.getText()), "Excluiu Ordem de Serviço", TelaPrincipal.lblcodEmpresa.getText(), txtNumOS.getText());
                    bkp.gravaBackup();
                    limparcampos();
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    //Metodo usado para remover os itens do orçamento
    private void exclui_itensos() {
        String codigo = txtCodProd.getText();
        String os = txtNumOS.getText();
        String item = txtSeqItem.getText();
        String removeitem = "DELETE FROM `itensorcamento`WHERE seqItem = ? and  ordemservico_os =? and produtos_codproduto= ?";
        try {
            if (txtSeqItem.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Selecione um item!");
            } else {

                int exclui = JOptionPane.showConfirmDialog(null, "Confirma a remoção do item?", "Alerta", JOptionPane.YES_NO_OPTION);
                if (exclui == JOptionPane.YES_OPTION) {
                    pst = conexao.prepareStatement(removeitem);
                    pst.setString(1, item);
                    pst.setString(2, os);
                    pst.setString(3, codigo);
                    int confirmando = pst.executeUpdate();
                    if (confirmando > 0) {
                        JOptionPane.showMessageDialog(null, "Item removido.");
                        verificaSequenciaOS();
                        buscaitens_banco();

                        //txtSeqItem.setVisible(true);
                        //lblAuxSeq.setText("");
                        txtCodProd.setText(null);
                        txtDescProd.setText(null);
                        txtValorUniProd.setText("0");
                        txtQtdeProd.setText("1");
                        txtDesconto.setText("0");
                        txtvlrFinal.setText("0");
                        calculatotal_os();
                        txtCodProd.setEnabled(true);
                        txtDescProd.setEnabled(true);
                        txtValorUniProd.setEnabled(true);
                        txtQtdeProd.setEnabled(true);
                        txtDesconto.setEnabled(true);
                        txtvlrFinal.setEnabled(true);
                    }
                } else {
                    buscaitens_banco();
                    txtCodProd.setText(null);
                    txtDescProd.setText(null);
                    txtValorUniProd.setText("0");
                    txtQtdeProd.setText("1");
                    txtDesconto.setText("0");
                    txtvlrFinal.setText("0");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void buscaitens_banco() {
        // String sql = "Select * from itensorcamento where ordemservico_os =? order by seqItem";
        String sql = "SELECT i.seqItem as Sequencia , i.produtos_codproduto as Codigo, p.descricao as Descrição, i.vlunitario as Valor, i.qtde as Qtde, i.vlrfinal as Total\n"
                + "FROM itensorcamento AS i\n"
                + "INNER JOIN produtos AS p ON i.produtos_codproduto = p.codproduto "
                + "where i.ordemservico_os = ? order by i.seqItem;";
        try {
            pst = conexao.prepareStatement(sql);
            // Passando o conteudo do campo pesquisar para o SQL
            pst.setString(1, txtNumOS.getText());
            rs = pst.executeQuery();
            // A linha abixo usa a rs2xml.jar 
            tblGridProd.setModel(DbUtils.resultSetToTableModel(rs));
//            if (rs.next()) {
//                tblGridProd.setModel(DbUtils.resultSetToTableModel(rs));
//            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            System.out.println("Erro aqui busca item");

        }
    }

    private void imprimeos() throws JRException, FileNotFoundException {

        java.util.Date data = new java.util.Date();
        SimpleDateFormat formatador = new SimpleDateFormat("ddMMyyyy");
        String dataFormatada = formatador.format(data);

        String caminhoexecucao = System.getProperty("user.dir");

        int confirma = JOptionPane.showOptionDialog(null, "Deseja imprimir a Ordem de Serviço? ", "Atenção", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
        if (confirma == JOptionPane.YES_OPTION) {
            try {
                // Usando a classe hasmap para criar o filtro
                HashMap os = new HashMap();
                //ImageIcon gto = new ImageIcon(getClass().getResource("/br/com/rpsystem/icones/iconeprincipal.png")); 
                os.put("os", Integer.parseInt(txtNumOS.getText()));
                // usando o jasper para gerar o relatório
                JasperPrint print = JasperFillManager.fillReport(getClass().getResourceAsStream("/reports/ordemServico.jasper"), os, conexao);
                JasperViewer.viewReport(print, false);

                OutputStream outputStream = new FileOutputStream(new File(caminhoexecucao + "\\relatorios\\OS" + txtNumOS.getText().concat("-").concat(dataFormatada).concat(".pdf")));
                JasperExportManager.exportReportToPdfStream(print, outputStream);

                if (TelaPrincipal.lblenviaemailos.getText().equals("Sim")) {
                    envioemail();
                }
                if (TelaPrincipal.lblenviaemailos.getText().equals("Per")) {
                    Object[] opc = {"Sim", "Não"};
                    int envia = JOptionPane.showOptionDialog(null, "Deseja enviar O.S via E-mail?", "Atenção", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, opc, opc[0]);
                    if (envia == JOptionPane.YES_OPTION) {
                        envioemail();
                    }
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        } else {
            // Caminho para salvar o arquivo PDF
            String pdfFilePath = caminhoexecucao + "\\relatorios\\OS" + txtNumOS.getText().concat("-").concat(dataFormatada).concat(".pdf");

            try {

                // Usando a classe hasmap para criar o filtro
                HashMap os = new HashMap();
                os.put("os", Integer.parseInt(txtNumOS.getText()));
                JasperPrint print = JasperFillManager.fillReport(getClass().getResourceAsStream("/reports/ordemServico.jasper"), os, conexao);
                // JasperPrint jasperPrint = (JasperPrint) JRLoader.loadObject(getClass().getResourceAsStream("/reports/ordemServico.jasper"), os, conexao);

                // Exporta para PDF
                OutputStream outputStream = new FileOutputStream(new File(pdfFilePath));
                JasperExportManager.exportReportToPdfStream(print, outputStream);
                outputStream.close();

                if (TelaPrincipal.lblenviaemailos.getText().equals("Sim")) {
                    envioemail();
                }
                if (TelaPrincipal.lblenviaemailos.getText().equals("Per")) {
                    Object[] opc = {"Sim", "Não"};
                    int envia = JOptionPane.showOptionDialog(null, "Deseja enviar O.S via E-mail?", "Atenção", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, opc, opc[0]);
                    if (envia == JOptionPane.YES_OPTION) {
                        envioemail();
                    }
                }

            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(null, e);
                e.printStackTrace();
            } catch (JRException e) {
                JOptionPane.showMessageDialog(null, e);
                e.printStackTrace();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
                e.printStackTrace();
            }
        }
    }

    private void recuperaos() {
        String sql = "select max(os),dataos from ordemservico";
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                txtNumOS.setText(rs.getString(1));
                txtDataOS.setText(rs.getString(2));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void insereitens_tabela() {
        //insere itens do TXT na tabela 

        String sequencia = txtSeqItem.getText().trim();
        String codprod = txtCodProd.getText().trim();
        String descprod = txtDescProd.getText().trim();
        String vlruni = txtValorUniProd.getText().replace(".", ",");
        String qtdeprod = txtQtdeProd.getText().replace(".", ",");
        String vlrtotal = txtvlrFinal.getText().replace(".", ",");
        DefaultTableModel val = (DefaultTableModel) tblGridProd.getModel();
        val.addRow(new String[]{sequencia, codprod, descprod, vlruni, qtdeprod, vlrtotal});
        tblGridProd.getColumnModel().getColumn(0).setPreferredWidth(1);
        tblGridProd.getColumnModel().getColumn(1).setPreferredWidth(1);
        tblGridProd.getColumnModel().getColumn(2).setPreferredWidth(250);
        tblGridProd.getColumnModel().getColumn(3).setPreferredWidth(2);
        tblGridProd.getColumnModel().getColumn(4).setPreferredWidth(2);
        tblGridProd.getColumnModel().getColumn(5).setPreferredWidth(2);
////           tblPesquisaProd.getColumnModel().getColumn(0).setPreferredWidth(1);  // Coluna "Valor" com largura 80 pixels
////            tblPesquisaProd.getColumnModel().getColumn(1).setPreferredWidth(200);
////            tblPesquisaProd.getColumnModel().getColumn(2).setPreferredWidth(2);

        txtDescProd.setText("");
        txtValorUniProd.setText("");
        txtQtdeProd.setText("1");
        txtvlrFinal.setText("");
        txtPesquisaProd.requestFocus();
        txtCodProd.setText(null);

    }

    public void calculatotalitem() {
        NumberFormat formata = new DecimalFormat("#0.00");
        Double qtdprod = Double.valueOf(txtQtdeProd.getText().replace(",", "."));
        Double vlrunitario = Double.valueOf(txtValorUniProd.getText().replace(",", "."));
        Double desconto = Double.valueOf(txtDesconto.getText().replace(",", "."));

        Double vlrtotalbruto = qtdprod * vlrunitario;
        lblTotalbrutoItem.setText(vlrtotalbruto.toString().replace(".", ","));

        Double vlrtotalfinal = vlrtotalbruto - desconto;
        txtvlrFinal.setText(vlrtotalfinal.toString().replace(".", ","));

//            NumberFormat formata = new DecimalFormat("#0.00");
//        txtValorVenda.setText(formata.format(novoprecovista));
    }

    public void sequencia_itens() {
        String sequencia = txtSeqItem.getText();
        int incrementa = Integer.parseInt(sequencia) + 1;
        String incrementou = String.valueOf(incrementa);
        txtSeqItem.setText(incrementou);
    }

    // Metodo que remove a OS ao clicar no botão cancelar ou fechar
    private void cancelaos() {
        String disp = TelaPrincipal.lblDispositivo.getText();
        String numOs = "select os from ordemservico where selecionado = 'X' and dispositivo = ?";
        String impresso = "select impresso from ordemservico where selecionado = 'X' and dispositivo = ?";
        try {
            pst = conexao.prepareStatement(impresso);
            pst.setString(1, disp);

            rs = pst.executeQuery();
            if (rs.next()) {
                String imp = rs.getString(1);
                if (imp.equals("1")) {
                    remove_os_selecionada();
                    limparcampos();
                    bloqueiacampos();

                } else {
                    try {
                        pst = conexao.prepareStatement(numOs);
                        pst.setString(1, disp);
                        rs = pst.executeQuery();
                        if (rs.next()) {
                            String numeroos = rs.getString(1);
                            String cancelaositens = "delete from itensorcamento where ordemservico_os = ?";
                            try {

                                int exclui = JOptionPane.showOptionDialog(null, "Tem Certeza que deseja cancelar?", "Alerta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
                                if (exclui == JOptionPane.YES_OPTION) {
                                    pst = conexao.prepareStatement(cancelaositens);
                                    pst.setString(1, numeroos);
                                    pst.executeUpdate();
                                    String cancelaos = "delete from ordemservico where os = ? and dispositivo = ? and selecionado = 'X'";
                                    try {
                                        pst = conexao.prepareStatement(cancelaos);
                                        pst.setString(1, numeroos);
                                        pst.setString(2, disp);
                                        pst.executeUpdate();
                                    } catch (Exception e) {
                                        JOptionPane.showMessageDialog(null, e);
                                    }
                                }

                            } catch (Exception e) {
                                JOptionPane.showMessageDialog(null, e);
                            }
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(TelaOsProdeServ.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    //Salva/emite ordem de serviço 
    private void salvar_os() {

        String disp = TelaPrincipal.lblDispositivo.getText();
        String numOs = "select os from ordemservico where selecionado = 'X' and dispositivo = ?";
        try {
            pst = conexao.prepareStatement(numOs);
            pst.setString(1, disp);
            rs = pst.executeQuery();
            if (rs.next()) {
                String numeroos = rs.getString(1);
                String sql = "UPDATE `ordemservico`\n"
                        + "SET`tipo` = ?,`situacao` = ?,`servico` = ?,`tecnico` = ?,`valor` = ?,`clientes_idclientes` = ?,\n"
                        + "`usuarios_id` = ?,`empresa_codempresa` =?,`dispositivo` = ?,`selecionado` = ?,impresso = ?,totaldescontos = ?,totalbruto = ?,formaspagamento_codigo = ? \n"
                        + "WHERE `os` = ?;";
                try {
                    pst = conexao.prepareStatement(sql);
                    pst.setString(1, tipo);
                    pst.setString(2, cboSitacao.getSelectedItem().toString());
                    pst.setString(3, txtServico.getText());
                    pst.setString(4, cboVendedor.getSelectedItem().toString());
                    pst.setString(5, txtValorOrdemServico.getText().replace(",", "."));
                    pst.setString(6, txtCodCliOs.getText());
                    pst.setString(7, TelaPrincipal.lblCodUsoPrincipal.getText());
                    pst.setString(8, TelaPrincipal.lblcodEmpresa.getText());
                    pst.setString(9, TelaPrincipal.lblDispositivo.getText());
                    pst.setString(10, null);
                    pst.setString(11, "1");
                    pst.setDouble(12, Double.parseDouble(txtValorDescontoOS.getText().replace(",", ".")));
                    pst.setDouble(13, Double.parseDouble(txtValorBruto.getText().replace(",", ".")));

                    pgtocomp = cboFormaPgto.getSelectedItem().toString();
                    pgtoc = pgtocomp.substring(0, 2)
                            .replace(" ", "")
                            .replace("-", "");

                    pst.setInt(14, Integer.parseInt(pgtoc));
                    pst.setString(15, numeroos);
                    if ((txtCodCliOs.getText().isEmpty()) || (txtValorOrdemServico.getText().isEmpty())) {
                        JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
                    } else {
                        int alterando = pst.executeUpdate();
                        if (alterando > 0) {

                            String cli = "select nome from pessoas where id  = ? ";
                            try {
                                pst = conexao.prepareStatement(cli);
                                pst.setString(1, txtCodCliOs.getText());
                                rs = pst.executeQuery();
                                if (rs.next()) {
                                    txtPesquisaCliOS.setText(rs.getString(1));
                                }
                            } catch (Exception e) {
                                JOptionPane.showMessageDialog(null, e);

                            }

                            JOptionPane.showMessageDialog(null, "Emitido com sucesso");

                            // neste trecho eu checo se ja exite na tabela de lancamentofinanceiro e caso não exita eu insiro.
                            //inicio 
                            if (TelaPrincipal.lblInsereFinAuto.getText().equals("true") && cboSitacao.getSelectedItem().toString().equals(TelaPrincipal.lblSituacaoosfinaliza.getText())) {
                                // -> 
                                String sql2 = "SELECT COUNT(*) FROM lancamentofinanceiro WHERE numos = ?";

                                try {
                                    pst = conexao.prepareStatement(sql2);
                                    pst.setString(1, txtNumOS.getText());
                                    rs = pst.executeQuery();

                                    if (rs.next()) {
                                        int count = rs.getInt(1);
                                        if (count > 0) {

                                        } else {

                                            // Realize a inserção do novo registro aqui
                                            inserefinanceiro();
                                        }
                                    }
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }

                            imprimeos();
                            limparcampos();
                            bloqueiacampos();
                        }
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);

                }

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    // Remove todas as ordens selecionas
    public void remove_os_selecionada() {
        String disp = TelaPrincipal.lblDispositivo.getText();
        String sql = "update ordemservico set selecionado = null where selecionado = 'X' and dispositivo = ? ";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, disp);
            pst.execute();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    public void pesquisando_os() {

        String sql = "Select * from ordemservico where os =?";
        String codigo = txtNumOS.getText();
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, codigo);
            rs = pst.executeQuery();
            if (rs.next()) {
                txtDataOS.setText(rs.getString(2));
                String tipo = (rs.getString(3));
                if (tipo.equals("OS")) {
                    rbtOrdemDeServico.setSelected(true);
                }
                if (tipo.equals("Venda")) {
                    rbtVenda.setSelected(true);
                }
                if (tipo.equals("Orçamento")) {
                    rbtOrcamento.setSelected(true);
                }
                cboSitacao.setSelectedItem(rs.getString(4).toString());
                txtServico.setText(rs.getString(7));
                cboVendedor.setSelectedItem(rs.getString(8).toString());
                txtValorOrdemServico.setText(rs.getString(9).replace(".", ","));

                String textoDesejado = rs.getString(19);
//                System.out.println(textoDesejado);

                if (textoDesejado != null) {

                    for (int i = 0; i < cboFormaPgto.getItemCount(); i++) {
                        String item = cboFormaPgto.getItemAt(i);
                        if (item.startsWith(textoDesejado)) {
                            cboFormaPgto.setSelectedIndex(i);
                            break;
                        }
                    }
                }
                if (cboFormaPgto.getSelectedIndex() == -1) {
                    cboFormaPgto.setSelectedItem(null);
                }

                txtCodCliOs.setText(rs.getString(10));
                String cli = "select nome from pessoas where id = ? ";
                try {
                    pst = conexao.prepareStatement(cli);
                    pst.setString(1, txtCodCliOs.getText());
                    rs = pst.executeQuery();
                    if (rs.next()) {
                        txtPesquisaCliOS.setText(rs.getString(1));
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);

                }

                buscaitens_banco();
                verificaSequenciaOS();
                calculatotal_os();

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            System.out.println(e);
            System.out.println("Erro aqui");
        }
    }

    public void verificaSequenciaOS() {
        String verificaSequencia = "select max(seqItem) from itensorcamento where ordemservico_os = ?";
        String numOs = txtNumOS.getText();
        int maxSeq = 0;
        try {
            pst = conexao.prepareStatement(verificaSequencia);
            pst.setString(1, numOs);
            rs = pst.executeQuery();
            if (rs.next()) {
                int sequencia = rs.getInt(1);
                maxSeq = sequencia + 1;
                String novasequencia = String.valueOf(maxSeq);
                txtSeqItem.setText(novasequencia);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            System.out.println("Erro no verifica sequencia");

        }
    }

    public void calculatotal_os() {
        NumberFormat formata = new DecimalFormat("#0.00");
//        txtValorVenda.setText(formata.format(novoprecovista));

        String ordem = txtNumOS.getText();
        String seq = txtSeqItem.getText();
        if (seq != "0") {
            String sql = "select sum(vltotal),sum(vlrdesconto),sum(vlrfinal) from itensorcamento where ordemservico_os = ? ;";
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, ordem);
                rs = pst.executeQuery();
                if (rs.next()) {
                    txtValorBruto.setText(rs.getString(1).replace(".", ","));
                    txtValorDescontoOS.setText(rs.getString(2).replace(".", ","));
                    txtValorOrdemServico.setText(rs.getString(3).replace(".", ","));
                } else {
                    JOptionPane.showMessageDialog(null, "Os Sem item", "Atenção", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception e) {

                JOptionPane.showMessageDialog(null, e, "Erro", JOptionPane.INFORMATION_MESSAGE);
                System.out.println("Erro no calcula total");
            }
        }

    }

    public void selecionaOsAlteracao() {
        String numOs = txtNumOS.getText();
        String disp = TelaPrincipal.lblDispositivo.getText();

        String sql = "update ordemservico set dispositivo = ? , selecionado = ? where os = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, disp);
            pst.setString(2, "X");
            pst.setString(3, numOs);

            int alterando = pst.executeUpdate();
            if (alterando > 0) {
                //JOptionPane.showMessageDialog(null, "iniciou alteração");
                verificaSequenciaOS();
                liberacampos();

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    public void envioemail() {

        String caminhoexecucao = System.getProperty("user.dir");
        java.util.Date data = new java.util.Date();
        SimpleDateFormat formatador = new SimpleDateFormat("ddMMyyyy");
        String dataFormatada = formatador.format(data);

        String codigo = txtCodCliOs.getText();

        String buscaemailcli = "select email from pessoas "
                + "where id = ? and empresa_codempresa = " + TelaPrincipal.lblcodEmpresa.getText();
        try {
            pst = conexao.prepareStatement(buscaemailcli);
            pst.setString(1, codigo);
            rs = pst.executeQuery();
            if (rs.next()) {
                String emailcli = rs.getString(1);

                String destinatario = emailcli;
                String assunto = "Você esta recebendo a order de serviço da empresa: " + TelaPrincipal.lblFantasiaEmpresaprin.getText();

                String mensagem
                        = "<!DOCTYPE html lang=\"pt-BR\"> <html>\n"
                        + "<body>\n"
                        + "<p>Olá " + txtPesquisaCliOS.getText() + ",</p>\n"
                        + "<p>Foi gerado a OS de número: " + txtNumOS.getText() + ".</p>\n"
                        + "<p>Em caso de dúvidas, entre em contato com a empresa "
                        + TelaPrincipal.lblFantasiaEmpresaprin.getText() + "através do número "
                        + TelaPrincipal.lblFoneEmpresa.getText() + ".</p>\n"
                        + "</body>\n"
                        + "</html>";

                String arquivo = caminhoexecucao + "\\relatorios\\OS" + txtNumOS.getText().concat("-").concat(dataFormatada).concat(".pdf");
                EnviarEmail mail = new EnviarEmail(destinatario, assunto, mensagem, arquivo);
                //remetente, senha, smtp, porta, smtpautenticado,
                mail.enviar();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    public void inserefinanceiro() throws ParseException {

        // Cria um SimpleDateFormat.
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        // Converte o conteúdo do JTextField para um objeto Date.
        java.util.Date dataemissao;
        dataemissao = sdf.parse(TelaPrincipal.lblData.getText());
        java.sql.Date dtemi = new java.sql.Date(dataemissao.getTime());

        pgtocomp = cboFormaPgto.getSelectedItem().toString();
        pgtoc = pgtocomp.substring(0, 2)
                .replace(" ", "")
                .replace("-", "");

        // pega codigo da conta
        String contaforma = "select contacorrente from formaspagamento where codigo = ? and status = 0 and empresa_codempresa = ? ";
        String codconta = null;
        try {
            pst = conexao.prepareStatement(contaforma);
            pst.setString(1, pgtoc);
            pst.setInt(2, Integer.parseInt(TelaPrincipal.lblcodEmpresa.getText()));
            rs = pst.executeQuery();
            if (rs.next()) {

                codconta = rs.getString(1);
                System.out.println("A conta é =" + codconta);

                String sql = "INSERT INTO `lancamentofinanceiro`\n"
                        + "(`tipo`,`descricao`,`dtemissao`,`dtvencimento`,`valor`,`formaspagamento_codigo`,`classificacaofinanceira_cod`,`pessoas_id`,\n"
                        + "`usuarios_id`,`empresa_codempresa`,`excluido`,`observacao`,`dtpagamento`,numos,codConta)\n"
                        + "VALUES\n"
                        + "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                try {
                    pst = conexao.prepareStatement(sql);
                    pst.setString(1, "Receita");
                    pst.setString(2, "Recebimento Os: ".concat(txtNumOS.getText()));
                    pst.setDate(3, dtemi);
                    pst.setDate(4, dtemi);
                    pst.setDouble(5, Double.parseDouble(txtValorOrdemServico.getText().replace(",", ".")));
                    pst.setString(6, null);

                    pst.setInt(7, Integer.parseInt(TelaPrincipal.lblClassificacaofinanceira.getText()));

                    pst.setInt(8, Integer.parseInt(txtCodCliOs.getText()));
                    pst.setInt(9, Integer.parseInt(TelaPrincipal.lblCodUsoPrincipal.getText()));
                    pst.setInt(10, Integer.parseInt(TelaPrincipal.lblcodEmpresa.getText()));
                    pst.setInt(11, 0);
                    pst.setString(12, "Referente ao recebimento da ordem de serviço num: ".concat(txtNumOS.getText()));
                    pst.setDate(13, dtemi);
                    pst.setInt(14, Integer.parseInt(txtNumOS.getText()));

                    pst.setString(15, codconta);

                    int adicionado = 0;
                    adicionado = pst.executeUpdate();

                    if (adicionado > 0) {
                        double valor = Double.valueOf(txtValorOrdemServico.getText().replace(",", "."));
                        String data = TelaPrincipal.lblData.getText();
                        int conta = Integer.parseInt(codconta);
                        AtualizaSaldoContas atu = new AtualizaSaldoContas(tipo, data, valor, conta);
                        atu.atualiza_saldo();
                    }

                    //pst.executeUpdate();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);

                }

            } else {
              //  codconta = null;
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

        //aqui
    }

    /*
     */
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel2 = new javax.swing.JPanel();
        txtPesquisaCliOS = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblListarCli = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        txtCodCliOs = new javax.swing.JTextField();
        rbtOrdemDeServico = new javax.swing.JRadioButton();
        rbtOrcamento = new javax.swing.JRadioButton();
        txtNumOS = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtDataOS = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        cboSitacao = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        cboVendedor = new javax.swing.JComboBox<>();
        jLabel16 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txtSeqItem = new javax.swing.JTextField();
        cboFormaPgto = new javax.swing.JComboBox<>();
        jLabel18 = new javax.swing.JLabel();
        lblAviso = new javax.swing.JLabel();
        rbtVenda = new javax.swing.JRadioButton();
        btnIncluir = new javax.swing.JButton();
        btnPesquisaOs = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        btnExcluiOs = new javax.swing.JButton();
        btnImprimeOs = new javax.swing.JButton();
        pnDadosOs = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtValorOrdemServico = new javax.swing.JTextField();
        txtPesquisaProd = new javax.swing.JTextField();
        txtDescProd = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtQtdeProd = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtValorUniProd = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtvlrFinal = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtCodProd = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        txtServico = new javax.swing.JTextArea();
        txtDesconto = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblGridProd = new javax.swing.JTable();
        lblTotalbrutoItem = new javax.swing.JLabel();
        lblDesconto = new javax.swing.JLabel();
        txtValorDescontoOS = new javax.swing.JTextField();
        txtValorBruto = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblPesquisaProd = new javax.swing.JTable();
        btnAltera = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnFechar = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("Movimentações");
        setToolTipText("");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/rpsystem/icones/computer.png"))); // NOI18N
        setMaximumSize(new java.awt.Dimension(1151, 607));
        setMinimumSize(new java.awt.Dimension(1151, 607));
        setPreferredSize(new java.awt.Dimension(1151, 607));
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained(evt);
            }
        });
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosing(evt);
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameOpened(evt);
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

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txtPesquisaCliOS.setToolTipText("Digite o cliente");
        txtPesquisaCliOS.setEnabled(false);
        txtPesquisaCliOS.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtPesquisaCliOSMouseClicked(evt);
            }
        });
        txtPesquisaCliOS.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPesquisaCliOSKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPesquisaCliOSKeyReleased(evt);
            }
        });

        tblListarCli = new javax.swing.JTable(){
            public boolean  isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        tblListarCli.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        tblListarCli.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Nome", "Fone"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblListarCli.getColumnModel().getColumn(0).setPreferredWidth(1);
        tblListarCli.getColumnModel().getColumn(1).setPreferredWidth(100);
        tblListarCli.getColumnModel().getColumn(2).setPreferredWidth(20);
        tblListarCli.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblListarCliMouseClicked(evt);
            }
        });
        tblListarCli.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblListarCliKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblListarCli);

        jLabel4.setText("*Id:");

        txtCodCliOs.setEditable(false);
        txtCodCliOs.setEnabled(false);

        buttonGroup1.add(rbtOrdemDeServico);
        rbtOrdemDeServico.setText("Ordem de Serviço");
        rbtOrdemDeServico.setEnabled(false);
        rbtOrdemDeServico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtOrdemDeServicoActionPerformed(evt);
            }
        });

        buttonGroup1.add(rbtOrcamento);
        rbtOrcamento.setText("Orçamento");
        rbtOrcamento.setEnabled(false);
        rbtOrcamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtOrcamentoActionPerformed(evt);
            }
        });

        txtNumOS.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNumOSKeyPressed(evt);
            }
        });

        jLabel1.setText("Numero OS:");

        jLabel2.setText("Data:");

        txtDataOS.setEditable(false);
        txtDataOS.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtDataOS.setEnabled(false);
        txtDataOS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDataOSActionPerformed(evt);
            }
        });

        jLabel3.setText("Situação:");

        cboSitacao.setEnabled(false);

        jLabel8.setText("Tecnico:");

        cboVendedor.setEnabled(false);

        jLabel16.setText("Pesquisa de cliente:");

        jLabel15.setVisible(false);
        jLabel15.setText("Seq");

        txtSeqItem.setVisible(false);
        txtSeqItem.setEditable(false);
        txtSeqItem.setText("1");
        txtSeqItem.setEnabled(false);

        cboFormaPgto.setEnabled(false);
        cboFormaPgto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboFormaPgtoActionPerformed(evt);
            }
        });

        jLabel18.setText("Forma Pgto:");

        lblAviso.setVisible(false);
        lblAviso.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/rpsystem/icones/help.png"))); // NOI18N
        lblAviso.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblAvisoMouseClicked(evt);
            }
        });

        buttonGroup1.add(rbtVenda);
        rbtVenda.setText("Venda");
        rbtVenda.setEnabled(false);
        rbtVenda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtVendaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel1)
                        .addComponent(txtNumOS, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(rbtOrcamento, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabel2))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rbtOrdemDeServico)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rbtVenda))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDataOS, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(57, 57, 57)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel8)
                    .addComponent(jLabel18)
                    .addComponent(jLabel3))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(cboFormaPgto, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cboVendedor, javax.swing.GroupLayout.Alignment.LEADING, 0, 173, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(cboSitacao, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblAviso)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addComponent(txtSeqItem, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(87, 87, 87)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 312, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtPesquisaCliOS, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(txtCodCliOs, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(19, 19, 19))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel3)
                    .addComponent(cboSitacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblAviso))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboFormaPgto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18))
                .addGap(428, 428, 428))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel15)
                        .addComponent(txtSeqItem, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(txtCodCliOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPesquisaCliOS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(404, 404, 404))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jLabel2))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtDataOS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNumOS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(14, 14, 14)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbtOrcamento)
                    .addComponent(rbtOrdemDeServico)
                    .addComponent(rbtVenda))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnIncluir.setText("Incluir");
        btnIncluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIncluirActionPerformed(evt);
            }
        });

        btnPesquisaOs.setText("Pesquisar");
        btnPesquisaOs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPesquisaOsActionPerformed(evt);
            }
        });

        btnSalvar.setText("Salvar");
        btnSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvarActionPerformed(evt);
            }
        });

        btnExcluiOs.setText("Excluir");
        btnExcluiOs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcluiOsActionPerformed(evt);
            }
        });

        btnImprimeOs.setText("Imp. / Envia");
        btnImprimeOs.setToolTipText("Imprime ou envia OS por e-mail");
        btnImprimeOs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImprimeOsActionPerformed(evt);
            }
        });

        pnDadosOs.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jLabel7.setText("Observação:");

        jLabel9.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel9.setText("Total OS:");

        txtValorOrdemServico.setEditable(false);
        txtValorOrdemServico.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        txtValorOrdemServico.setText("0");
        txtValorOrdemServico.setEnabled(false);

        txtPesquisaProd.setEnabled(false);
        txtPesquisaProd.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPesquisaProdKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPesquisaProdKeyReleased(evt);
            }
        });

        txtDescProd.setEditable(false);
        txtDescProd.setEnabled(false);

        jLabel5.setText("Descrição");

        txtQtdeProd.setText("1");
        txtQtdeProd.setEnabled(false);
        txtQtdeProd.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtQtdeProdKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtQtdeProdKeyReleased(evt);
            }
        });

        jLabel6.setText("Qtd.");
        jLabel6.setToolTipText("");
        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel6MouseClicked(evt);
            }
        });

        txtValorUniProd.setEnabled(false);
        txtValorUniProd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtValorUniProdActionPerformed(evt);
            }
        });
        txtValorUniProd.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtValorUniProdKeyPressed(evt);
            }
        });

        jLabel10.setText("Valor Uni.");

        jLabel11.setText("Pesquisa de Produtos");

        txtvlrFinal.setEditable(false);
        txtvlrFinal.setText("0");
        txtvlrFinal.setToolTipText("Total ja com desconto");
        txtvlrFinal.setEnabled(false);
        txtvlrFinal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtvlrFinalActionPerformed(evt);
            }
        });
        txtvlrFinal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtvlrFinalKeyPressed(evt);
                txtInsereTablea(evt);
            }
        });

        jLabel12.setText("Total Item");
        jLabel12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel12MouseClicked(evt);
            }
        });

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/rpsystem/icones/deleteitem.png"))); // NOI18N
        jLabel13.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel13MouseClicked(evt);
            }
        });

        txtCodProd.setEditable(false);
        txtCodProd.setEnabled(false);

        jLabel14.setText("Cod.");

        txtServico.setColumns(20);
        txtServico.setLineWrap(true);
        txtServico.setRows(5);
        txtServico.setEnabled(false);
        jScrollPane5.setViewportView(txtServico);

        txtDesconto.setText("0");
        txtDesconto.setEnabled(false);
        txtDesconto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDescontoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDescontoKeyReleased(evt);
            }
        });

        jLabel17.setText("Desc.");

        tblGridProd = new javax.swing.JTable(){
            public boolean  isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        tblGridProd.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Seq.", "Cod.", "Descricao", "Vl. Uni", "Qtd.", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblGridProd.setEnabled(false);
        tblGridProd.getColumnModel().getColumn(0).setPreferredWidth(1);
        tblGridProd.getColumnModel().getColumn(1).setPreferredWidth(1);
        tblGridProd.getColumnModel().getColumn(2).setPreferredWidth(250);
        tblGridProd.getColumnModel().getColumn(3).setPreferredWidth(2);
        tblGridProd.getColumnModel().getColumn(4).setPreferredWidth(2);
        tblGridProd.getColumnModel().getColumn(5).setPreferredWidth(2);
        tblGridProd.getTableHeader().setReorderingAllowed(false);
        tblGridProd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblGridProdMouseClicked(evt);
            }
        });
        tblGridProd.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblGridProdKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblGridProdKeyReleased(evt);
            }
        });
        jScrollPane4.setViewportView(tblGridProd);

        lblTotalbrutoItem.setVisible(false);
        lblTotalbrutoItem.setText("0");

        lblDesconto.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblDesconto.setText("Desconto:");

        txtValorDescontoOS.setEditable(false);
        txtValorDescontoOS.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        txtValorDescontoOS.setText("0");
        txtValorDescontoOS.setEnabled(false);

        txtValorBruto.setEditable(false);
        txtValorBruto.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        txtValorBruto.setText("0");
        txtValorBruto.setEnabled(false);

        jLabel19.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel19.setText("Valor Bruto:");

        tblPesquisaProd = new javax.swing.JTable(){
            public boolean  isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        tblPesquisaProd.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cod", "Descrição", "Valor"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblPesquisaProd.getColumnModel().getColumn(0).setPreferredWidth(1);  // Coluna "Valor" com largura 80 pixels
        tblPesquisaProd.getColumnModel().getColumn(1).setPreferredWidth(200);
        tblPesquisaProd.getColumnModel().getColumn(2).setPreferredWidth(2);
        tblPesquisaProd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPesquisaProdMouseClicked(evt);
            }
        });
        tblPesquisaProd.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblPesquisaProdKeyPressed(evt);
            }
        });
        jScrollPane3.setViewportView(tblPesquisaProd);

        javax.swing.GroupLayout pnDadosOsLayout = new javax.swing.GroupLayout(pnDadosOs);
        pnDadosOs.setLayout(pnDadosOsLayout);
        pnDadosOsLayout.setHorizontalGroup(
            pnDadosOsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnDadosOsLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(pnDadosOsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel7)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 399, Short.MAX_VALUE)
                    .addComponent(txtPesquisaProd)
                    .addComponent(jLabel11)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGroup(pnDadosOsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnDadosOsLayout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addGroup(pnDadosOsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCodProd, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnDadosOsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(pnDadosOsLayout.createSequentialGroup()
                                .addComponent(txtDescProd, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(pnDadosOsLayout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblTotalbrutoItem, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(58, 58, 58)))
                        .addGroup(pnDadosOsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtValorUniProd, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnDadosOsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtQtdeProd, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnDadosOsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtDesconto)
                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnDadosOsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtvlrFinal, javax.swing.GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(62, 62, 62)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(22, 22, 22))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnDadosOsLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 671, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pnDadosOsLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtValorBruto, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblDesconto)
                        .addGap(18, 18, 18)
                        .addComponent(txtValorDescontoOS, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtValorOrdemServico, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(41, 41, 41))))
        );
        pnDadosOsLayout.setVerticalGroup(
            pnDadosOsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnDadosOsLayout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(pnDadosOsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel11)
                    .addComponent(jLabel14)
                    .addComponent(jLabel5)
                    .addComponent(jLabel10)
                    .addComponent(jLabel6)
                    .addComponent(jLabel17)
                    .addComponent(jLabel12)
                    .addComponent(lblTotalbrutoItem))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnDadosOsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(txtPesquisaProd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCodProd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDescProd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtValorUniProd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtQtdeProd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDesconto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtvlrFinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnDadosOsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnDadosOsLayout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(pnDadosOsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(txtValorBruto)
                            .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblDesconto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtValorDescontoOS)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtValorOrdemServico))
                        .addGap(18, 18, 18))
                    .addGroup(pnDadosOsLayout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        btnAltera.setText("Alterar");
        btnAltera.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlteraActionPerformed(evt);
            }
        });

        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnFechar.setText("Fechar ");
        btnFechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFecharActionPerformed(evt);
            }
        });
        btnFechar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnFecharKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnDadosOs, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(13, 13, 13))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnIncluir)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAltera)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancelar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPesquisaOs)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSalvar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnExcluiOs)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnImprimeOs)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnFechar)
                .addGap(239, 239, 239))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnDadosOs, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnPesquisaOs)
                        .addComponent(btnIncluir)
                        .addComponent(btnAltera)
                        .addComponent(btnCancelar))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnSalvar)
                        .addComponent(btnExcluiOs))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnImprimeOs)
                        .addComponent(btnFechar)))
                .addGap(26, 26, 26))
        );

        setBounds(0, 0, 1151, 607);
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        // TODO add your handling code here:

        rbtOrcamento.setSelected(true);
        tipo = "Orçamento";
        //consultaAvancada();
        remove_os_selecionada();

        //preenchetecino();
        //preenchestatus();
        Funcoes cartao = new Funcoes(cboFormaPgto);
        cartao.f_formapgto(cboFormaPgto);
        cartao.f_situacaoOs(cboSitacao);
        cartao.f_preenchetecino(cboVendedor);


    }//GEN-LAST:event_formInternalFrameOpened

    private void btnIncluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIncluirActionPerformed
        // TODO add your handling code here:
        iniciaos();
        limparcampos();
        liberacampos();


    }//GEN-LAST:event_btnIncluirActionPerformed

    private void btnPesquisaOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPesquisaOsActionPerformed
//        // TODO add your handling code here:
        TelaPesquisa pesquisa = new TelaPesquisa();
        pesquisa.setVisible(true);

        TelaPrincipal.Desktop.add(pesquisa);
        //coiso 
        CentralizaForm c = new CentralizaForm(pesquisa);
        c.centralizaForm();
        //fim coiso 

        pesquisa.toFront();
        pesquisa.txtPesquisaOs.requestFocus();

        TelaPesquisa.lblOrigem.setText("ProdServ");

    }//GEN-LAST:event_btnPesquisaOsActionPerformed

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
        // TODO add your handling code here:
        salvar_os();

    }//GEN-LAST:event_btnSalvarActionPerformed

    private void btnAlteraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlteraActionPerformed
        // TODO add your handling code here:
        String codos = txtNumOS.getText();
        if (codos.equals("")) {
            JOptionPane.showMessageDialog(null, "Selecione uma OS.");

        } else {

            selecionaOsAlteracao();
        }

    }//GEN-LAST:event_btnAlteraActionPerformed

    private void btnExcluiOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluiOsActionPerformed
        // TODO add your handling code here:
        String codos = txtNumOS.getText();
        if (codos.equals("")) {
            JOptionPane.showMessageDialog(null, "Selecione uma OS.");

        } else {
            excluios();
        }

    }//GEN-LAST:event_btnExcluiOsActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // TODO add your handling code here:
        cancelaos();
        limparcampos();
        bloqueiacampos();


    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnImprimeOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimeOsActionPerformed
        // TODO add your handling code here:
        String codos = txtNumOS.getText();
        if (codos.equals("")) {
            JOptionPane.showMessageDialog(null, "Selecione uma OS.");
        } else {
            try {
                imprimeos();
            } catch (JRException ex) {
                Logger.getLogger(TelaOsProdeServ.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(TelaOsProdeServ.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }//GEN-LAST:event_btnImprimeOsActionPerformed

    private void txtValorUniProdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtValorUniProdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtValorUniProdActionPerformed

    private void txtPesquisaProdKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesquisaProdKeyReleased
        // TODO add your handling code here:
        consultaprod();
    }//GEN-LAST:event_txtPesquisaProdKeyReleased

    private void txtQtdeProdKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtQtdeProdKeyReleased
        // TODO add your handling code here:
        calculatotalitem();

    }//GEN-LAST:event_txtQtdeProdKeyReleased

    private void jLabel12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel12MouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_jLabel12MouseClicked

    private void txtvlrFinalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtvlrFinalKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtvlrFinalKeyPressed

    private void jLabel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseClicked
        // TODO add your handling code here:
        calculatotalitem();
    }//GEN-LAST:event_jLabel6MouseClicked

    private void txtInsereTablea(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtInsereTablea
        // TODO add your handling code here:
        //aqui vai o q voce deseja fazer quando o usuario clicar enter naquele jtextfield
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            gravaitens_banco();
            txtDesconto.setText("0");

        }
    }//GEN-LAST:event_txtInsereTablea

    private void txtQtdeProdKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtQtdeProdKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtDesconto.requestFocus();
//aqui vai o q voce deseja fazer quando o usuario clicar enter naquele jtextfield
        }
    }//GEN-LAST:event_txtQtdeProdKeyPressed

    private void txtValorUniProdKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtValorUniProdKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtQtdeProd.requestFocus();
//aqui vai o q voce deseja fazer quando o usuario clicar enter naquele jtextfield
        }
    }//GEN-LAST:event_txtValorUniProdKeyPressed

    private void jLabel13MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel13MouseClicked
        // TODO add your handling code here:
        exclui_itensos();
    }//GEN-LAST:event_jLabel13MouseClicked

    private void btnFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFecharActionPerformed
        // TODO add your handling code here:
        cancelaos();
        remove_os_selecionada();
        this.dispose();
    }//GEN-LAST:event_btnFecharActionPerformed

    private void formFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_formFocusGained

    private void formKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            cancelaos();
            remove_os_selecionada();
            this.dispose();

        }
    }//GEN-LAST:event_formKeyReleased

    private void btnFecharKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnFecharKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            cancelaos();
            remove_os_selecionada();
            this.dispose();

        }
    }//GEN-LAST:event_btnFecharKeyPressed

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            cancelaos();
            remove_os_selecionada();
            this.dispose();
        }

    }//GEN-LAST:event_formKeyPressed

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        // TODO add your handling code here:
        cancelaos();
        remove_os_selecionada();
        this.dispose();
    }//GEN-LAST:event_formInternalFrameClosing

    private void txtPesquisaProdKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesquisaProdKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            tblPesquisaProd.requestFocus();
//aqui vai o q voce deseja fazer quando o usuario clicar enter naquele jtextfield
        }
    }//GEN-LAST:event_txtPesquisaProdKeyPressed

    private void txtvlrFinalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtvlrFinalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtvlrFinalActionPerformed

    private void txtDescontoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDescontoKeyPressed
        // TODO add your handling code here:

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            //aqui vai o q voce deseja fazer quando o usuario clicar enter naquele jtextfield
            txtvlrFinal.requestFocus();
            calculatotalitem();
        }
    }//GEN-LAST:event_txtDescontoKeyPressed

    private void txtDescontoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDescontoKeyReleased
        // TODO add your handling code here:
        calculatotalitem();
    }//GEN-LAST:event_txtDescontoKeyReleased

    private void tblGridProdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGridProdMouseClicked
        // TODO add your handling code here:
        seleciona_produto_removacao();
    }//GEN-LAST:event_tblGridProdMouseClicked

    private void tblGridProdKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblGridProdKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tblGridProdKeyPressed

    private void tblGridProdKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblGridProdKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tblGridProdKeyReleased

    private void tblPesquisaProdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPesquisaProdMouseClicked
        // TODO add your handling code here:
        setaprod();
    }//GEN-LAST:event_tblPesquisaProdMouseClicked

    private void tblPesquisaProdKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblPesquisaProdKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            setaprod();
            //  txtQtdeProd.requestFocus();

//aqui vai o q voce deseja fazer quando o usuario clicar enter naquele jtextfield
        }

        if (evt.getKeyCode() == KeyEvent.VK_TAB) {
            txtQtdeProd.requestFocus();
        }

    }//GEN-LAST:event_tblPesquisaProdKeyPressed

    private void txtDataOSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDataOSActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDataOSActionPerformed

    private void txtNumOSKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumOSKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            pesquisando_os();
            //aqui vai o q voce deseja fazer quando o usuario clicar enter naquele jtextfield
        }
    }//GEN-LAST:event_txtNumOSKeyPressed

    private void rbtOrcamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtOrcamentoActionPerformed
        // TODO add your handling code here:
        tipo = "Orçamento";
        cboSitacao.setEnabled(true);

    }//GEN-LAST:event_rbtOrcamentoActionPerformed

    private void rbtOrdemDeServicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtOrdemDeServicoActionPerformed
        // TODO add your handling code here:
        tipo = "OS";
        cboSitacao.setEnabled(true);
    }//GEN-LAST:event_rbtOrdemDeServicoActionPerformed

    private void tblListarCliKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblListarCliKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            setar_codCliente();
            txtPesquisaProd.requestFocus();

        }
    }//GEN-LAST:event_tblListarCliKeyPressed

    private void tblListarCliMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblListarCliMouseClicked
        // TODO add your handling code here:
        setar_codCliente();
    }//GEN-LAST:event_tblListarCliMouseClicked

    private void txtPesquisaCliOSKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesquisaCliOSKeyReleased
        // TODO add your handling code here:
        consultaAvancada();
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            tblListarCli.requestFocus();
            //aqui vai o q voce deseja fazer quando o usuario clicar enter naquele jtextfield
        }
    }//GEN-LAST:event_txtPesquisaCliOSKeyReleased

    private void txtPesquisaCliOSKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesquisaCliOSKeyPressed
        // TODO add your handling code here:
        //  txtPesquisaCliOS.setText(null);
    }//GEN-LAST:event_txtPesquisaCliOSKeyPressed

    private void txtPesquisaCliOSMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPesquisaCliOSMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPesquisaCliOSMouseClicked

    private void cboFormaPgtoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboFormaPgtoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboFormaPgtoActionPerformed

    private void rbtVendaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtVendaActionPerformed
        // TODO add your handling code here:
        tipo = "Venda";

        //Preenche combo do classificação
        String classificacao = TelaPrincipal.lblSituacaoosfinaliza.getText();
        //System.out.println( TelaPrincipal.lblSituacaoosfinaliza.getText());

        cboSitacao.setSelectedItem(TelaPrincipal.lblSituacaoosfinaliza.getText());
        cboSitacao.setEnabled(false);

    }//GEN-LAST:event_rbtVendaActionPerformed

    private void lblAvisoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblAvisoMouseClicked
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(null, "Caso seja selecionado o Status: ".concat(TelaPrincipal.lblSituacaoosfinaliza.getText().concat(" \nSerá inserido o lançamento financeiro automaticamente.")),"Aviso",JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_lblAvisoMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAltera;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnExcluiOs;
    private javax.swing.JButton btnFechar;
    private javax.swing.JButton btnImprimeOs;
    public static javax.swing.JButton btnIncluir;
    private javax.swing.JButton btnPesquisaOs;
    private javax.swing.JButton btnSalvar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cboFormaPgto;
    public static javax.swing.JComboBox<String> cboSitacao;
    public static javax.swing.JComboBox<String> cboVendedor;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JLabel lblAviso;
    private javax.swing.JLabel lblDesconto;
    private javax.swing.JLabel lblTotalbrutoItem;
    public static javax.swing.JPanel pnDadosOs;
    public static javax.swing.JRadioButton rbtOrcamento;
    public static javax.swing.JRadioButton rbtOrdemDeServico;
    private javax.swing.JRadioButton rbtVenda;
    public static javax.swing.JTable tblGridProd;
    public static javax.swing.JTable tblListarCli;
    public static javax.swing.JTable tblPesquisaProd;
    public static javax.swing.JTextField txtCodCliOs;
    private javax.swing.JTextField txtCodProd;
    public static javax.swing.JTextField txtDataOS;
    private javax.swing.JTextField txtDescProd;
    private javax.swing.JTextField txtDesconto;
    public static javax.swing.JTextField txtNumOS;
    private javax.swing.JTextField txtPesquisaCliOS;
    private javax.swing.JTextField txtPesquisaProd;
    private javax.swing.JTextField txtQtdeProd;
    private javax.swing.JTextField txtSeqItem;
    private javax.swing.JTextArea txtServico;
    public static javax.swing.JTextField txtValorBruto;
    public static javax.swing.JTextField txtValorDescontoOS;
    public static javax.swing.JTextField txtValorOrdemServico;
    private javax.swing.JTextField txtValorUniProd;
    private javax.swing.JTextField txtvlrFinal;
    // End of variables declaration//GEN-END:variables
}
