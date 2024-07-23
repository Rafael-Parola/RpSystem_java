/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package br.com.rpsystem.telas;

import br.com.rpsystem.dal.ModuloConexao;
import br.com.rpsystem.funcoes.Funcoes;
import static br.com.rpsystem.telas.TelaFechaFatura.cboConta;
import static br.com.rpsystem.telas.TelaOs.txtCodCliOs;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author Rafael Veiga
 */
public class TelaPesquisaFinanceira extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    private TelaLancamentoFinanceiro telaLancamentoFinanceiro;

    /**
     * Creates new form TelaPesquisa
     */
    public TelaPesquisaFinanceira() {

        conexao = ModuloConexao.conector();

        initComponents();
        txtPesquisa.requestFocus();
        Funcoes conta = new Funcoes(cboConta);
        conta.f_carregaconta(cboConta);

        // Mapeie a tecla ESC para fechar a tela
        InputMap inputMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = this.getActionMap();

        // Adicione o KeyStroke e a ação correspondente para fechar a tela com ESC
        inputMap.put(KeyStroke.getKeyStroke("ESCAPE"), "fecharTela");
        actionMap.put("fecharTela", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

    }

    private void pesquisa_lancamento() {
        String conta_completa = null;
        int codigoconta;

        conta_completa = cboConta.getSelectedItem().toString();
        if (conta_completa.equals("Todos")) {
            codigoconta = 0;

        } else {
            codigoconta = Integer.parseInt(conta_completa.substring(0, 2).replaceAll("\\s|-", ""));
        }

        String sql = "select l.cod as 'Cod.', l.tipo as Tipo,c.nome as Conta, l.descricao as 'Descrição', l.valor as Valor,\n"
                + "l.dtemissao as 'Emissão', l.dtvencimento as 'Vencimento', l.dtpagamento as 'Pagamento' \n"
                + "from lancamentofinanceiro l\n"
                + "inner join cadastrocontas c \n"
                + "on l.codConta= c.codigo\n"
                + "where l.descricao like ?  and l.excluido = 0 and l.empresa_codempresa = ? and l.codConta like ? order by l.cod desc;";

        try {
            pst = conexao.prepareStatement(sql);
            // Passando o conteúdo do campo pesquisar para o SQL
            pst.setString(1, "%" + txtPesquisa.getText() + "%");
            pst.setString(2, TelaPrincipal.lblcodEmpresa.getText());

            if (codigoconta == 0) {
                pst.setString(3, "%%");
            } else {
                pst.setString(3, "%" + codigoconta + "%");
            }

            rs = pst.executeQuery();

            // Criar o modelo da tabela
            DefaultTableModel model = (DefaultTableModel) tblPesquisa.getModel();

            // Limpar o modelo da tabela
            model.setRowCount(0);

            // Adicionar dados ao modelo
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            while (rs.next()) {
                int codigo = rs.getInt("Cod.");
                String tipo = rs.getString("Tipo");
                String conta = rs.getString("Conta");
                String descricao = rs.getString("Descrição");
                double valor = rs.getDouble("Valor");
                Date emissao = rs.getDate("Emissão");
                Date vencimento = rs.getDate("Vencimento");
                Date pagamento = rs.getDate("Pagamento");

                // Formatar as datas conforme necessário
                String emissaoFormatada = (emissao != null) ? sdf.format(emissao) : "";
                String vencimentoFormatado = (vencimento != null) ? sdf.format(vencimento) : "";
                String pagamentoFormatado = (pagamento != null) ? sdf.format(pagamento) : "";

                model.addRow(new Object[]{codigo, tipo, conta, descricao, valor, emissaoFormatada, vencimentoFormatado, pagamentoFormatado});
            }

            // Ajustar a largura das colunas da tabela
            tblPesquisa.getColumnModel().getColumn(0).setPreferredWidth(1);
            tblPesquisa.getColumnModel().getColumn(1).setPreferredWidth(15);
            tblPesquisa.getColumnModel().getColumn(2).setPreferredWidth(15);
            tblPesquisa.getColumnModel().getColumn(3).setPreferredWidth(160);
            tblPesquisa.getColumnModel().getColumn(4).setPreferredWidth(25);
            tblPesquisa.getColumnModel().getColumn(5).setPreferredWidth(25);
            tblPesquisa.getColumnModel().getColumn(6).setPreferredWidth(25);
            tblPesquisa.getColumnModel().getColumn(7).setPreferredWidth(25);

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

        jScrollPane1 = new javax.swing.JScrollPane();
        tblPesquisa = new javax.swing.JTable();
        txtPesquisa = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        cboConta = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setResizable(true);

        tblPesquisa = new javax.swing.JTable(){
            public boolean  isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        tblPesquisa.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Cod.", "Tipo", "Conta", "Descricao", "Valor", "Emissão", "Vencimento", "Pagamento"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
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
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblPesquisaKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblPesquisa);

        txtPesquisa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPesquisaKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPesquisaKeyReleased(evt);
            }
        });

        jLabel1.setText("Pesquisa:");

        cboConta.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos" }));
        cboConta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboContaActionPerformed(evt);
            }
        });
        cboConta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cboContaKeyPressed(evt);
            }
        });

        jLabel2.setText("Conta:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 991, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(cboConta, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(txtPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, 684, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(cboConta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE)
                .addGap(15, 15, 15))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblPesquisaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPesquisaMouseClicked
        // TODO add your handling code here:
        // txtCodFin.setText(tblPesquisa.getModel().getValueAt(setar, 0).toString());
        int setar = tblPesquisa.getSelectedRow();
        TelaLancamentoFinanceiro.txtCodFin.setText(tblPesquisa.getModel().getValueAt(setar, 0).toString());
        //System.out.println(TelaLancamentoFinanceiro.txtCodFin.getText());

        this.dispose();

    }//GEN-LAST:event_tblPesquisaMouseClicked

    private void tblPesquisaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblPesquisaKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            int setar = tblPesquisa.getSelectedRow();
            TelaLancamentoFinanceiro.txtCodFin.setText(tblPesquisa.getModel().getValueAt(setar, 0).toString());
            //System.out.println(TelaLancamentoFinanceiro.txtCodFin.getText());

            this.dispose();

        }

    }//GEN-LAST:event_tblPesquisaKeyPressed

    private void txtPesquisaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesquisaKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            tblPesquisa.requestFocus();
        }
    }//GEN-LAST:event_txtPesquisaKeyPressed

    private void txtPesquisaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesquisaKeyReleased
        // TODO add your handling code here:
        pesquisa_lancamento();

    }//GEN-LAST:event_txtPesquisaKeyReleased

    private void cboContaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboContaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboContaActionPerformed

    private void cboContaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cboContaKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtPesquisa.requestFocus();

        }
    }//GEN-LAST:event_cboContaKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cboConta;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblPesquisa;
    public static javax.swing.JTextField txtPesquisa;
    // End of variables declaration//GEN-END:variables
}
