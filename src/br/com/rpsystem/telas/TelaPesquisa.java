/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package br.com.rpsystem.telas;

import br.com.rpsystem.dal.ModuloConexao;
import static br.com.rpsystem.telas.TelaOs.txtCodCliOs;
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
public class TelaPesquisa extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    /**
     * Creates new form TelaPesquisa
     */
    public TelaPesquisa() {
        conexao = ModuloConexao.conector();

        initComponents();
    }

    private void consultaAvancada() {
        String sql = "SELECT O.os as OS , O.dataos, O.tipo,O.situacao, O.equipamento,O.defeito, O.Servico, "
                + "O.tecnico, O.Valor,c.id,c.nome"
                + " FROM ordemservico O INNER JOIN pessoas c ON O.clientes_idclientes = c.id "
                + "where c.nome like ? and O.excluido != '1'";
        try {
            pst = conexao.prepareStatement(sql);
            // Passando o conteudo do campo pesquisar para o SQL
            pst.setString(1, "%" + txtPesquisaOs.getText() + "%");
            rs = pst.executeQuery();
            // A linha abixo usa a rs2xml.jar 
            tblPesquisaOs.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void setarcampos() {

        int setar = tblPesquisaOs.getSelectedRow();
        TelaOs.txtNumOS.setText(tblPesquisaOs.getModel().getValueAt(setar, 0).toString());
        TelaOs.txtDataOS.setText(tblPesquisaOs.getModel().getValueAt(setar, 1).toString());

        String tipo = tblPesquisaOs.getModel().getValueAt(setar, 2).toString();
        if (tipo.equals("OS")) {
            TelaOs.rbtOrdemDeServico.setSelected(true);

        } else {
            TelaOs.rbtOrcamento.setSelected(true);

        }
        TelaOs.cboSitacao.setSelectedItem(tblPesquisaOs.getModel().getValueAt(setar, 3));
        TelaOs.txtEquipamento.setText(tblPesquisaOs.getModel().getValueAt(setar, 4).toString());
        TelaOs.txtDefeito.setText(tblPesquisaOs.getModel().getValueAt(setar, 5).toString());
        TelaOs.txtServico.setText(tblPesquisaOs.getModel().getValueAt(setar, 6).toString());
        TelaOs.cboVendedor.setSelectedItem(tblPesquisaOs.getModel().getValueAt(setar, 7));
// TelaOs.txtTecnico.setText(tblPesquisaOs.getModel().getValueAt(setar, 7).toString());
        TelaOs.txtValor.setText(tblPesquisaOs.getModel().getValueAt(setar, 8).toString());
        TelaOs.txtCodCliOs.setText(tblPesquisaOs.getModel().getValueAt(setar, 9).toString());
        bloqueicampos();

        this.dispose();

    }

    //bloqueando campos. 
    public void bloqueicampos() {

        TelaOs.txtEquipamento.setEnabled(false);
        TelaOs.txtDefeito.setEnabled(false);
        TelaOs.txtServico.setEnabled(false);
        // TelaOs.txtTecnico.setEnabled(false);
        TelaOs.cboVendedor.setEnabled(false);
        TelaOs.txtValor.setEnabled(false);
        TelaOs.rbtOrcamento.setEnabled(false);
        TelaOs.rbtOrdemDeServico.setEnabled(false);

    }

    public void consultaclienteos() {
        String sql = "select id as Codigo, nome as Nome, telefone as Telefone from pessoas "
                + "where id= ? and excluido != '1' and empresa_codempresa =" + TelaPrincipal.lblcodEmpresa.getText();
        try {
            pst = conexao.prepareStatement(sql);
            // Passando o conteudo do campo pesquisar para o SQL
            pst.setString(1, txtCodCliOs.getText());
            rs = pst.executeQuery();

            // A linha abixo usa a rs2xml.jar 
            TelaOs.tblListarCli.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    //Pesquisa Ordem serviço com produtos
    public void pesquisa_os_prodserv() {
        String sql = "select o.os as OS, o.dataos as Dataos, o.tipo as Tipo, c.nome as Cliente\n"
                + "from ordemservico as o inner join pessoas as c on o.clientes_idclientes = c.id "
                + "where c.nome like ? and o.excluido =0 and o.empresa_codempresa = " + TelaPrincipal.lblcodEmpresa.getText() + " order by o.os desc ;";
        try {
            pst = conexao.prepareStatement(sql);
            // Passando o conteudo do campo pesquisar para o SQL
            pst.setString(1, "%" + txtPesquisaOs.getText() + "%");
            rs = pst.executeQuery();
            // A linha abixo usa a rs2xml.jar 
            tblPesquisaOs.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    //Pesquisa Ordem serviço com produtos
    public void pesquisaEmpresa() {
        String sql = "select \n"
                + "codempresa as Codigo, razaoSocial as 'Razão Social', \n"
                + "fantasia as 'Nome fantasia',cnpjcpf as CNPJ, \n"
                + "rginscri as 'Insc Estadual',endereco as 'Endereço',\n"
                + "numero as 'Numero',cep as CEP,complemento as Complemento,\n"
                + "bairro as Bairro,cidade as Cidade,estado as UF\n"
                + "from empresa where fantasia like ?;";
        try {
            pst = conexao.prepareStatement(sql);
            // Passando o conteudo do campo pesquisar para o SQL
            pst.setString(1, "%" + txtPesquisaOs.getText() + "%");
            rs = pst.executeQuery();
            // A linha abixo usa a rs2xml.jar 
            tblPesquisaOs.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    public void seta_codCliente_prodServ() {

        int setar = tblPesquisaOs.getSelectedRow();
        TelaOsProdeServ.txtNumOS.setText(tblPesquisaOs.getModel().getValueAt(setar, 0).toString());
        TelaOsProdeServ.txtNumOS.requestFocus();

        this.dispose();

    }

    public void pesquisafornecedorpgto() {
        String sql = "select id, nome from pessoas where nome like ? and fornecedor = '1' and empresa_codempresa = " + TelaPrincipal.lblcodEmpresa.getText();
        try {
            pst = conexao.prepareStatement(sql);
            // Passando o conteudo do campo pesquisar para o SQL
            pst.setString(1, "%" + txtPesquisaOs.getText() + "%");
            rs = pst.executeQuery();
            // A linha abixo usa a rs2xml.jar 
            tblPesquisaOs.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void setafornecedorpgto() {
        int setar = tblPesquisaOs.getSelectedRow();
        TelaCadFormasPgto.txtFornecedorFormaPgto.setText(tblPesquisaOs.getModel().getValueAt(setar, 0).toString());
        TelaCadFormasPgto.txtNomeForncedorFormaPgto.setText(tblPesquisaOs.getModel().getValueAt(setar, 1).toString());
        this.dispose();
    }

    public void pesquisaformapagamento() {
        String sql = "select codigo,descricao from formaspagamento where descricao like ? and status <> 1 and empresa_codempresa = " + TelaPrincipal.lblcodEmpresa.getText();
        try {
            pst = conexao.prepareStatement(sql);
            // Passando o conteudo do campo pesquisar para o SQL
            pst.setString(1, "%" + txtPesquisaOs.getText() + "%");
            rs = pst.executeQuery();
            // A linha abixo usa a rs2xml.jar 
            tblPesquisaOs.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    public void setaformadepagamento() {
        int setar = tblPesquisaOs.getSelectedRow();
        TelaCadFormasPgto.txtCodFormaPgto.setText(tblPesquisaOs.getModel().getValueAt(setar, 0).toString());
        TelaCadFormasPgto.txtCodFormaPgto.requestFocus();

        this.dispose();

    }

    public void setaformadepagamentoclassificacaofinanceira() {
        int setar = tblPesquisaOs.getSelectedRow();
        TelaLancamentoFinanceiro.txtCodFornecedor.setText(tblPesquisaOs.getModel().getValueAt(setar, 0).toString());
        TelaLancamentoFinanceiro.txtNomeFornecedor.setText(tblPesquisaOs.getModel().getValueAt(setar, 1).toString());
        TelaLancamentoFinanceiro.txtDescricao.requestFocus();
        this.dispose();

    }

    public void pesquisafornecedorcadastrocartoes() {
        String sql = "select id, nome from pessoas where nome like ? and fornecedor = '1' and empresa_codempresa = " + TelaPrincipal.lblcodEmpresa.getText();
        try {
            pst = conexao.prepareStatement(sql);
            // Passando o conteudo do campo pesquisar para o SQL
            pst.setString(1, "%" + txtPesquisaOs.getText() + "%");
            rs = pst.executeQuery();
            // A linha abixo usa a rs2xml.jar 
            tblPesquisaOs.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void setafornecedorcadatrocartoes() {
        int setar = tblPesquisaOs.getSelectedRow();
        TelaCadastroCartoes.txtCodFornecedor.setText(tblPesquisaOs.getModel().getValueAt(setar, 0).toString());
        TelaCadastroCartoes.txtNomeFornecedor.setText(tblPesquisaOs.getModel().getValueAt(setar, 1).toString());
        this.dispose();
    }

    public void setafornecedorcadatroccontas() {
        int setar = tblPesquisaOs.getSelectedRow();
        TelaCadConta.txtCodFornecedor.setText(tblPesquisaOs.getModel().getValueAt(setar, 0).toString());
        TelaCadConta.txtNomeFornecedor.setText(tblPesquisaOs.getModel().getValueAt(setar, 1).toString());
        this.dispose();
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
        tblPesquisaOs = new javax.swing.JTable();
        txtPesquisaOs = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        lblOrigem = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setResizable(true);

        tblPesquisaOs = new javax.swing.JTable(){
            public boolean  isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        tblPesquisaOs.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Numero", "Data", "Tipo", "Situação", "Equipamento"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblPesquisaOs.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPesquisaOsMouseClicked(evt);
            }
        });
        tblPesquisaOs.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblPesquisaOsKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblPesquisaOs);

        txtPesquisaOs.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPesquisaOsKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPesquisaOsKeyReleased(evt);
            }
        });

        jLabel1.setText("Pesquisa:");

        lblOrigem.setText("0");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 989, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(txtPesquisaOs))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(lblOrigem, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(41, 41, 41)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPesquisaOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addComponent(lblOrigem)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblPesquisaOsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPesquisaOsMouseClicked
        // TODO add your handling code here:

        String tipopesquisa = lblOrigem.getText();
        switch (tipopesquisa) {
            case "EMP":
                pesquisaEmpresa();
                break;
            case "ProdServ":
                seta_codCliente_prodServ();
                break;
            case "OS":
                setarcampos();
                break;
            case "FORPGTO":
                setafornecedorpgto();
                break;
            case "PGTO":
                setaformadepagamento();
                break;
            case "CLASSFIN":
                setaformadepagamentoclassificacaofinanceira();
                break;
            case "cartao":
                setafornecedorcadatrocartoes();
                break;
            case "conta":
                setafornecedorcadatroccontas();
                break;
            default:
                System.out.println("Número inválido");
        }

    }//GEN-LAST:event_tblPesquisaOsMouseClicked

    private void tblPesquisaOsKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblPesquisaOsKeyPressed
        // TODO add your handling code here:

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String tipopesquisa = lblOrigem.getText();
            switch (tipopesquisa) {
                case "EMP":
                    pesquisaEmpresa();
                    break;
                case "ProdServ":
                    seta_codCliente_prodServ();
                    break;
                case "OS":
                    setarcampos();
                    break;
                case "FORPGTO":
                    setafornecedorpgto();
                    break;
                case "PGTO":
                    setaformadepagamento();
                    break;
                case "CLASSFIN":
                    setaformadepagamentoclassificacaofinanceira();
                    break;
                case "cartao":
                    setafornecedorcadatrocartoes();
                    break;
                case "conta":
                    setafornecedorcadatroccontas();
                    break;
                default:
                    System.out.println("Número inválido");

                //aqui vai o q voce deseja fazer quando o usuario clicar enter naquele jtextfield
            }
        }
    }//GEN-LAST:event_tblPesquisaOsKeyPressed

    private void txtPesquisaOsKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesquisaOsKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            tblPesquisaOs.requestFocus();
        }
    }//GEN-LAST:event_txtPesquisaOsKeyPressed

    private void txtPesquisaOsKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesquisaOsKeyReleased
        // TODO add your handling code here:

        String tipopesquisa = lblOrigem.getText();
        switch (tipopesquisa) {
            case "EMP":
                pesquisaEmpresa();
                break;
            case "ProdServ":
                pesquisa_os_prodserv();
                break;
            case "OS":
                consultaAvancada();
                break;
            case "FORPGTO":
                pesquisafornecedorpgto();
                break;
            case "PGTO":
                pesquisaformapagamento();

                break;
            case "CLASSFIN":
                pesquisafornecedorpgto();

                break;
            case "cartao":
                pesquisafornecedorpgto();
                break;
                 case "conta":
                pesquisafornecedorpgto();
                break;
            default:
                System.out.println("Número inválido");
        }
    }//GEN-LAST:event_txtPesquisaOsKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    public static javax.swing.JLabel lblOrigem;
    private javax.swing.JTable tblPesquisaOs;
    public static javax.swing.JTextField txtPesquisaOs;
    // End of variables declaration//GEN-END:variables
}
