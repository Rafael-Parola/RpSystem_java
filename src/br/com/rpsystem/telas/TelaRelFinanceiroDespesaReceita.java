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
public class TelaRelFinanceiroDespesaReceita extends javax.swing.JInternalFrame {

    ModuloConexao conexao = new ModuloConexao();

    java.sql.Date dtpgto = null;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    String tipo = "Pagamento";

    String pagos;

    java.sql.Date datainip = null;
    Date dtinip = null;

    java.sql.Date datafimp = null;
    Date dtfimp = null;

    java.sql.Date dataemissaoini = null;
    Date dtemiini = null;

    java.sql.Date dataemissaofim = null;
    Date dtemifim = null;

    java.sql.Date datavenciemntoini = null;
    Date dtvencini = null;

    java.sql.Date datavenciemntofim = null;
    Date dtvencfim = null;

    PreparedStatement pst = null;
    ResultSet rs = null;

    /**
     * Creates new form TelaRelFinanceiroDespesaReceita
     */
    public TelaRelFinanceiroDespesaReceita() {
        initComponents();
        jrbDtpagamento.setSelected(true);
        tipo = "Pagamento";
        jrdTodos.setSelected(true);
        pagos = "Todos";
    }

    public static void generateReport(String tipo, Date dtinicial, Date dtfinal, Date emiini, Date emifim,
            Date vencini, Date vencfim, String pagos) {
        try {

// Definir os parâmetros do relatório
            Map<String, Object> parametros = new HashMap<>();

            parametros.put("tipo", tipo);
            //pagamento
            parametros.put("dtinicial", dtinicial);
            parametros.put("dtfinal", dtfinal);
            //Emissao
            parametros.put("dtemiini", emiini);
            parametros.put("dtemifim", emifim);
            //vencimentodtvencini
            parametros.put("dtvencini", vencini);
            parametros.put("dtvencfim", vencfim);

            parametros.put("MostrarPagos", pagos);

            // Gerar o relatório a partir do arquivo .jasper
            JasperPrint relatorio = JasperFillManager.fillReport(
                    TelaRelFinanceiroDespesaReceita.class.getResourceAsStream("/reports/RelLancamentoFinanceiro.jasper"),
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

        if (tipo.equals("Pagamento")) {
            //System.out.println(tipo);
            if (datainicial.matches("\\d{2}/\\d{2}/\\d{4}")) {

                dtinip = sdf.parse(txtdatainicial.getText());
                datainip = new java.sql.Date(dtinip.getTime());

                dtfimp = sdf.parse(txtdatafinal.getText());
                datafimp = new java.sql.Date(dtfimp.getTime());

                dataemissaoini = null;
                dataemissaofim = null;
                datavenciemntoini = null;
                datavenciemntofim = null;

                //datafim = (java.sql.Date) sdf.parse(datafinal);
            } else {
                datainip = null;
                datafimp = null;

            }

        }
        if (tipo.equals("Emissão")) {
//            System.out.println("caiu no emissao");
//            System.out.println(tipo);

            if (datainicial.matches("\\d{2}/\\d{2}/\\d{4}")) {

                dtemiini = sdf.parse(txtdatainicial.getText());
                dataemissaoini = new java.sql.Date(dtemiini.getTime());

                dtemifim = sdf.parse(txtdatafinal.getText());
                dataemissaofim = new java.sql.Date(dtemifim.getTime());

                datainip = null;
                datafimp = null;
                datavenciemntoini = null;
                datavenciemntofim = null;

                //datafim = (java.sql.Date) sdf.parse(datafinal);
            } else {
                dataemissaoini = null;
                dataemissaoini = null;

            }

        }
        if (tipo.equals("Vencimento")) {
            // System.out.println(tipo);

            if (datainicial.matches("\\d{2}/\\d{2}/\\d{4}")) {

                dtvencini = sdf.parse(txtdatainicial.getText());
                datavenciemntoini = new java.sql.Date(dtvencini.getTime());

                dtvencfim = sdf.parse(txtdatafinal.getText());
                datavenciemntofim = new java.sql.Date(dtvencfim.getTime());

                datainip = null;
                datafimp = null;
                dtemiini = null;
                dtemifim = null;

                //datafim = (java.sql.Date) sdf.parse(datafinal);
            } else {
                datavenciemntoini = null;
                datavenciemntofim = null;

            }
        } else {
            datavenciemntoini = null;
            datavenciemntofim = null;
            dataemissaoini = null;
            dataemissaoini = null;
            datainip = null;
            datafimp = null;
        }

        String tipo = cboTipoLan.getSelectedItem().toString();
        if (tipo.equals("Todos")) {
            tipo = null;
        } else {
            tipo = cboTipoLan.getSelectedItem().toString();
        }

        generateReport(tipo, datainip, datafimp,
                dataemissaoini, dataemissaofim, datavenciemntoini, datavenciemntofim, pagos); // Gerar relatório filtrando com filtros"

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
        jrbDtEmissao = new javax.swing.JRadioButton();
        jrbDtpagamento = new javax.swing.JRadioButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtdatainicial = new javax.swing.JFormattedTextField();
        txtdatafinal = new javax.swing.JFormattedTextField();
        jPanel2 = new javax.swing.JPanel();
        jrdEmAberto = new javax.swing.JRadioButton();
        jrdSomentePagos = new javax.swing.JRadioButton();
        jrdTodos = new javax.swing.JRadioButton();
        cboTipoLan = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();

        jLabel1.setText("jLabel1");

        jRadioButton2.setText("jRadioButton2");

        setTitle("Rel Financeiro");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/rpsystem/icones/report.png"))); // NOI18N

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
        jrbDtVencimento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrbDtVencimentoActionPerformed(evt);
            }
        });

