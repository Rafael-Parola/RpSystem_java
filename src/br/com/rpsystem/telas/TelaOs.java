/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package br.com.rpsystem.telas;

import java.sql.*;
import br.com.rpsystem.dal.ModuloConexao;

import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;
import br.com.rpsystem.telas.TelaPrincipal;
import java.util.HashMap;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Rafael Veiga
 */
public class TelaOs extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    // armazenando o texto de acor do com radio butom
    public String tipo;

    /**
     * Creates new form TelaOs
     */
    public TelaOs() {
        initComponents();

        conexao = ModuloConexao.conector();

    }

    private void consultaAvancada() {
        String sql = "select idclientes as Codigo, nomeCliente as Nome, "
                + "telefone as Telefone from clientes where nomeCliente like ?";
        try {
            pst = conexao.prepareStatement(sql);
            // Passando o conteudo do campo pesquisar para o SQL
            pst.setString(1, "%" + txtPesquisaCliOS.getText() + "%");
            rs = pst.executeQuery();
            // A linha abixo usa a rs2xml.jar 
            tblListarCli.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void preenchetecino() {
        String vendedor = "select nomeVendedor from vendedores where demitido= 'Não'";
        conexao = ModuloConexao.conector();
        try {
            pst = conexao.prepareStatement(vendedor);
            rs = pst.executeQuery();
            if (rs.next()) {
                do {
                    cboVendedor.addItem(rs.getString("nomeVendedor"));
                } while (rs.next());

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

        public void preenchestatus() {
        String status = "select descricao from statusos where excluido= 0";
        conexao = ModuloConexao.conector();
        try {
            pst = conexao.prepareStatement(status);
            rs = pst.executeQuery();
            if (rs.next()) {
                do {
                    cboSitacao.addItem(rs.getString("descricao"));
                } while (rs.next());

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }
        
    public void setarcampos() {

        int setar = tblListarCli.getSelectedRow();
        txtCodCliOs.setText(tblListarCli.getModel().getValueAt(setar, 0).toString());
    }

    private void limparcampos() {
        txtNumOS.setText(null);
        txtDataOS.setText(null);
        txtEquipamento.setText(null);
        txtDefeito.setText(null);
        txtServico.setText(null);
        // txtTecnico.setText(null);
        txtValor.setText(null);
        txtCodCliOs.setText(null);
        txtPesquisaCliOS.setText("Pesquisa cliente");
        

    }

    private void bloqueiacampos() {
        rbtOrdemDeServico.setEnabled(false);
        rbtOrcamento.setEnabled(false);
        cboSitacao.setEnabled(false);
        txtEquipamento.setEnabled(false);
        txtDefeito.setEnabled(false);
        txtServico.setEnabled(false);
        //   txtTecnico.setEnabled(false);
        cboVendedor.setEnabled(false);
        txtValor.setEnabled(false);
        txtCodCliOs.setEnabled(false);
        txtPesquisaCliOS.setEnabled(false);
        txtPesquisaCliOS.setText("Pesquisa cliente");
    }

    private void liberacampos() {

        rbtOrdemDeServico.setEnabled(true);
        rbtOrcamento.setEnabled(true);
        txtEquipamento.setEnabled(true);
        txtDefeito.setEnabled(true);
        txtServico.setEnabled(true);
        cboVendedor.setEnabled(true);
        cboSitacao.setEnabled(true);
        // txtTecnico.setEnabled(true);
        txtValor.setEnabled(true);
        txtCodCliOs.setEnabled(true);
        txtPesquisaCliOS.setEnabled(true);
        txtPesquisaCliOS.setText("Pesquisa cliente");
    }

    //cadastra os 
    private void emitiros() {
        String sql = "insert into ordemservico (tipo,situacao,equipamento,defeito,servico,tecnico,valor,"
                + "clientes_idclientes,usuarios_id) values (?,?,?,?,?,?,?,?,?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, tipo);
            pst.setString(2, cboSitacao.getSelectedItem().toString());
            pst.setString(3, txtEquipamento.getText());
            pst.setString(4, txtDefeito.getText());
            pst.setString(5, txtServico.getText());
            pst.setString(6, cboVendedor.getSelectedItem().toString());
            // pst.setString(6, txtTecnico.getText());
            pst.setString(7, txtValor.getText().replace(",", "."));
            pst.setString(8, txtCodCliOs.getText());
            pst.setString(9, TelaPrincipal.lblCodUsoPrincipal.getText());

            if ((txtCodCliOs.getText().isEmpty()) || (txtEquipamento.getText().isEmpty()) || (txtDefeito.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha os campos obrigatórios.");

            } else {
                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "OS emitida com sucasso.");
                    recuperaos();
                    imprimeos();
                    limparcampos();
                    bloqueiacampos();
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void alteraros() {
        String alteraros = "update ordemservico set tipo=?,situacao=?,equipamento=?,defeito=?,"
                + "servico=?,tecnico=?,valor=? ,clientes_idclientes = ?, usuarios_id =? where os = ? ";
        try {
            pst = conexao.prepareStatement(alteraros);
            pst.setString(1, tipo);
            pst.setString(2, cboSitacao.getSelectedItem().toString());
            pst.setString(3, txtEquipamento.getText());
            pst.setString(4, txtDefeito.getText());
            pst.setString(5, txtServico.getText());
            pst.setString(6, cboVendedor.getSelectedItem().toString());
            // pst.setString(6, txtTecnico.getText());
            pst.setString(7, txtValor.getText());
            pst.setString(8, txtCodCliOs.getText());
            pst.setString(9, TelaPrincipal.lblCodUsoPrincipal.getText());
            pst.setString(10, txtNumOS.getText());
            int altera = pst.executeUpdate();
            if (altera > 0) {
                JOptionPane.showMessageDialog(null, "OS Alterada com sucesso");
                bloqueiacampos();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void geralog() {

        //String url = "jdbc:mysql://"+caminho+":3306/"+banco;
        String os = txtNumOS.getText();
        String usuariologado = TelaPrincipal.lblUsuario.getText();
        String geralog = "insert into log (obs,user) values ('excluiu os " + os + "','Usuario = " + usuariologado + "')";
        try {
            pst = conexao.prepareStatement(geralog);
            pst.execute(geralog);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void excluios() {
        String excluios = "update ordemservico set excluido = 1 where os = ?";

        try {

            int exclui = JOptionPane.showConfirmDialog(null, "Tem Certeza que deseja excluir?", "Alerta", JOptionPane.YES_NO_OPTION);
            if (exclui == JOptionPane.YES_OPTION) {
                pst = conexao.prepareStatement(excluios);
                pst.setString(1, txtNumOS.getText());

                int excluilogico = pst.executeUpdate();
                if (excluilogico > 0) {
                    geralog();
                    JOptionPane.showMessageDialog(null, "Excluido com sucesso");
                    limparcampos();
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void imprimeos() {
        int confirma = JOptionPane.showConfirmDialog(null, "Confirma a impressão desta Os? ", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            try {
                // Usando a classe hasmap para criar o filtro
                HashMap os = new HashMap();
                os.put("os", Integer.parseInt(txtNumOS.getText()));
                // usando o jasper para gerar o relatório
                JasperPrint print = JasperFillManager.fillReport("C:/RpSystem/reports/os.jasper", os, conexao);
                JasperViewer.viewReport(print, false);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    private void recuperaos() {
        String sql = "select max(os) from ordemservico";
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                txtNumOS.setText(rs.getString(1));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    // pesquisando os 
/*
    private void pesquisaros() {
        String num_os = JOptionPane.showInputDialog("Numero da OS");
        String sql = "select * from ordemservico where os =" + num_os;
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                txtNumOS.setText(rs.getString(1));
                txtDataOS.setText(rs.getString(2));
                String rbtTipo = rs.getString(3);
                if (rbtTipo.equals("OS")) {
                    rbtOrdemDeServico.setSelected(true);
                    tipo= "OS";
                } else {
                    rbdOrcamento.setSelected(true);
                    tipo = "Orçamento";
                }
                 
//cboSitacao.setSelectedItem(rs.getString(3));
            } else {
                JOptionPane.showMessageDialog(null, "OS não encontrada");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    /*
     */
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtNumOS = new javax.swing.JTextField();
        txtDataOS = new javax.swing.JTextField();
        rbtOrcamento = new javax.swing.JRadioButton();
        rbtOrdemDeServico = new javax.swing.JRadioButton();
        jLabel3 = new javax.swing.JLabel();
        cboSitacao = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        txtPesquisaCliOS = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblListarCli = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        txtCodCliOs = new javax.swing.JTextField();
        btnIncluir = new javax.swing.JButton();
        btnPesquisaOs = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        btnExcluiOs = new javax.swing.JButton();
        btnImprimeOs = new javax.swing.JButton();
        pnDadosOs = new javax.swing.JPanel();
        txtEquipamento = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtDefeito = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtServico = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtValor = new javax.swing.JTextField();
        cboVendedor = new javax.swing.JComboBox<>();
        btnAltera = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("Ordem de Serviço");
        setMaximumSize(new java.awt.Dimension(856, 547));
        setMinimumSize(new java.awt.Dimension(856, 547));
        setPreferredSize(new java.awt.Dimension(856, 547));
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

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setText("Numero OS:");

        jLabel2.setText("Data:");

        txtNumOS.setEditable(false);
        txtNumOS.setEnabled(false);

        txtDataOS.setEditable(false);
        txtDataOS.setEnabled(false);

        buttonGroup1.add(rbtOrcamento);
        rbtOrcamento.setText("Orçamento");
        rbtOrcamento.setEnabled(false);
        rbtOrcamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtOrcamentoActionPerformed(evt);
            }
        });

        buttonGroup1.add(rbtOrdemDeServico);
        rbtOrdemDeServico.setText("Ordem de Serviço");
        rbtOrdemDeServico.setEnabled(false);
        rbtOrdemDeServico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtOrdemDeServicoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1)
                    .addComponent(rbtOrcamento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtNumOS))
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(rbtOrdemDeServico)
                            .addComponent(jLabel2))
                        .addGap(0, 59, Short.MAX_VALUE))
                    .addComponent(txtDataOS))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNumOS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDataOS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbtOrcamento)
                    .addComponent(rbtOrdemDeServico))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        jLabel3.setText("Situação:");

        cboSitacao.setEnabled(false);

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txtPesquisaCliOS.setText("Pesquisa de cliente");
        txtPesquisaCliOS.setToolTipText("Digite o cliente");
        txtPesquisaCliOS.setEnabled(false);
        txtPesquisaCliOS.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtPesquisaCliOSMouseClicked(evt);
            }
        });
        txtPesquisaCliOS.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPesquisaCliOSKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPesquisaCliOSKeyReleased(evt);
            }
        });

        tblListarCli.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Id", "Nome", "Fone"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblListarCli.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblListarCliMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblListarCli);

        jLabel4.setText("*Id:");

        txtCodCliOs.setEditable(false);
        txtCodCliOs.setEnabled(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(txtPesquisaCliOS, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(txtCodCliOs, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 436, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(0, 10, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(txtCodCliOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtPesquisaCliOS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );

        btnIncluir.setText("Incluir");
        btnIncluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIncluirActionPerformed(evt);
            }
        });

        btnPesquisaOs.setText("Pesquisar");
        btnPesquisaOs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPesquisaOsActionPerformed(evt);
            }
        });

        btnSalvar.setText("Salvar");
        btnSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvarActionPerformed(evt);
            }
        });

        btnExcluiOs.setText("Excluir");
        btnExcluiOs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcluiOsActionPerformed(evt);
            }
        });

        btnImprimeOs.setText("Imprimir");
        btnImprimeOs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImprimeOsActionPerformed(evt);
            }
        });

        pnDadosOs.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txtEquipamento.setEnabled(false);

        jLabel6.setText("*Defeito:");

        txtDefeito.setEnabled(false);

        jLabel5.setText("*Equipamento:");

        jLabel7.setText("Serviço:");

        txtServico.setEnabled(false);

        jLabel8.setText("Tecnico:");

        jLabel9.setText("Valor:");

        txtValor.setText("0");
        txtValor.setEnabled(false);

        cboVendedor.setEnabled(false);

        javax.swing.GroupLayout pnDadosOsLayout = new javax.swing.GroupLayout(pnDadosOs);
        pnDadosOs.setLayout(pnDadosOsLayout);
        pnDadosOsLayout.setHorizontalGroup(
            pnDadosOsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnDadosOsLayout.createSequentialGroup()
                .addGap(117, 117, 117)
                .addGroup(pnDadosOsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pnDadosOsLayout.createSequentialGroup()
                        .addGroup(pnDadosOsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel5))
                        .addGap(23, 23, 23)
                        .addGroup(pnDadosOsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtDefeito)
                            .addComponent(txtEquipamento)))
                    .addGroup(pnDadosOsLayout.createSequentialGroup()
                        .addGroup(pnDadosOsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnDadosOsLayout.createSequentialGroup()
                                .addGap(33, 33, 33)
                                .addComponent(jLabel7)
                                .addGap(23, 23, 23))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnDadosOsLayout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addGap(18, 18, 18)))
                        .addGroup(pnDadosOsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnDadosOsLayout.createSequentialGroup()
                                .addComponent(cboVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel9)
                                .addGap(18, 18, 18)
                                .addComponent(txtValor, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtServico, javax.swing.GroupLayout.PREFERRED_SIZE, 367, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(192, Short.MAX_VALUE))
        );
        pnDadosOsLayout.setVerticalGroup(
            pnDadosOsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnDadosOsLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(pnDadosOsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtEquipamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnDadosOsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtDefeito, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnDadosOsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtServico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnDadosOsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtValor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        btnAltera.setText("Alterar");
        btnAltera.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlteraActionPerformed(evt);
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
                        .addGap(152, 152, 152)
                        .addComponent(btnIncluir)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAltera)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancelar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPesquisaOs)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSalvar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnExcluiOs)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnImprimeOs))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(4, 4, 4)
                                        .addComponent(jLabel3)
                                        .addGap(18, 18, 18)
                                        .addComponent(cboSitacao, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(22, 22, 22)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(pnDadosOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(34, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(cboSitacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(pnDadosOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnPesquisaOs)
                        .addComponent(btnIncluir)
                        .addComponent(btnAltera)
                        .addComponent(btnCancelar))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnSalvar)
                        .addComponent(btnExcluiOs))
                    .addComponent(btnImprimeOs))
                .addGap(34, 34, 34))
        );

        setBounds(0, 0, 856, 482);
    }// </editor-fold>//GEN-END:initComponents

    private void txtPesquisaCliOSMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPesquisaCliOSMouseClicked
        // TODO add your handling code here:
        txtPesquisaCliOS.setText(null);
    }//GEN-LAST:event_txtPesquisaCliOSMouseClicked

    private void txtPesquisaCliOSKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesquisaCliOSKeyReleased
        // TODO add your handling code here:
        consultaAvancada();
    }//GEN-LAST:event_txtPesquisaCliOSKeyReleased

    private void tblListarCliMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblListarCliMouseClicked
        // TODO add your handling code here:
        setarcampos();
    }//GEN-LAST:event_tblListarCliMouseClicked

    private void rbtOrcamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtOrcamentoActionPerformed
        // TODO add your handling code here:
        tipo = "Orçamento";
    }//GEN-LAST:event_rbtOrcamentoActionPerformed

    private void rbtOrdemDeServicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtOrdemDeServicoActionPerformed
        // TODO add your handling code here:
        tipo = "OS";

    }//GEN-LAST:event_rbtOrdemDeServicoActionPerformed

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        // TODO add your handling code here:
        rbtOrcamento.setSelected(true);
        tipo = "Orçamento";
        //consultaAvancada();
        preenchetecino();
        preenchestatus();

    }//GEN-LAST:event_formInternalFrameOpened

    private void btnIncluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIncluirActionPerformed
        // TODO add your handling code here:
       limparcampos();
        liberacampos();
        
    }//GEN-LAST:event_btnIncluirActionPerformed

    private void btnPesquisaOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPesquisaOsActionPerformed
        // TODO add your handling code here:
        TelaPesquisa pesquisa = new TelaPesquisa();
        pesquisa.setVisible(true);
    }//GEN-LAST:event_btnPesquisaOsActionPerformed

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
        // TODO add your handling code here:
        String numeroos = txtNumOS.getText();
        if (numeroos.equals("")) {

            emitiros();
            limparcampos();
        } else {
            alteraros();
        }
    }//GEN-LAST:event_btnSalvarActionPerformed

    private void btnAlteraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlteraActionPerformed
        // TODO add your handling code here:
        String codos = txtNumOS.getText();
        if (codos.equals("")) {
            JOptionPane.showMessageDialog(null, "Selecione uma OS.");

        } else {
            liberacampos();
        }

    }//GEN-LAST:event_btnAlteraActionPerformed

    private void btnExcluiOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluiOsActionPerformed
        // TODO add your handling code here:
        String codos = txtNumOS.getText();
        if (codos.equals("")) {
            JOptionPane.showMessageDialog(null, "Selecione uma OS.");

        } else {
            excluios();
        }

    }//GEN-LAST:event_btnExcluiOsActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // TODO add your handling code here:
        bloqueiacampos();

    }//GEN-LAST:event_btnCancelarActionPerformed

    private void txtPesquisaCliOSKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesquisaCliOSKeyPressed
        // TODO add your handling code here:
      //  txtPesquisaCliOS.setText(null);
    }//GEN-LAST:event_txtPesquisaCliOSKeyPressed

    private void btnImprimeOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimeOsActionPerformed
        // TODO add your handling code here:
        String codos = txtNumOS.getText();
        if (codos.equals("")) {
            JOptionPane.showMessageDialog(null, "Selecione uma OS.");
        } else {
            imprimeos();
        }

    }//GEN-LAST:event_btnImprimeOsActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAltera;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnExcluiOs;
    private javax.swing.JButton btnImprimeOs;
    private javax.swing.JButton btnIncluir;
    private javax.swing.JButton btnPesquisaOs;
    private javax.swing.JButton btnSalvar;
    private javax.swing.ButtonGroup buttonGroup1;
    public static javax.swing.JComboBox<String> cboSitacao;
    public static javax.swing.JComboBox<String> cboVendedor;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    public static javax.swing.JPanel pnDadosOs;
    public static javax.swing.JRadioButton rbtOrcamento;
    public static javax.swing.JRadioButton rbtOrdemDeServico;
    public static javax.swing.JTable tblListarCli;
    public static javax.swing.JTextField txtCodCliOs;
    public static javax.swing.JTextField txtDataOS;
    public static javax.swing.JTextField txtDefeito;
    public static javax.swing.JTextField txtEquipamento;
    public static javax.swing.JTextField txtNumOS;
    private javax.swing.JTextField txtPesquisaCliOS;
    public static javax.swing.JTextField txtServico;
    public static javax.swing.JTextField txtValor;
    // End of variables declaration//GEN-END:variables
}
