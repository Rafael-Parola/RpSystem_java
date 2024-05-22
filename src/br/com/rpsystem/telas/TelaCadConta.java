/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package br.com.rpsystem.telas;

import br.com.rpsystem.dal.ModuloConexao;
import br.com.rpsystem.funcoes.CentralizaForm;
import br.com.rpsystem.funcoes.GeraLog;
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
public class TelaCadConta extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    Object[] options = {"Sim", "Não"};
    public int codigo;
    int exibetelainicial;

    /**
     * Creates new form TelaCadastroCartoes
     */
    public TelaCadConta() {
        initComponents();

        conexao = ModuloConexao.conector();
        consultaAvancada();
    }

    public void liberacampos() {
        // txtCodFornecedor.setEnabled(true);
        txtNomeConta.setEnabled(true);
        btnIncluir.setEnabled(false);
        btnAlterar.setEnabled(false);
        btnExcluir.setEnabled(false);
        cboTipoConta.setEnabled(true);
        chkBoxTelaInicial.setEnabled(true);
        txtNomeConta.requestFocus();
         
            if (cboTipoConta.getSelectedItem().toString().equals("Dinheiro")) {
                txtCodFornecedor.setEnabled(false);
            }else{
                txtCodFornecedor.setEnabled(true);
            }

    }

    public void bloqueiacampo() {
        txtCodFornecedor.setEnabled(false);
        txtNomeConta.setEnabled(false);
        btnIncluir.setEnabled(true);
        btnAlterar.setEnabled(true);
        btnExcluir.setEnabled(true);
        cboTipoConta.setEnabled(false);
        chkBoxTelaInicial.setEnabled(false);

    }

    public void limpacampos() {
        txtCod.setText("");
        txtCodFornecedor.setText("");
        txtNomeFornecedor.setText("");
        txtNomeConta.setText("");
        chkBoxTelaInicial.setSelected(false);

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

    private void insereconta() {
        String forncedor = txtCodFornecedor.getText();
        String sql = "INSERT INTO `cadastrocontas` (`codigo`,`nome`,`tipo`,`codfornecedor`,`exibetelaincial`,`codusuario`,`codempresa`) \n"
                + "select 1+ coalesce((select max(codigo)from cadastrocontas), 0),?,?,?,?,?,?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtNomeConta.getText());
            pst.setString(2, cboTipoConta.getSelectedItem().toString());
            //pst.setString(3, txtCodFornecedor.getText().replace("", "0"));
            if (forncedor.equals("")) {
                pst.setString(3, null);

            } else {
                pst.setString(3, forncedor);
            }

            pst.setInt(4, exibetelainicial);
            pst.setInt(5, Integer.parseInt(TelaPrincipal.lblCodUsoPrincipal.getText()));
            pst.setInt(6, Integer.parseInt(TelaPrincipal.lblcodEmpresa.getText()));

            if (txtNomeConta.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Preencha o nome da conta.");
            } else {
                int adiciona = pst.executeUpdate();
                if (adiciona > 0) {

                    JOptionPane.showMessageDialog(null, "Cadastrado com sucesso");
                    bloqueiacampo();
                    limpacampos();
                    consultaAvancada();
                }

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void consultaAvancada() {
        String sql = "select codigo,nome,tipo from cadastrocontas where nome like ? and codempresa = ? and status = 0";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, "%" + txtPesquisa.getText() + "%");
            pst.setInt(2, Integer.parseInt(TelaPrincipal.lblcodEmpresa.getText()));
            rs = pst.executeQuery();
            tblPesquisa.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void setacampos() {
        String sql = "select * from cadastrocontas where codigo = ? and codempresa = ?";
        int seta = tblPesquisa.getSelectedRow();
        txtCod.setText(tblPesquisa.getModel().getValueAt(seta, 0).toString());
        String codigo = txtCod.getText();
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, codigo);
            pst.setString(2, TelaPrincipal.lblcodEmpresa.getText());
            rs = pst.executeQuery();
            if (rs.next()) {
                txtNomeConta.setText(rs.getString(2));
                cboTipoConta.setSelectedItem(rs.getString(3).toString());
                
                txtCodFornecedor.setText(rs.getString(4));
                txtCodFornecedor.setEnabled(false);
                String telaincial = rs.getString(5);
                //System.out.println(telaincial);
                if (telaincial.equals("1")) {
                    chkBoxTelaInicial.setSelected(true);
                    exibetelainicial = 1;
                } else {
                    exibetelainicial = 0;
                }
                lblSaldoConta.setText(rs.getString(10));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void alteraconta() {
        String forncedor = txtCodFornecedor.getText();

        String sql = "update cadastrocontas set nome = ?,tipo=?,codfornecedor=?,exibetelaincial=?,codusuario=?"
                + " where codigo = ? and codempresa = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtNomeConta.getText());
            pst.setString(2, cboTipoConta.getSelectedItem().toString());

            //pst.setInt(3, Integer.parseInt(txtCodFornecedor.getText()));
            if (forncedor.equals("")) {
                pst.setString(3, null);

            } else {
                pst.setString(3, forncedor);
                preenche_fornecedor();
            }

            pst.setInt(4, exibetelainicial);
            pst.setInt(5, Integer.parseInt(TelaPrincipal.lblCodUsoPrincipal.getText()));

            pst.setString(6, txtCod.getText());
            pst.setString(7, TelaPrincipal.lblcodEmpresa.getText());

            int altera = pst.executeUpdate();
            if (altera > 0) {
                JOptionPane.showMessageDialog(null, "Alterado com sucesso");
                bloqueiacampo();
                consultaAvancada();

                GeraLog bkp = new GeraLog("Tela cadcartoes", TelaPrincipal.lblCodUsoPrincipal.getText().concat(" - ").concat(TelaPrincipal.lblUsuario.getText()), "Alterou cadastro de contas", TelaPrincipal.lblcodEmpresa.getText(), txtCod.getText());
                bkp.gravaBackup();
            } else {
                JOptionPane.showMessageDialog(null, "Erro ao alterar");

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void excluiConta() {
        String sql = "update cadastrocontas set status = 1 where codigo = ? and codempresa = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtCod.getText());
            pst.setString(2, TelaPrincipal.lblcodEmpresa.getText());
            int exclui = pst.executeUpdate();
            if (exclui > 0) {
                JOptionPane.showMessageDialog(null, "Excluido com sucesso");
                limpacampos();
                bloqueiacampo();
                consultaAvancada();

//                GeraLog bkp = new GeraLog("Tela cadcartoes", TelaPrincipal.lblCodUsoPrincipal.getText().concat(" - ").concat(TelaPrincipal.lblUsuario.getText()), "Excluiu a conta", TelaPrincipal.lblcodEmpresa.getText(), txtCod.getText());
//                bkp.gravaBackup();
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

        jLabel1 = new javax.swing.JLabel();
        txtCod = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtNomeFornecedor = new javax.swing.JTextField();
        txtCodFornecedor = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtNomeConta = new javax.swing.JTextField();
        btnCancelar = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        btnIncluir = new javax.swing.JButton();
        btnAlterar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPesquisa = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        txtPesquisa = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        cboTipoConta = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        lblSaldoConta = new javax.swing.JLabel();
        chkBoxTelaInicial = new javax.swing.JCheckBox();

        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setTitle("Cadastro de contas");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/rpsystem/icones/conta.png"))); // NOI18N

        jLabel1.setVisible(false);
        jLabel1.setText("Cod.:");

        txtCod.setVisible(false);

        jLabel2.setText("Fornecedor (F3):");

        txtNomeFornecedor.setEnabled(false);
        txtNomeFornecedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNomeFornecedorActionPerformed(evt);
            }
        });

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
        });

        jLabel3.setText("Nome da conta:");

        txtNomeConta.setEnabled(false);
        txtNomeConta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNomeContaKeyPressed(evt);
            }
        });

        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
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

        tblPesquisa = new javax.swing.JTable(){
            public boolean  isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        tblPesquisa.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Codigo", "Nome", "Dia Fechamento", "Dia Vencimento"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
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
        tblPesquisa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblPesquisaKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tblPesquisa);

        jLabel6.setText("Pesquisa:");

        txtPesquisa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPesquisaKeyReleased(evt);
            }
        });

        jLabel8.setText("Tipo:");

        cboTipoConta.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Dinheiro", "Conta corrente", "Poupança", "Investimentos", "Outros" }));
        cboTipoConta.setEnabled(false);
        cboTipoConta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboTipoContaActionPerformed(evt);
            }
        });
        cboTipoConta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cboTipoContaKeyPressed(evt);
            }
        });

        jLabel4.setText("Saldo da Conta:");

        lblSaldoConta.setText("0,00");

        chkBoxTelaInicial.setText("Exibe saldo na tela incial");
        chkBoxTelaInicial.setEnabled(false);
        chkBoxTelaInicial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkBoxTelaInicialActionPerformed(evt);
            }
        });
        chkBoxTelaInicial.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                chkBoxTelaInicialKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(txtCod, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(lblSaldoConta, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(62, 62, 62))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(txtCodFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtNomeFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(chkBoxTelaInicial))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addComponent(txtNomeConta, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel8)
                                .addGap(18, 18, 18)
                                .addComponent(cboTipoConta, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 11, Short.MAX_VALUE)))
                .addContainerGap(11, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(18, 18, 18)
                        .addComponent(txtPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnIncluir)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnAlterar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnSalvar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCancelar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnExcluir)))
                .addGap(89, 89, 89))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtCod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(lblSaldoConta))
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel3)
                    .addComponent(txtNomeConta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboTipoConta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtNomeFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCodFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkBoxTelaInicial, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnIncluir)
                    .addComponent(btnCancelar)
                    .addComponent(btnSalvar)
                    .addComponent(btnAlterar)
                    .addComponent(btnExcluir))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setBounds(0, 0, 631, 396);
    }// </editor-fold>//GEN-END:initComponents

    private void btnIncluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIncluirActionPerformed
        // TODO add your handling code here:
        limpacampos();
        liberacampos();

    }//GEN-LAST:event_btnIncluirActionPerformed

    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarActionPerformed
        // TODO add your handling code here:
        String vazio = txtCod.getText();
        if (vazio.equals("")) {
            JOptionPane.showMessageDialog(null, "Selecione uma conta");
        } else {
            liberacampos();

        }
    }//GEN-LAST:event_btnAlterarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // TODO add your handling code here:

        bloqueiacampo();
        limpacampos();

    }//GEN-LAST:event_btnCancelarActionPerformed

    private void txtNomeFornecedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeFornecedorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNomeFornecedorActionPerformed

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
        // TODO add your handling code here:
        String vazio = txtCod.getText();
        if (vazio.equals("")) {
            insereconta();

        } else {
            alteraconta();
        }
    }//GEN-LAST:event_btnSalvarActionPerformed

    private void txtCodFornecedorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodFornecedorKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_F3) {
            TelaPesquisa pesquisa = new TelaPesquisa();
            TelaPrincipal.Desktop.add(pesquisa);

            pesquisa.setVisible(true);
            CentralizaForm c = new CentralizaForm(pesquisa);
            c.centralizaForm();
            pesquisa.toFront();

            pesquisa.setTitle("");
            pesquisa.lblOrigem.setText("conta");
        }
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            preenche_fornecedor();
            chkBoxTelaInicial.requestFocus();
        }


    }//GEN-LAST:event_txtCodFornecedorKeyPressed

    private void tblPesquisaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblPesquisaKeyReleased
        // TODO add your handling code here:

    }//GEN-LAST:event_tblPesquisaKeyReleased

    private void txtPesquisaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesquisaKeyReleased
        // TODO add your handling code here:
        consultaAvancada();
    }//GEN-LAST:event_txtPesquisaKeyReleased

    private void tblPesquisaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPesquisaMouseClicked
        // TODO add your handling code here:
        setacampos();
    }//GEN-LAST:event_tblPesquisaMouseClicked

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
        // TODO add your handling code here:
        String vazio = txtCod.getText();
        if (vazio.equals("")) {
            JOptionPane.showMessageDialog(null, "Selecione uma conta");
        } else {
            excluiConta();
        }
    }//GEN-LAST:event_btnExcluirActionPerformed

    private void cboTipoContaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboTipoContaActionPerformed
        // TODO add your handling code here:
        if (cboTipoConta.getSelectedItem().toString().equals("Dinheiro")) {
            txtCodFornecedor.setEnabled(false);

        } else {
            txtCodFornecedor.setEnabled(true);
        }
    }//GEN-LAST:event_cboTipoContaActionPerformed

    private void cboTipoContaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cboTipoContaKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (cboTipoConta.getSelectedItem().toString().equals("Dinheiro")) {
                chkBoxTelaInicial.requestFocus();
            } else {
                txtCodFornecedor.requestFocus();
            }
        }

    }//GEN-LAST:event_cboTipoContaKeyPressed

    private void txtNomeContaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNomeContaKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cboTipoConta.requestFocus();
        }

    }//GEN-LAST:event_txtNomeContaKeyPressed

    private void txtCodFornecedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodFornecedorActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_txtCodFornecedorActionPerformed

    private void chkBoxTelaInicialKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_chkBoxTelaInicialKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnSalvar.requestFocus();
        }

    }//GEN-LAST:event_chkBoxTelaInicialKeyPressed

    private void btnSalvarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnSalvarKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String vazio = txtCod.getText();
            if (vazio.equals("")) {
                insereconta();

            } else {
                alteraconta();
            }
        }

    }//GEN-LAST:event_btnSalvarKeyPressed

    private void chkBoxTelaInicialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkBoxTelaInicialActionPerformed
        // TODO add your handling code here:

        if (chkBoxTelaInicial.isSelected()) {
            exibetelainicial = 1;
            System.out.println(exibetelainicial);
        } else {
            exibetelainicial = 0;
            System.out.println(exibetelainicial);

        }
    }//GEN-LAST:event_chkBoxTelaInicialActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAlterar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnIncluir;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JComboBox<String> cboTipoConta;
    private javax.swing.JCheckBox chkBoxTelaInicial;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblSaldoConta;
    private javax.swing.JTable tblPesquisa;
    private javax.swing.JTextField txtCod;
    public static javax.swing.JTextField txtCodFornecedor;
    private javax.swing.JTextField txtNomeConta;
    public static javax.swing.JTextField txtNomeFornecedor;
    private javax.swing.JTextField txtPesquisa;
    // End of variables declaration//GEN-END:variables
}
