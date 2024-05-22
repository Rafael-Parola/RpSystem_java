/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package br.com.rpsystem.telas;

import java.sql.Connection;
import br.com.rpsystem.dal.ModuloConexao;
import java.awt.event.KeyEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Rafael Veiga
 */
public class TelaRelFinanceiroMovCartoes extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    java.sql.Date dtpgto = null;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    String tipo = "Emissão";

    java.sql.Date dataemissaoini = null;
    Date dtemiini = null;

    java.sql.Date dataemissaofim = null;
    Date dtemifim = null;

    java.sql.Date datavenciemntoini = null;
    Date dtvencini = null;

    java.sql.Date datavenciemntofim = null;
    Date dtvencfim = null;

    /**
     * Creates new form TelaRelFinanceiroDespesaReceita
     */
    public TelaRelFinanceiroMovCartoes() {
        initComponents();
        //  jrbDtpagamento.setSelected(true);
        jrbDtEmissao.setSelected(true);
        tipo = "Emissão";

        //  pagos = "Todos";
    }

    public void classificacao() {

        conexao = ModuloConexao.conector();

        var classificacao = "select cod,descricao from classificacaofinanceira where excluido =0";
        conexao = ModuloConexao.conector();
        try {
            pst = conexao.prepareStatement(classificacao);
            rs = pst.executeQuery();
            if (rs.next()) {
                do {
                    cboClassificacao.addItem(rs.getString("cod").concat(" - ").concat(rs.getString("descricao")));
                } while (rs.next());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    public static void generateReport(String tipo, Date emiini, Date emifim,
            Date vencini, Date vencfim) {
      //  System.out.println(tipo + "," + emiini + "," + emifim + "," + vencini + "," + vencfim);
        try {

// Definir os parâmetros do relatório
            Map<String, Object> parametros = new HashMap<>();

            parametros.put("classificacao", tipo);

            //Emissao
            parametros.put("dtemiini", emiini);
            parametros.put("dtemifim", emifim);
            //vencimentodtvencini
            parametros.put("dtvencini", vencini);
            parametros.put("dtvencfim", vencfim);

            //parametros.put("MostrarPagos", pagos);
            // Gerar o relatório a partir do arquivo .jasper
            JasperPrint relatorio = JasperFillManager.fillReport(TelaRelFinanceiroMovCartoes.class.getResourceAsStream("/reports/RelMovCartoes.jasper"),
                    parametros,
                    ModuloConexao.conector()
            );

            // Exibir o relatório em uma janela
            JasperViewer.viewReport(relatorio, false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void rel() throws ParseException {
        String datainicial = txtdatainicial.getText().trim();
        String datafinal = txtdatafinal.getText().trim();

        if (tipo.equals("Emissão")) {
         //   System.out.println("caiu no emissao");
           // System.out.println(tipo);

            if (datainicial.matches("\\d{2}/\\d{2}/\\d{4}")) {

                dtemiini = sdf.parse(txtdatainicial.getText());
                dataemissaoini = new java.sql.Date(dtemiini.getTime());
             //   System.out.println("A data iniciael é : " + dataemissaoini);

                dtemifim = sdf.parse(txtdatafinal.getText());
                dataemissaofim = new java.sql.Date(dtemifim.getTime());
             //   System.out.println("A data final é : " + dataemissaofim);

                datavenciemntoini = null;
                datavenciemntofim = null;

            }

        }
        if (tipo.equals("Vencimento")) {
         //   System.out.println(tipo);

            if (datainicial.matches("\\d{2}/\\d{2}/\\d{4}")) {

                dtvencini = sdf.parse(txtdatainicial.getText());
                datavenciemntoini = new java.sql.Date(dtvencini.getTime());
            //    System.out.println("A data ini é : " + datavenciemntoini);

                dtvencfim = sdf.parse(txtdatafinal.getText());
                datavenciemntofim = new java.sql.Date(dtvencfim.getTime());
             //   System.out.println("A data final é : " + datavenciemntofim);

                dataemissaoini = null;
                dataemissaofim = null;
//             
            }
        } else {

        }

        //captura o codigo5 do cboclassificacao
        String classifica_caocompleta = null;
        String tipo;
        classifica_caocompleta = cboClassificacao.getSelectedItem().toString();

        if (classifica_caocompleta.equals("Todos")) {
            tipo = null;
        } else {
            tipo = classifica_caocompleta.substring(0, 2).replaceAll("\\s|-", "");

        }

        generateReport(tipo, dataemissaoini, dataemissaofim, datavenciemntoini, datavenciemntofim); // Gerar relatório filtrando com filtros"

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jRadioButton2 = new javax.swing.JRadioButton();
        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        btnGerar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jrbDtVencimento = new javax.swing.JRadioButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtdatainicial = new javax.swing.JFormattedTextField();
        txtdatafinal = new javax.swing.JFormattedTextField();
        cboClassificacao = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jrbDtEmissao = new javax.swing.JRadioButton();

        jLabel1.setText("jLabel1");

        jRadioButton2.setText("jRadioButton2");

        setTitle("Rel lançamentos cartão");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/rpsystem/icones/report.png"))); // NOI18N
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

        btnGerar.setText("Gerar");
        btnGerar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGerarActionPerformed(evt);
            }
        });

        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Filtrar por:"));

        buttonGroup1.add(jrbDtVencimento);
        jrbDtVencimento.setText("Vencimento");
        jrbDtVencimento.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jrbDtVencimentoMouseClicked(evt);
            }
        });
        jrbDtVencimento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrbDtVencimentoActionPerformed(evt);
            }
        });

        jLabel2.setText("Inicial:");

        jLabel3.setText("Final:");

        try {
            txtdatainicial.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtdatainicial.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtdatainicial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtdatainicialActionPerformed(evt);
            }
        });
        txtdatainicial.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtdatainicialKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtdatainicialKeyReleased(evt);
            }
        });

        try {
            txtdatafinal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtdatafinal.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtdatafinal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtdatafinalActionPerformed(evt);
            }
        });
        txtdatafinal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtdatafinalKeyPressed(evt);
            }
        });

        cboClassificacao.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos" }));

        jLabel4.setText("Tipo:");

        buttonGroup1.add(jrbDtEmissao);
        jrbDtEmissao.setText("Emissão");
        jrbDtEmissao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jrbDtEmissaoMouseClicked(evt);
            }
        });
        jrbDtEmissao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrbDtEmissaoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(txtdatainicial, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtdatafinal, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(cboClassificacao, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jrbDtEmissao)
                        .addGap(18, 18, 18)
                        .addComponent(jrbDtVencimento)
                        .addGap(67, 67, 67))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jrbDtVencimento)
                    .addComponent(jrbDtEmissao))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel3)
                    .addComponent(txtdatainicial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtdatafinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboClassificacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btnGerar)
                        .addGap(49, 49, 49)
                        .addComponent(btnCancelar)
                        .addGap(66, 66, 66))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGerar)
                    .addComponent(btnCancelar))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        setBounds(0, 0, 355, 290);
    }// </editor-fold>//GEN-END:initComponents

    private void btnGerarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGerarActionPerformed

        try {
            // TODO add your handling code here:

            rel();
        } catch (ParseException ex) {
            Logger.getLogger(TelaRelFinanceiroMovCartoes.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_btnGerarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void txtdatainicialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtdatainicialActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtdatainicialActionPerformed

    private void txtdatainicialKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtdatainicialKeyPressed
        // TODO add your handling code here:
        if (txtdatainicial.getText().equals("  /  /    ")) {
            if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                txtdatainicial.setText(TelaPrincipal.lblData.getText());
                txtdatafinal.requestFocus();

            }
        } else {
            if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                txtdatafinal.requestFocus();
            }
        }

        if (evt.getKeyCode() == KeyEvent.VK_TAB) {
            txtdatainicial.setText("  /  /    ");
            txtdatafinal.requestFocus();
        }


    }//GEN-LAST:event_txtdatainicialKeyPressed

    private void txtdatafinalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtdatafinalActionPerformed
        // TOD  O add your handling code here:
    }//GEN-LAST:event_txtdatafinalActionPerformed

    private void txtdatafinalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtdatafinalKeyPressed
        // TODO add your handling code here:
        if (txtdatafinal.getText().equals("  /  /    ")) {
            if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                txtdatafinal.setText(TelaPrincipal.lblData.getText());
                cboClassificacao.requestFocus();

            }
        } else {
            if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                cboClassificacao.requestFocus();
            }
        }

        if (evt.getKeyCode() == KeyEvent.VK_TAB) {
            txtdatafinal.setText("  /  /    ");
            cboClassificacao.requestFocus();
        }
    }//GEN-LAST:event_txtdatafinalKeyPressed

    private void jrbDtVencimentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrbDtVencimentoActionPerformed
        // TODO add your handling code here:
        tipo = "Vencimento";


    }//GEN-LAST:event_jrbDtVencimentoActionPerformed

    private void txtdatainicialKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtdatainicialKeyReleased
        // TODO add your handling code here:

    }//GEN-LAST:event_txtdatainicialKeyReleased

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        // TODO add your handling code here:
        classificacao();
    }//GEN-LAST:event_formInternalFrameOpened

    private void jrbDtEmissaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrbDtEmissaoActionPerformed
        // TODO add your handling code here:
        tipo = "Emissão";


    }//GEN-LAST:event_jrbDtEmissaoActionPerformed

    private void jrbDtVencimentoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jrbDtVencimentoMouseClicked
        // TODO add your handling code here:


    }//GEN-LAST:event_jrbDtVencimentoMouseClicked

    private void jrbDtEmissaoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jrbDtEmissaoMouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_jrbDtEmissaoMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnGerar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JComboBox<String> cboClassificacao;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jrbDtEmissao;
    private javax.swing.JRadioButton jrbDtVencimento;
    private javax.swing.JFormattedTextField txtdatafinal;
    private javax.swing.JFormattedTextField txtdatainicial;
    // End of variables declaration//GEN-END:variables

}
