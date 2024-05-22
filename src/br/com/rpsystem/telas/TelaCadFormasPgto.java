/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package br.com.rpsystem.telas;

import br.com.rpsystem.dal.ModuloConexao;
import br.com.rpsystem.funcoes.CentralizaForm;
import br.com.rpsystem.funcoes.Funcoes;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

/**
 *
 * @author Rafael Veiga
 */
public class TelaCadFormasPgto extends javax.swing.JInternalFrame {

    Object[] options = {"Sim", "Não"};
    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public String tipo;
    public int status;

    /**
     * Creates new form TelaCadFormasPgto
     */
    public TelaCadFormasPgto() {
        initComponents();
        conexao = ModuloConexao.conector();
        Funcoes conta = new Funcoes(cboCcFormaPgto);
        conta.f_carregaconta(cboCcFormaPgto);

    }

    public void liberacampos() {
        txtDescricaoFormaPgto.setEnabled(true);
        rbtDinheiro.setEnabled(true);
        rbtCartao.setEnabled(true);
        rbtBoleto.setEnabled(true);
        rbtCheque.setEnabled(true);
        rbtPgtoDigital.setEnabled(true);
        rbtOutros.setEnabled(true);
        txtVencPrimeiraparcela.setEnabled(true);
        txtIntervalorVencimentoDemaisparcelas.setEnabled(true);
        txtNumMaxFormaPgto.setEnabled(true);
        txtVlrMinParcelaFormaPgto.setEnabled(true);
        txtTaxaTransPrimeiraFormaPgto.setEnabled(true);
        txtTaxaTransDemasiParcFormaPgto.setEnabled(true);
        txtFornecedorFormaPgto.setEnabled(true);
        cboCcFormaPgto.setEnabled(true);
        cboBancoFormaPgto.setEnabled(true);
        cboStatusFormaPgto.setEnabled(true);
        btnAlterarFormaPgto.setEnabled(false);
        btnExcluirFormaPgto.setEnabled(false);
        btnInclurFormaPgto.setEnabled(false);

        txtDescricaoFormaPgto.requestFocus();
    }

    public void bloqueiacampos() {
        txtDescricaoFormaPgto.setEnabled(false);
        rbtDinheiro.setEnabled(false);
        rbtCartao.setEnabled(false);
        rbtBoleto.setEnabled(false);
        rbtCheque.setEnabled(false);
        rbtOutros.setEnabled(false);
        rbtPgtoDigital.setEnabled(false);
        txtVencPrimeiraparcela.setEnabled(false);
        txtIntervalorVencimentoDemaisparcelas.setEnabled(false);
        txtNumMaxFormaPgto.setEnabled(false);
        txtVlrMinParcelaFormaPgto.setEnabled(false);
        txtTaxaTransPrimeiraFormaPgto.setEnabled(false);
        txtTaxaTransDemasiParcFormaPgto.setEnabled(false);
        txtFornecedorFormaPgto.setEnabled(false);
        cboCcFormaPgto.setEnabled(false);
        cboBancoFormaPgto.setEnabled(false);
        cboStatusFormaPgto.setEnabled(false);
        btnAlterarFormaPgto.setEnabled(true);
        btnExcluirFormaPgto.setEnabled(true);
        btnInclurFormaPgto.setEnabled(true);

    }

    public void limpacampos() {
        txtDescricaoFormaPgto.setText("");
        txtCodFormaPgto.setText("");
        txtVencPrimeiraparcela.setText("0");
        txtIntervalorVencimentoDemaisparcelas.setText("0");
        txtTaxaTransPrimeiraFormaPgto.setText("0");
        txtTaxaTransDemasiParcFormaPgto.setText("0");
        txtNumMaxFormaPgto.setText("0");
        txtVlrMinParcelaFormaPgto.setText("0");
        txtFornecedorFormaPgto.setText("0");
        txtNomeForncedorFormaPgto.setText("");
        rbtDinheiro.setSelected(true);
        bloqueiaparcelamento();
    }

