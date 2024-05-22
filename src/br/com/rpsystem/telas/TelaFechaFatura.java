/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package br.com.rpsystem.telas;

import br.com.rpsystem.dal.ModuloConexao;
import br.com.rpsystem.funcoes.Funcoes;
import br.com.rpsystem.funcoes.PagamentoFatura;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import mondrian.olap.Connection;

/**
 *
 * @author Rafael Veiga
 */
public class TelaFechaFatura extends javax.swing.JInternalFrame {

    String cartaoCompleto = null;
    int cod_cartao;
    int codfatura;
    Object[] options = {"Sim", "Não"};
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sdfSaida = new SimpleDateFormat("dd/MM/yyyy");

    java.sql.Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    /**
     * Creates new form TelaFechaFatura
     */
    public TelaFechaFatura() {
        initComponents();
        conexao = ModuloConexao.conector();

//        Funcoes f = new Funcoes(cboForma);
//        f.f_formapgto(cboForma);
        Funcoes conta = new Funcoes(cboConta);
        conta.f_carregaconta(cboConta);

    }

    public void ver_fatura() {
        //chkParcelado.isSelected()
        String sql = null;
        if (chkExibePagas.isSelected()) {
           sql = "SELECT \n"
                    + "    f.cod, \n"
                    + "    f.cartao, \n"
                    + "    c.nomecartao, \n"
                    + "    FORMAT(SUM(pf.valorpago), 2) AS pago,\n"
                    + "    CEILING((f.valor - COALESCE(SUM(pf.valorpago), 0)) * 100) / 100 AS saldo, \n"
                    + "    f.vencimento, \n"
                    + "    f.status\n"
                    + "FROM \n"
                    + "    fatura f\n"
                    + "INNER JOIN \n"
                    + "    cadcartoes c ON f.cartao = c.codigo\n"
                    + "LEFT JOIN \n"
                    + "    pagamentos_faturas pf ON f.cod = pf.cod_fatura AND pf.status_pagamento = 0\n"
                    + "WHERE \n"
                    + "    f.codempresa = ? \n"
                    + "GROUP BY \n"
                    + "    f.cod, f.cartao, c.nomecartao, f.vencimento, f.status\n"
//                    + "HAVING\n"
//                    + "    saldo > 0\n"
                    + "ORDER BY \n"
                    + "    f.vencimento;";

        } else {
            sql = "SELECT \n"
                    + "    f.cod, \n"
                    + "    f.cartao, \n"
                    + "    c.nomecartao, \n"
                    + "    FORMAT(SUM(pf.valorpago), 2) AS pago,\n"
                    + "    CEILING((f.valor - COALESCE(SUM(pf.valorpago), 0)) * 100) / 100 AS saldo, \n"
                    + "    f.vencimento, \n"
                    + "    f.status\n"
                    + "FROM \n"
                    + "    fatura f\n"
                    + "INNER JOIN \n"
                    + "    cadcartoes c ON f.cartao = c.codigo\n"
                    + "LEFT JOIN \n"
                    + "    pagamentos_faturas pf ON f.cod = pf.cod_fatura AND pf.status_pagamento = 0\n"
                    + "WHERE \n"
                    + "    f.codempresa = ? AND f.status = 'A'\n"
                    + "GROUP BY \n"
                    + "    f.cod, f.cartao, c.nomecartao, f.vencimento, f.status\n"
                    + "HAVING\n"
                    + "    saldo > 0\n"
                    + "ORDER BY \n"
                    + "    f.vencimento;";
        }

        try {
            pst = conexao.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(TelaPrincipal.lblcodEmpresa.getText()));
            rs = pst.executeQuery();

            // Criar o modelo da tabela
            DefaultTableModel model = (DefaultTableModel) tblFatura.getModel();
            // Limpar o modelo da tabela
            model.setRowCount(0);
            // Preencher o modelo da tabela com os dados do ResultSet

            while (rs.next()) {
                String codfatura = rs.getString("f.cod");
                String cartao = rs.getString("f.cartao").concat(" - ").concat(rs.getString("c.nomecartao"));
//                String cartao = rs.getString("c.nomecartao");
                String datavencimento = rs.getString("f.vencimento");
                java.util.Date dtvenc = sdf.parse(datavencimento);
                String dtvencformatada = sdfSaida.format(dtvenc);
                double pago = rs.getDouble("pago");
                double total = rs.getDouble("saldo");
                String status = rs.getString("f.status").replace("A", "Aberto").replace("P", "Pago");

                // Adicionar uma nova linha ao modelo da tabela
                model.addRow(new Object[]{codfatura, cartao, dtvencformatada, pago, total, status});
            }

            // Definir o modelo da tabela
            tblFatura.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void seta_fatura() {

        String status = "";
        int setar = tblFatura.getSelectedRow();
        //caputuro da tabela o status da fatura
        status = tblFatura.getModel().getValueAt(setar, 5).toString();

        if (status.equals("Pago")) {
            JOptionPane.showMessageDialog(null, "Fatura ja paga");
        } else {
            //caputra o codigo da fatura e joga na lblcodfatura
            codfatura = Integer.parseInt(tblFatura.getModel().getValueAt(setar, 0).toString());
            lblCodFatura.setText(String.valueOf(codfatura));

            //pega a coluna completa do cartão e extrai so o codigo e seta na lbl 
            cartaoCompleto = tblFatura.getModel().getValueAt(setar, 1).toString();
            cod_cartao = Integer.parseInt(cartaoCompleto.substring(0, 2).replaceAll("\\s|-", ""));
            txtNomeCartao.setText(cartaoCompleto);
            lblCartao.setText(String.valueOf((cod_cartao)));

            pesquiafornecedorCartao();

            txtVencimento.setText(tblFatura.getModel().getValueAt(setar, 2).toString());
            txtValor.setText(tblFatura.getModel().getValueAt(setar, 4).toString());

            txtValor.setEditable(true);
        }

    }

    public void pesquiafornecedorCartao() {
        String sqlCartao = "select * from cadcartoes where codigo = ? and cod_empresa = " + TelaPrincipal.lblcodEmpresa.getText();
        //String sqlCodFornecedor = "select id from pessoas where id = ? and fornecedor = '1' and empresa_codempresa = " + TelaPrincipal.lblcodEmpresa.getText();
        try {
            pst = conexao.prepareStatement(sqlCartao);
            pst.setString(1, lblCartao.getText());
            rs = pst.executeQuery();
            if (rs.next()) {
                lblCodFornecedor.setText(rs.getString(3));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    public void limpacampos() {
        lblCartao.setText("0");
        lblCodFornecedor.setText("0");
        txtValor.setText("0");
        txtVencimento.setText("0");
        txtNomeCartao.setText("0");

    }

    public void pagafatura() {

        int codfatura = Integer.parseInt(lblCodFatura.getText());
        double valor = Double.parseDouble(txtValor.getText().replace(",", "."));
        String obs = "teste";
        String datapg = TelaPrincipal.lblData.getText();
        String dataven = txtVencimento.getText();

        PagamentoFatura pag = new PagamentoFatura(codfatura, valor, datapg, dataven, obs);
        pag.pgfatura();

        if (pag.insere > 0) {
            txtValor.setEditable(false);
            limpacampos();
            ver_fatura();
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        tblFatura = new javax.swing.JTable();
        lblAtualizar = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        txtVencimento = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtValor = new javax.swing.JTextField();
        lblCartao = new javax.swing.JLabel();
        lblCodFornecedor = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtNomeCartao = new javax.swing.JTextField();
        chkExibePagas = new javax.swing.JCheckBox();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        lblCodFatura = new javax.swing.JLabel();
        btnPagar = new javax.swing.JButton();
        cboConta = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setTitle("Controle de Faturas");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/rpsystem/icones/fcartao.png"))); // NOI18N
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
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
                formInternalFrameOpened(evt);
            }
        });

        tblFatura.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        tblFatura.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cod.", "Cartão", "Vencimento", "Pago", "Valor", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblFatura.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblFaturaMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblFatura);

        lblAtualizar.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        lblAtualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/rpsystem/icones/reload.png"))); // NOI18N
        lblAtualizar.setText("Clique para atualizar");
        lblAtualizar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblAtualizarMouseClicked(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel1.setText("Vencimento:");

        txtVencimento.setEditable(false);
        txtVencimento.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        jLabel2.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel2.setText("Valor:");

        txtValor.setEditable(false);
        txtValor.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        lblCartao.setVisible(false);
        lblCartao.setText("Cartão");

        lblCodFornecedor.setVisible(false);
        lblCodFornecedor.setText("jLabel3");

        jLabel3.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Selecione  a fatura que deseja pagar e clique em pagar para efetivar o pagamento.");

        jLabel4.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel4.setText("Cartão:");

        txtNomeCartao.setEditable(false);
        txtNomeCartao.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        chkExibePagas.setText("Exibe pagas");
        chkExibePagas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkExibePagasActionPerformed(evt);
            }
        });

        jLabel5.setVisible(false);
        jLabel5.setText("Cod Fornecedor");

        jLabel6.setVisible(false);
        jLabel6.setText("Cod cartao");

        lblCodFatura.setVisible(false);
        lblCodFatura.setText("jLabel7");

        btnPagar.setText("Pagar");
        btnPagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPagarActionPerformed(evt);
            }
        });

        jLabel7.setText("Conta");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(chkExibePagas)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblAtualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jScrollPane2)
                        .addContainerGap())))
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNomeCartao, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtVencimento, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtValor, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cboConta, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 132, Short.MAX_VALUE)
                        .addComponent(btnPagar)
                        .addGap(65, 65, 65))))
            .addGroup(layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(lblCodFatura)
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblCartao)
                .addGap(27, 27, 27)
                .addComponent(jLabel5)
                .addGap(39, 39, 39)
                .addComponent(lblCodFornecedor)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAtualizar)
                    .addComponent(chkExibePagas))
                .addGap(12, 12, 12)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(txtVencimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtValor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboConta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNomeCartao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblCartao)
                        .addComponent(jLabel6)
                        .addComponent(lblCodFatura))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblCodFornecedor)
                        .addComponent(jLabel5)))
                .addGap(31, 31, 31))
            .addGroup(layout.createSequentialGroup()
                .addGap(254, 254, 254)
                .addComponent(btnPagar)
                .addGap(74, 74, 74))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        // TODO add your handling code here:
        ver_fatura();
    }//GEN-LAST:event_formInternalFrameOpened

    private void lblAtualizarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblAtualizarMouseClicked
        // TODO add your handling code here:
        ver_fatura();
    }//GEN-LAST:event_lblAtualizarMouseClicked

    private void tblFaturaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblFaturaMouseClicked
        // TODO add your handling code here:
        seta_fatura();
    }//GEN-LAST:event_tblFaturaMouseClicked

    private void chkExibePagasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkExibePagasActionPerformed
        // TODO add your handling code here:
        ver_fatura();
    }//GEN-LAST:event_chkExibePagasActionPerformed

    private void btnPagarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPagarActionPerformed
        // TODO add your handling code here:

        pagafatura();
    }//GEN-LAST:event_btnPagarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnPagar;
    public static javax.swing.JComboBox<String> cboConta;
    private javax.swing.JCheckBox chkExibePagas;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    public static javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblAtualizar;
    private javax.swing.JLabel lblCartao;
    private javax.swing.JLabel lblCodFatura;
    public static javax.swing.JLabel lblCodFornecedor;
    private javax.swing.JTable tblFatura;
    private javax.swing.JTextField txtNomeCartao;
    public static javax.swing.JTextField txtValor;
    public static javax.swing.JTextField txtVencimento;
    // End of variables declaration//GEN-END:variables
}
