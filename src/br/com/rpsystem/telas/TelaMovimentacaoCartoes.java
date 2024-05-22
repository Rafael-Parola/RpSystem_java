/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package br.com.rpsystem.telas;

import br.com.rpsystem.dal.ModuloConexao;
import br.com.rpsystem.funcoes.Funcoes;
import br.com.rpsystem.funcoes.MovCartaoCredito;
import java.awt.AWTKeyStroke;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author Rafael Veiga
 */
public class TelaMovimentacaoCartoes extends javax.swing.JInternalFrame {

    public String parcelado;
    public String repeticoes;
    public String pesquisatodosresultados;
    public String mostrapagos;
    String classifica_caocompleta = null;
    int cod_classificacao;
    String cartao_completo = null;
    int cod_cartao;

    String cartao = null;
    String cartao_completo_p = null;
    int cod_cartao_p;

    int diafechamento;
    int diavencimento;
    int nparcelas;
    int nrepeticoes;
    double valor;
    Object[] options = {"Sim", "Não"};
    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    // Variável para rastrear o estado da tecla 'i'
    private boolean teclaIPressionada = false;

    /**
     * Creates new form TelaMovimentacaoCartoes
     */
    public TelaMovimentacaoCartoes() {
        initComponents();
        repeticoes = "0";
        parcelado = "0";
        pesquisatodosresultados = "0";
        mostrapagos = "0";
        chkRepeticoes.setEnabled(false);
        chkParcelado.setEnabled(false);
        // verifica_chekbox();
        conexao = ModuloConexao.conector();

        Funcoes cla = new Funcoes(cboClassificacao);
        cla.f_classificacao(cboClassificacao);

        Funcoes c = new Funcoes(cboCartao);
        c.f_cartao(cboCartao);
        c.f_cartao(cboCartaoPesquisa);

    }

    private void contarCaracteres() {
        String texto = txtObservacoes.getText();
        int numeroCaracteres = texto.length();
        if (numeroCaracteres > 3000) {
            txtObservacoes.setEditable(false); // Desabilita a edição
            JOptionPane.showMessageDialog(null, "Numero máximo de caracteres atingigo");
        } else {
            txtObservacoes.setEditable(true);  // Habilita a edição
        }
        lblCaracteres.setText(numeroCaracteres + " /3000");
    }

    private void verifica_chekbox() {
        if (parcelado.equals("1")) {
            txtNumParcelas.setEnabled(true);
            // lblParcelas.setVisible(true);
            txtNumParcelas.setEnabled(true);
            chkRepeticoes.setEnabled(false);
            // lblValorPorParcela.setVisible(true);

        }
        if (parcelado.equals("0")) {
            txtNumParcelas.setEnabled(false);
            //lblValorPorParcela.setVisible(false);
            txtNumParcelas.setText("0");

            //lblValorPorParcela.setText(txtValor.getText());
            chkRepeticoes.setEnabled(true);

        }
        if (repeticoes.equals("1")) {
            txtNumRepeticoes.setEnabled(true);
            lblRepeticoes.setEnabled(true);
            chkParcelado.setEnabled(false);

        } else {
            txtNumRepeticoes.setEnabled(false);
            txtNumRepeticoes.setText("0");
            lblRepeticoes.setEnabled(false);
            chkParcelado.setEnabled(true);

        }
    }

    private void calculaparcela() {
        double valor = Double.parseDouble(txtValor.getText().replace(",", "."));
        int parcelas = Integer.parseInt(txtNumParcelas.getText());
        if (parcelas > 0) {
            try {
                double valorParcela = valor / parcelas;
                // Formata o valor para exibir apenas duas casas decimais após a vírgula
                DecimalFormat decimalFormat = new DecimalFormat("#0.00");
                String valorFormatado = decimalFormat.format(valorParcela);

                lblValorPorParcela.setText(txtNumParcelas.getText() + "x R$: " + valorFormatado);
            } catch (NumberFormatException e) {
                System.out.println(e);
                lblValorPorParcela.setText("Resultado: ");
            }
        }

    }

