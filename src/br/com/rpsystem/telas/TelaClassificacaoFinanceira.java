/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package br.com.rpsystem.telas;

import br.com.rpsystem.dal.ModuloConexao;
import static br.com.rpsystem.telas.TelaPrincipal.lblData;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author Rafael Veiga
 */
public class TelaClassificacaoFinanceira extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    /**
     * Creates new form TelaGrupo
     */
    public TelaClassificacaoFinanceira() {
        initComponents();
        conexao = ModuloConexao.conector();

    }

    private void limpacampos() {
        txtDescricao.setText(null);
        txtCod.setText(null);
        txtPesquisa.setText(null);
    }

    private void liberacampos() {
        txtDescricao.setEnabled(true);
        btnAlterar.setEnabled(false);
        btnExcluir.setEnabled(false);
        txtDescricao.requestFocus();
        cboTipo.setEnabled(true);
    }

    private void bloqueiacampos() {
        txtDescricao.setEnabled(false);
        btnAlterar.setEnabled(true);
        btnExcluir.setEnabled(true);
        btnIncluir.requestFocus();
        cboTipo.setEnabled(false);
        //   txtCodGrupo.setEnabled(false);
    }

    private void incluir() {
        String inserir = " INSERT INTO classificacaofinanceira "
                + "  (cod, descricao,usuarios_id,empresa_codempresa,tipo)\n"
                + " SELECT 1 + coalesce((SELECT max(cod) FROM classificacaofinanceira), 0), ?,?,?,?";

        /*"insert into grupos (descricaogrupo,usuarios_id,empresa_codempresa) values (?,?,?)";*/
        try {
            pst = conexao.prepareStatement(inserir);
            pst.setString(1, txtDescricao.getText());
            pst.setString(2, TelaPrincipal.lblCodUsoPrincipal.getText());
            pst.setString(3, TelaPrincipal.lblcodEmpresa.getText());
            
            // Tipo sera implementado futuramente
            pst.setString(4, cboTipo.getSelectedItem().toString());
            //Fim do tippo 
            
            
            ///    pst.setString(4, dataatual.toString());

            if (txtDescricao.getText().isEmpty()) {
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
        String inserir = "update classificacaofinanceira set descricao= ?,usuarios_id =?,empresa_codempresa=?,tipo=? where cod = ? ";
        try {
            pst = conexao.prepareStatement(inserir);
            pst.setString(1, txtDescricao.getText());
            pst.setString(2, TelaPrincipal.lblCodUsoPrincipal.getText());
            pst.setString(3, TelaPrincipal.lblcodEmpresa.getText());
            pst.setString(4, cboTipo.getSelectedItem().toString());
            pst.setString(5, txtCod.getText());
            int adiciona = pst.executeUpdate();
            if (adiciona > 0) {

                JOptionPane.showMessageDialog(null, "Alterado com sucesso");
                btnIncluir.setEnabled(true);
                limpacampos();
                bloqueiacampos();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void excluigrupos() {
        String excluigrupo = "update classificacaofinanceira set excluido = 1,usuarios_id =?,empresa_codempresa= ? where cod = ?";
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
        String sql = "select cod as 'Cod.', descricao as 'Descrição',tipo as 'Tipo' from classificacaofinanceira where descricao like ? and excluido != 1";
        try {
            pst = conexao.prepareStatement(sql);
            // Passando o conteudo do campo pesquisar para o SQL
            pst.setString(1, "%" + txtPesquisa.getText() + "%");
            rs = pst.executeQuery();

            // A linha abixo usa a rs2xml.jar 
            tblPesquisa.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    public void setarcampos() {

        int setar = tblPesquisa.getSelectedRow();
        txtCod.setText(tblPesquisa.getModel().getValueAt(setar, 0).toString());
        txtDescricao.setText(tblPesquisa.getModel().getValueAt(setar, 1).toString());
        cboTipo.setSelectedItem(tblPesquisa.getModel().getValueAt(setar,2));

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
        txtDescricao = new javax.swing.JTextField();
        btnIncluir = new javax.swing.JButton();
        btnAlterar = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPesquisa = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        txtPesquisa = new javax.swing.JTextField();
        btnCancelar = new javax.swing.JButton();
        cboTipo = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setTitle("Cadastro de Classificação");

        jLabel1.setText("Codigo:");

        txtCod.setEnabled(false);

        jLabel2.setText("Descrição:");

        txtDescricao.setEnabled(false);
        txtDescricao.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDescricaoKeyPressed(evt);
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

        tblPesquisa = new javax.swing.JTable(){
            public boolean  isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        tblPesquisa.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Cod.", "Descrição", "Tipo"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblPesquisa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPesquisaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblPesquisa);

        jLabel3.setText("Pesquisar:");

        txtPesquisa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPesquisaKeyReleased(evt);
            }
        });

        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        cboTipo.setVisible(false);
        cboTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "=", "+", "- " }));
        cboTipo.setEnabled(false);

        jLabel4.setVisible(false);
        jLabel4.setText("Tipo:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 24, Short.MAX_VALUE)
                        .addComponent(btnIncluir, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAlterar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSalvar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnExcluir)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancelar))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addComponent(txtPesquisa))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))))
                .addGap(26, 26, 26))
            .addGroup(layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(txtDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(txtCod, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(60, 60, 60)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cboTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel1)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtCod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cboTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel2)
                    .addComponent(txtDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnIncluir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAlterar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSalvar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnExcluir)
                    .addComponent(btnCancelar))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                .addContainerGap())
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

    private void txtPesquisaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesquisaKeyReleased
        // TODO add your handling code here:
        consultaAvancada();
    }//GEN-LAST:event_txtPesquisaKeyReleased

    private void tblPesquisaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPesquisaMouseClicked
        // TODO add your handling code here:
        setarcampos();
    }//GEN-LAST:event_tblPesquisaMouseClicked

    private void btnIncluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIncluirActionPerformed
        // TODO add your handling code here:
        liberacampos();
        btnIncluir.setEnabled(false);

    }//GEN-LAST:event_btnIncluirActionPerformed

    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarActionPerformed
        // TODO add your handling code here:
        String vazio = txtCod.getText();
        if (vazio.equals("")) {
            JOptionPane.showMessageDialog(null, "Selecione um Grupo");

        } else {
            liberacampos();
            btnIncluir.setEnabled(false);
        }


    }//GEN-LAST:event_btnAlterarActionPerformed

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
        // TODO add your handling code here:
        String vazio = txtCod.getText();
        if (vazio.equals("")) {
            JOptionPane.showMessageDialog(null, "Selecione um Grupo");

        } else {
            excluigrupos();
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

    private void txtDescricaoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDescricaoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnSalvar.requestFocus();
//aqui vai o q voce deseja fazer quando o usuario clicar enter naquele jtextfield
        }
    }//GEN-LAST:event_txtDescricaoKeyPressed

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
    private javax.swing.JComboBox<String> cboTipo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblPesquisa;
    private javax.swing.JTextField txtCod;
    private javax.swing.JTextField txtDescricao;
    private javax.swing.JTextField txtPesquisa;
    // End of variables declaration//GEN-END:variables
}