    private void bloqueiaparcelamento() {
        txtVencPrimeiraparcela.setText("0");
        txtIntervalorVencimentoDemaisparcelas.setText("0");
        txtTaxaTransPrimeiraFormaPgto.setText("0");
        txtTaxaTransDemasiParcFormaPgto.setText("0");
        txtNumMaxFormaPgto.setText("0");
        txtVlrMinParcelaFormaPgto.setText("0");
        txtVencPrimeiraparcela.setEnabled(false);
        txtIntervalorVencimentoDemaisparcelas.setEnabled(false);
        txtNumMaxFormaPgto.setEnabled(false);
        txtVlrMinParcelaFormaPgto.setEnabled(false);
        txtTaxaTransPrimeiraFormaPgto.setEnabled(false);
        txtTaxaTransDemasiParcFormaPgto.setEnabled(false);
        txtFornecedorFormaPgto.setEnabled(false);

        cboBancoFormaPgto.setEnabled(false);

    }

    private void habilitaparcelamento() {
        txtVencPrimeiraparcela.setEnabled(true);
        txtIntervalorVencimentoDemaisparcelas.setEnabled(true);
        txtNumMaxFormaPgto.setEnabled(true);
        txtVlrMinParcelaFormaPgto.setEnabled(true);
        txtTaxaTransPrimeiraFormaPgto.setEnabled(true);
        txtTaxaTransDemasiParcFormaPgto.setEnabled(true);
        txtFornecedorFormaPgto.setEnabled(true);
        cboCcFormaPgto.setEnabled(true);
        cboBancoFormaPgto.setEnabled(true);

    }

