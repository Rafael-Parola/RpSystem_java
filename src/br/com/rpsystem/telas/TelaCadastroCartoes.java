/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package br.com.rpsystem.telas;

import br.com.rpsystem.dal.ModuloConexao;
import br.com.rpsystem.funcoes.CentralizaForm;
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
public class TelaCadastroCartoes extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    Object[] options = {"Sim", "Não"};
    public int codigo;

    /**
     * Creates new form TelaCadastroCartoes
     */
    public TelaCadastroCartoes() {
        initComponents();

        conexao = ModuloConexao.conector();
        consultaAvancada();
    }

    public void liberacampos() {
        txtCodFornecedor.setEnabled(true);
        txtNomeCartao.setEnabled(true);
        txtDiaFechamento.setEnabled(true);
        txtDiaVencimento.setEnabled(true);
        btnIncluir.setEnabled(false);
        btnAlterar.setEnabled(false);
        btnExcluir.setEnabled(false);
        txtLimite.setEnabled(true);

    }

    public void bloqueiacampo() {
        txtCodFornecedor.setEnabled(false);
        txtNomeCartao.setEnabled(false);
        txtDiaFechamento.setEnabled(false);
        txtDiaVencimento.setEnabled(false);
        btnIncluir.setEnabled(true);
        btnAlterar.setEnabled(true);
        btnExcluir.setEnabled(true);
        txtLimite.setEnabled(false);

    }

    public void limpacampos() {
        txtCod.setText("");
        txtCodFornecedor.setText("");
        txtNomeFornecedor.setText("");
        txtNomeCartao.setText("");
        txtDiaFechamento.setText("");
        txtDiaVencimento.setText("");
        txtLimite.setText("");

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
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void inserecartao() {
        String sql = "INSERT INTO `cadcartoes`(`codigo`,`nomecartao`,`fornecedor`,`diafechamentofatura`,`diavencimentofatura`,`usuario`,`cod_empresa`,excluido,limite)\n"
                + "select 1+ coalesce((select max(codigo) from cadcartoes ),0),?,?,?,?,?,?,?,?;";

        try {

            pst = conexao.prepareStatement(sql);

            pst.setString(1, txtNomeCartao.getText());
            pst.setInt(2, Integer.parseInt(txtCodFornecedor.getText()));
            pst.setInt(3, Integer.parseInt(txtDiaFechamento.getText()));
            pst.setInt(4, Integer.parseInt(txtDiaVencimento.getText()));
            pst.setInt(5, Integer.parseInt(TelaPrincipal.lblCodUsoPrincipal.getText()));
            pst.setInt(6, Integer.parseInt(TelaPrincipal.lblcodEmpresa.getText()));
            pst.setInt(7, 0);
            pst.setDouble(8, Double.parseDouble(txtLimite.getText().replace(",", ".")));

            if ((txtCodFornecedor.getText().isEmpty()) || (txtNomeCartao.getText().isEmpty()) || (txtDiaFechamento.getText().isEmpty())
                    || (txtDiaVencimento.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos");

            } else {
                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Cadastrado com sucesso");
                    bloqueiacampo();
                    consultaAvancada();
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            System.out.println(e);
        }

    }

    private void consultaAvancada() {
        String sql = "select codigo as Codigo, nomecartao as Nome, diafechamentofatura as 'Dia Fechamento', diavencimentofatura as 'Dia Vencimente' from cadcartoes "
                + "where nomecartao like ? and cod_empresa = ? and excluido =0 order by CODIGO";
        try {
            pst = conexao.prepareStatement(sql);
            // Passando o conteudo do campo pesquisar para o SQL
            pst.setString(1, "%" + txtPesquisa.getText() + "%");
            pst.setString(2, TelaPrincipal.lblcodEmpresa.getText());
            rs = pst.executeQuery();
            // A linha abixo usa a rs2xml.jar 
            tblPesquisa.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void setacampos() {
        String sql = "select * from cadcartoes where codigo = ? and cod_empresa = ?";
        int setar = tblPesquisa.getSelectedRow();
        txtCod.setText(tblPesquisa.getModel().getValueAt(setar, 0).toString());
        String codigo = txtCod.getText();
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, codigo);
            pst.setString(2, TelaPrincipal.lblcodEmpresa.getText());
            rs = pst.executeQuery();
            if (rs.next()) {

                txtNomeCartao.setText(rs.getString(2));
                txtCodFornecedor.setText(rs.getString(3));

                txtDiaFechamento.setText(rs.getString(4));
                txtDiaVencimento.setText(rs.getString(5));
                txtLimite.setText(rs.getString(10));
                preenche_fornecedor();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void alteracartao() {
        String sql = "update cadcartoes set nomecartao = ?,fornecedor = ?,diafechamentofatura = ?,diavencimentofatura = ?,"
                + "`usuario` = ?,`cod_empresa`=?,limite=? where codigo = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtNomeCartao.getText());
            pst.setInt(2, Integer.parseInt(txtCodFornecedor.getText()));
            pst.setInt(3, Integer.parseInt(txtDiaFechamento.getText()));
            pst.setInt(4, Integer.parseInt(txtDiaVencimento.getText()));
            pst.setInt(5, Integer.parseInt(TelaPrincipal.lblCodUsoPrincipal.getText()));
            pst.setInt(6, Integer.parseInt(TelaPrincipal.lblcodEmpresa.getText()));
            pst.setDouble(7, Double.parseDouble(txtLimite.getText().replace(",", ".")));
            pst.setString(8, txtCod.getText());

            int alterou = pst.executeUpdate();
            if (alterou > 0) {
                JOptionPane.showMessageDialog(null, "Alterado com sucesso");
                bloqueiacampo();
                consultaAvancada();

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void excluiCartao() {
        String sql = "update cadcartoes set excluido = 1 where codigo = ?";
        try {
            int exclui = JOptionPane.showOptionDialog(null, "Confirma a impressão do relatório? ", "Atenção", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (exclui == JOptionPane.YES_OPTION) {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtCod.getText());

                int alterou = pst.executeUpdate();
                if (alterou > 0) {
                    JOptionPane.showMessageDialog(null, "Removido com sucesso");
                    bloqueiacampo();
                    limpacampos();
                    consultaAvancada();

                }
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
        txtNomeCartao = new javax.swing.JTextField();
        txtDiaFechamento = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtDiaVencimento = new javax.swing.JTextField();
        btnCancelar = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        btnIncluir = new javax.swing.JButton();
        btnAlterar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPesquisa = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        txtPesquisa = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtLimite = new javax.swing.JTextField();

        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setTitle("Cadastro de cartões");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/rpsystem/icones/cartoes.png"))); // NOI18N

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
        txtCodFornecedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCodFornecedorKeyPressed(evt);
            }
        });

        jLabel3.setText("Nome Cartão:");

        txtNomeCartao.setEnabled(false);

        txtDiaFechamento.setEnabled(false);

        jLabel4.setText("Dia Fechamento");
        jLabel4.setToolTipText("Informe o dia de fechamento da fatura");

        jLabel5.setText("Dia Vencimento");

        txtDiaVencimento.setEnabled(false);
        txtDiaVencimento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDiaVencimentoActionPerformed(evt);
            }
        });
        txtDiaVencimento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDiaVencimentoKeyPressed(evt);
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

        jLabel7.setText("Limite:");

        txtLimite.setEnabled(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18)
                                .addComponent(txtCod, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel7))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(txtDiaFechamento, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(26, 26, 26)
                                        .addComponent(jLabel5)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtDiaVencimento, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(txtNomeCartao, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(txtCodFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtNomeFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(txtLimite, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(15, 15, 15)
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
                                .addComponent(btnExcluir))))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 430, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtCod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtNomeFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCodFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtNomeCartao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDiaFechamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(txtDiaVencimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtLimite, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        setBounds(0, 0, 454, 508);
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
            JOptionPane.showMessageDialog(null, "Selecione um cartão");
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

    private void txtDiaVencimentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDiaVencimentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDiaVencimentoActionPerformed

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
        // TODO add your handling code here:
        String vazio = txtCod.getText();
        if (vazio.equals("")) {
            inserecartao();

        } else {
            alteracartao();
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
            pesquisa.lblOrigem.setText("cartao");
        }
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            preenche_fornecedor();
            txtNomeCartao.requestFocus();
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
            JOptionPane.showMessageDialog(null, "Selecione um cartão");
        } else {
            excluiCartao();
        }
    }//GEN-LAST:event_btnExcluirActionPerformed

    private void txtDiaVencimentoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDiaVencimentoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDiaVencimentoKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAlterar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnIncluir;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblPesquisa;
    private javax.swing.JTextField txtCod;
    public static javax.swing.JTextField txtCodFornecedor;
    private javax.swing.JTextField txtDiaFechamento;
    private javax.swing.JTextField txtDiaVencimento;
    private javax.swing.JTextField txtLimite;
    private javax.swing.JTextField txtNomeCartao;
    public static javax.swing.JTextField txtNomeFornecedor;
    private javax.swing.JTextField txtPesquisa;
    // End of variables declaration//GEN-END:variables
}
