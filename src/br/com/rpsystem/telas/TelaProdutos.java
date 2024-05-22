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
import java.text.DecimalFormat;
import java.text.NumberFormat;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author Rafael Veiga
 */
public class TelaProdutos extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public boolean mostrainativos;

    /**
     * Creates new form TelaProdutos
     */
    public TelaProdutos() {
        initComponents();
        conexao = ModuloConexao.conector();
    }

    public void liberacampos() {
        txtDescricaoProd.setEnabled(true);
        cboStatusProd.setEnabled(true);
        txtValorVenda.setEnabled(true);
        txtCustoProduto.setEnabled(true);
        cboGrupoProd.setEnabled(true);
        btnExcluirProd.setEnabled(false);
        btnAlteraProd.setEnabled(false);
        btnIncluirProd.setEnabled(false);
        txtDescricaoProd.requestFocus();
        txtMargemVista.setEnabled(true);
    }

    public void bloqueiacampos() {
        txtDescricaoProd.setEnabled(false);
        cboStatusProd.setEnabled(false);
        txtValorVenda.setEnabled(false);
        txtCustoProduto.setEnabled(false);
        cboGrupoProd.setEnabled(false);
        btnExcluirProd.setEnabled(true);
        btnAlteraProd.setEnabled(true);
        btnIncluirProd.setEnabled(true);
        btnIncluirProd.requestFocus();
        txtMargemVista.setEnabled(false);
    }

    public void limpacampos() {
        txtEstqoue.setText(null);
        txtCodProduto.setText(null);
        txtDescricaoProd.setText(null);
        txtValorVenda.setText("0");
        txtCustoProduto.setText("0");
        txtMargemVista.setText("0");
    }

    public void carregagrupo() {
        String grupo = "select descricaogrupo from grupos where excluido != 1 and empresa_codempresa = ?";
        try {
            pst = conexao.prepareStatement(grupo);
            pst.setString(1, TelaPrincipal.lblcodEmpresa.getText());
            rs = pst.executeQuery();
            if (rs.next()) {
                do {
                    cboGrupoProd.addItem(rs.getString("descricaogrupo"));
                } while (rs.next());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void cadastro() {
        String cadastroprod = " INSERT INTO produtos (codproduto,descricao,status,valorvenda,valorcusto,usuarios_id,grupo,empresa_codempresa,margemlucro)\n"
                + " SELECT 1 + coalesce((SELECT max(codproduto) FROM produtos), 0), ?,?,?,?,?,?,?,?;";

        /* "INSERT INTO produtos (descricao,status,valorvenda,valorcusto,usuarios_id,grupo,empresa_codempresa) "
                + "VALUES(?,?,?,?,?,?,?)";*/
        try {
            pst = conexao.prepareStatement(cadastroprod);
            pst.setString(1, txtDescricaoProd.getText());
            pst.setString(2, cboStatusProd.getSelectedItem().toString());
            pst.setString(3, txtValorVenda.getText().replace(",", "."));
            pst.setString(4, txtCustoProduto.getText().replace(",", "."));
            pst.setString(5, TelaPrincipal.lblCodUsoPrincipal.getText());
            pst.setString(6, cboGrupoProd.getSelectedItem().toString());
            pst.setString(7, TelaPrincipal.lblcodEmpresa.getText());
            pst.setString(8, txtMargemVista.getText().replace(",", "."));
            if ((txtDescricaoProd.getText().isEmpty()) || (cboStatusProd.getSelectedItem().toString().isEmpty())
                    || (txtValorVenda.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha os campos obrigatórios.");
            } else {
                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Cadastrado com sucasso.");
                    bloqueiacampos();
                    limpacampos();
                    btnIncluirProd.setEnabled(true);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void consultaAvancada() {

        String sql = null;

        if (mostrainativos == true) {
            sql = "select "
                    + "codproduto as 'Código',\n"
                    + "descricao as 'Descrição',\n"
                    + "status as 'Status',\n"
                    + "estoque as 'Estoque',\n"
                    + "valorcusto as 'Preço Custo',"
                    + "margemlucro as 'Margem (%)',"
                    + "valorvenda as 'Preço Venda',\n"
                    + "grupo as 'Grupo' "
                    + "from produtos where empresa_codempresa = ? and descricao like ?";

        } else {
            sql = "select "
                    + "codproduto as 'Código',\n"
                    + "descricao as 'Descrição',\n"
                    + "status as 'Status',\n"
                    + "estoque as 'Estoque',\n"
                    + "valorcusto as 'Preço Custo',"
                    + "margemlucro as 'Margem (%)',"
                    + "valorvenda as 'Preço Venda',\n"
                    + "grupo as 'Grupo' "
                    + "from produtos where empresa_codempresa = ? and descricao like ? and status ='Ativo' ";
        }

        try {
            pst = conexao.prepareStatement(sql);
            // Passando o conteudo do campo pesquisar para o SQL
            pst.setString(1, TelaPrincipal.lblcodEmpresa.getText());
            pst.setString(2, "%" + txtPesquisaProd.getText() + "%");
            rs = pst.executeQuery();

            // A linha abixo usa a rs2xml.jar 
            tblPesquisaPrpd.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    public void setarcampos() {
        String sql = "select * from produtos where empresa_codempresa = ? and codproduto = ?";
        int setar = tblPesquisaPrpd.getSelectedRow();
        txtCodProduto.setText(tblPesquisaPrpd.getModel().getValueAt(setar, 0).toString());
        String cod = txtCodProduto.getText();
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, TelaPrincipal.lblcodEmpresa.getText());
            pst.setString(2, cod);
            rs = pst.executeQuery();
            if (rs.next()) {
                txtDescricaoProd.setText(rs.getString(2));
                cboStatusProd.setSelectedItem(rs.getString(3).toString());
                txtEstqoue.setText(rs.getString(4));
                txtValorVenda.setText(rs.getString(5));
                txtCustoProduto.setText(rs.getString(6));
                cboGrupoProd.setSelectedItem(rs.getString(8).toString());
                txtMargemVista.setText(rs.getString(11));

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void alterarprod() {
        String alterarprod = "UPDATE produtos SET\n"
                + "descricao = ?,\n"
                + "status =?,\n"
                + "valorvenda =?,\n"
                + "valorcusto =?,\n"
                + "usuarios_id  = ? ,\n"
                + "grupo = ?,\n"
                + "empresa_codempresa = ?,"
                + "margemlucro = ?"
                + "WHERE codproduto = ? and empresa_codempresa =? ";
        try {
            pst = conexao.prepareStatement(alterarprod);
            pst.setString(1, txtDescricaoProd.getText());
            pst.setString(2, cboStatusProd.getSelectedItem().toString());
            //pst.setString(3, txtEstqoue.getText());
            pst.setString(3, txtValorVenda.getText().replace(",", "."));
            pst.setString(4, txtCustoProduto.getText().replace(",", "."));
            pst.setString(5, TelaPrincipal.lblCodUsoPrincipal.getText());
            pst.setString(6, cboGrupoProd.getSelectedItem().toString());
            pst.setString(7, TelaPrincipal.lblcodEmpresa.getText());
            if (txtMargemVista.getText().equals("")) {
                pst.setString(8, "0");
            } else {
                pst.setString(8, txtMargemVista.getText().replace(",", "."));
            }

            //.replace("", "0")
            pst.setString(9, txtCodProduto.getText());
            pst.setString(10, TelaPrincipal.lblcodEmpresa.getText());

            int altera = pst.executeUpdate();
            if (altera > 0) {
                JOptionPane.showMessageDialog(null, "Alterada com sucesso");
                bloqueiacampos();
                limpacampos();
                consultaAvancada();

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void excluiprod() {
        String exclui = "update produtos set status = 'Inativo' where codproduto = ? and empresa_codempresa =?";

        try {
            int confirma = JOptionPane.showConfirmDialog(null, "Tem Certeza que deseja excluir?", "Alerta", JOptionPane.YES_NO_OPTION);
            if (confirma == JOptionPane.YES_OPTION) {
                pst = conexao.prepareStatement(exclui);
                pst.setString(1, txtCodProduto.getText());
                pst.setString(2, TelaPrincipal.lblcodEmpresa.getText());
                int excluiprodutos = pst.executeUpdate();
                if (excluiprodutos > 0) {
                    JOptionPane.showMessageDialog(null, "Excluido com sucesso");
                    bloqueiacampos();
                    limpacampos();
                    consultaAvancada();
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void calculamargem() {
        Double custo = Double.valueOf(txtCustoProduto.getText().replace(",", "."));
        Double margem = Double.valueOf(txtMargemVista.getText().replace(",", "."));
        if (custo > 0) {
            Double novoprecovista = custo + (custo * margem) / 100;
            NumberFormat formata = new DecimalFormat("#0.00");
            txtValorVenda.setText(formata.format(novoprecovista));
        }

    }

    private void calculamargemReverso() {

        Double custo = Double.valueOf(txtCustoProduto.getText().replace(",", "."));
        Double vlvenda = Double.valueOf(txtValorVenda.getText().replace(",", "."));
        if (custo > 0) {
            Double margematual = ((vlvenda - custo) / custo) * 100;
            NumberFormat formata = new DecimalFormat("#0.00");
            txtMargemVista.setText(formata.format(margematual));
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
        tblPesquisaPrpd = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txtPesquisaProd = new javax.swing.JTextField();
        btnIncluirProd = new javax.swing.JButton();
        btnAlteraProd = new javax.swing.JButton();
        btnSalvarProd = new javax.swing.JButton();
        btnExcluirProd = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txtCodProduto = new javax.swing.JTextField();
        txtDescricaoProd = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        cboStatusProd = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtEstqoue = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtValorVenda = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtCustoProduto = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        cboGrupoProd = new javax.swing.JComboBox<>();
        btnCancelar = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        txtMargemVista = new javax.swing.JTextField();
        chkMostraInativos = new javax.swing.JCheckBox();

        setClosable(true);
        setIconifiable(true);
        setTitle("Cadastro de Produtos");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/rpsystem/icones/produtos.png"))); // NOI18N
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

        tblPesquisaPrpd = new javax.swing.JTable(){
            public boolean  isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        tblPesquisaPrpd.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Código", "Descrição", "Status", "Estoque", "Preço custo", "Margem (%)", "Preço Venda", "Grupo"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblPesquisaPrpd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPesquisaPrpdMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblPesquisaPrpd);

        jLabel1.setText("Pesquisar:");

        txtPesquisaProd.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPesquisaProdKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPesquisaProdKeyReleased(evt);
            }
        });

        btnIncluirProd.setText("Incluir");
        btnIncluirProd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIncluirProdActionPerformed(evt);
            }
        });
        btnIncluirProd.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnIncluirProdKeyPressed(evt);
            }
        });

        btnAlteraProd.setText("Alterar");
        btnAlteraProd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlteraProdActionPerformed(evt);
            }
        });

        btnSalvarProd.setText("Salvar");
        btnSalvarProd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvarProdActionPerformed(evt);
            }
        });
        btnSalvarProd.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnSalvarProdKeyPressed(evt);
            }
        });

        btnExcluirProd.setText("Excluir");
        btnExcluirProd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcluirProdActionPerformed(evt);
            }
        });

        jLabel2.setText("Codigo:");

        txtCodProduto.setEnabled(false);

        txtDescricaoProd.setEnabled(false);
        txtDescricaoProd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDescricaoProdActionPerformed(evt);
            }
        });
        txtDescricaoProd.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDescricaoProdKeyPressed(evt);
            }
        });

        jLabel3.setText("*Descrição:");

        cboStatusProd.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Ativo", "Inativo" }));
        cboStatusProd.setEnabled(false);
        cboStatusProd.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cboStatusProdKeyPressed(evt);
            }
        });

        jLabel4.setText("*Status:");

        jLabel5.setText("Estoque:");

        txtEstqoue.setEditable(false);
        txtEstqoue.setEnabled(false);

        jLabel6.setText("*Preço Venda:");

        txtValorVenda.setText("0");
        txtValorVenda.setEnabled(false);
        txtValorVenda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtValorVendaActionPerformed(evt);
            }
        });
        txtValorVenda.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtValorVendaKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtValorVendaKeyReleased(evt);
            }
        });

        jLabel7.setText("Preço Custo:");

        txtCustoProduto.setText("0");
        txtCustoProduto.setEnabled(false);
        txtCustoProduto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCustoProdutoKeyPressed(evt);
            }
        });

        jLabel8.setText("Grupo:");

        cboGrupoProd.setEnabled(false);
        cboGrupoProd.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cboGrupoProdKeyPressed(evt);
            }
        });

        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        jLabel9.setText("Margem (%):");
        jLabel9.setToolTipText("");

        txtMargemVista.setText("0");
        txtMargemVista.setToolTipText("Preço de custo + % rusulta no preço de venda");
        txtMargemVista.setEnabled(false);
        txtMargemVista.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMargemVistaActionPerformed(evt);
            }
        });
        txtMargemVista.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtMargemVistaKeyPressed(evt);
            }
        });

        chkMostraInativos.setText("Mostra Inativos");
        chkMostraInativos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkMostraInativosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(jLabel2)
                                                .addComponent(jLabel3)
                                                .addComponent(jLabel5))
                                            .addGap(18, 18, 18)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(txtCodProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGroup(layout.createSequentialGroup()
                                                    .addComponent(txtEstqoue, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(jLabel7)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(txtCustoProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(jLabel9)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(txtMargemVista)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(jLabel6)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(txtValorVenda, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGap(53, 53, 53))
                                                .addGroup(layout.createSequentialGroup()
                                                    .addComponent(txtDescricaoProd, javax.swing.GroupLayout.PREFERRED_SIZE, 489, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(jLabel4)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(cboStatusProd, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jLabel8)
                                            .addGap(18, 18, 18)
                                            .addComponent(cboGrupoProd, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(532, 532, 532)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(14, 14, 14)
                                        .addComponent(jLabel1)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtPesquisaProd, javax.swing.GroupLayout.PREFERRED_SIZE, 546, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(chkMostraInativos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGap(0, 28, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnIncluirProd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAlteraProd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancelar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSalvarProd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnExcluirProd)
                        .addGap(203, 203, 203))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtCodProduto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDescricaoProd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(cboStatusProd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEstqoue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel7)
                    .addComponent(txtCustoProduto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(txtValorVenda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(txtMargemVista, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(cboGrupoProd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnIncluirProd, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnAlteraProd, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnSalvarProd)
                        .addComponent(btnCancelar))
                    .addComponent(btnExcluirProd, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtPesquisaProd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkMostraInativos))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalvarProdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarProdActionPerformed
        // TODO add your handling code here:
        String cod = txtCodProduto.getText();
        if (cod.equals("")) {
            cadastro();
        } else {
            alterarprod();
        }

    }//GEN-LAST:event_btnSalvarProdActionPerformed

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        // TODO add your handling code here:
        carregagrupo();
    }//GEN-LAST:event_formInternalFrameOpened

    private void btnIncluirProdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIncluirProdActionPerformed
        // TODO add your handling code here:
        limpacampos();
        liberacampos();

    }//GEN-LAST:event_btnIncluirProdActionPerformed

    private void btnAlteraProdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlteraProdActionPerformed
        // TODO add your handling code here:
        String altera = txtCodProduto.getText();
        if (altera.equals("")) {
            JOptionPane.showMessageDialog(null, "Selecione um Produto");
        } else {
            liberacampos();
        }

    }//GEN-LAST:event_btnAlteraProdActionPerformed

    private void txtPesquisaProdKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesquisaProdKeyPressed
        // TODO add your handling code here:
        //consultaAvancada();
    }//GEN-LAST:event_txtPesquisaProdKeyPressed

    private void tblPesquisaPrpdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPesquisaPrpdMouseClicked
        // TODO add your handling code here:
        setarcampos();
    }//GEN-LAST:event_tblPesquisaPrpdMouseClicked

    private void txtPesquisaProdKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesquisaProdKeyReleased
        // TODO add your handling code here:
        consultaAvancada();
    }//GEN-LAST:event_txtPesquisaProdKeyReleased

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // TODO add your handling code here:
        bloqueiacampos();
        limpacampos();
        btnIncluirProd.setEnabled(true);

    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnExcluirProdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirProdActionPerformed
        // TODO add your handling code here:
        String exclui = txtCodProduto.getText();
        if (exclui.equals("")) {
            JOptionPane.showMessageDialog(null, "Selecione um Produto");
        } else {
            excluiprod();
        }


    }//GEN-LAST:event_btnExcluirProdActionPerformed

    private void txtDescricaoProdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDescricaoProdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDescricaoProdActionPerformed

    private void txtDescricaoProdKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDescricaoProdKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cboStatusProd.requestFocus();
        }
    }//GEN-LAST:event_txtDescricaoProdKeyPressed

    private void cboStatusProdKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cboStatusProdKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtCustoProduto.requestFocus();

        }
    }//GEN-LAST:event_cboStatusProdKeyPressed

    private void txtCustoProdutoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCustoProdutoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtMargemVista.requestFocus();

        }
    }//GEN-LAST:event_txtCustoProdutoKeyPressed

    private void txtValorVendaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtValorVendaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtValorVendaActionPerformed

    private void txtValorVendaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtValorVendaKeyPressed
        // TODO add your handling code here:

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            calculamargemReverso();
            cboGrupoProd.requestFocus();
        }
    }//GEN-LAST:event_txtValorVendaKeyPressed

    private void cboGrupoProdKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cboGrupoProdKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnSalvarProd.requestFocus();
        }
    }//GEN-LAST:event_cboGrupoProdKeyPressed

    private void btnSalvarProdKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnSalvarProdKeyPressed
        // TODO add your handling code here:

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String cod = txtCodProduto.getText();
            if (cod.equals("")) {
                cadastro();
            } else {
                alterarprod();

            }
        }


    }//GEN-LAST:event_btnSalvarProdKeyPressed

    private void btnIncluirProdKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnIncluirProdKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            limpacampos();
            liberacampos();

        }
    }//GEN-LAST:event_btnIncluirProdKeyPressed

    private void txtMargemVistaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMargemVistaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMargemVistaActionPerformed

    private void txtMargemVistaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMargemVistaKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            calculamargem();
            txtValorVenda.requestFocus();

        }
    }//GEN-LAST:event_txtMargemVistaKeyPressed

    private void txtValorVendaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtValorVendaKeyReleased
        // TODO add your handling code here:

    }//GEN-LAST:event_txtValorVendaKeyReleased

    private void chkMostraInativosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkMostraInativosActionPerformed
        // TODO add your handling code here:
        if (chkMostraInativos.isSelected()) {
            mostrainativos = true;
            consultaAvancada();

        } else {
            mostrainativos = false;
            consultaAvancada();

        }
    }//GEN-LAST:event_chkMostraInativosActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAlteraProd;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnExcluirProd;
    private javax.swing.JButton btnIncluirProd;
    private javax.swing.JButton btnSalvarProd;
    private javax.swing.JComboBox<String> cboGrupoProd;
    private javax.swing.JComboBox<String> cboStatusProd;
    private javax.swing.JCheckBox chkMostraInativos;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblPesquisaPrpd;
    private javax.swing.JTextField txtCodProduto;
    private javax.swing.JTextField txtCustoProduto;
    private javax.swing.JTextField txtDescricaoProd;
    private javax.swing.JTextField txtEstqoue;
    private javax.swing.JTextField txtMargemVista;
    private javax.swing.JTextField txtPesquisaProd;
    private javax.swing.JTextField txtValorVenda;
    // End of variables declaration//GEN-END:variables
}
