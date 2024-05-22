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
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author Rafael Veiga
 */
public class TelaVendedor extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    /**
     * Creates new form TelaVendedor
     */
    public TelaVendedor() {
        initComponents();
        conexao = ModuloConexao.conector();
    }

    private void limpacampos() {
        txtCodVendedor.setText(null);
        txtFoneVendedor.setText(null);
        txtNomeVendedor.setText(null);
        txtDemissaovendedor.setText(null);

    }

    private void liberacampos() {
        txtNomeVendedor.requestFocus();
        txtFoneVendedor.setEnabled(true);
        txtNomeVendedor.setEnabled(true);
        txtDemissaovendedor.setEnabled(true);
        cboDemitido.setEnabled(true);
        btnIncluir.setEnabled(false);
        btnAlterar.setEnabled(false);
    }

    private void bloqueiacampos() {
        txtFoneVendedor.setEnabled(false);
        txtNomeVendedor.setEnabled(false);
        txtDemissaovendedor.setEnabled(false);
        cboDemitido.setEnabled(false);
        btnIncluir.setEnabled(true);
        btnAlterar.setEnabled(true);
    }

    private void incluir() {
        String inserir = "insert into vendedores (nomeVendedor,foneVendedor,usuarios_id,empresa_codempresa) values (?,?,?,?)";
        try {
            pst = conexao.prepareStatement(inserir);
            pst.setString(1, txtNomeVendedor.getText());
            pst.setString(2, txtFoneVendedor.getText());
            pst.setString(3, TelaPrincipal.lblCodUsoPrincipal.getText());
            pst.setString(4, TelaPrincipal.lblcodEmpresa.getText());

            if ((txtNomeVendedor.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha os campos obrigatórios.");

            } else {
                int adiciona = pst.executeUpdate();
                if (adiciona > 0) {

                    JOptionPane.showMessageDialog(null, "Vendedor adicionado com sucesso.");
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

    private void alteravendor() {
        String inserir = "update vendedores set nomeVendedor = ?, foneVendedor = ?,usuarios_id=?,demitido=?,dtdemissao=?,empresa_codempresa=? "
                + "where codVendedor = ? ";
        try {
            pst = conexao.prepareStatement(inserir);
            pst.setString(1, txtNomeVendedor.getText());
            pst.setString(2, txtFoneVendedor.getText());
            pst.setString(3, TelaPrincipal.lblCodUsoPrincipal.getText());
            pst.setString(4, cboDemitido.getSelectedItem().toString());
            pst.setString(5, txtDemissaovendedor.getText());
            pst.setString(6, TelaPrincipal.lblcodEmpresa.getText());
            pst.setString(7, txtCodVendedor.getText());
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

    private void consultaAvancada() {
        String sql = "select codVendedor as'Código', nomeVendedor as 'Nome',foneVendedor as 'Telefone',demitido as 'Demitido', "
                + "dtdemissao as 'Data' from vendedores where nomevendedor like ?";
        try {
            pst = conexao.prepareStatement(sql);
            // Passando o conteudo do campo pesquisar para o SQL
            pst.setString(1, "%" + txtPesquisaVendedor.getText() + "%");
            rs = pst.executeQuery();

            // A linha abixo usa a rs2xml.jar 
            tblVendedor.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    public void setarcampos() {

        int setar = tblVendedor.getSelectedRow();
        txtCodVendedor.setText(tblVendedor.getModel().getValueAt(setar, 0).toString());
        txtNomeVendedor.setText(tblVendedor.getModel().getValueAt(setar, 1).toString());
        txtFoneVendedor.setText(tblVendedor.getModel().getValueAt(setar, 2).toString());
        cboDemitido.setSelectedItem(tblVendedor.getModel().getValueAt(setar, 3));
        String habilidademitido = cboDemitido.getSelectedItem().toString();
        
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
        jLabel2 = new javax.swing.JLabel();
        txtCodVendedor = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtNomeVendedor = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtFoneVendedor = new javax.swing.JTextField();
        btnIncluir = new javax.swing.JButton();
        btnAlterar = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblVendedor = new javax.swing.JTable();
        txtPesquisaVendedor = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cboDemitido = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        txtDemissaovendedor = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();

        jLabel1.setText("jLabel1");

        setClosable(true);
        setIconifiable(true);
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

        jLabel2.setText("Codigo: ");

        txtCodVendedor.setEnabled(false);
        txtCodVendedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodVendedorActionPerformed(evt);
            }
        });

        jLabel3.setText("*Nome:");

        txtNomeVendedor.setEnabled(false);
        txtNomeVendedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNomeVendedorKeyPressed(evt);
            }
        });

        jLabel4.setText("Fone:");

        txtFoneVendedor.setEnabled(false);
        txtFoneVendedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtFoneVendedorKeyPressed(evt);
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

        tblVendedor = new javax.swing.JTable(){
            public boolean  isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        tblVendedor.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Codigo", "Nome", "Telefone", "Demissão"
            }
        ));
        tblVendedor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblVendedorMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblVendedor);

        txtPesquisaVendedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPesquisaVendedorKeyReleased(evt);
            }
        });

        jLabel5.setText("Pesquisa:");

        jLabel6.setText("Demissão:");

        cboDemitido.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Não", "Sim" }));
        cboDemitido.setEnabled(false);
        cboDemitido.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cboDemitidoMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                cboDemitidoMousePressed(evt);
            }
        });

        jButton1.setText("Cancelar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        txtDemissaovendedor.setEnabled(false);

        jLabel7.setText("Data:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 345, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(txtPesquisaVendedor))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(btnIncluir)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAlterar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSalvar))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel3)
                                        .addComponent(jLabel2))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(9, 9, 9)
                                        .addComponent(jLabel4))))
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(cboDemitido, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtDemissaovendedor))
                            .addComponent(txtCodVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNomeVendedor, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                            .addComponent(txtFoneVendedor))))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtCodVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(txtNomeVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtFoneVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(cboDemitido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDemissaovendedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(42, 42, 42)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnIncluir)
                    .addComponent(jButton1)
                    .addComponent(btnAlterar)
                    .addComponent(btnSalvar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtPesquisaVendedor, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnIncluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIncluirActionPerformed
        // TODO add your handling code here:
        
        limpacampos();
        liberacampos();
        btnIncluir.setEnabled(false);
        String habilidademitido = cboDemitido.getSelectedItem().toString();
       
    }//GEN-LAST:event_btnIncluirActionPerformed

    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarActionPerformed
        // TODO add your handling code here:

        String codigovend = txtCodVendedor.getText();
        if (codigovend.equals("")) {
            JOptionPane.showMessageDialog(null, "Selecione um vendedor");
        } else {
            liberacampos();
        }

    }//GEN-LAST:event_btnAlterarActionPerformed

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
        // TODO add your handling code here:
        String cod = txtCodVendedor.getText();

        if (cod.equals("")) {
            incluir();
            
        } else {
            alteravendor();
            
        }
    }//GEN-LAST:event_btnSalvarActionPerformed

    private void tblVendedorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblVendedorMouseClicked
        // TODO add your handling code here:
        setarcampos();
    }//GEN-LAST:event_tblVendedorMouseClicked

    private void txtPesquisaVendedorKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesquisaVendedorKeyReleased
        // TODO add your handling code here:
        consultaAvancada();
    }//GEN-LAST:event_txtPesquisaVendedorKeyReleased

    private void txtCodVendedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodVendedorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodVendedorActionPerformed

    private void cboDemitidoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cboDemitidoMouseClicked
        // TODO add your handling code here:
       
    }//GEN-LAST:event_cboDemitidoMouseClicked

    private void cboDemitidoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cboDemitidoMousePressed
        // TODO add your handling code here:
      
    }//GEN-LAST:event_cboDemitidoMousePressed

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        // TODO add your handling code here:

    }//GEN-LAST:event_formInternalFrameOpened

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        bloqueiacampos();
        limpacampos();
        btnIncluir.setEnabled(true);
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtNomeVendedorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNomeVendedorKeyPressed
        // TODO add your handling code here:
            if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
           txtFoneVendedor.requestFocus();
//aqui vai o q voce deseja fazer quando o usuario clicar enter naquele jtextfield
          }
    }//GEN-LAST:event_txtNomeVendedorKeyPressed

    private void txtFoneVendedorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFoneVendedorKeyPressed
        // TODO add your handling code here:
            if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
           btnSalvar.requestFocus();
//aqui vai o q voce deseja fazer quando o usuario clicar enter naquele jtextfield
          }
    }//GEN-LAST:event_txtFoneVendedorKeyPressed

    private void btnSalvarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnSalvarKeyPressed
        // TODO add your handling code here:
            if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
           incluir();
//aqui vai o q voce deseja fazer quando o usuario clicar enter naquele jtextfield
          }
    }//GEN-LAST:event_btnSalvarKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAlterar;
    private javax.swing.JButton btnIncluir;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JComboBox<String> cboDemitido;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblVendedor;
    private javax.swing.JTextField txtCodVendedor;
    private javax.swing.JTextField txtDemissaovendedor;
    private javax.swing.JTextField txtFoneVendedor;
    private javax.swing.JTextField txtNomeVendedor;
    private javax.swing.JTextField txtPesquisaVendedor;
    // End of variables declaration//GEN-END:variables
}