        buttonGroup1.add(jrbDtEmissao);
        jrbDtEmissao.setText("Emissão");
        jrbDtEmissao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrbDtEmissaoActionPerformed(evt);
            }
        });

        buttonGroup1.add(jrbDtpagamento);
        jrbDtpagamento.setText("Pagamento");
        jrbDtpagamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrbDtpagamentoActionPerformed(evt);
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(txtdatainicial, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(19, 19, 19)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtdatafinal, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jrbDtEmissao)
                        .addGap(18, 18, 18)
                        .addComponent(jrbDtVencimento)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jrbDtpagamento)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jrbDtEmissao)
                    .addComponent(jrbDtVencimento)
                    .addComponent(jrbDtpagamento))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel3)
                    .addComponent(txtdatainicial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtdatafinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Filtro:"));

        buttonGroup2.add(jrdEmAberto);
        jrdEmAberto.setText("Em Aberto");
        jrdEmAberto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jrdEmAbertoMouseClicked(evt);
            }
        });
        jrdEmAberto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrdEmAbertoActionPerformed(evt);
            }
        });

        buttonGroup2.add(jrdSomentePagos);
        jrdSomentePagos.setText("Somente pagos");
        jrdSomentePagos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jrdSomentePagosMouseClicked(evt);
            }
        });
        jrdSomentePagos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrdSomentePagosActionPerformed(evt);
            }
        });

        buttonGroup2.add(jrdTodos);
        jrdTodos.setText("Todos");
        jrdTodos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jrdTodosMouseClicked(evt);
            }
        });
        jrdTodos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrdTodosActionPerformed(evt);
            }
        });

        cboTipoLan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos", "Despesas", "Receita" }));
        cboTipoLan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cboTipoLanKeyPressed(evt);
            }
        });

        jLabel4.setText("Tipo:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(cboTipoLan, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jrdSomentePagos)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jrdEmAberto)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jrdTodos)))
                .addContainerGap(36, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jrdSomentePagos)
                    .addComponent(jrdTodos)
                    .addComponent(jrdEmAberto))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboTipoLan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnGerar)
                .addGap(39, 39, 39)
                .addComponent(btnCancelar)
                .addGap(77, 77, 77))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGerar)
                    .addComponent(btnCancelar))
                .addGap(27, 27, 27))
        );

        setBounds(0, 0, 355, 342);
    }// </editor-fold>//GEN-END:initComponents

    private void btnGerarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGerarActionPerformed

        try {
            // TODO add your handling code here:

            rel();
        } catch (ParseException ex) {
            Logger.getLogger(TelaRelFinanceiroDespesaReceita.class.getName()).log(Level.SEVERE, null, ex);
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
                cboTipoLan.requestFocus();

            }
        } else {
            if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                cboTipoLan.requestFocus();
            }
        }

        if (evt.getKeyCode() == KeyEvent.VK_TAB) {
            txtdatafinal.setText("  /  /    ");
            cboTipoLan.requestFocus();
        }
    }//GEN-LAST:event_txtdatafinalKeyPressed

    private void jrbDtpagamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrbDtpagamentoActionPerformed
        // TODO add your handling code here:
        tipo = "Pagamento";
    }//GEN-LAST:event_jrbDtpagamentoActionPerformed

    private void jrbDtVencimentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrbDtVencimentoActionPerformed
        // TODO add your handling code here:
        tipo = "Vencimento";
    }//GEN-LAST:event_jrbDtVencimentoActionPerformed

    private void jrbDtEmissaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrbDtEmissaoActionPerformed
        // TODO add your handling code here:
        tipo = "Emissão";
    }//GEN-LAST:event_jrbDtEmissaoActionPerformed

    private void txtdatainicialKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtdatainicialKeyReleased
        // TODO add your handling code here:

    }//GEN-LAST:event_txtdatainicialKeyReleased

    private void jrdSomentePagosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jrdSomentePagosMouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_jrdSomentePagosMouseClicked

    private void jrdTodosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jrdTodosMouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_jrdTodosMouseClicked

    private void jrdSomentePagosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrdSomentePagosActionPerformed
        // TODO add your handling code here:
        pagos = "Pagos";

    }//GEN-LAST:event_jrdSomentePagosActionPerformed

    private void jrdEmAbertoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jrdEmAbertoMouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_jrdEmAbertoMouseClicked

    private void jrdEmAbertoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrdEmAbertoActionPerformed
        // TODO add your handling code here:
        pagos = "Abertos";

    }//GEN-LAST:event_jrdEmAbertoActionPerformed

    private void jrdTodosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrdTodosActionPerformed
        // TODO add your handling code here:
        pagos = "Todos";


    }//GEN-LAST:event_jrdTodosActionPerformed

    private void cboTipoLanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cboTipoLanKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboTipoLanKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnGerar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JComboBox<String> cboTipoLan;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jrbDtEmissao;
    private javax.swing.JRadioButton jrbDtVencimento;
    public static javax.swing.JRadioButton jrbDtpagamento;
    private javax.swing.JRadioButton jrdEmAberto;
    private javax.swing.JRadioButton jrdSomentePagos;
    private javax.swing.JRadioButton jrdTodos;
    private javax.swing.JFormattedTextField txtdatafinal;
    private javax.swing.JFormattedTextField txtdatainicial;
    // End of variables declaration//GEN-END:variables

}
