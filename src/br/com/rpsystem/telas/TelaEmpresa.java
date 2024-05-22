/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package br.com.rpsystem.telas;

import br.com.rpsystem.dal.ModuloConexao;
import br.com.rpsystem.funcoes.CentralizaForm;
import java.awt.ComponentOrientation;
import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

/**
 *
 * @author rafae
 */
public class TelaEmpresa extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    /**
     * Creates new form TelaEmpresa
     */
    public TelaEmpresa() {
        initComponents();
        initComponents();
        conexao = ModuloConexao.conector();

    }

    public void bloqueiacampos() {
        txtCodEmpresa.setEnabled(false);
        txtRazaoSocial.setEnabled(false);
        txtNomeFantasiaEmpresa.setEnabled(false);
        txtCnpjEmp.setEnabled(false);
        txtInscricaoEmp.setEnabled(false);
        txtEnderecoEmp.setEnabled(false);
        txtNumEmp.setEnabled(false);
        txtCepEmp.setEnabled(false);
        txtComplementoEmp.setEnabled(false);
        txtBairoEmp.setEnabled(false);
        txtCidadeEmp.setEnabled(false);
        cboEstado.setEnabled(false);
        btnAlterar.setEnabled(true);
        btnInclir.setEnabled(true);
        txtFone.setEnabled(false);
                
    }

    public void liberaCampos() {
        txtCodEmpresa.setEnabled(true);
        txtRazaoSocial.setEnabled(true);
        txtNomeFantasiaEmpresa.setEnabled(true);
        txtCnpjEmp.setEnabled(true);
        txtInscricaoEmp.setEnabled(true);
        txtEnderecoEmp.setEnabled(true);
        txtNumEmp.setEnabled(true);
        txtCepEmp.setEnabled(true);
        txtComplementoEmp.setEnabled(true);
        txtBairoEmp.setEnabled(true);
        txtCidadeEmp.setEnabled(true);
        cboEstado.setEnabled(true);
        btnAlterar.setEnabled(false);
        btnInclir.setEnabled(false);
        txtFone.setEnabled(true);
        }

    public void carregaestado() {
        String grupo = "select sigla from estados ";
        try {
            pst = conexao.prepareStatement(grupo);
            rs = pst.executeQuery();
            if (rs.next()) {
                do {
                    cboEstado.addItem(rs.getString("sigla"));
                } while (rs.next());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void alteraempresa() {

        int codemp = Integer.parseInt(txtCodEmpresa.getText());
        String alteraempresa = "UPDATE empresa\n"
                + "SET razaoSocial = ?, fantasia = ?, cnpjcpf = ?, rginscri = ?, usuarios_id = ?, endereco = ?,\n"
                + "numero = ?, cep = ?, complemento = ?,bairro = ?,cidade = ?,estado = ?,telefone=? WHERE codempresa =?";
        try {
            pst = conexao.prepareStatement(alteraempresa);
            pst.setString(1, txtRazaoSocial.getText());
            pst.setString(2, txtNomeFantasiaEmpresa.getText());
            pst.setString(3, txtCnpjEmp.getText());
            pst.setString(4, txtInscricaoEmp.getText());
            pst.setString(5, TelaPrincipal.lblCodUsoPrincipal.getText());
            pst.setString(6, txtEnderecoEmp.getText());
            pst.setString(7, txtNumEmp.getText());
            pst.setString(8, txtCepEmp.getText());
            pst.setString(9, txtComplementoEmp.getText());
            pst.setString(10, txtBairoEmp.getText());
            pst.setString(11, txtCidadeEmp.getText());
            pst.setString(12, cboEstado.getSelectedItem().toString());
            pst.setString(13, txtFone.getText());
            pst.setInt(14, Integer.valueOf(txtCodEmpresa.getText()));
            if ((txtRazaoSocial.getText().isEmpty()) || (txtNomeFantasiaEmpresa.getText().isEmpty()) || (txtEnderecoEmp.getText().isEmpty()) || (txtNumEmp.getText().isEmpty())
                    || (txtCnpjEmp.getText().isEmpty()) || (txtInscricaoEmp.getText().isEmpty()) || (txtCepEmp.getText().isEmpty())
                    || (txtBairoEmp.getText().isEmpty()) || (txtCidadeEmp.getText().isEmpty()) || (cboEstado.getSelectedItem().toString().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos");

            } else {
                int adiciona = pst.executeUpdate();
                if (adiciona > 0) {
                    JOptionPane.showMessageDialog(null, "Alterado com sucesso");
                    //JOptionPane.showMessageDialog(null, "Necessário encerrar o sistema para alterar os dados, deseja fazer isso agora?");

                    int sair = JOptionPane.showConfirmDialog(null, "Nessário reiniciar o sistema para que as alterações entrem em vigor, deseja fazer isso agora? ", "Atenção", JOptionPane.YES_NO_OPTION);
                    if (sair == JOptionPane.YES_OPTION) {
                        JOptionPane.showMessageDialog(null, "O sistema será encerrado, abra o novamente", "Atenção", JOptionPane.OK_OPTION);

                        System.exit(sair);
                    } else {
                        TelaPrincipal.lblAviso.setText("Existem altearações pendentes, reinicie o sistema");
                        TelaPrincipal.lblAviso.setToolTipText("Encerre o sistema para alterar os dados da empresa !");

                        Thread thread = new Thread(new Runnable() {
                            public void run() {
                                try {
                                    while (true) {
                                        EventQueue.invokeLater(new Runnable() {
                                            public void run() {
                                                TelaPrincipal.lblAviso.setVisible(!TelaPrincipal.lblAviso.isVisible());
                                            }
                                        });
                                        Thread.sleep(1000);
                                    //Tempo em milisegundos para exibir e ocultar a label 
                                    }
                                } catch (InterruptedException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        });
                        thread.setDaemon(true);
                        thread.start();

                        bloqueiacampos();
                    }

                }
            }
        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void incluir() {
        String inserir = "insert into empresa (codempresa, razaoSocial,fantasia,cnpjcfp,rginscr,usuarios_id,`endereco`,\n"
                + "`numero`,\n"
                + "`cep`,\n"
                + "`complemento`,\n"
                + "`bairro`,\n"
                + "`cidade`,\n"
                + "`estado`,"
                + "telefone) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            pst = conexao.prepareStatement(inserir);
            pst.setString(1, txtCodEmpresa.getText());
            pst.setString(2, txtRazaoSocial.getText());
            pst.setString(3, txtNomeFantasiaEmpresa.getText());
            pst.setString(4, txtCnpjEmp.getText());
            pst.setString(5, txtInscricaoEmp.getText());
            pst.setString(6, TelaPrincipal.lblCodUsoPrincipal.getText());
            pst.setString(7, txtEnderecoEmp.getText());
            pst.setString(8, txtNumEmp.getText());
            pst.setString(9, txtCepEmp.getText());
            pst.setString(10, txtComplementoEmp.getText());
            pst.setString(11, txtBairoEmp.getText());
            pst.setString(12, txtCidadeEmp.getText());
            pst.setString(13, cboEstado.getSelectedItem().toString());
            pst.setString(14, cboEstado.getSelectedItem().toString());

            int adiciona = pst.executeUpdate();
            if (adiciona > 0) {
                JOptionPane.showMessageDialog(null, "Alterado com sucesso");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void cunsultaempresa() {
        String sql = "select * from empresa where codempresa =? ";
        try {
            pst = conexao.prepareStatement(sql);
            // pst.setString(1, "%" + txtPesquisaCli.getText() + "%");
            pst.setString(1, TelaPrincipal.lblcodEmpresa.getText());
            rs = pst.executeQuery();
            if (rs.next()) {
                txtCodEmpresa.setText(rs.getString(1));
                txtRazaoSocial.setText(rs.getString(2));
                txtNomeFantasiaEmpresa.setText(rs.getString(3));
                txtCnpjEmp.setText(rs.getString(4));
                txtInscricaoEmp.setText(rs.getString(5));
                txtEnderecoEmp.setText(rs.getString(7));
                txtNumEmp.setText(rs.getString(8));
                txtCepEmp.setText(rs.getString(9));
                txtComplementoEmp.setText(rs.getString(10));
                txtBairoEmp.setText(rs.getString(11));
                txtCidadeEmp.setText(rs.getString(12));

                cboEstado.setSelectedItem(rs.getString(13));

            } else {
                JOptionPane.showMessageDialog(null, "Erro");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
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

        txtCodEmpresa = new javax.swing.JTextField();
        txtCnpjEmp = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtRazaoSocial = new javax.swing.JTextField();
        txtInscricaoEmp = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        btnCancel = new javax.swing.JButton();
        txtNomeFantasiaEmpresa = new javax.swing.JTextField();
        btnSalvar = new javax.swing.JButton();
        btnPesquisa = new javax.swing.JButton();
        btnInclir = new javax.swing.JButton();
        btnAlterar = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        txtEnderecoEmp = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtNumEmp = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtComplementoEmp = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtCepEmp = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtBairoEmp = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtCidadeEmp = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        cboEstado = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtFone = new javax.swing.JTextField();

        setClosable(true);
        setIconifiable(true);
        setTitle("Tela Empresa");
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
                formInternalFrameOpened(evt);
            }
        });

        txtCodEmpresa.setEditable(false);
        txtCodEmpresa.setEnabled(false);

        txtCnpjEmp.setEnabled(false);
        txtCnpjEmp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCnpjEmpActionPerformed(evt);
            }
        });

        jLabel2.setText("Razão Social: ");

        jLabel12.setText("Inscrição Est.:");

        txtRazaoSocial.setEnabled(false);

        txtInscricaoEmp.setEnabled(false);

        jLabel3.setText("Fantasia:");

        btnCancel.setText("Cancelar");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        txtNomeFantasiaEmpresa.setEnabled(false);

        btnSalvar.setText("Salvar");
        btnSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvarActionPerformed(evt);
            }
        });

        btnPesquisa.setText("Pesquisa");
        btnPesquisa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPesquisaActionPerformed(evt);
            }
        });

        btnInclir.setVisible(false);
        btnInclir.setText("Incluir");
        btnInclir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInclirActionPerformed(evt);
            }
        });

        btnAlterar.setText("Alterar");
        btnAlterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlterarActionPerformed(evt);
            }
        });

        jLabel5.setText("Endereço:");

        txtEnderecoEmp.setEnabled(false);

        jLabel6.setText("Numero:");

        txtNumEmp.setEnabled(false);

        jLabel15.setText("Comp:");

        txtComplementoEmp.setEnabled(false);

        jLabel8.setText("CEP:");

        txtCepEmp.setEnabled(false);
        txtCepEmp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCepEmpActionPerformed(evt);
            }
        });

        jLabel7.setText("Bairro:");

        txtBairoEmp.setEnabled(false);

        jLabel9.setText("Cidade:");

        txtCidadeEmp.setEnabled(false);
        txtCidadeEmp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCidadeEmpActionPerformed(evt);
            }
        });

        jLabel10.setText("Estado:");

        cboEstado.setEnabled(false);

        jLabel1.setText("Cod Empresa");

        jLabel11.setText("CNPJ/CPF:");

        jLabel4.setText("Fone:");

        txtFone.setEnabled(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel11)
                                .addComponent(jLabel6)
                                .addComponent(jLabel8)
                                .addComponent(jLabel9))
                            .addGap(18, 18, 18)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(txtCepEmp, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jLabel5)
                                    .addGap(18, 18, 18)
                                    .addComponent(txtEnderecoEmp))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(txtCnpjEmp, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jLabel12)
                                    .addGap(18, 18, 18)
                                    .addComponent(txtInscricaoEmp, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(txtCidadeEmp, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(txtNumEmp, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel15)
                                            .addGap(18, 18, 18)
                                            .addComponent(txtComplementoEmp, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGap(18, 18, 18)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jLabel7)
                                            .addGap(18, 18, 18)
                                            .addComponent(txtBairoEmp))
                                        .addGroup(layout.createSequentialGroup()
                                            .addGap(0, 0, Short.MAX_VALUE)
                                            .addComponent(jLabel10)
                                            .addGap(18, 18, 18)
                                            .addComponent(cboEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(60, 60, 60)
                            .addComponent(btnInclir)
                            .addGap(18, 18, 18)
                            .addComponent(btnSalvar)
                            .addGap(18, 18, 18)
                            .addComponent(btnAlterar)
                            .addGap(18, 18, 18)
                            .addComponent(btnCancel)
                            .addGap(18, 18, 18)
                            .addComponent(btnPesquisa)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtCodEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(txtRazaoSocial))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtNomeFantasiaEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel4)
                                .addGap(18, 18, 18)
                                .addComponent(txtFone, javax.swing.GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE)))))
                .addGap(0, 42, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(txtCodEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(txtRazaoSocial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtNomeFantasiaEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(txtFone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtCnpjEmp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(txtInscricaoEmp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtCepEmp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(txtEnderecoEmp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNumEmp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(txtComplementoEmp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15)
                    .addComponent(jLabel7)
                    .addComponent(txtBairoEmp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtCidadeEmp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel9))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addComponent(jLabel10)
                        .addComponent(cboEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(btnSalvar)
                    .addComponent(btnAlterar)
                    .addComponent(btnCancel)
                    .addComponent(btnPesquisa)
                    .addComponent(btnInclir))
                .addContainerGap(107, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtCnpjEmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCnpjEmpActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCnpjEmpActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        bloqueiacampos();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
        // TODO add your handling code here:
        String codemp = txtCodEmpresa.getText();
        if (codemp.equals("")) {
            incluir();
        } else {
            alteraempresa();
        }

    }//GEN-LAST:event_btnSalvarActionPerformed

    private void btnPesquisaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPesquisaActionPerformed
        // TODO add your handling code here:

        TelaPesquisa pesquisa = new TelaPesquisa();
        TelaPrincipal.Desktop.add(pesquisa);
        pesquisa.setVisible(true);
        CentralizaForm c = new CentralizaForm(pesquisa);
        pesquisa.toFront();
        pesquisa.lblOrigem.setText("EMP");

    }//GEN-LAST:event_btnPesquisaActionPerformed

    private void btnInclirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInclirActionPerformed
        // TODO add your handling code here:
        liberaCampos();
    }//GEN-LAST:event_btnInclirActionPerformed

    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarActionPerformed
        // TODO add your handling code here:
        liberaCampos();
    }//GEN-LAST:event_btnAlterarActionPerformed

    private void txtCepEmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCepEmpActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCepEmpActionPerformed

    private void txtCidadeEmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCidadeEmpActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCidadeEmpActionPerformed

    private void formInternalFrameActivated(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameActivated
        // TODO add your handling code here:

    }//GEN-LAST:event_formInternalFrameActivated

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        // TODO add your handling code here:
        carregaestado();
        cunsultaempresa();
    }//GEN-LAST:event_formInternalFrameOpened


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAlterar;
    private javax.swing.JButton btnCancel;
    public static javax.swing.JButton btnInclir;
    private javax.swing.JButton btnPesquisa;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JComboBox<String> cboEstado;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField txtBairoEmp;
    private javax.swing.JTextField txtCepEmp;
    private javax.swing.JTextField txtCidadeEmp;
    private javax.swing.JTextField txtCnpjEmp;
    public static javax.swing.JTextField txtCodEmpresa;
    private javax.swing.JTextField txtComplementoEmp;
    private javax.swing.JTextField txtEnderecoEmp;
    private javax.swing.JTextField txtFone;
    private javax.swing.JTextField txtInscricaoEmp;
    public static javax.swing.JTextField txtNomeFantasiaEmpresa;
    private javax.swing.JTextField txtNumEmp;
    public static javax.swing.JTextField txtRazaoSocial;
    // End of variables declaration//GEN-END:variables
}
