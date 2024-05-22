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
public class TelaGrupo extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    /**
     * Creates new form TelaGrupo
     */
    public TelaGrupo() {
        initComponents();
        conexao = ModuloConexao.conector();

    }

    private void limpacampos() {
        txtDescricaoGrupo.setText(null);
        txtCodGrupo.setText(null);
        txtPesquisaGrup.setText(null);
    }

    private void liberacampos() {
        txtDescricaoGrupo.setEnabled(true);
        btnAlterar.setEnabled(false);
        btnExcluir.setEnabled(false);
        //  txtCodGrupo.setEnabled(true);
    }

    private void bloqueiacampos() {
        txtDescricaoGrupo.setEnabled(false);
        btnAlterar.setEnabled(true);
        btnExcluir.setEnabled(true);
        btnIncluir.requestFocus();
        //   txtCodGrupo.setEnabled(false);
    }

    private void incluir() {
        String inserir = " INSERT INTO grupos (codgrupos, descricaogrupo,usuarios_id,empresa_codempresa)\n"
                + " SELECT 1 + coalesce((SELECT max(codgrupos) FROM grupos), 0), ?,?,?";

        /*"insert into grupos (descricaogrupo,usuarios_id,empresa_codempresa) values (?,?,?)";*/
        try {
            pst = conexao.prepareStatement(inserir);
            pst.setString(1, txtDescricaoGrupo.getText());
            pst.setString(2, TelaPrincipal.lblCodUsoPrincipal.getText());
            pst.setString(3, TelaPrincipal.lblcodEmpresa.getText());
            ///    pst.setString(4, dataatual.toString());

            if (txtDescricaoGrupo.getText().isEmpty()) {
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
        String inserir = "update grupos set descricaogrupo = ?,usuarios_id =?,empresa_codempresa=? where codgrupos = ? ";
        try {
            pst = conexao.prepareStatement(inserir);
            pst.setString(1, txtDescricaoGrupo.getText());
            pst.setString(2, TelaPrincipal.lblCodUsoPrincipal.getText());
            pst.setString(3, TelaPrincipal.lblcodEmpresa.getText());
            pst.setString(4, txtCodGrupo.getText());
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
        String excluigrupo = "update grupos set excluido = 1,usuarios_id =?,empresa_codempresa= ? where codgrupos = ?";
        try {

            int exclui = JOptionPane.showConfirmDialog(null, "Tem Certeza que deseja excluir?", "Alerta", JOptionPane.YES_NO_OPTION);
            if (exclui == JOptionPane.YES_OPTION) {
                pst = conexao.prepareStatement(excluigrupo);
                pst.setString(1, TelaPrincipal.lblCodUsoPrincipal.getText());
                pst.setString(2, TelaPrincipal.lblcodEmpresa.getText());
                pst.setString(3, txtCodGrupo.getText());

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
        String sql = "select codgrupos as 'Cod.', descricaogrupo as 'Descrição' from grupos where descricaogrupo like ? and excluido != 1";
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
        txtCodGrupo.setText(tblPesquisaGrupo.getModel().getValueAt(setar, 0).toString());
        txtDescricaoGrupo.setText(tblPesquisaGrupo.getModel().getValueAt(setar, 1).toString());

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
        txtCodGrupo = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtDescricaoGrupo = new javax.swing.JTextField();
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
        setTitle("Cadastro de Grupos");

        jLabel1.setText("Codigo:");

        txtCodGrupo.setEnabled(false);

        jLabel2.setText("Descrição:");

        txtDescricaoGrupo.setEnabled(false);
        txtDescricaoGrupo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDescricaoGrupoKeyPressed(evt);
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
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
                                .addComponent(txtPesquisaGrup))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 395, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCodGrupo, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDescricaoGrupo, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(26, 26, 26))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(24, 24, 24)
                        .addComponent(jLabel2))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtCodGrupo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtDescricaoGrupo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(39, 39, 39)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnIncluir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAlterar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSalvar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnExcluir)
                    .addComponent(btnCancelar))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtPesquisaGrup, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
        // TODO add your handling code here:
        String codigo = txtCodGrupo.getText();
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
        String vazio = txtCodGrupo.getText();
        if (vazio.equals("")) {
            JOptionPane.showMessageDialog(null, "Selecione um Grupo");

        } else {
            liberacampos();
            btnIncluir.setEnabled(false);
        }


    }//GEN-LAST:event_btnAlterarActionPerformed

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
        // TODO add your handling code here:
        String vazio = txtCodGrupo.getText();
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

    private void txtDescricaoGrupoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDescricaoGrupoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnSalvar.requestFocus();
//aqui vai o q voce deseja fazer quando o usuario clicar enter naquele jtextfield
        }
    }//GEN-LAST:event_txtDescricaoGrupoKeyPressed

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
    private javax.swing.JTextField txtCodGrupo;
    private javax.swing.JTextField txtDescricaoGrupo;
    private javax.swing.JTextField txtPesquisaGrup;
    // End of variables declaration//GEN-END:variables
}
