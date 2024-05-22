/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package br.com.rpsystem.telas;

import br.com.rpsystem.dal.ModuloConexao;
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
public class TelaPessoas extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    public String cliente;
    public String fornecedor;
    public String funcionario;

    /**
     * Creates new form TelaClientes
     */
    public TelaPessoas() {
        initComponents();
        conexao = ModuloConexao.conector();
    }

    private void consultaAvancada() {
        String sql = "select id as Codigo, nome as Nome, status as Status, cpfcnpj as 'CPF/CNPJ' from pessoas "
                + "where nome like ? and empresa_codempresa = ? order by nome";
        try {
            pst = conexao.prepareStatement(sql);
            // Passando o conteudo do campo pesquisar para o SQL
            pst.setString(1, "%" + txtPesquisaCli.getText() + "%");
            pst.setString(2, TelaPrincipal.lblcodEmpresa.getText());
            rs = pst.executeQuery();
            // A linha abixo usa a rs2xml.jar 
            tblCli.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void incluir() {
        String inserir = " INSERT INTO pessoas (id, nome,status,telefone,endereco,numero,complemento,bairro,cidade,"
                + "cep,estado,cpfcnpj,rginscricao,email,usuarios_id,empresa_codempresa,cliente,fornecedor,funcionario,observacoes)\n"
                + " SELECT 1 + coalesce((SELECT max(id) FROM pessoas), 0), ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?";

        try {
            pst = conexao.prepareStatement(inserir);

            pst.setString(1, txtNomeCli.getText());
            pst.setString(2, cboStatusCli.getSelectedItem().toString());
            pst.setString(3, txtFoneCli.getText());
            pst.setString(4, txtEndereco.getText());
            pst.setString(5, txtNumCli.getText());
            pst.setString(6, txtComplementoCli.getText());
            pst.setString(7, txtBairoCli.getText());
            pst.setString(8, txtCidadeCli.getText());
            pst.setString(9, txtCepCli.getText());
            pst.setString(10, cboEstado.getSelectedItem().toString());
            pst.setString(11, txtCnpjcli.getText());
            pst.setString(12, txtInscCli.getText());
            pst.setString(13, txtEmail.getText());
            pst.setString(14, TelaPrincipal.lblCodUsoPrincipal.getText());
            pst.setString(15, TelaPrincipal.lblcodEmpresa.getText());
            pst.setString(16, cliente);
            pst.setString(17, fornecedor);
            pst.setString(18, funcionario);
            pst.setString(19, jTextObservacao.getText());
            
            if ((txtNomeCli.getText().isEmpty()) || (txtFoneCli.getText().isEmpty())
                    || (txtCnpjcli.getText().isEmpty()) || (cliente.equals("0"))&&(funcionario.equals("0") )&&(fornecedor.equals("0")) ) {
                JOptionPane.showMessageDialog(null, "Preencha os campos obrigatórios.");
            } else {
                int adiciona = pst.executeUpdate();

                if (adiciona > 0) {

                    JOptionPane.showMessageDialog(null, "Cliente adicionado com sucesso.");
                    limpacampos();
                    desabilitacampos();
                    btnIncluir.setEnabled(true);
                    //consultaAvancada();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void alterar() {

        String alterar = "UPDATE pessoas SET nome = ?, status = ?, telefone = ?, endereco = ?, numero = ?,\n"
                + "complemento = ?, bairro = ?, cidade = ?, cep = ?, estado = ?, cpfcnpj = ?,\n"
                + "rginscricao = ?, email = ? , usuarios_id = ?,empresa_codempresa=?,cliente = ?,fornecedor = ?,funcionario =?,observacoes = ? "
                + "WHERE id = ? and empresa_codempresa = ?";
        try {
            pst = conexao.prepareStatement(alterar);
            pst.setString(1, txtNomeCli.getText());
            pst.setString(2, cboStatusCli.getSelectedItem().toString());
            pst.setString(3, txtFoneCli.getText());
            pst.setString(4, txtEndereco.getText());
            pst.setString(5, txtNumCli.getText());
            pst.setString(6, txtComplementoCli.getText());
            pst.setString(7, txtBairoCli.getText());
            pst.setString(8, txtCidadeCli.getText());
            pst.setString(9, txtCepCli.getText());
            pst.setString(10, cboEstado.getSelectedItem().toString());
            pst.setString(11, txtCnpjcli.getText());
            pst.setString(12, txtInscCli.getText());
            pst.setString(13, txtEmail.getText());
            pst.setString(14, TelaPrincipal.lblCodUsoPrincipal.getText());
            pst.setString(15, TelaPrincipal.lblcodEmpresa.getText());
            pst.setString(16, cliente);
            pst.setString(17, fornecedor);
            pst.setString(18, funcionario);
            pst.setString(19, jTextObservacao.getText());
            
            pst.setString(20, txtCodCli.getText());
            pst.setString(21, TelaPrincipal.lblcodEmpresa.getText());

            int altera = pst.executeUpdate();
            if (altera > 0) {
                JOptionPane.showMessageDialog(null, "Cliente alterado com sucesso.");
                //limpacampos();
                desabilitacampos();
                consultaAvancada();
                btnIncluir.setEnabled(true);

                GeraLog bkp = new GeraLog("Tela Clientes", TelaPrincipal.lblCodUsoPrincipal.getText().concat(" - ").concat(TelaPrincipal.lblUsuario.getText()), "Alterou cliente", TelaPrincipal.lblcodEmpresa.getText(), txtCodCli.getText());
                bkp.gravaBackup();

            } else {
                JOptionPane.showMessageDialog(null, "Erro ao alterar");

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void setarcampos() {

        String sql = "select * from pessoas where id =? and empresa_codempresa = ? ";
        int setar = tblCli.getSelectedRow();
        txtCodCli.setText(tblCli.getModel().getValueAt(setar, 0).toString());
        String codigo = txtCodCli.getText();
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, codigo);
            pst.setString(2, TelaPrincipal.lblcodEmpresa.getText());
            rs = pst.executeQuery();
            if (rs.next()) {
                //txtCodCli.setText(rs.getString(codigo));
                txtNomeCli.setText(rs.getString(2));
                cboStatusCli.setSelectedItem(rs.getString(3).toString());
                txtFoneCli.setText(rs.getString(4));
                txtEndereco.setText(rs.getString(5));
                txtNumCli.setText(rs.getString(6));
                txtComplementoCli.setText(rs.getString(7));
                txtBairoCli.setText(rs.getString(8));
                txtCidadeCli.setText(rs.getString(9));
                txtCepCli.setText(rs.getString(10));
                cboEstado.setSelectedItem(rs.getString(11));
                txtCnpjcli.setText(rs.getString(12));
                txtInscCli.setText(rs.getString(13));
                txtEmail.setText(rs.getString(14));

                cliente = (rs.getString(18));
                if (cliente.equals("1")) {
                    chkCliente.setSelected(true);
                }else{
                    chkCliente.setSelected(false);
                }
                fornecedor = (rs.getString(19));
                if (fornecedor.equals("1")) {
                    chkFornecedor.setSelected(true);
                }else {
                    chkFornecedor.setSelected(false);
                }
                funcionario = (rs.getString(20));
                if (funcionario.equals("1")) {
                    chkFuncionario.setSelected(true);
                }else{
                      chkFuncionario.setSelected(false);
                }
                jTextObservacao.setText(rs.getString(21));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void desabilitacampos() {
        txtNomeCli.setEnabled(false);
        txtFoneCli.setEnabled(false);
        txtEndereco.setEnabled(false);
        txtNumCli.setEnabled(false);
        txtComplementoCli.setEnabled(false);
        txtBairoCli.setEnabled(false);
        txtCidadeCli.setEnabled(false);
        txtCepCli.setEnabled(false);
        cboEstado.setEnabled(false);
        txtCnpjcli.setEnabled(false);
        txtInscCli.setEnabled(false);
        txtEmail.setEnabled(false);
        cboStatusCli.setEnabled(false);
        btnAlterar.setEnabled(true);
        jTextObservacao.setEnabled(false);
        btnIncluir.requestFocus();
        chkCliente.setEnabled(false);
        chkFornecedor.setEnabled(false);
        chkFuncionario.setEnabled(false);

    }

    public void habilitacampos() {
        txtNomeCli.setEnabled(true);
        txtFoneCli.setEnabled(true);
        txtEndereco.setEnabled(true);
        txtNumCli.setEnabled(true);
        txtComplementoCli.setEnabled(true);
        txtBairoCli.setEnabled(true);
        txtCidadeCli.setEnabled(true);
        txtCepCli.setEnabled(true);
        cboEstado.setEnabled(true);
        txtCnpjcli.setEnabled(true);
        txtInscCli.setEnabled(true);
        txtEmail.setEnabled(true);
        cboStatusCli.setEnabled(true);
        btnAlterar.setEnabled(false);
        btnIncluir.setEnabled(false);
        jTextObservacao.setEnabled(true);
        txtNomeCli.requestFocus();
        chkCliente.setEnabled(true);
        chkFornecedor.setEnabled(true);
        chkFuncionario.setEnabled(true);
    }

    public void limpacampos() {
        txtCodCli.setText(null);
        txtNomeCli.setText(null);
        txtFoneCli.setText(null);
        txtEndereco.setText(null);
        txtNumCli.setText(null);
        txtComplementoCli.setText(null);
        txtBairoCli.setText(null);
        txtCidadeCli.setText(null);
        txtCepCli.setText(null);
        // cboEstado.setText(null);
        txtCnpjcli.setText(null);
        txtInscCli.setText(null);
        txtEmail.setText(null);
        jTextObservacao.setText(null);
        chkCliente.setSelected(true);
        chkFornecedor.setSelected(false);
        chkFuncionario.setSelected(false);
    }

    public void carregaestaodcli() {
        String grupo = "select sigla from estados ";
        try {
            pst = conexao.prepareStatement(grupo);
            rs = pst.executeQuery();
            if (rs.next()) {
                do {
                    cboEstado.addItem(rs.getString("sigla"));
                    cboEstado.setSelectedItem("SP");
                } while (rs.next());
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
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCli = new javax.swing.JTable();
        txtCodCli = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtNomeCli = new javax.swing.JTextField();
        txtFoneCli = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtEndereco = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtNumCli = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtBairoCli = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtCepCli = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        cboStatusCli = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtCnpjcli = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtInscCli = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtComplementoCli = new javax.swing.JTextField();
        btnSalvar = new javax.swing.JButton();
        btnIncluir = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        cboEstado = new javax.swing.JComboBox<>();
        jLabel16 = new javax.swing.JLabel();
        txtCidadeCli = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtPesquisaCli = new javax.swing.JTextField();
        btnAlterar = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextObservacao = new javax.swing.JTextPane();
        jLabel17 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        chkFornecedor = new javax.swing.JCheckBox();
        chkCliente = new javax.swing.JCheckBox();
        chkFuncionario = new javax.swing.JCheckBox();

        setClosable(true);
        setIconifiable(true);
        setTitle("Cadastro de Pessoas");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/rpsystem/icones/pessoas.png"))); // NOI18N
        setPreferredSize(new java.awt.Dimension(672, 483));
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
            }
        });

        jLabel1.setText("Codigo:");

        tblCli = new javax.swing.JTable(){
            public boolean  isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        tblCli.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Codigo", "Nome", "Status", "Telefone", "Endereço"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblCli.setFocusable(false);
        tblCli.getTableHeader().setResizingAllowed(false);
        tblCli.getTableHeader().setReorderingAllowed(false);
        tblCli.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblCliMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblCli);
        if (tblCli.getColumnModel().getColumnCount() > 0) {
            tblCli.getColumnModel().getColumn(0).setResizable(false);
        }

        txtCodCli.setEditable(false);
        txtCodCli.setEnabled(false);
        txtCodCli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodCliActionPerformed(evt);
            }
        });

        jLabel2.setText("*Nome:");

        txtNomeCli.setEnabled(false);
        txtNomeCli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNomeCliActionPerformed(evt);
            }
        });
        txtNomeCli.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNomeCliKeyPressed(evt);
            }
        });

        txtFoneCli.setEnabled(false);
        txtFoneCli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFoneCliActionPerformed(evt);
            }
        });
        txtFoneCli.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtFoneCliKeyPressed(evt);
            }
        });

        jLabel3.setText("*Telefone:");

        jLabel4.setText("Endereço:");

        txtEndereco.setEnabled(false);
        txtEndereco.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEnderecoKeyPressed(evt);
            }
        });

        jLabel5.setText("Numero:");

        txtNumCli.setEnabled(false);
        txtNumCli.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNumCliKeyPressed(evt);
            }
        });

        jLabel6.setText("Bairro:");

        txtBairoCli.setEnabled(false);
        txtBairoCli.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBairoCliKeyPressed(evt);
            }
        });

        jLabel7.setText("Cidade:");

        jLabel8.setText("CEP:");

        txtCepCli.setEnabled(false);
        txtCepCli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCepCliActionPerformed(evt);
            }
        });
        txtCepCli.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCepCliKeyPressed(evt);
            }
        });

        jLabel9.setText("Estado:");

        cboStatusCli.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Ativo", "Inativo" }));
        cboStatusCli.setEnabled(false);
        cboStatusCli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboStatusCliActionPerformed(evt);
            }
        });

        jLabel10.setText("Status:");

        jLabel11.setText("*CFP / CNPJ:");

        txtCnpjcli.setEnabled(false);
        txtCnpjcli.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCnpjcliKeyPressed(evt);
            }
        });

        jLabel12.setText("RG / Incs Estadual: ");

        txtInscCli.setEnabled(false);
        txtInscCli.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtInscCliKeyPressed(evt);
            }
        });

        jLabel13.setText("E-mail:");

        txtEmail.setEnabled(false);
        txtEmail.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEmailKeyPressed(evt);
            }
        });

        jLabel15.setText("Comp:");

        txtComplementoCli.setEnabled(false);
        txtComplementoCli.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtComplementoCliKeyPressed(evt);
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

        btnIncluir.setText("Incluir");
        btnIncluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIncluirActionPerformed(evt);
            }
        });
        btnIncluir.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnIncluirKeyPressed(evt);
            }
        });

        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        cboEstado.setEnabled(false);
        cboEstado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cboEstadoKeyPressed(evt);
            }
        });

        jLabel16.setText("* Campos Obrigatórios");
        jLabel16.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel16MouseClicked(evt);
            }
        });

        txtCidadeCli.setEnabled(false);
        txtCidadeCli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCidadeCliActionPerformed(evt);
            }
        });
        txtCidadeCli.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCidadeCliKeyPressed(evt);
            }
        });

        jLabel14.setText("Pesquisa:");

        txtPesquisaCli.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPesquisaCliKeyReleased(evt);
            }
        });

        btnAlterar.setText("Alterar");
        btnAlterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlterarActionPerformed(evt);
            }
        });

        jTextObservacao.setEnabled(false);
        jTextObservacao.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextObservacaoKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(jTextObservacao);

        jLabel17.setText("Observações:");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("*Tipo da pessoa"));

        chkFornecedor.setText("Fornecedor");
        chkFornecedor.setEnabled(false);
        chkFornecedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkFornecedorActionPerformed(evt);
            }
        });

        chkCliente.setText("Cliente");
        chkCliente.setEnabled(false);
        chkCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkClienteActionPerformed(evt);
            }
        });

        chkFuncionario.setText("Funcionario");
        chkFuncionario.setEnabled(false);
        chkFuncionario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkFuncionarioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(chkCliente)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(chkFornecedor)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(chkFuncionario)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(chkCliente)
                    .addComponent(chkFornecedor)
                    .addComponent(chkFuncionario))
                .addGap(0, 6, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(170, 170, 170)
                .addComponent(btnIncluir, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(170, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(jScrollPane1))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(jLabel14)
                        .addGap(18, 18, 18)
                        .addComponent(txtPesquisaCli))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel13)
                            .addComponent(jLabel9)
                            .addComponent(jLabel2)
                            .addComponent(jLabel15)
                            .addComponent(jLabel17)
                            .addComponent(jLabel1))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtCepCli, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel5)
                                .addGap(18, 18, 18)
                                .addComponent(txtNumCli, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(txtNomeCli, javax.swing.GroupLayout.PREFERRED_SIZE, 364, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel3))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(txtCodCli, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel10)))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cboStatusCli, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtFoneCli)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtComplementoCli, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel6)
                                .addGap(18, 18, 18)
                                .addComponent(txtBairoCli, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel7)
                                .addGap(18, 18, 18)
                                .addComponent(txtCidadeCli))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(cboEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel11)
                                .addGap(18, 18, 18)
                                .addComponent(txtCnpjcli, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel12)
                                .addGap(11, 11, 11)
                                .addComponent(txtInscCli))
                            .addComponent(jScrollPane2)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel16)))))
                .addGap(24, 24, 24))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel16)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addComponent(jLabel10)
                        .addComponent(cboStatusCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtCodCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel2)
                    .addComponent(txtNomeCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(txtFoneCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(txtNumCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtCepCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel8)
                        .addComponent(jLabel4)
                        .addComponent(txtEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel15)
                    .addComponent(txtComplementoCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(txtBairoCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(txtCidadeCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel9)
                    .addComponent(cboEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(txtCnpjcli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(txtInscCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(btnSalvar)
                    .addComponent(btnIncluir)
                    .addComponent(btnCancelar)
                    .addComponent(btnAlterar))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(txtPesquisaCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17))
        );

        setBounds(0, 0, 770, 601);
    }// </editor-fold>//GEN-END:initComponents

    private void txtCepCliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCepCliActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCepCliActionPerformed

    private void txtNomeCliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeCliActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNomeCliActionPerformed

    private void cboStatusCliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboStatusCliActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboStatusCliActionPerformed

    private void tblCliMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCliMouseClicked
        // TODO add your handling code here:
        desabilitacampos();
        setarcampos();
    }//GEN-LAST:event_tblCliMouseClicked

    private void txtFoneCliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFoneCliActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFoneCliActionPerformed

    private void btnIncluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIncluirActionPerformed
        // TODO add your handling code here:
        limpacampos();
        habilitacampos();

    }//GEN-LAST:event_btnIncluirActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // TODO add your handling code here:
        limpacampos();
        desabilitacampos();
        btnIncluir.setEnabled(true);
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
        // TODO add your handling code here:
        String vazio = txtCodCli.getText();
        if (vazio.equals("")) {
            incluir();
        } else {
            alterar();
        }
    }//GEN-LAST:event_btnSalvarActionPerformed

    private void formInternalFrameActivated(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameActivated
        // TODO add your handling code here:
        carregaestaodcli();
        cliente = "1";
        fornecedor = "0";
        funcionario = "0";
    }//GEN-LAST:event_formInternalFrameActivated

    private void jLabel16MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel16MouseClicked
        // TODO add your handling code here:


    }//GEN-LAST:event_jLabel16MouseClicked

    private void txtCidadeCliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCidadeCliActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCidadeCliActionPerformed

    private void txtPesquisaCliKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesquisaCliKeyReleased
        // TODO add your handling code here:
        consultaAvancada();
    }//GEN-LAST:event_txtPesquisaCliKeyReleased

    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarActionPerformed
        // TODO add your handling code here:
        String vazio = txtCodCli.getText();
        if (vazio.equals("")) {
            JOptionPane.showMessageDialog(null, "Selecione um cliente.");

        } else {
            habilitacampos();
            btnIncluir.setEnabled(false);
        }
    }//GEN-LAST:event_btnAlterarActionPerformed

    private void btnSalvarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnSalvarKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String vazio = txtCodCli.getText();
            if (vazio.equals("")) {
                incluir();
            } else {
                alterar();
            }
        }
    }//GEN-LAST:event_btnSalvarKeyPressed

    private void txtNomeCliKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNomeCliKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtFoneCli.requestFocus();
        }
    }//GEN-LAST:event_txtNomeCliKeyPressed

    private void txtFoneCliKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFoneCliKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtCepCli.requestFocus();
        }
    }//GEN-LAST:event_txtFoneCliKeyPressed

    private void txtEnderecoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEnderecoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtNumCli.requestFocus();
        }
    }//GEN-LAST:event_txtEnderecoKeyPressed

    private void txtNumCliKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumCliKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtComplementoCli.requestFocus();
        }
    }//GEN-LAST:event_txtNumCliKeyPressed

    private void txtComplementoCliKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtComplementoCliKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtBairoCli.requestFocus();
        }
    }//GEN-LAST:event_txtComplementoCliKeyPressed

    private void txtBairoCliKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBairoCliKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtCidadeCli.requestFocus();
        }
    }//GEN-LAST:event_txtBairoCliKeyPressed

    private void txtCidadeCliKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCidadeCliKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cboEstado.requestFocus();
        }
    }//GEN-LAST:event_txtCidadeCliKeyPressed

    private void txtCepCliKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCepCliKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtEndereco.requestFocus();
        }
    }//GEN-LAST:event_txtCepCliKeyPressed

    private void cboEstadoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cboEstadoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtCnpjcli.requestFocus();
        }
    }//GEN-LAST:event_cboEstadoKeyPressed

    private void txtCnpjcliKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCnpjcliKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtInscCli.requestFocus();
        }

    }//GEN-LAST:event_txtCnpjcliKeyPressed

    private void txtInscCliKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtInscCliKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtEmail.requestFocus();
        }
    }//GEN-LAST:event_txtInscCliKeyPressed

    private void txtEmailKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmailKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jTextObservacao.requestFocus();
        }
    }//GEN-LAST:event_txtEmailKeyPressed

    private void btnIncluirKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnIncluirKeyPressed
        // TODO add your handling code here:
        limpacampos();
        habilitacampos();
    }//GEN-LAST:event_btnIncluirKeyPressed

    private void txtCodCliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodCliActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodCliActionPerformed

    private void chkClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkClienteActionPerformed
        // TODO add your handling code here:
        if (chkCliente.isSelected()) {
            cliente = "1";
        } else {
            cliente = "0";
        }

    }//GEN-LAST:event_chkClienteActionPerformed

    private void chkFornecedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkFornecedorActionPerformed
        // TODO add your handling code here:
        if (chkFornecedor.isSelected()) {
            fornecedor = "1";
        } else {
            fornecedor = "0";
        }

    }//GEN-LAST:event_chkFornecedorActionPerformed

    private void chkFuncionarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkFuncionarioActionPerformed
        // TODO add your handling code here:
        if (chkFuncionario.isSelected()) {
            funcionario = "1";

        } else {
            funcionario = "0";
        }

    }//GEN-LAST:event_chkFuncionarioActionPerformed

    private void jTextObservacaoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextObservacaoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_TAB) {
            btnSalvar.requestFocus();
        }
    }//GEN-LAST:event_jTextObservacaoKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAlterar;
    private javax.swing.JButton btnCancelar;
    public static javax.swing.JButton btnIncluir;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JComboBox<String> cboEstado;
    private javax.swing.JComboBox<String> cboStatusCli;
    private javax.swing.JCheckBox chkCliente;
    private javax.swing.JCheckBox chkFornecedor;
    private javax.swing.JCheckBox chkFuncionario;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextPane jTextObservacao;
    private javax.swing.JTable tblCli;
    private javax.swing.JTextField txtBairoCli;
    private javax.swing.JTextField txtCepCli;
    private javax.swing.JTextField txtCidadeCli;
    private javax.swing.JTextField txtCnpjcli;
    private javax.swing.JTextField txtCodCli;
    private javax.swing.JTextField txtComplementoCli;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtEndereco;
    private javax.swing.JTextField txtFoneCli;
    private javax.swing.JTextField txtInscCli;
    private javax.swing.JTextField txtNomeCli;
    private javax.swing.JTextField txtNumCli;
    private javax.swing.JTextField txtPesquisaCli;
    // End of variables declaration//GEN-END:variables
}
