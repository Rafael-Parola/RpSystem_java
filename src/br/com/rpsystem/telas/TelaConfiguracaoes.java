/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package br.com.rpsystem.telas;

import br.com.rpsystem.dal.ModuloConexao;
import br.com.rpsystem.funcoes.Arquivo;
import br.com.rpsystem.funcoes.Funcoes;
import java.awt.EventQueue;
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
public class TelaConfiguracaoes extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    String caminhoConf = System.getProperty("user.dir");
    int classificacao_auto_os;
    String classificacaocompleta;
    String codclassificacao;

    String classificacaocompleta_finalizacao_os;
    String codclassificacao_finalizacao_os;

    String formapgtocompleta;
    String codformapgto;

    /**
     * Creates new form TelaConfiguracaoes
     */
    public TelaConfiguracaoes() {
        initComponents();
        conexao = ModuloConexao.conector();

        Funcoes situacao = new Funcoes(cboSituacaoOs);
        situacao.f_situacaoOs(cboSituacaoOs);

        // Duas linhas abixo carrega o combo classificação fatura e classificação os
        Funcoes f1 = new Funcoes(cboClassificacaoFatura);
        f1.f_classificacao(cboClassificacaoFatura);
        f1.f_classificacao(cboClassificacao_os_financeiro);
//
//        Funcoes f = new Funcoes(cboContaPgFatura);
//        f.f_carregaconta(cboContaPgFatura);

    }

    public void carregaConfigi() {
        String configuracoes = "select * from configuracoes where empresa_codempresa = ?";
        try {
            pst = conexao.prepareStatement(configuracoes);
            pst.setString(1, TelaPrincipal.lblcodEmpresa.getText());
            rs = pst.executeQuery();
            if (rs.next()) {
                lblCod.setText(rs.getString(1));
                txtCaminhoDump.setText(rs.getString(2));
                cboHabilitaOsServ.setSelectedItem(rs.getString(4));
                cboHabilitaOsProdServ.setSelectedItem(rs.getString(5));
                cboHabilitaVenda.setSelectedItem(rs.getString(6));
                cboHabilitaBackup.setSelectedItem(rs.getString(7));
                txtEmailEnvio.setText(rs.getString(9));
                txtSenhaEmailEnvio.setText(rs.getString(10));
                txtSmtpEmailEnvio.setText(rs.getString(11));
                txtPortaEmailEnvio.setText(rs.getString(12));
                cboSmtpAutenticado.setSelectedItem(rs.getString(13));
                cboEnviaEmailOs.setSelectedItem(rs.getString(14));
                classificacao_auto_os = rs.getInt(15);
                // System.out.println(classificacao_auto_os);
                if (classificacao_auto_os == 1) {
                    cboFinanceiroOs.setSelectedItem("Sim");
                    // System.out.println("foi igual a sim");
                }
                if (classificacao_auto_os == 0) {
                    cboFinanceiroOs.setSelectedItem("Não");
                    //  System.out.println("passou no = 0");
                }

                String classificao = rs.getString(17);
                if (classificao != null) {

                    for (int i = 0; i < cboClassificacao_os_financeiro.getItemCount(); i++) {
                        String item = cboClassificacao_os_financeiro.getItemAt(i);
                        if (item.startsWith(classificao)) {
                            cboClassificacao_os_financeiro.setSelectedIndex(i);
                            break;
                        }
                    }
                }
                cboSituacaoOs.setSelectedItem(rs.getString(18));

//19-20
                String classificaofatura = rs.getString(19);
                if (classificaofatura != null) {

                    for (int i = 0; i < cboClassificacaoFatura.getItemCount(); i++) {
                        String item1 = cboClassificacaoFatura.getItemAt(i);
                        if (item1.startsWith(classificaofatura)) {
                            cboClassificacaoFatura.setSelectedIndex(i);
                            break;
                        }
                    }
                }
                // aqui 

                // Se não houver correspondência, deixe o combobox sem seleção
                if (cboClassificacaoFatura.getSelectedIndex() == -1) {
                    cboClassificacaoFatura.setSelectedItem(null);
                }
//
//                String cartao = rs.getString(20);
//                if (cartao != null) {
//
//                    for (int i = 0; i < cboFormaPgto.getItemCount(); i++) {
//                        String item2 = cboFormaPgto.getItemAt(i);
//                        if (item2.startsWith(cartao)) {
//                            cboFormaPgto.setSelectedIndex(i);
//                            break;
//                        }
//                    }
//                }
                //String pergunta_cartao_auto = rs.getString(21);
                cboLancCartaoEntrar.setSelectedItem(rs.getString(21));

                cboCopiaBkp.setSelectedItem(rs.getString(22));
                txtDestinoBkp.setText(rs.getString(23));

            } else {
                JOptionPane.showMessageDialog(null, "Não encontrado");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);

        }
    }

    private void alteraconfig() {
        classificacaocompleta_finalizacao_os = cboClassificacao_os_financeiro.getSelectedItem().toString();

        if (classificacaocompleta_finalizacao_os.equals("") || classificacaocompleta_finalizacao_os.equals(" ") || classificacaocompleta_finalizacao_os.equals(null) ) {
            codclassificacao_finalizacao_os = null;
        } else {
            codclassificacao_finalizacao_os = classificacaocompleta_finalizacao_os.substring(0, 2).replace(" ", "").replace("-", "");
        }

        classificacaocompleta = cboClassificacaoFatura.getSelectedItem().toString();
        if (classificacaocompleta.equals("") || classificacaocompleta.equals(" ")) {
            codclassificacao = null;
        } else {
            codclassificacao = classificacaocompleta.substring(0, 2).replace(" ", "").replace("-", "");
        }

//        formapgtocompleta = cboFormaPgto.getSelectedItem().toString();
//        codformapgto = formapgtocompleta.substring(0, 2).replace(" ", "").replace("-", "");
        String sql = "UPDATE `configuracoes` SET\n"
                + "`caminho_backup` = ?,\n"
                + "`osservico` = ?,\n"
                + "`osprodserv` = ?,\n"
                + "`vendas` = ?,\n"
                + "`habilitabackup` = ?,\n"
                + "`emailenvio` = ?,\n"
                + "`senhaenvio` = ?,\n"
                + "`smtp` =?,\n"
                + "`porta` = ?,\n"
                + "`smtpautenticado` = ?,\n"
                + "`enviaemailos` =?,\n"
                + "`usuarios_id` = ?,\n"//----------Aqui
                + "`inserefinanceiro_os_auto` = ?,\n"///// 
                + "`classificacao_padrao_os` = ?,\n"/////
                + "`situacaoos` = ?,\n"////////////
                + "`classificacao_padrao_fatura` = ?,\n"
                + "`forma_pgto_fatura` = ?,"
                + "pergunta_movimento_cartao_abertura=?,\n"
                + "copiabkp = ?,"
                + "caminhoparacopia = ?"
                + "WHERE `cod` = ? AND `empresa_codempresa` = ?;";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtCaminhoDump.getText());
            pst.setString(2, cboHabilitaOsServ.getSelectedItem().toString());
            pst.setString(3, cboHabilitaOsProdServ.getSelectedItem().toString());
            pst.setString(4, cboHabilitaVenda.getSelectedItem().toString());
            pst.setString(5, cboHabilitaBackup.getSelectedItem().toString());
            pst.setString(6, txtEmailEnvio.getText());
            pst.setString(7, txtSenhaEmailEnvio.getText());
            pst.setString(8, txtSmtpEmailEnvio.getText());
            pst.setString(9, txtPortaEmailEnvio.getText());

            String smtp = cboSmtpAutenticado.getSelectedItem().toString();
            System.out.println(smtp);
            if (smtp.equals("") || smtp.equals(" ")) {
                pst.setString(10, null);
            } else {
                pst.setString(10, smtp);
            }

            pst.setString(11, cboEnviaEmailOs.getSelectedItem().toString());
            pst.setString(12, TelaPrincipal.lblCodUsoPrincipal.getText());
            pst.setInt(13, classificacao_auto_os);
            pst.setString(14, codclassificacao_finalizacao_os);

            String situacaoos = cboSituacaoOs.getSelectedItem().toString();
            if (situacaoos.equals("") || situacaoos.equals(" ")) {
                pst.setString(15, null);
            } else {
                pst.setString(15, situacaoos);
            }
            //cboSituacaoOs.getSelectedItem().toString());

            pst.setString(16, codclassificacao);
            pst.setString(17, null);

            String lancacartaoentrada = cboLancCartaoEntrar.getSelectedItem().toString();
            if (lancacartaoentrada.equals("") || lancacartaoentrada.equals(" ")) {
                pst.setString(18, null);
            } else {
                pst.setString(18, lancacartaoentrada);
            }

            String copiabkp = cboCopiaBkp.getSelectedItem().toString();
            if (copiabkp.equals("") || copiabkp.equals(" ")) {
                pst.setString(19, null);

            } else {
                pst.setString(19, copiabkp);
            }

            pst.setString(20, txtDestinoBkp.getText());

            pst.setString(21, lblCod.getText());
            pst.setInt(22, Integer.parseInt(TelaPrincipal.lblcodEmpresa.getText()));

            int alterabkp = pst.executeUpdate();
            if (alterabkp > 0) {
                JOptionPane.showMessageDialog(null, "Salvo com sucesso");

                TelaPrincipal.lblAviso.setText("Existem altearações pendentes, reinicie o sistema");
                TelaPrincipal.lblAviso.setToolTipText("Encerre o sistema para efetivar as alterações");

                TelaPrincipal.lblAviso.setVisible(true);

                Thread thread = new Thread(new Runnable() {
                    public void run() {
                        try {
                            while (true) {
                                EventQueue.invokeLater(new Runnable() {
                                    public void run() {
                                        TelaPrincipal.lblAviso.setVisible(!TelaPrincipal.lblAviso.isVisible());
                                    }
                                });
                                Thread.sleep(1000);
                                //Tempo em milisegundos para exibir e ocultar a label 
                            }
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
                thread.setDaemon(true);
                thread.start();

            }
        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void gravaconf() {

        String sql = " insert into configuracoes (caminho_backup,empresa_codempresa,osservico,osprodserv) values (?,?,?,?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtCaminhoDump.getText());
            pst.setString(2, TelaPrincipal.lblcodEmpresa.getText());
            pst.setString(3, cboHabilitaOsServ.getSelectedItem().toString());
            pst.setString(4, cboHabilitaOsProdServ.getSelectedItem().toString());
            int addbkp = pst.executeUpdate();
            if (addbkp > 0) {
                JOptionPane.showMessageDialog(null, "Salvo com sucesso");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /*
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnAlterar = new javax.swing.JButton();
        btncaminhoBkp = new javax.swing.JButton();
        lblCod = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        cboHabilitaVenda = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        cboHabilitaOsServ = new javax.swing.JComboBox<>();
        cboHabilitaOsProdServ = new javax.swing.JComboBox<>();
        cboHabilitaBackup = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        cboEnviaEmailOs = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        cboLancCartaoEntrar = new javax.swing.JComboBox<>();
        jLabel17 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtEmailEnvio = new javax.swing.JTextField();
        txtSenhaEmailEnvio = new javax.swing.JPasswordField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtSmtpEmailEnvio = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtPortaEmailEnvio = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        cboSmtpAutenticado = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        cboClassificacaoFatura = new javax.swing.JComboBox<>();
        jLabel18 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        cboFinanceiroOs = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        cboClassificacao_os_financeiro = new javax.swing.JComboBox<>();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        cboSituacaoOs = new javax.swing.JComboBox<>();
        jPanel9 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtCaminhoDump = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        cboCopiaBkp = new javax.swing.JComboBox<>();
        txtDestinoBkp = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setTitle("Configurações");
        setFrameIcon(null);
        setMaximumSize(new java.awt.Dimension(885, 511));
        setMinimumSize(new java.awt.Dimension(885, 511));
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

        btnAlterar.setText("Alterar");
        btnAlterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlterarActionPerformed(evt);
            }
        });

        btncaminhoBkp.setText("Salvar");
        btncaminhoBkp.setEnabled(false);
        btncaminhoBkp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncaminhoBkpActionPerformed(evt);
            }
        });

        lblCod.setVisible(false);
        lblCod.setText("cod");

        jTabbedPane1.setMaximumSize(new java.awt.Dimension(843, 414));
        jTabbedPane1.setMinimumSize(new java.awt.Dimension(843, 414));
        jTabbedPane1.setPreferredSize(new java.awt.Dimension(843, 414));

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel6.setToolTipText("");
        jPanel6.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel6.setMaximumSize(new java.awt.Dimension(889, 420));
        jPanel6.setMinimumSize(new java.awt.Dimension(889, 420));
        jPanel6.setPreferredSize(new java.awt.Dimension(889, 420));

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Telas Liberadas"));

        cboHabilitaVenda.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sim", "Não" }));

        jLabel8.setText("Habilita Venda:");

        jLabel9.setText("Habilita OS Prod + Serv:");

        jLabel10.setText("Habilita OS Serviço:");

        cboHabilitaOsServ.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sim", "Não" }));

        cboHabilitaOsProdServ.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sim", "Não" }));
        cboHabilitaOsProdServ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboHabilitaOsProdServActionPerformed(evt);
            }
        });

        cboHabilitaBackup.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sim", "Não" }));

        jLabel4.setText("Habilita Backup:");

        jLabel12.setText("Envia E-mail automatico O.S:");

        cboEnviaEmailOs.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sim", "Não", "Per" }));

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/rpsystem/icones/help.png"))); // NOI18N
        jLabel14.setToolTipText("Caminho icone do relatório = C:\\Windows \\logo.jpg");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(18, 18, 18)
                        .addComponent(cboHabilitaOsServ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(18, 18, 18)
                        .addComponent(cboHabilitaOsProdServ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(18, 18, 18)
                        .addComponent(cboHabilitaVenda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel12)
                            .addComponent(jLabel4))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cboHabilitaBackup, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboEnviaEmailOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel14)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboHabilitaOsServ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboHabilitaOsProdServ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboHabilitaVenda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboHabilitaBackup, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(cboEnviaEmailOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Ações ao entrar"));

        cboLancCartaoEntrar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Não", "Sim" }));

        jLabel17.setText("Lanc Cartão ao entrar: ");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17)
                .addGap(18, 18, 18)
                .addComponent(cboLancCartaoEntrar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(28, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(cboLancCartaoEntrar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(145, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(343, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(207, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Telas", jPanel6);

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel7.setMaximumSize(new java.awt.Dimension(889, 420));
        jPanel7.setMinimumSize(new java.awt.Dimension(889, 420));
        jPanel7.setName(""); // NOI18N

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Configuração e-mail"));

        jLabel1.setText("E-mail:");

        jLabel5.setText("Senha:");

        jLabel6.setText("Smtp:");

        jLabel7.setText("Porta:");

        txtPortaEmailEnvio.setText("0");

        jLabel11.setText("SMTP Autencicado:");

        cboSmtpAutenticado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "true", "false" }));

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/rpsystem/icones/help.png"))); // NOI18N
        jLabel13.setToolTipText("Para Gmail use a porta 465 e SMTP autenticado = true");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel13))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSmtpEmailEnvio)
                            .addComponent(txtSenhaEmailEnvio)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(txtPortaEmailEnvio, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cboSmtpAutenticado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtEmailEnvio))))
                .addGap(16, 16, 16))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtEmailEnvio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSenhaEmailEnvio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtSmtpEmailEnvio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtPortaEmailEnvio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(cboSmtpAutenticado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Tela de Fatura"));

        cboClassificacaoFatura.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));

        jLabel18.setText("Classificação padrão no financeiro:");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cboClassificacaoFatura, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboClassificacaoFatura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Grava financeiro após conclusão O.S"));

        cboFinanceiroOs.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Não", "Sim" }));
        cboFinanceiroOs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboFinanceiroOsActionPerformed(evt);
            }
        });

        jLabel2.setText("Insere financeiro automatico na os?");

        cboClassificacao_os_financeiro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));
        cboClassificacao_os_financeiro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboClassificacao_os_financeiroActionPerformed(evt);
            }
        });

        jLabel15.setText("Classificação padrão no financeiro:");

        jLabel16.setText("Selecione a situação de define quando finalizado");

        cboSituacaoOs.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel15)
            .addComponent(cboClassificacao_os_financeiro, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cboSituacaoOs, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jLabel2)
            .addComponent(cboFinanceiroOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboFinanceiroOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboSituacaoOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboClassificacao_os_financeiro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(250, 250, 250))))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(115, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("E-mail e outros", jPanel7);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Configurações Backup"));

        jLabel3.setText("Caminho Mysql Dump:");

        txtCaminhoDump.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCaminhoDumpActionPerformed(evt);
            }
        });

        jLabel22.setText("Copia para nuvem?");

        cboCopiaBkp.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Não", "Sim" }));

        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/rpsystem/icones/help.png"))); // NOI18N
        jLabel23.setToolTipText("");
        jLabel23.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel23MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel22)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboCopiaBkp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel23)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDestinoBkp, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCaminhoDump)))
                .addGap(18, 18, 18))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtCaminhoDump, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel22)
                        .addComponent(cboCopiaBkp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtDestinoBkp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(401, 401, 401))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(247, 247, 247))
        );

        jTabbedPane1.addTab("Backup", jPanel9);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(lblCod)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnAlterar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btncaminhoBkp))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 843, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCod)
                    .addComponent(btncaminhoBkp)
                    .addComponent(btnAlterar))
                .addGap(18, 18, 18)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 414, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        setBounds(0, 0, 885, 511);
    }// </editor-fold>//GEN-END:initComponents

    private void btncaminhoBkpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncaminhoBkpActionPerformed
        // TODO add your handling code here:
        gravaconf();
    }//GEN-LAST:event_btncaminhoBkpActionPerformed

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        // TODO add your handling code here:
        carregaConfigi();

    }//GEN-LAST:event_formInternalFrameOpened

    private void txtCaminhoDumpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCaminhoDumpActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCaminhoDumpActionPerformed

    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarActionPerformed
        // TODO add your handling code here:
        alteraconfig();
    }//GEN-LAST:event_btnAlterarActionPerformed

    private void cboHabilitaOsProdServActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboHabilitaOsProdServActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboHabilitaOsProdServActionPerformed

    private void cboFinanceiroOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboFinanceiroOsActionPerformed
        // TODO add your handling code here:
        if (cboFinanceiroOs.getSelectedItem().equals("Não")) {
            classificacao_auto_os = 0;

        } else {
            classificacao_auto_os = 1;
        }
    }//GEN-LAST:event_cboFinanceiroOsActionPerformed

    private void cboClassificacao_os_financeiroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboClassificacao_os_financeiroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboClassificacao_os_financeiroActionPerformed

    private void jLabel23MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel23MouseClicked
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(null, "Deve ser instalado o Google drive ou One Drive\n e informado o caminho no campo a seguir");
    }//GEN-LAST:event_jLabel23MouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAlterar;
    private javax.swing.JButton btncaminhoBkp;
    private javax.swing.JComboBox<String> cboClassificacaoFatura;
    private javax.swing.JComboBox<String> cboClassificacao_os_financeiro;
    private javax.swing.JComboBox<String> cboCopiaBkp;
    private javax.swing.JComboBox<String> cboEnviaEmailOs;
    private javax.swing.JComboBox<String> cboFinanceiroOs;
    private javax.swing.JComboBox<String> cboHabilitaBackup;
    private javax.swing.JComboBox<String> cboHabilitaOsProdServ;
    private javax.swing.JComboBox<String> cboHabilitaOsServ;
    private javax.swing.JComboBox<String> cboHabilitaVenda;
    private javax.swing.JComboBox<String> cboLancCartaoEntrar;
    private javax.swing.JComboBox<String> cboSituacaoOs;
    private javax.swing.JComboBox<String> cboSmtpAutenticado;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblCod;
    public static javax.swing.JTextField txtCaminhoDump;
    private javax.swing.JTextField txtDestinoBkp;
    private javax.swing.JTextField txtEmailEnvio;
    private javax.swing.JTextField txtPortaEmailEnvio;
    private javax.swing.JPasswordField txtSenhaEmailEnvio;
    private javax.swing.JTextField txtSmtpEmailEnvio;
    // End of variables declaration//GEN-END:variables
}