    private void insereformapgto() {
//        
//          classificacaocompleta = cboClassificacao.getSelectedItem().toString();
//        codclassificacao = Integer.parseInt(classificacaocompleta.substring(0, 2).replace(" ", "").replace("-", ""));

        String conta = cboCcFormaPgto.getSelectedItem().toString();
        int codconta = Integer.parseInt(conta.substring(0, 2).replace(" ", "").replace("-", ""));

        String inserir = "INSERT INTO formaspagamento (codigo, descricao,status,tipo,vencimentoprimeiraparcela,vencimentodemaisparcelas,\n"
                + "nummaximoparcelas,taxaprimeiraparcela,taxademaisparcelas,fornecedor,banco,contacorrente,usuarios_id,empresa_codempresa,valorminimoparcelas)\n"
                + "SELECT 1 + coalesce((SELECT max(codigo) FROM formaspagamento), 0), ?,?,?,?,?,?,?,?,?,?,?,?,?,?";
        try {
            pst = conexao.prepareStatement(inserir);

            pst.setString(1, txtDescricaoFormaPgto.getText());
            pst.setDouble(2, status);
            pst.setString(3, tipo);
            pst.setString(4, txtVencPrimeiraparcela.getText());
            pst.setString(5, txtIntervalorVencimentoDemaisparcelas.getText());
            pst.setString(6, txtNumMaxFormaPgto.getText());
            pst.setString(7, txtTaxaTransPrimeiraFormaPgto.getText().replace(",", "."));
            pst.setString(8, txtTaxaTransDemasiParcFormaPgto.getText().replace(",", "."));
            pst.setString(9, txtFornecedorFormaPgto.getText());
            pst.setString(10, cboBancoFormaPgto.getSelectedItem().toString());
            pst.setInt(11, codconta);
            pst.setString(12, TelaPrincipal.lblCodUsoPrincipal.getText());
            pst.setString(13, TelaPrincipal.lblcodEmpresa.getText());
            pst.setString(14, txtVlrMinParcelaFormaPgto.getText().replace(",", "."));

            if (txtDescricaoFormaPgto.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Preencha a descrição.");
            } else {

                int adiciona = pst.executeUpdate();
                if (adiciona > 0) {

                    JOptionPane.showMessageDialog(null, "Forma de pagamento adicionada");
                    bloqueiacampos();
                    limpacampos();

                    //consultaAvancada();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void pesquisaformapgto() {
        String pesquisa = "select * from formaspagamento where codigo = ? and empresa_codempresa = " + TelaPrincipal.lblcodEmpresa.getText();
        try {
            pst = conexao.prepareStatement(pesquisa);
            pst.setString(1, txtCodFormaPgto.getText());
            rs = pst.executeQuery();
            if (rs.next()) {
                txtDescricaoFormaPgto.setText(rs.getString(2));
                cboStatusFormaPgto.setSelectedItem(rs.getString(3).toString());
                
                String tipo = (rs.getString(4));
                if (tipo != null) {

                    if (tipo.equals("Dinheiro")) {
                        rbtDinheiro.setSelected(true);
                        tipo = "Dinheiro";
                    }
                    if (tipo.equals("Cartão")) {
                        rbtCartao.setSelected(true);
                        tipo = "Cartão";
                    }
                    if (tipo.equals("Cheque")) {
                        rbtCheque.setSelected(true);
                        tipo = "Cheque";
                    }
                    if (tipo.equals("Pgto Digital")) {
                        rbtPgtoDigital.setSelected(true);
                        tipo = "Pgto Digital";
                    }
                    if (tipo.equals("Boleto")) {
                        rbtBoleto.setSelected(true);
                        tipo = "Boleto";
                    }
                    if (tipo.equals("Outros")) {
                        rbtOutros.setSelected(true);
                        tipo = "Outros";
                    }
                }
                txtVencPrimeiraparcela.setText(rs.getString(5));
                txtIntervalorVencimentoDemaisparcelas.setText(rs.getString(6));
                txtNumMaxFormaPgto.setText(rs.getString(7));
                txtVlrMinParcelaFormaPgto.setText(rs.getString(8).replace(".", ","));
                txtTaxaTransPrimeiraFormaPgto.setText(rs.getString(9).replace(".", ","));
                txtTaxaTransDemasiParcFormaPgto.setText(rs.getString(10).replace(".", ","));
                txtFornecedorFormaPgto.setText(rs.getString(11));
                cboBancoFormaPgto.setSelectedItem(rs.getString(12).toString());
                //cboCcFormaPgto.setSelectedItem(rs.getString(13).toString());
                String conta = rs.getString(13);
                if (conta != null) {
                    for (int i = 0; i < cboCcFormaPgto.getItemCount(); i++) {
                        String item = cboCcFormaPgto.getItemAt(i);
                        if (item.startsWith(conta)) {
                            cboCcFormaPgto.setSelectedIndex(i);
                            break;
                        }
                    }
                }

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void removeformapgto() {
        String sql = "update formaspagamento set status = 1 "
                + "where codigo = ? and empresa_codempresa= " + TelaPrincipal.lblcodEmpresa.getText();
        try {
            int confirma = JOptionPane.showOptionDialog(null, "Deseja excluir a forma de pagamento? ", "Atenção", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
            if (confirma == JOptionPane.YES_OPTION) {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtCodFormaPgto.getText());
                int exclui = pst.executeUpdate();
                if (exclui > 0) {
                    JOptionPane.showMessageDialog(null, "Excluído com sucesso");
                    limpacampos();

                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void alteraforma() {

        String conta = cboCcFormaPgto.getSelectedItem().toString();
        int codconta = Integer.parseInt(conta.substring(0, 2).replace(" ", "").replace("-", ""));

        String sql = "UPDATE `formaspagamento` SET\n"
                + "`descricao` = ?,`status` = ?,`tipo` = ?,`vencimentoprimeiraparcela` = ?,\n"
                + "`vencimentodemaisparcelas` = ?,`nummaximoparcelas` =?,`valorminimoparcelas` = ?,\n"
                + "`taxaprimeiraparcela` = ?,`taxademaisparcelas` =?,`fornecedor` = ?,`banco` =?,\n"
                + "`contacorrente` =?,`usuarios_id` =?,`empresa_codempresa` = ?\n"
                + "WHERE `codigo` = ?;";
        try {
            pst = conexao.prepareStatement(sql);

            pst.setString(1, txtDescricaoFormaPgto.getText());
            pst.setDouble(2, status);
            pst.setString(3, tipo);
            pst.setString(4, txtVencPrimeiraparcela.getText().replace(",", "."));
            pst.setString(5, txtIntervalorVencimentoDemaisparcelas.getText().replace(",", "."));
            pst.setString(6, txtNumMaxFormaPgto.getText().replace(",", "."));
            pst.setString(7, txtVlrMinParcelaFormaPgto.getText().replace(",", "."));
            pst.setString(8, txtTaxaTransPrimeiraFormaPgto.getText().replace(",", "."));
            pst.setString(9, txtTaxaTransDemasiParcFormaPgto.getText().replace(",", "."));
            pst.setString(10, txtFornecedorFormaPgto.getText().replace(",", ""));
            pst.setString(11, cboBancoFormaPgto.getSelectedItem().toString());
            pst.setInt(12, codconta);
            pst.setString(13, TelaPrincipal.lblCodUsoPrincipal.getText());
            pst.setString(14, TelaPrincipal.lblcodEmpresa.getText());
            pst.setString(15, txtCodFormaPgto.getText());

            int adiciona = pst.executeUpdate();
            if (adiciona > 0) {

                JOptionPane.showMessageDialog(null, "Forma de pagamento alterado");
                bloqueiacampos();
                limpacampos();

                //consultaAvancada();
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        txtCodFormaPgto = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtDescricaoFormaPgto = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtFornecedorFormaPgto = new javax.swing.JTextField();
        btnInclurFormaPgto = new javax.swing.JButton();
        btnAlterarFormaPgto = new javax.swing.JButton();
        btnSalvaFormaPgto = new javax.swing.JButton();
        btnCancelarFormaPgto = new javax.swing.JButton();
        btnExcluirFormaPgto = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        rbtDinheiro = new javax.swing.JRadioButton();
        rbtCheque = new javax.swing.JRadioButton();
        rbtBoleto = new javax.swing.JRadioButton();
        rbtCartao = new javax.swing.JRadioButton();
        rbtPgtoDigital = new javax.swing.JRadioButton();
        rbtOutros = new javax.swing.JRadioButton();
        jLabel4 = new javax.swing.JLabel();
        cboStatusFormaPgto = new javax.swing.JComboBox<>();
        JPanelParcelamento = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        txtVencPrimeiraparcela = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtNumMaxFormaPgto = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtIntervalorVencimentoDemaisparcelas = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtVlrMinParcelaFormaPgto = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtTaxaTransPrimeiraFormaPgto = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtTaxaTransDemasiParcFormaPgto = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        cboBancoFormaPgto = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        cboCcFormaPgto = new javax.swing.JComboBox<>();
        txtNomeForncedorFormaPgto = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("Formas de Pagamento");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/rpsystem/icones/pgto.png"))); // NOI18N
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

        jLabel1.setText("Cod:");

        txtCodFormaPgto.setEditable(false);
        txtCodFormaPgto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCodFormaPgtoFocusGained(evt);
            }
        });
        txtCodFormaPgto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCodFormaPgtoKeyPressed(evt);
            }
        });

        jLabel2.setText("Descrição:");

        txtDescricaoFormaPgto.setEnabled(false);

        jLabel3.setText("Fornecedor:");
        jLabel3.setToolTipText("Pressione F3 para pesquisar");

        txtFornecedorFormaPgto.setText("0");
        txtFornecedorFormaPgto.setToolTipText("Pressione F3 para pesquisar");
        txtFornecedorFormaPgto.setEnabled(false);
        txtFornecedorFormaPgto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtFornecedorFormaPgtoKeyPressed(evt);
            }
        });

        btnInclurFormaPgto.setText("Incluir");
        btnInclurFormaPgto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInclurFormaPgtoActionPerformed(evt);
            }
        });

        btnAlterarFormaPgto.setText("Alterar");
        btnAlterarFormaPgto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlterarFormaPgtoActionPerformed(evt);
            }
        });

        btnSalvaFormaPgto.setText("Salvar");
        btnSalvaFormaPgto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvaFormaPgtoActionPerformed(evt);
            }
        });

        btnCancelarFormaPgto.setText("Cancelar");
        btnCancelarFormaPgto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarFormaPgtoActionPerformed(evt);
            }
        });

        btnExcluirFormaPgto.setText("Excluir");
        btnExcluirFormaPgto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcluirFormaPgtoActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Tipo"));

        buttonGroup1.add(rbtDinheiro);
        rbtDinheiro.setText("Dinheiro");
        rbtDinheiro.setEnabled(false);
        rbtDinheiro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtDinheiroActionPerformed(evt);
            }
        });

        buttonGroup1.add(rbtCheque);
        rbtCheque.setText("Cheque");
        rbtCheque.setEnabled(false);
        rbtCheque.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtChequeActionPerformed(evt);
            }
        });

        buttonGroup1.add(rbtBoleto);
        rbtBoleto.setText("Boleto");
        rbtBoleto.setEnabled(false);
        rbtBoleto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtBoletoActionPerformed(evt);
            }
        });

        buttonGroup1.add(rbtCartao);
        rbtCartao.setText("Cartão");
        rbtCartao.setEnabled(false);
        rbtCartao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtCartaoActionPerformed(evt);
            }
        });

        buttonGroup1.add(rbtPgtoDigital);
        rbtPgtoDigital.setText("Pgto Digital");
        rbtPgtoDigital.setEnabled(false);
        rbtPgtoDigital.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtPgtoDigitalActionPerformed(evt);
            }
        });

        buttonGroup1.add(rbtOutros);
        rbtOutros.setText("Outros");
        rbtOutros.setEnabled(false);
        rbtOutros.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtOutrosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rbtCartao)
                    .addComponent(rbtDinheiro))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rbtCheque)
                    .addComponent(rbtPgtoDigital))
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rbtOutros)
                    .addComponent(rbtBoleto))
                .addContainerGap(91, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbtDinheiro)
                    .addComponent(rbtCheque)
                    .addComponent(rbtBoleto))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbtCartao)
                    .addComponent(rbtPgtoDigital)
                    .addComponent(rbtOutros))
                .addContainerGap())
        );

        jLabel4.setText("Status:");

        cboStatusFormaPgto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Ativo", "Inativo" }));
        cboStatusFormaPgto.setEnabled(false);
        cboStatusFormaPgto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboStatusFormaPgtoActionPerformed(evt);
            }
        });

        JPanelParcelamento.setBorder(javax.swing.BorderFactory.createTitledBorder("Parcelamento e taxas"));

        jLabel5.setText("Nº Maximo de Parcelas:");

        txtVencPrimeiraparcela.setText("0");
        txtVencPrimeiraparcela.setEnabled(false);

        jLabel6.setText("Vencimentos 1º Parcela:");

        txtNumMaxFormaPgto.setText("0");
        txtNumMaxFormaPgto.setEnabled(false);

        jLabel7.setText("Demais parcelas:");

        txtIntervalorVencimentoDemaisparcelas.setText("0");
        txtIntervalorVencimentoDemaisparcelas.setEnabled(false);

        jLabel8.setText("Valor minimo Parcela:");

        txtVlrMinParcelaFormaPgto.setText("0");
        txtVlrMinParcelaFormaPgto.setEnabled(false);

        jLabel9.setText("Taxa transação primeira parcela:");

        txtTaxaTransPrimeiraFormaPgto.setEnabled(false);

        jLabel10.setText("Demais");

        txtTaxaTransDemasiParcFormaPgto.setEnabled(false);

        javax.swing.GroupLayout JPanelParcelamentoLayout = new javax.swing.GroupLayout(JPanelParcelamento);
        JPanelParcelamento.setLayout(JPanelParcelamentoLayout);
        JPanelParcelamentoLayout.setHorizontalGroup(
            JPanelParcelamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelParcelamentoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(JPanelParcelamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, JPanelParcelamentoLayout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTaxaTransPrimeiraFormaPgto)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTaxaTransDemasiParcFormaPgto))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, JPanelParcelamentoLayout.createSequentialGroup()
                        .addGroup(JPanelParcelamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, JPanelParcelamentoLayout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtNumMaxFormaPgto, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, JPanelParcelamentoLayout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtVencPrimeiraparcela, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(JPanelParcelamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(JPanelParcelamentoLayout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtIntervalorVencimentoDemaisparcelas))
                            .addGroup(JPanelParcelamentoLayout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtVlrMinParcelaFormaPgto)))))
                .addGap(13, 13, 13))
        );
        JPanelParcelamentoLayout.setVerticalGroup(
            JPanelParcelamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelParcelamentoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(JPanelParcelamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtVencPrimeiraparcela, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JPanelParcelamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7)
                        .addComponent(txtIntervalorVencimentoDemaisparcelas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(3, 3, 3)
                .addGroup(JPanelParcelamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JPanelParcelamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtNumMaxFormaPgto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel8)
                        .addComponent(txtVlrMinParcelaFormaPgto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JPanelParcelamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtTaxaTransPrimeiraFormaPgto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(txtTaxaTransDemasiParcFormaPgto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jLabel11.setVisible(false);
        jLabel11.setText("Banco:");

        cboBancoFormaPgto.setVisible(false);
        cboBancoFormaPgto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1" }));

        jLabel12.setText("Conta Corrente:");

        cboCcFormaPgto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));
        cboCcFormaPgto.setEnabled(false);

        txtNomeForncedorFormaPgto.setEditable(false);
        txtNomeForncedorFormaPgto.setEnabled(false);
        txtNomeForncedorFormaPgto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNomeForncedorFormaPgtoActionPerformed(evt);
            }
        });

        jButton1.setText("Pesquisar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 7, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1, 12, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(JPanelParcelamento, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel11)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel12))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(cboBancoFormaPgto, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(cboCcFormaPgto, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(txtFornecedorFormaPgto, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtNomeForncedorFormaPgto, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addGap(15, 15, 15))
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtCodFormaPgto, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(85, 85, 85)
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(cboStatusFormaPgto, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtDescricaoFormaPgto, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnInclurFormaPgto, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnCancelarFormaPgto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAlterarFormaPgto, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnSalvaFormaPgto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnExcluirFormaPgto, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(70, 70, 70))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtCodFormaPgto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(cboStatusFormaPgto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtDescricaoFormaPgto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(JPanelParcelamento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtFornecedorFormaPgto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNomeForncedorFormaPgto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(cboBancoFormaPgto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(cboCcFormaPgto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(45, 45, 45)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnInclurFormaPgto)
                    .addComponent(btnAlterarFormaPgto)
                    .addComponent(btnSalvaFormaPgto))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(btnCancelarFormaPgto)
                    .addComponent(btnExcluirFormaPgto))
                .addGap(25, 25, 25))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnInclurFormaPgtoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInclurFormaPgtoActionPerformed
        // TODO add your handling code here:
        liberacampos();
        limpacampos();
        txtDescricaoFormaPgto.requestFocus();
    }//GEN-LAST:event_btnInclurFormaPgtoActionPerformed

    private void btnCancelarFormaPgtoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarFormaPgtoActionPerformed
        // TODO add your handling code here:
        bloqueiacampos();
        limpacampos();

    }//GEN-LAST:event_btnCancelarFormaPgtoActionPerformed

    private void txtNomeForncedorFormaPgtoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeForncedorFormaPgtoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNomeForncedorFormaPgtoActionPerformed

    private void txtFornecedorFormaPgtoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFornecedorFormaPgtoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_F3) {
            TelaPesquisa pesquisa = new TelaPesquisa();
            TelaPrincipal.Desktop.add(pesquisa);

            pesquisa.setVisible(true);
            CentralizaForm c = new CentralizaForm(pesquisa);
            c.centralizaForm();
            pesquisa.toFront();

            pesquisa.setTitle("Formas Pagamento");
            pesquisa.lblOrigem.setText("FORPGTO");

            //
        }
    }//GEN-LAST:event_txtFornecedorFormaPgtoKeyPressed

    private void rbtDinheiroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtDinheiroActionPerformed
        // TODO add your handling code here:
        tipo = "Dinheiro";
        bloqueiaparcelamento();
    }//GEN-LAST:event_rbtDinheiroActionPerformed

    private void rbtCartaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtCartaoActionPerformed
        // TODO add your handling code here:
        tipo = "Cartão";
        habilitaparcelamento();
    }//GEN-LAST:event_rbtCartaoActionPerformed

    private void rbtChequeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtChequeActionPerformed
        // TODO add your handling code here:
        tipo = "Cheque";
        habilitaparcelamento();
    }//GEN-LAST:event_rbtChequeActionPerformed

    private void rbtPgtoDigitalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtPgtoDigitalActionPerformed
        // TODO add your handling code here:
        tipo = "Pgto Digital";
        habilitaparcelamento();
    }//GEN-LAST:event_rbtPgtoDigitalActionPerformed

    private void rbtBoletoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtBoletoActionPerformed
        // TODO add your handling code here:
        tipo = "Boleto";
        habilitaparcelamento();
    }//GEN-LAST:event_rbtBoletoActionPerformed

    private void rbtOutrosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtOutrosActionPerformed
        // TODO add your handling code here:
        tipo = "Outros";
        habilitaparcelamento();
    }//GEN-LAST:event_rbtOutrosActionPerformed

    private void cboStatusFormaPgtoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboStatusFormaPgtoActionPerformed
        // TODO add your handling code here:
        if (cboStatusFormaPgto.getSelectedItem().equals("Ativo")) {
            status = 0;

        } else {
            status = 1;
        }
    }//GEN-LAST:event_cboStatusFormaPgtoActionPerformed

    private void btnSalvaFormaPgtoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvaFormaPgtoActionPerformed
        // TODO add your handling code here:
        if (txtCodFormaPgto.getText().equals("")) {
            insereformapgto();
        } else {
            //JOptionPane.showMessageDialog(null, "Altera");
            alteraforma();
        }

    }//GEN-LAST:event_btnSalvaFormaPgtoActionPerformed

    private void formInternalFrameActivated(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameActivated
        // TODO add your handling code here:
        status = 0;
    }//GEN-LAST:event_formInternalFrameActivated

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        TelaPesquisa pesquisa = new TelaPesquisa();
        TelaPrincipal.Desktop.add(pesquisa);

        pesquisa.setVisible(true);

        CentralizaForm c = new CentralizaForm(pesquisa);
        c.centralizaForm();

        pesquisa.toFront();
        pesquisa.lblOrigem.setText("PGTO");

    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtCodFormaPgtoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCodFormaPgtoFocusGained
        // TODO add your handling code here:

    }//GEN-LAST:event_txtCodFormaPgtoFocusGained

    private void txtCodFormaPgtoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodFormaPgtoKeyPressed
        // TODO add your handling code here:
        pesquisaformapgto();
    }//GEN-LAST:event_txtCodFormaPgtoKeyPressed

    private void btnAlterarFormaPgtoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarFormaPgtoActionPerformed
        // TODO add your handling code here:
        if (txtCodFormaPgto.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Selecione uma forma de pagamento");
        } else {
            liberacampos();
        }

    }//GEN-LAST:event_btnAlterarFormaPgtoActionPerformed

    private void btnExcluirFormaPgtoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirFormaPgtoActionPerformed
        // TODO add your handling code here:
        if (txtCodFormaPgto.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Selecione uma forma de pagamento");
        } else {
            removeformapgto();
        }


    }//GEN-LAST:event_btnExcluirFormaPgtoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel JPanelParcelamento;
    private javax.swing.JButton btnAlterarFormaPgto;
    private javax.swing.JButton btnCancelarFormaPgto;
    private javax.swing.JButton btnExcluirFormaPgto;
    private javax.swing.JButton btnInclurFormaPgto;
    private javax.swing.JButton btnSalvaFormaPgto;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cboBancoFormaPgto;
    private javax.swing.JComboBox<String> cboCcFormaPgto;
    private javax.swing.JComboBox<String> cboStatusFormaPgto;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JRadioButton rbtBoleto;
    private javax.swing.JRadioButton rbtCartao;
    private javax.swing.JRadioButton rbtCheque;
    private javax.swing.JRadioButton rbtDinheiro;
    private javax.swing.JRadioButton rbtOutros;
    private javax.swing.JRadioButton rbtPgtoDigital;
    public static javax.swing.JTextField txtCodFormaPgto;
    private javax.swing.JTextField txtDescricaoFormaPgto;
    public static javax.swing.JTextField txtFornecedorFormaPgto;
    private javax.swing.JTextField txtIntervalorVencimentoDemaisparcelas;
    public static javax.swing.JTextField txtNomeForncedorFormaPgto;
    private javax.swing.JTextField txtNumMaxFormaPgto;
    private javax.swing.JTextField txtTaxaTransDemasiParcFormaPgto;
    private javax.swing.JTextField txtTaxaTransPrimeiraFormaPgto;
    private javax.swing.JTextField txtVencPrimeiraparcela;
    private javax.swing.JTextField txtVlrMinParcelaFormaPgto;
    // End of variables declaration//GEN-END:variables
}
