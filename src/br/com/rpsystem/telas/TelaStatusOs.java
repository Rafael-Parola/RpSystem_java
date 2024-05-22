/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package br.com.rpsystem.telas;

import br.com.rpsystem.dal.ModuloConexao;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author Rafael Veiga
 */
public class TelaStatusOs extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    /**
     * Creates new form TelaGrupo
     */
    public TelaStatusOs() {
        initComponents();
        conexao = ModuloConexao.conector();

    }

    private void limpacampos() {
        txtdescricao.setText(null);
        txtCod.setText(null);
        txtPesquisaGrup.setText(null);
    }

    private void liberacampos() {
        txtdescricao.setEnabled(true);
        btnIncluir.setEnabled(false);
        btnAlterar.setEnabled(false);
        btnExcluir.setEnabled(false);
        //  txtCodGrupo.setEnabled(true);
    }

    private void bloqueiacampos() {
        txtdescricao.setEnabled(false);
        btnIncluir.setEnabled(true);
        btnAlterar.setEnabled(true);
        btnExcluir.setEnabled(true);
        //   txtCodGrupo.setEnabled(false);
    }

    private void incluir() {
        String inserir = "insert into statusOs (descricao,usuarios_id,empresa_codempresa) values (?,?,?)";
        try {
            pst = conexao.prepareStatement(inserir);
            pst.setString(1, txtdescricao.getText());
            pst.setString(2, TelaPrincipal.lblCodUsoPrincipal.getText());
            pst.setString(3, TelaPrincipal.lblcodEmpresa.getText());
            if (txtdescricao.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Preencha a descrição.");
            } else {

                int adiciona = pst.executeUpdate();
                if (adiciona > 0) {

                    JOptionPane.showMessageDialog(null, "Adicionado com sucesso.");
                    btnIncluir.setEnabled(true);
                    limpacampos();
                    bloqueiacampos();
                    consultaAvancada();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void alterar() {
        String inserir = "update statusOs set descricao = ?,usuarios_id = ?,empresa_codempresa=? where codstatus = ? ";
        try {
            pst = conexao.prepareStatement(inserir);
            pst.setString(1, txtdescricao.getText());
            pst.setString(2, TelaPrincipal.lblCodUsoPrincipal.getText());
            pst.setString(3, TelaPrincipal.lblcodEmpresa.getText());
            pst.setString(4, txtCod.getText());
            int adiciona = pst.executeUpdate();
            if (adiciona > 0) {

                JOptionPane.showMessageDialog(null, "Alterado com sucesso");
                btnIncluir.setEnabled(true);
                limpacampos();
                bloqueiacampos();
                consultaAvancada();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void excluistatusOS() {
        String excluigrupo = "update statusOS set excluido = 1,usuarios_id =?,empresa_codempresa=? where codstatus = ?";
        try {

            int exclui = JOptionPane.showConfirmDialog(null, "Tem Certeza que deseja excluir?", "Alerta", JOptionPane.YES_NO_OPTION);
            if (exclui == JOptionPane.YES_OPTION) {
                pst = conexao.prepareStatement(excluigrupo);
                pst.setString(1, TelaPrincipal.lblCodUsoPrincipal.getText());
                pst.setString(2, TelaPrincipal.lblcodEmpresa.getText());
                pst.setString(3, txtCod.getText());

                int excluilogico = pst.executeUpdate();
                if (excluilogico > 0) {

                    JOptionPane.showMessageDialog(null, "Excluido com sucesso");

                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void consultaAvancada() {
        String sql = "select codstatus as 'Cod.', descricao as 'Descrição'  from statusOS where descricao like ? and excluido != 1";
        try {
            pst = conexao.prepareStatement(sql);
            // Passando o conteudo do campo pesquisar para o SQL
            pst.setString(1, "%" + txtPesquisaGrup.getText() + "%");
            rs = pst.executeQuery();

            // A linha abixo usa a rs2xml.jar 
            tblPesquisaGrupo.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    public void setarcampos() {

        int setar = tblPesquisaGrupo.getSelectedRow();
        txtCod.setText(tblPesquisaGrupo.getModel().getValueAt(setar, 0).toString());
        txtdescricao.setText(tblPesquisaGrupo.getModel().getValueAt(setar, 1).toString());

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
        txtCod = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtdescricao = new javax.swing.JTextField();
        btnIncluir = new javax.swing.JButton();
        btnAlterar = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPesquisaGrupo = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        txtPesquisaGrup = new javax.swing.JTextField();
        btnCancelar = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("Cadastro de Status");

        jLabel1.setText("Codigo:");

        txtCod.setEnabled(false);

        jLabel2.setText("Descrição:");

        txtdescricao.setEnabled(false);
        txtdescricao.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtdescricaoKeyPressed(evt);
            }
        });

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
        });

        btnExcluir.setText("Excluir");
        btnExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcluirActionPerformed(evt);
            }
        });

        tblPesquisaGrupo = new javax.swing.JTable(){
            public boolean  isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        tblPesquisaGrupo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Cod.", "Descrição"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblPesquisaGrupo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPesquisaGrupoMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblPesquisaGrupo);

        jLabel3.setText("Pesquisar:");

        txtPesquisaGrup.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPesquisaGrupKeyReleased(evt);
            }
        });

        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addComponent(txtPesquisaGrup))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel1))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtCod, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtdescricao, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnIncluir)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnAlterar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSalvar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnExcluir)
                                .addGap(3, 3, 3)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancelar)))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtCod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtdescricao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 82, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnIncluir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnAlterar)
                        .addComponent(btnSalvar)
                        .addComponent(btnExcluir)
                        .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtPesquisaGrup, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
        // TODO add your handling code here:
        String codigo = txtCod.getText();
        if (codigo.equals("")) {
            incluir();

        } else {
            alterar();

        }
    }//GEN-LAST:event_btnSalvarActionPerformed

    private void txtPesquisaGrupKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesquisaGrupKeyReleased
        // TODO add your handling code here:
        consultaAvancada();
    }//GEN-LAST:event_txtPesquisaGrupKeyReleased

    private void tblPesquisaGrupoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPesquisaGrupoMouseClicked
        // TODO add your handling code here:
        setarcampos();
    }//GEN-LAST:event_tblPesquisaGrupoMouseClicked

    private void btnIncluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIncluirActionPerformed
        // TODO add your handling code here:
        liberacampos();
        btnIncluir.setEnabled(false);
    }//GEN-LAST:event_btnIncluirActionPerformed

    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarActionPerformed
        // TODO add your handling code here:
        String codigo = txtCod.getText();
        if (codigo.equals("")) {
            JOptionPane.showMessageDialog(null, "Selecione um registro");
            consultaAvancada();

        } else {
            liberacampos();
            btnIncluir.setEnabled(false);
        }

    }//GEN-LAST:event_btnAlterarActionPerformed

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
        // TODO add your handling code here:
        String codigo = txtCod.getText();
        if (codigo.equals("")) {
            JOptionPane.showMessageDialog(null, "Selecione um registro");
            consultaAvancada();

        } else {
            excluistatusOS();
            consultaAvancada();
            limpacampos();
        }


    }//GEN-LAST:event_btnExcluirActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // TODO add your handling code here:
        limpacampos();
        bloqueiacampos();
        btnIncluir.setEnabled(true);
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void txtdescricaoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtdescricaoKeyPressed
        // TODO add your handling code here:
          if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
           btnSalvar.requestFocus();
//aqui vai o q voce deseja fazer quando o usuario clicar enter naquele jtextfield
          }
    }//GEN-LAST:event_txtdescricaoKeyPressed

    private void btnSalvarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnSalvarKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
        incluir();
//aqui vai o q voce deseja fazer quando o usuario clicar enter naquele jtextfield
          }
    }//GEN-LAST:event_btnSalvarKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAlterar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnIncluir;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblPesquisaGrupo;
    private javax.swing.JTextField txtCod;
    private javax.swing.JTextField txtPesquisaGrup;
    private javax.swing.JTextField txtdescricao;
    // End of variables declaration//GEN-END:variables
}