    private void inserelancamento() {
        //captura o codigo do cboclassificacao
        classifica_caocompleta = cboClassificacao.getSelectedItem().toString();
        cod_classificacao = Integer.parseInt(classifica_caocompleta.substring(0, 2).replaceAll("\\s|-", ""));
        //.out.println(" o codigo da classificacao é : " + cod_classificacao);
        cartao_completo = cboCartao.getSelectedItem().toString();

        cod_cartao = Integer.parseInt(cartao_completo.substring(0, 2).replaceAll("\\s|-", ""));
        // System.out.println(" o codigo da cartao é : " + cod_cartao);

        nparcelas = Integer.parseInt(txtNumParcelas.getText());
        //  System.out.println("numero parcelas " + nparcelas);

        nrepeticoes = Integer.parseInt(txtNumRepeticoes.getText());
        // System.out.println("numero repeticoes " + nrepeticoes);

        valor = Double.parseDouble(txtValor.getText().replace(",", "."));
        //    System.out.println("valor  " + valor);

        String obs = txtObservacoes.getText().toString();
        String descricao = txtDescricao.getText();

        // caputra a data e converte para o tipo date
        /// Inicio 
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        // Converte o conteúdo do JTextField para um objeto Date.
        Date dataemissao = null;
        try {
            dataemissao = sdf.parse(txtData.getText());
        } catch (ParseException ex) {
            Logger.getLogger(TelaMovimentacaoCartoes.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Converte o objeto Date para um objeto SQL Date.
        java.sql.Date dtemi = new java.sql.Date(dataemissao.getTime());
        //// Fim 

//        System.out.println("data emissao " + dtemi);
        String sql = "select diafechamentofatura,diavencimentofatura from cadcartoes where codigo = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setInt(1, cod_cartao);
            rs = pst.executeQuery();
            if (rs.next()) {
                diafechamento = rs.getInt(1);
                diavencimento = rs.getInt(2);

                //chama o metodo lançamento de gastos
                MovCartaoCredito lan = new MovCartaoCredito(dtemi, cod_classificacao, cod_cartao, nparcelas, nrepeticoes, valor, diafechamento, diavencimento, obs, descricao);
                lan.lancacartao();

                bloqueiaCampos();
                limpacampos();
                pesquisalancamento();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void liberacampos() {
        limpacampos();
        txtDescricao.requestFocus();
        txtDescricao.setEnabled(true);
        txtData.setEnabled(true);
        cboCartao.setEnabled(true);
        cboClassificacao.setEnabled(true);
        txtValor.setEnabled(true);
        chkParcelado.setEnabled(true);
        chkRepeticoes.setEnabled(true);
        txtObservacoes.setEnabled(true);
        btnIncluir.setEnabled(false);
        lblVencimento.setText(null);
        btnAlterar.setEnabled(false);
        verifica_chekbox();

    }

    private void liberacampos_alteracao() {
        txtDescricao.requestFocus();
        txtDescricao.setEnabled(true);
        // txtData.setEnabled(true);
        //cboCartao.setEnabled(true);
        cboClassificacao.setEnabled(true);
        txtValor.setEnabled(true);
        chkParcelado.setEnabled(false);
        chkRepeticoes.setEnabled(false);
        txtObservacoes.setEnabled(true);
        lblMsgAlteracao.setVisible(true);
        btnIncluir.setEnabled(false);

        btnAlterar.setEnabled(false);
        //verifica_chekbox();
    }

    private void bloqueiaCampos() {
        txtDescricao.setEnabled(false);
        txtData.setEnabled(false);
        cboCartao.setEnabled(false);
        cboClassificacao.setEnabled(false);
        txtValor.setEnabled(false);
        parcelado = "0";
        repeticoes = "0";
        chkParcelado.setEnabled(false);
        chkRepeticoes.setEnabled(false);
        txtObservacoes.setEnabled(false);
        lblMsgAlteracao.setVisible(false);
        txtNumParcelas.setEnabled(false);
        txtNumRepeticoes.setEnabled(false);
        btnIncluir.setEnabled(true);
        btnAlterar.setEnabled(true);
        btnIncluir.requestFocus();
        //verifica_chekbox();

    }

    private void pesquisalancamento() {
        cartao_completo_p = cboCartaoPesquisa.getSelectedItem().toString();

        if (cartao_completo_p.equals("Todos")) {
            cod_cartao_p = 0;

        } else {
            cod_cartao_p = Integer.parseInt(cartao_completo_p.substring(0, 2).replaceAll("\\s|-", ""));

        }

        String sql = null;
        if (pesquisatodosresultados.equals("0")) {

            sql = "select m.codigo as 'Codigo',m.descricao as 'Descrição', m.dataemissao as 'Emissão',"
                    + "c.nomecartao as 'Cartão', m.datavencimento as 'Vencimento', FORMAT(m.valor, 2) AS Valor  from movimentocartoes m \n"
                    + "inner join cadcartoes c\n"
                    + "on m.cartao = c.codigo\n"
                    + "where  m.descricao like ? "
                    + "and m.cartao like ?"
                    + "and m.excluido = 0 and m.pago = 0 order by m.codigo desc limit 50";
        }
        if (pesquisatodosresultados.equals("1")) {
            sql = "select m.codigo as 'Codigo',m.descricao as 'Descrição', m.dataemissao as 'Emissão',"
                    + "c.nomecartao as 'Cartão', m.datavencimento as 'Vencimento', FORMAT(m.valor, 2) AS Valor  from movimentocartoes m \n"
                    + "inner join cadcartoes c\n"
                    + "on m.cartao = c.codigo\n"
                    + "where  m.descricao like ? "
                    + "and m.cartao like ?"
                    + "and m.excluido = 0  and m.pago = 0 order by m.codigo desc";
        }
        if (mostrapagos.equals("1")) {
            sql = "select m.codigo as 'Codigo',m.descricao as 'Descrição', m.dataemissao as 'Emissão',"
                    + "c.nomecartao as 'Cartão', m.datavencimento as 'Vencimento', FORMAT(m.valor, 2) AS Valor  from movimentocartoes m \n"
                    + "inner join cadcartoes c\n"
                    + "on m.cartao = c.codigo\n"
                    + "where  m.descricao like ? "
                    + "and m.cartao like ?"
                    + "and m.excluido = 0 order by m.codigo desc";

        }

        try {
            pst = conexao.prepareStatement(sql);
            // Passando o conteudo do campo pesquisar para o SQL
            pst.setString(1, "%" + txtpesquisa.getText() + "%");
            if (cod_cartao_p == 0) {
                pst.setString(2, "%%");
            } else {
                pst.setString(2, "%" + cod_cartao_p + "%");
            }

            rs = pst.executeQuery();
            // A linha abixo usa a rs2xml.jar 
            tblPesquisa.setModel(DbUtils.resultSetToTableModel(rs));

            // Define a largura das colunas conforme necessário.
            tblPesquisa.getColumnModel().getColumn(0).setPreferredWidth(20);  // Coluna "Código" com largura 80 pixels
            tblPesquisa.getColumnModel().getColumn(1).setPreferredWidth(250); // Coluna "Descrição" com largura 150 pixels
            tblPesquisa.getColumnModel().getColumn(2).setPreferredWidth(80); // Coluna "Emissão" com largura 100 pixels
            tblPesquisa.getColumnModel().getColumn(3).setPreferredWidth(80); // Coluna "Cartão" com largura 100 pixels
            tblPesquisa.getColumnModel().getColumn(4).setPreferredWidth(80); // Coluna "Vencimento" com largura 120 pixels
            tblPesquisa.getColumnModel().getColumn(5).setPreferredWidth(80);  // Coluna "Valor" com largura 80 pixels
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void seta_item_alteração() {
        String sql = "select * from movimentocartoes where codigo = ? and empresa_codempresa = " + TelaPrincipal.lblcodEmpresa.getText();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfSaida = new SimpleDateFormat("dd/MM/yyyy");

        int setar = tblPesquisa.getSelectedRow();

        txtCod.setText(tblPesquisa.getModel().getValueAt(setar, 0).toString());
        String cod = txtCod.getText();

        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, cod);
            rs = pst.executeQuery();
            if (rs.next()) {

                java.util.Date dtemi = sdf.parse(rs.getString(2));
                String dtemiformatada = sdfSaida.format(dtemi);
                txtData.setText(dtemiformatada);

                //Preenche combo do cartão
                String cartao = rs.getString(4);
                for (int i = 0; i < cboCartao.getItemCount(); i++) {
                    String item = cboCartao.getItemAt(i);
                    if (item.startsWith(cartao)) {
                        cboCartao.setSelectedIndex(i);
                        break;
                    }
                }

                //Preenche combo do classificação
                String classificacao = rs.getString(5);
                for (int i = 0; i < cboClassificacao.getItemCount(); i++) {
                    String item = cboClassificacao.getItemAt(i);
                    if (item.startsWith(classificacao)) {
                        cboClassificacao.setSelectedIndex(i);
                        break;
                    }
                }
                txtValor.setText(rs.getString(7));
                txtObservacoes.setText(rs.getString(8));
                txtDescricao.setText(rs.getString(11));
                lblVencimento.setText(rs.getString(3));

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void limpacampos() {
        txtCod.setText("");
        txtDescricao.setText("");
        txtData.setText("");
        txtValor.setText("");
        txtObservacoes.setText("");
        chkParcelado.setSelected(false);
        chkRepeticoes.setSelected(false);
        txtNumParcelas.setText("0");
        lblValorPorParcela.setText("0");
        // lblValorPorParcela.setVisible(false);
        txtNumRepeticoes.setText("0");
        parcelado = "0";
        repeticoes = "0";

    }

    private void exclui_lancamento() {
        String sql = "update movimentocartoes set excluido = '1' where codigo = ?";
        try {
            int exclui = JOptionPane.showOptionDialog(null, "Tem certeza que deseja excluir?", "Atenção", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);

            if (exclui == JOptionPane.YES_OPTION) {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtCod.getText());
                int excluilogico = pst.executeUpdate();
                if (excluilogico > 0) {
                    JOptionPane.showMessageDialog(null, "Excluido com sucesso");
                    alterafatura();
                    bloqueiaCampos();
                    limpacampos();
                    pesquisalancamento();

                }

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    public void alterafatura() {
        cartao_completo = cboCartao.getSelectedItem().toString();
        cod_cartao = Integer.parseInt(cartao_completo.substring(0, 2).replaceAll("\\s|-", ""));
        Double valor = Double.valueOf( txtValor.getText());
     

        String realizarExclusao = "update fatura set valor = valor - " + valor + ""
                + " where vencimento = ? and cartao = ? and codempresa = ?";
        try {
            pst = conexao.prepareStatement(realizarExclusao);
            pst.setString(1, lblVencimento.getText());
            pst.setInt(2, cod_cartao);
            pst.setInt(3, Integer.parseInt(TelaPrincipal.lblcodEmpresa.getText()));
            pst.executeUpdate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            System.out.println("Erro na exclusão do SALDO");
        }
    }

    private void alteracao() {
        classifica_caocompleta = cboClassificacao.getSelectedItem().toString();
        cod_classificacao = Integer.parseInt(classifica_caocompleta.substring(0, 2).replaceAll("\\s|-", ""));
        valor = Double.parseDouble(txtValor.getText().replace(",", "."));

        String update = "update movimentocartoes set classificacao = ?, valor = ?,obs = ?,descricao = ? where codigo = ? and empresa_codempresa = " + TelaPrincipal.lblcodEmpresa.getText();
        txtData.setEnabled(false);
        cboCartao.setEnabled(false);

        try {
            pst = conexao.prepareStatement(update);
            pst.setInt(1, cod_classificacao);
            pst.setDouble(2, valor);
            pst.setString(3, txtObservacoes.getText());
            pst.setString(4, txtDescricao.getText());
            pst.setString(5, txtCod.getText());
            int adiciona = pst.executeUpdate();
            if (adiciona > 0) {

                JOptionPane.showMessageDialog(null, "Alterado com sucesso");
                chkParcelado.setEnabled(false);
                chkRepeticoes.setEnabled(true);
                btnIncluir.setEnabled(true);
                pesquisalancamento();
                limpacampos();
                bloqueiaCampos();
                lblMsgAlteracao.setVisible(false);
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
        txtDescricao = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtData = new javax.swing.JFormattedTextField();
        jLabel3 = new javax.swing.JLabel();
        cboClassificacao = new javax.swing.JComboBox<>();
        chkParcelado = new javax.swing.JCheckBox();
        txtNumParcelas = new javax.swing.JTextField();
        lblParcelas = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        cboCartao = new javax.swing.JComboBox<>();
        chkRepeticoes = new javax.swing.JCheckBox();
        txtNumRepeticoes = new javax.swing.JTextField();
        lblRepeticoes = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtObservacoes = new javax.swing.JTextArea();
        lblCaracteres = new javax.swing.JLabel();
        btnIncluir = new javax.swing.JButton();
        btnAlterar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        txtCod = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtValor = new javax.swing.JTextField();
        lblValorPorParcela = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblPesquisa = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        txtpesquisa = new javax.swing.JTextField();
        lblMsgAlteracao = new javax.swing.JLabel();
        chkMostraPagos = new javax.swing.JCheckBox();
        chkMostraTodosRegistros = new javax.swing.JCheckBox();
        cboCartaoPesquisa = new javax.swing.JComboBox<>();
        lblVencimento = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setTitle("Lançamento de cartões");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/rpsystem/icones/cartoes.png"))); // NOI18N
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                formKeyReleased(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel1.setText("Descrição:");

        txtDescricao.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtDescricao.setEnabled(false);
        txtDescricao.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDescricaoKeyPressed(evt);
            }
        });

        jLabel2.setText("Data:");

        try {
            txtData.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtData.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtData.setEnabled(false);
        txtData.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtData.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDataKeyPressed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel3.setText("Classificação:");

        cboClassificacao.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        cboClassificacao.setEnabled(false);
        cboClassificacao.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cboClassificacaoKeyPressed(evt);
            }
        });

        chkParcelado.setText("Parcelado");
        chkParcelado.setEnabled(false);
        chkParcelado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkParceladoActionPerformed(evt);
            }
        });
        chkParcelado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                chkParceladoKeyPressed(evt);
            }
        });

        txtNumParcelas.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtNumParcelas.setText("0");
        txtNumParcelas.setEnabled(false);
        txtNumParcelas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumParcelasActionPerformed(evt);
            }
        });
        txtNumParcelas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNumParcelasKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNumParcelasKeyReleased(evt);
            }
        });

        lblParcelas.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblParcelas.setText("N° Parcelas");

        jLabel5.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel5.setText("Cartão:");

        cboCartao.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        cboCartao.setEnabled(false);
        cboCartao.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cboCartaoKeyPressed(evt);
            }
        });

        chkRepeticoes.setText("Lançamento recorrente");
        chkRepeticoes.setEnabled(false);
        chkRepeticoes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkRepeticoesActionPerformed(evt);
            }
        });

        txtNumRepeticoes.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtNumRepeticoes.setText("0");
        txtNumRepeticoes.setEnabled(false);

        lblRepeticoes.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblRepeticoes.setText("N° Repetições");

        jLabel7.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel7.setText("Observações:");

        txtObservacoes.setColumns(20);
        txtObservacoes.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtObservacoes.setRows(5);
        txtObservacoes.setEnabled(false);
        txtObservacoes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtObservacoesKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(txtObservacoes);

        lblCaracteres.setFont(new java.awt.Font("Segoe UI", 0, 8)); // NOI18N
        lblCaracteres.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCaracteres.setText("0 /3000");

        btnIncluir.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
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
            public void keyReleased(java.awt.event.KeyEvent evt) {
                btnIncluirKeyReleased(evt);
            }
        });

        btnAlterar.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        btnAlterar.setText("Alterar");
        btnAlterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlterarActionPerformed(evt);
            }
        });
        btnAlterar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnAlterarKeyPressed(evt);
            }
        });

        btnCancelar.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });
        btnCancelar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnCancelarKeyPressed(evt);
            }
        });

        btnSalvar.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
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

        btnExcluir.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        btnExcluir.setText("Excluir");
        btnExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcluirActionPerformed(evt);
            }
        });

        jLabel8.setVisible(false);
        jLabel8.setText("Cod.:");

        txtCod.setVisible(false);

        jLabel4.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel4.setText("Valor:");

        txtValor.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtValor.setEnabled(false);
        txtValor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtValorActionPerformed(evt);
            }
        });
        txtValor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtValorKeyPressed(evt);
            }
        });

        lblValorPorParcela.setEnabled(false);
        lblValorPorParcela.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        lblValorPorParcela.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblValorPorParcela.setText("0");

        tblPesquisa = new javax.swing.JTable(){
            public boolean  isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        tblPesquisa.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        tblPesquisa.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Código", "Descrição", "Emissão", "Cartão", "Vencimento", "Valor"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        // Define a largura das colunas conforme necessário.
        tblPesquisa.getColumnModel().getColumn(0).setPreferredWidth(20);  // Coluna "Código" com largura 80 pixels
        tblPesquisa.getColumnModel().getColumn(1).setPreferredWidth(250); // Coluna "Descrição" com largura 150 pixels
        tblPesquisa.getColumnModel().getColumn(2).setPreferredWidth(80); // Coluna "Emissão" com largura 100 pixels
        tblPesquisa.getColumnModel().getColumn(3).setPreferredWidth(80); // Coluna "Cartão" com largura 100 pixels
        tblPesquisa.getColumnModel().getColumn(4).setPreferredWidth(80); // Coluna "Vencimento" com largura 120 pixels
        tblPesquisa.getColumnModel().getColumn(5).setPreferredWidth(80);  // Coluna "Valor" com largura 80 pixels
        tblPesquisa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPesquisaMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblPesquisa);

        jLabel6.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel6.setText("Pesquisa:");

        txtpesquisa.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtpesquisa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtpesquisaKeyReleased(evt);
            }
        });

        lblMsgAlteracao.setVisible(false);
        lblMsgAlteracao.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblMsgAlteracao.setForeground(new java.awt.Color(255, 0, 0));
        lblMsgAlteracao.setText("*Não é possivel alterar emissão e o Cartão");

        chkMostraPagos.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        chkMostraPagos.setText("Mostra lançamentos pagos");
        chkMostraPagos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                chkMostraPagosMouseClicked(evt);
            }
        });

        chkMostraTodosRegistros.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        chkMostraTodosRegistros.setText("Mostra todos os registros");
        chkMostraTodosRegistros.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                chkMostraTodosRegistrosMouseClicked(evt);
            }
        });

        cboCartaoPesquisa.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        cboCartaoPesquisa.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos" }));
        cboCartaoPesquisa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboCartaoPesquisaActionPerformed(evt);
            }
        });
        cboCartaoPesquisa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cboCartaoPesquisaKeyPressed(evt);
            }
        });

        lblVencimento.setVisible(false);
        lblVencimento.setText("jLabel9");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtValor, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(chkParcelado, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblParcelas, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNumParcelas, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblValorPorParcela, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42)
                        .addComponent(chkRepeticoes, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblRepeticoes, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtNumRepeticoes, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(232, 232, 232)
                        .addComponent(btnIncluir)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnAlterar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnSalvar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCancelar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnExcluir))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(296, 296, 296)
                        .addComponent(chkMostraTodosRegistros)
                        .addGap(55, 55, 55)
                        .addComponent(chkMostraPagos))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(72, 72, 72)
                            .addComponent(jLabel8)
                            .addGap(18, 18, 18)
                            .addComponent(txtCod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(lblVencimento)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblMsgAlteracao))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(25, 25, 25)
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(txtData, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cboClassificacao, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cboCartao, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblCaracteres, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(2, 2, 2)
                                        .addComponent(jLabel7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 756, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 836, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(21, 21, 21)
                                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(cboCartaoPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(12, 12, 12)
                                        .addComponent(txtpesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, 569, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                .addContainerGap(29, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel8)
                        .addComponent(txtCod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblMsgAlteracao)
                        .addComponent(lblVencimento))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel1))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                .addComponent(jLabel2)
                                .addComponent(txtData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cboClassificacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel3)
                                .addComponent(jLabel5)
                                .addComponent(cboCartao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(chkParcelado)
                    .addComponent(lblParcelas)
                    .addComponent(txtNumParcelas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtValor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(lblValorPorParcela)
                    .addComponent(txtNumRepeticoes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblRepeticoes)
                    .addComponent(chkRepeticoes))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(1, 1, 1)
                        .addComponent(lblCaracteres))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnIncluir)
                    .addComponent(btnCancelar)
                    .addComponent(btnSalvar)
                    .addComponent(btnAlterar)
                    .addComponent(btnExcluir))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkMostraTodosRegistros)
                    .addComponent(chkMostraPagos))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtpesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboCartaoPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtObservacoesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtObservacoesKeyPressed
        // TODO add your handling code here:
        // Crie uma instância da classe ContaCaracteres e passe os componentes relevantes
        contarCaracteres();
        if (evt.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            txtObservacoes.setEditable(true);
        }

    }//GEN-LAST:event_txtObservacoesKeyPressed

    private void chkParceladoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkParceladoActionPerformed
        // TODO add your handling code here:
        if (chkParcelado.isSelected()) {
            parcelado = "1";
            verifica_chekbox();

        } else {
            parcelado = "0";
            verifica_chekbox();

        }
        //verifica_chekbox();
    }//GEN-LAST:event_chkParceladoActionPerformed

    private void chkRepeticoesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkRepeticoesActionPerformed
        // TODO add your handling code here:
        if (chkRepeticoes.isSelected()) {
            repeticoes = "1";
        } else {
            repeticoes = "0";
        }
        verifica_chekbox();

    }//GEN-LAST:event_chkRepeticoesActionPerformed

    private void txtNumParcelasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumParcelasKeyPressed
        // TODO add your handling code here:

    }//GEN-LAST:event_txtNumParcelasKeyPressed

    private void txtValorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtValorKeyPressed
        // TODO add your handling code here:

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnSalvar.requestFocus();
        }


    }//GEN-LAST:event_txtValorKeyPressed

    private void chkParceladoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_chkParceladoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER && parcelado.equals("1")) {
            txtNumParcelas.requestFocus();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ENTER && parcelado.equals("0")) {
            chkRepeticoes.requestFocus();
        }
    }//GEN-LAST:event_chkParceladoKeyPressed

    private void txtNumParcelasKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumParcelasKeyReleased
        // TODO add your handling code here:
        int parcelas = Integer.parseInt(txtNumParcelas.getText());

        if (parcelas != 0) {

            calculaparcela();
        }

    }//GEN-LAST:event_txtNumParcelasKeyReleased

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
        // TODO add your handling code here:
        //inserelancamento();
        String codigo = txtCod.getText();
        if (codigo.equals("")) {
            inserelancamento();

        } else {
            alteracao();

        }

    }//GEN-LAST:event_btnSalvarActionPerformed

    private void btnIncluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIncluirActionPerformed
        // TODO add your handling code here:
        liberacampos();
    }//GEN-LAST:event_btnIncluirActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // TODO add your handling code here:
        bloqueiaCampos();

        limpacampos();

    }//GEN-LAST:event_btnCancelarActionPerformed

    private void txtDescricaoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDescricaoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtData.requestFocus();
        }
    }//GEN-LAST:event_txtDescricaoKeyPressed

    private void txtDataKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDataKeyPressed
        // TODO add your handling code here:
        if ((evt.getKeyCode() == KeyEvent.VK_ENTER) && txtData.getText().equals("  /  /    ")) {
            txtData.setText(TelaPrincipal.lblData.getText());
            cboClassificacao.requestFocus();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cboClassificacao.requestFocus();
        }
    }//GEN-LAST:event_txtDataKeyPressed

    private void cboClassificacaoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cboClassificacaoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cboCartao.requestFocus();
        }

    }//GEN-LAST:event_cboClassificacaoKeyPressed

    private void cboCartaoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cboCartaoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtValor.requestFocus();
        }
    }//GEN-LAST:event_cboCartaoKeyPressed

    private void txtValorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtValorActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_txtValorActionPerformed

    private void txtpesquisaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtpesquisaKeyReleased
        // TODO add your handling code here:
        pesquisalancamento();
    }//GEN-LAST:event_txtpesquisaKeyReleased

    private void tblPesquisaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPesquisaMouseClicked
        // TODO add your handling code here:
        seta_item_alteração();
    }//GEN-LAST:event_tblPesquisaMouseClicked

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
        // TODO add your handling code here:
        exclui_lancamento();
    }//GEN-LAST:event_btnExcluirActionPerformed

    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarActionPerformed
        // TODO add your handling code here:
        String codigo = txtCod.getText();
        if (codigo.equals("")) {
            JOptionPane.showMessageDialog(null, "Selecione um registro");

        } else {
            liberacampos_alteracao();

        }
    }//GEN-LAST:event_btnAlterarActionPerformed

    private void chkMostraPagosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_chkMostraPagosMouseClicked
        // TODO add your handling code here:

        if (chkMostraPagos.isSelected()) {
            mostrapagos = "1";
            pesquisatodosresultados = "1";
            chkMostraTodosRegistros.setSelected(true);

        } else {
            mostrapagos = "0";
            pesquisatodosresultados = "0";
            chkMostraTodosRegistros.setSelected(false);
        }

    }//GEN-LAST:event_chkMostraPagosMouseClicked

    private void chkMostraTodosRegistrosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_chkMostraTodosRegistrosMouseClicked
        // TODO add your handling code here:
        if (chkMostraTodosRegistros.isSelected()) {
            pesquisatodosresultados = "1";
        } else {
            pesquisatodosresultados = "0";
        }
    }//GEN-LAST:event_chkMostraTodosRegistrosMouseClicked

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.out.println("aqui");
            // Fechar o JInternalFrame quando a tecla "Esc" for pressionada
            dispose();
        }
    }//GEN-LAST:event_formKeyPressed

    private void cboCartaoPesquisaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cboCartaoPesquisaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboCartaoPesquisaKeyPressed

    private void cboCartaoPesquisaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboCartaoPesquisaActionPerformed
        // TODO add your handling code here:
        pesquisalancamento();
    }//GEN-LAST:event_cboCartaoPesquisaActionPerformed

    private void btnSalvarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnSalvarKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String codigo = txtCod.getText();
            if (codigo.equals("")) {
                inserelancamento();

            } else {
                alteracao();

            }
        }
    }//GEN-LAST:event_btnSalvarKeyPressed

    private void btnIncluirKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnIncluirKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            liberacampos();
        }
    }//GEN-LAST:event_btnIncluirKeyPressed

    private void btnAlterarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnAlterarKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            liberacampos();
        }
    }//GEN-LAST:event_btnAlterarKeyPressed

    private void btnCancelarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnCancelarKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            bloqueiaCampos();
            limpacampos();
        }
    }//GEN-LAST:event_btnCancelarKeyPressed

    private void formKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyReleased
        // TODO add your handling code here:

    }//GEN-LAST:event_formKeyReleased

    private void btnIncluirKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnIncluirKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_btnIncluirKeyReleased

    private void txtNumParcelasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumParcelasActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_txtNumParcelasActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAlterar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnExcluir;
    public static javax.swing.JButton btnIncluir;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JComboBox<String> cboCartao;
    private javax.swing.JComboBox<String> cboCartaoPesquisa;
    private javax.swing.JComboBox<String> cboClassificacao;
    private javax.swing.JCheckBox chkMostraPagos;
    private javax.swing.JCheckBox chkMostraTodosRegistros;
    private javax.swing.JCheckBox chkParcelado;
    private javax.swing.JCheckBox chkRepeticoes;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblCaracteres;
    private javax.swing.JLabel lblMsgAlteracao;
    private javax.swing.JLabel lblParcelas;
    private javax.swing.JLabel lblRepeticoes;
    private javax.swing.JLabel lblValorPorParcela;
    private javax.swing.JLabel lblVencimento;
    public static javax.swing.JTable tblPesquisa;
    private javax.swing.JTextField txtCod;
    private javax.swing.JFormattedTextField txtData;
    private javax.swing.JTextField txtDescricao;
    private javax.swing.JTextField txtNumParcelas;
    private javax.swing.JTextField txtNumRepeticoes;
    private javax.swing.JTextArea txtObservacoes;
    private javax.swing.JTextField txtValor;
    private javax.swing.JTextField txtpesquisa;
    // End of variables declaration//GEN-END:variables
}
