/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package br.com.rpsystem.telas;

import br.com.rpsystem.dal.ModuloConexao;
import br.com.rpsystem.funcoes.Arquivo;
import br.com.rpsystem.funcoes.AtualizaSaldoContas;
import br.com.rpsystem.funcoes.CarregaSaldoContas;
import br.com.rpsystem.funcoes.CentralizaForm;
import br.com.rpsystem.funcoes.GeraBackup;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Rafael Veiga
 */
public class TelaPrincipal extends javax.swing.JFrame {

    public String aberto;
    public boolean inserefinanceiroauto;
    public String classificacaofinanceiraos;
    public String situacaoosfinal;
    String caminhoexecucao = System.getProperty("user.dir");
    String pergunta_mov_cartao_automatico;

    Object[] options = {"Sim", "Não"};
    Connection conexao = null;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sdfSaida = new SimpleDateFormat("dd/MM/yyyy");

    PreparedStatement pst = null;
    // PreparedStatement cofig = null;
    ResultSet rs = null;

    /**
     * Creates new form TelaPrincipal
     */
    public TelaPrincipal() {

        initComponents();

        conexao = ModuloConexao.conector();

        try {
            String dispositivo = InetAddress.getLocalHost().getHostName();
            lblDispositivo.setText(dispositivo);
        } catch (UnknownHostException ex) {
            Logger.getLogger(TelaLogin.class.getName()).log(Level.SEVERE, null, ex);
        }

        URL icone = this.getClass().getResource("/br/com/rpsystem/icones/iconeprincipal.png");
        Image imgImage = Toolkit.getDefaultToolkit().getImage(icone);
        this.setIconImage(imgImage);

        this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        String caminhoexecucao = System.getProperty("user.dir");

        

    }

    private void implantacao() {
        String user = " insert into usuarios (id,nome,fone,login,senha,perfil,excluido,usuarios_id) "
                + "values (1,'Admin','1','Admin','Admin','Admin',0,1);";
        try {
            pst = conexao.prepareStatement(user);

            int addbkp = pst.executeUpdate();
            if (addbkp > 0) {
                String emp = "INSERT INTO `empresa`\n"
                        + "(`codempresa`,`razaoSocial`,`fantasia`,`cnpjcpf`,`rginscri`,`usuarios_id`,`endereco`,`numero`,`cep`,`complemento`,`bairro`,\n"
                        + "`cidade`,`estado`)VALUES (1,'Implantação','Implantação',1,1,1,'a',1,1,'a','a','PP','SP');";
                try {

                    pst = conexao.prepareStatement(emp);
                    int addemp = pst.executeUpdate();
                    if (addemp > 0) {
                        String bkp = "  insert into configuracoes (cod,caminho_backup,empresa_codempresa,osservico,osprodserv,vendas) \n"
                                + " values (1,'C:/Program Files/MySQL/MySQL Server 8.0/bin',1,'Sim','Sim','Sim')";
                        try {

                            pst = conexao.prepareStatement(bkp);
                            int cbkp = pst.executeUpdate();
                            if (cbkp > 0) {
                                JOptionPane.showMessageDialog(null, "Configurções Defaut inserida!");

                                String cestado = " INSERT INTO `estados` VALUES (1,'Acre','AC'),(2,'Alagoas','AL'),(3,'Amapá','AP'),"
                                        + "(4,'Amazonas','AM'),(5,'Bahia','BA'),(6,'Ceara','CE'),(7,'Distrito Federal','DF'),(8,'Espírito Santo','ES'),"
                                        + "(9,'Goiás','GO'),(10,'Maranhão','MA'),(11,'Mato Grosso','MT'),(12,'Mato Grosso do Sul','MS'),"
                                        + "(13,'Minas Gerais','MG'),(14,'Pará','PA'),(15,'Paraíba','PB'),(16,'Paraná','PR'),(17,'Pernambuco','PE'),"
                                        + "(18,'Piauí','PI'),(19,'Rio de Janeiro','RJ'),(20,'Rio Grande do Norte','RN'),(21,'Rio Grande do Sul','RS'),"
                                        + "(22,'Rondônia','RO'),(23,'Roraima','RR'),(24,'Santa Catarina','SC'),(25,'São Paulo','SP'),(26,'Sergipe','SE'),"
                                        + "(27,'Tocantins','TO')";

                                try {
                                    pst = conexao.prepareStatement(cestado);

                                    int criaestado = pst.executeUpdate();
                                    if (criaestado > 0) {
                                        JOptionPane.showMessageDialog(null, "Tabela estados Criada");

                                        String cli = "INSERT INTO pessoas\n"
                                                + "(idclientes,nomeCliente,status,telefone,endereco,numero,complemento,bairro,cidade,cep,estado,cpfcnpj,rginscricao,email,usuarios_id,empresa_codempresa)VALUES\n"
                                                + "('1','VENDA AO CONSUMIDOR','Ativo','11111111111','A','1','','A','A','1','SP','11111111111','1','a@a.com',1,1);";
                                        try {
                                            pst = conexao.prepareStatement(cli);
                                            int criacli = pst.executeUpdate();
                                            if (criacli > 0) {
                                                JOptionPane.showMessageDialog(null, "Cliente 1 insereido");
                                                String grupo = "INSERT INTO grupos\n"
                                                        + "(codgrupos,descricaogrupo,excluido,usuarios_id,empresa_codempresa)\n"
                                                        + "VALUES\n"
                                                        + "('1','Padrão','0','1',1)";
                                                try {
                                                    pst = conexao.prepareStatement(grupo);
                                                    int criagrupo = pst.executeUpdate();
                                                    if (criagrupo > 0) {
                                                        JOptionPane.showMessageDialog(null, "Grupo inserido");

                                                        String vendedor = "INSERT INTO vendedores\n"
                                                                + "(codVendedor,nomeVendedor,foneVendedor,usuarios_id,demitido,dtdemissao,empresa_codempresa)\n"
                                                                + "VALUES(1,'CAIXA','11111111111',1,'Nao','',1)";
                                                        try {
                                                            pst = conexao.prepareStatement(vendedor);
                                                            int criavendedor = pst.executeUpdate();
                                                            if (criavendedor > 0) {
                                                                JOptionPane.showMessageDialog(null, "Venderdor 1 inserido");

                                                                String status = "INSERT INTO `statusos`\n"
                                                                        + "(`codstatus`,`descricao`,`excluido`,`usuarios_id`,empresa_codempresa)\n"
                                                                        + "VALUES(1,'PADRÃO',0,1,1);";

                                                                try {
                                                                    pst = conexao.prepareStatement(status);
                                                                    int crioustatus = pst.executeUpdate();
                                                                    if (crioustatus > 0) {
                                                                        JOptionPane.showMessageDialog(null, "Status padrão criado");
                                                                    }

                                                                } catch (Exception e) {
                                                                    JOptionPane.showMessageDialog(null, e);
                                                                }
                                                            }
                                                        } catch (Exception e) {
                                                            JOptionPane.showMessageDialog(null, e);
                                                        }

                                                    }

                                                } catch (Exception e) {
                                                    JOptionPane.showMessageDialog(null, e);
                                                }
                                            }
                                        } catch (Exception e) {
                                            JOptionPane.showMessageDialog(null, e);
                                        }

                                        JOptionPane.showMessageDialog(null, "O sistema será encerrado");
                                        this.dispose();
                                    }
                                } catch (Exception e) {
                                    JOptionPane.showMessageDialog(null, e);
                                }

                            }
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, e);
                        }
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
                }

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    private void setaempresa() {

        String consultaemp = "Select codempresa,fantasia,telefone from empresa where codempresa = '" + lblcodEmpresa.getText() + "'";
        // System.Systemout.println(consultaemp);
        try {
            pst = conexao.prepareStatement(consultaemp);
            rs = pst.executeQuery();
            if (rs.next()) {
                // TelaPrincipal.lblcodEmpresa.setText(rs.getString(1));
                TelaPrincipal.lblFantasiaEmpresaprin.setText(rs.getString(2));
                TelaPrincipal.lblFoneEmpresa.setText(rs.getString(3));

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    public void lerconfiguracao() {
        String emp = TelaLogin.lblCodempresa.getText();

        String configuracoes = "select * from configuracoes where empresa_codempresa =" + emp;
        String acesso = "Select * from acessos";

        try {
            pst = conexao.prepareStatement(configuracoes);
            //  pst.setString(1,  TelaLogin.lblCodempresa.getText());
            rs = pst.executeQuery();
            if (rs.next()) {
                String os1 = rs.getString(4);
                if (os1.equals("Sim")) {
                    TelaPrincipal.menOs.setVisible(true);

                }
                if (os1.equals("Não")) {
                    TelaPrincipal.menOs.setVisible(false);
                }
                String os2 = rs.getString(5);
                if (os2.equals("Sim")) {
                    TelaPrincipal.menOrc2.setVisible(true);

                } else {
                    TelaPrincipal.menOrc2.setVisible(false);
                }
                String venda = rs.getString(6);
                if (venda.equals("Sim")) {
                    TelaPrincipal.menVendas.setVisible(true);
                } else {
                    TelaPrincipal.menVendas.setVisible(false);
                }
                String backup = rs.getString(7);
                if (backup.equals("Sim")) {
                    TelaPrincipal.menBackup.setVisible(true);
                } else {
                    TelaPrincipal.menBackup.setVisible(false);
                }
                lblenviaemailos.setText(rs.getString(14));

                inserefinanceiroauto = rs.getBoolean(16);
                if (inserefinanceiroauto) {
                    //  System.out.println(inserefinanceiroauto);
                    lblInsereFinAuto.setText("true");
                } else {
                    lblInsereFinAuto.setText("false");
                }
                classificacaofinanceiraos = rs.getString(17);
                lblClassificacaofinanceira.setText(classificacaofinanceiraos);
                lblSituacaoosfinaliza.setText(rs.getString(18));

                lblClassificacaoFatura.setText(rs.getString(19));

                lblFormaPgtoFatura.setText(rs.getString(20));
                pergunta_mov_cartao_automatico = rs.getString(21);
                //System.out.println(inserefinanceiroauto);
            } else {
                JOptionPane.showMessageDialog(null, "Não encontrado");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);

        }

        try {
            pst = null;
            pst = conexao.prepareStatement(acesso);
            rs = pst.executeQuery();
//            if (rs.next()) {
//                String exibeiconeos = rs.getString(2);
                // System.out.println(exibeiconeos);
//                if (exibeiconeos.equals("1")) {
//                    lblIconeOrc.setVisible(true);
//                }
//                if (exibeiconeos.equals("0")) {
//                    lblIconeOrc.setVisible(false);
//                } else {
//                    lblIconeOrc.setVisible(false);
//                }
//
//            } else {
//                lblIconeOrc.setVisible(false);
//            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void relCliente() {
        String caminhoExecucao = System.getProperty("user.dir");

        int confirma = JOptionPane.showOptionDialog(
                null, "Confirma a impressão do relatório? ", "Atenção",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]
        );

        if (confirma == JOptionPane.YES_OPTION) {
            try {
                JasperPrint print = JasperFillManager.fillReport(
                        getClass().getResourceAsStream("/reports/rel_lista_cli.jasper"),
                        null,
                        conexao
                );
                JasperViewer.viewReport(print, false);

                // Exportando o relatório para PDF
                OutputStream outputStream = new FileOutputStream(
                        new File(caminhoExecucao + "\\relatorios\\" + menRelClientes.getText().concat(".pdf"))
                );
                JasperExportManager.exportReportToPdfStream(print, outputStream);

                // Adicione aqui o código para abrir o relatório PDF, se necessário
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    private void relListaProdutos() {

        //  int sair = JOptionPane.showOptionDialog(null, "Tem certeza que deseja trocar o usuário?", "Atenção", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,null,options,options[1]);
        int confirma = JOptionPane.showOptionDialog(null, "Confirma a impressão do relatório? ", "Atenção", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
        if (confirma == JOptionPane.YES_OPTION) {
            int modelo = JOptionPane.showOptionDialog(null, "Exibir os produtos Inativos?", "Atenção", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
            if (modelo == JOptionPane.YES_OPTION) {

                try {
                    // usando o jasper para gerar o relatório
                    JasperPrint print = JasperFillManager.fillReport(getClass().getResourceAsStream("/reports/listaProdutos.jasper"), null, conexao);
                    JasperViewer.viewReport(print, false);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
                }

            } else {
                // Somente produtos ativos
                try {
                    JasperPrint print = JasperFillManager.fillReport(getClass().getResourceAsStream("/reports/listaProdutosSomenteAtivos.jasper"), null, conexao);
                    JasperViewer.viewReport(print, false);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
                }

            }

        }
    }

//    private void saindo() {
//        if (options.length >= 2) {
//            JOptionPane pane = new JOptionPane("Tem certeza que deseja sair?",
//                    JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION, null, options, options[1]);
//            JDialog dialog = pane.createDialog(null, "Atenção");
//            dialog.setVisible(true);
//
//            Object selectedValue = pane.getValue();
//
//            if (selectedValue instanceof Integer) {
//                int sair = (int) selectedValue;
//
//                if (sair == JOptionPane.YES_OPTION) {
//                    dispose();
//                }
//            }
//        }
//
//    }

     private void saindo() {
        if (options.length >= 2) {
            JOptionPane pane = new JOptionPane("Tem certeza que deseja sair?",
                    JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION, null, options, options[1]);
            JDialog dialog = pane.createDialog(null, "Atenção");
            dialog.setVisible(true);

            Object selectedValue = pane.getValue();

            if (selectedValue instanceof Integer) {
                int sair = (int) selectedValue;

                if (sair == JOptionPane.NO_OPTION) {
                    dialog.dispose();
                    
                } else if (sair == JOptionPane.YES_OPTION) {
                   dispose();
                }
            }
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

        jMenu1 = new javax.swing.JMenu();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        Desktop = new javax.swing.JDesktopPane();
        lblDispositivo = new javax.swing.JLabel();
        lblFoneEmpresa = new javax.swing.JLabel();
        lblenviaemailos = new javax.swing.JLabel();
        lblClassificacaofinanceira = new javax.swing.JLabel();
        lblSituacaoosfinaliza = new javax.swing.JLabel();
        lblInsereFinAuto = new javax.swing.JLabel();
        lblClassificacaoFatura = new javax.swing.JLabel();
        lblFormaPgtoFatura = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblSaldo = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        lblUsuario = new javax.swing.JLabel();
        lblData = new javax.swing.JLabel();
        lblcodEmpresa = new javax.swing.JLabel();
        lblFantasiaEmpresaprin = new javax.swing.JLabel();
        lblCodUsoPrincipal = new javax.swing.JLabel();
        lblAviso = new javax.swing.JLabel();
        Menu = new javax.swing.JMenuBar();
        menCad = new javax.swing.JMenu();
        menPessoas = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        menProduto = new javax.swing.JMenuItem();
        menGrupos = new javax.swing.JMenuItem();
        menCadFormaPgto = new javax.swing.JMenuItem();
        menCadVendedorTecnico = new javax.swing.JMenuItem();
        menStatusOs = new javax.swing.JMenuItem();
        menUsuarios = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenu5 = new javax.swing.JMenu();
        menClassificacao = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();
        menLancamentoFinanceiro = new javax.swing.JMenuItem();
        menMoviCartao = new javax.swing.JMenuItem();
        menFatura = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        menOs = new javax.swing.JMenuItem();
        menOrc2 = new javax.swing.JMenuItem();
        menRel = new javax.swing.JMenu();
        menRelClientes = new javax.swing.JMenuItem();
        menRelProd = new javax.swing.JMenuItem();
        menRelFinanceiro = new javax.swing.JMenuItem();
        menRelMovimentoCartoes = new javax.swing.JMenuItem();
        menAjuda = new javax.swing.JMenu();
        menSobre = new javax.swing.JMenuItem();
        menAnydesk = new javax.swing.JMenuItem();
        menCalc = new javax.swing.JMenuItem();
        menSair = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        menTrocaUsuario = new javax.swing.JMenuItem();
        menBackup = new javax.swing.JMenuItem();
        menEmpresa = new javax.swing.JMenuItem();
        menConfig = new javax.swing.JMenuItem();
        menProgramacao = new javax.swing.JMenu();
        menVendas = new javax.swing.JMenuItem();
        menStartImpantacao = new javax.swing.JMenuItem();

        jMenu1.setText("jMenu1");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(jList1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("RpSystem");
        setBackground(new java.awt.Color(51, 51, 51));
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        Desktop.setBackground(new java.awt.Color(102, 102, 102));
        Desktop.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        Desktop.setToolTipText("");
        Desktop.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        Desktop.setPreferredSize(new java.awt.Dimension(907, 503));

        lblDispositivo.setVisible(false);
        lblDispositivo.setText("nome do pc");

        lblFoneEmpresa.setVisible(false);
        lblFoneEmpresa.setText("foneempresa");

        lblenviaemailos.setVisible(false);
        lblenviaemailos.setText("enviaEmailOs");

        lblClassificacaofinanceira.setVisible(false);
        lblClassificacaofinanceira.setText("ClassificacaoPadrao");

        lblSituacaoosfinaliza.setVisible(false);
        lblSituacaoosfinaliza.setText("SituacaoInsertFin");

        lblInsereFinAuto.setVisible(false);
        lblInsereFinAuto.setText("InsereOSAuto");

        lblClassificacaoFatura.setVisible(false);
        lblClassificacaoFatura.setText("lblClassificacaoFatura");

        lblFormaPgtoFatura.setVisible(false);
        lblFormaPgtoFatura.setText("lblFormaPgtoFatura");

        tblSaldo = new javax.swing.JTable(){
            public boolean  isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        tblSaldo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        tblSaldo.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        tblSaldo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Conta", "Saldo"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tblSaldo);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/rpsystem/icones/logo_novoSF.png"))); // NOI18N
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });

        Desktop.setLayer(lblDispositivo, javax.swing.JLayeredPane.DEFAULT_LAYER);
        Desktop.setLayer(lblFoneEmpresa, javax.swing.JLayeredPane.DEFAULT_LAYER);
        Desktop.setLayer(lblenviaemailos, javax.swing.JLayeredPane.DEFAULT_LAYER);
        Desktop.setLayer(lblClassificacaofinanceira, javax.swing.JLayeredPane.DEFAULT_LAYER);
        Desktop.setLayer(lblSituacaoosfinaliza, javax.swing.JLayeredPane.DEFAULT_LAYER);
        Desktop.setLayer(lblInsereFinAuto, javax.swing.JLayeredPane.DEFAULT_LAYER);
        Desktop.setLayer(lblClassificacaoFatura, javax.swing.JLayeredPane.DEFAULT_LAYER);
        Desktop.setLayer(lblFormaPgtoFatura, javax.swing.JLayeredPane.DEFAULT_LAYER);
        Desktop.setLayer(jScrollPane2, javax.swing.JLayeredPane.DEFAULT_LAYER);
        Desktop.setLayer(jLabel2, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout DesktopLayout = new javax.swing.GroupLayout(Desktop);
        Desktop.setLayout(DesktopLayout);
        DesktopLayout.setHorizontalGroup(
            DesktopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, DesktopLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(DesktopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, DesktopLayout.createSequentialGroup()
                        .addGroup(DesktopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(DesktopLayout.createSequentialGroup()
                                .addComponent(lblenviaemailos)
                                .addGap(32, 32, 32)
                                .addComponent(lblFoneEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(lblDispositivo, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(DesktopLayout.createSequentialGroup()
                                .addComponent(lblInsereFinAuto)
                                .addGap(30, 30, 30)
                                .addComponent(lblSituacaoosfinaliza)
                                .addGap(18, 18, 18)
                                .addComponent(lblClassificacaofinanceira)))
                        .addGap(10, 10, 10))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, DesktopLayout.createSequentialGroup()
                        .addComponent(lblFormaPgtoFatura)
                        .addGap(84, 84, 84)
                        .addComponent(lblClassificacaoFatura)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, DesktopLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46))
        );
        DesktopLayout.setVerticalGroup(
            DesktopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DesktopLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 264, Short.MAX_VALUE)
                .addGroup(DesktopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, DesktopLayout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, DesktopLayout.createSequentialGroup()
                        .addGroup(DesktopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblClassificacaoFatura)
                            .addComponent(lblFormaPgtoFatura))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(DesktopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblClassificacaofinanceira)
                            .addComponent(lblSituacaoosfinaliza)
                            .addComponent(lblInsereFinAuto))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(DesktopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblDispositivo)
                            .addComponent(lblFoneEmpresa)
                            .addComponent(lblenviaemailos))
                        .addGap(20, 20, 20))))
        );

        lblUsuario.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        lblUsuario.setText("Usuario");

        lblData.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        lblData.setText("Data");

        //lblcodEmpresa.setVisible(false);
        lblcodEmpresa.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        lblcodEmpresa.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblcodEmpresa.setText("1");

        lblFantasiaEmpresaprin.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        lblFantasiaEmpresaprin.setText("Nome Fantasia");

        lblCodUsoPrincipal.setVisible(false);
        lblCodUsoPrincipal.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        lblCodUsoPrincipal.setText("cod");

        lblAviso.setVisible(false);
        lblAviso.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        lblAviso.setForeground(new java.awt.Color(255, 0, 51));
        lblAviso.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAviso.setText("Avisoaqui");
        lblAviso.setToolTipText("");

        menCad.setText("Cadastros");
        menCad.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        menCad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menCadActionPerformed(evt);
            }
        });

        menPessoas.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        menPessoas.setText("Cadastro Pessoas");
        menPessoas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menPessoasActionPerformed(evt);
            }
        });
        menCad.add(menPessoas);

        jMenu3.setText("Produtos");
        jMenu3.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N

        menProduto.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.ALT_DOWN_MASK));
        menProduto.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        menProduto.setText("Produtos");
        menProduto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menProdutoActionPerformed(evt);
            }
        });
        jMenu3.add(menProduto);

        menGrupos.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.ALT_DOWN_MASK));
        menGrupos.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        menGrupos.setText("Cadastro de Grupos");
        menGrupos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menGruposActionPerformed(evt);
            }
        });
        jMenu3.add(menGrupos);

        menCad.add(jMenu3);

        menCadFormaPgto.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        menCadFormaPgto.setText("Formas Pagamento");
        menCadFormaPgto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menCadFormaPgtoActionPerformed(evt);
            }
        });
        menCad.add(menCadFormaPgto);

        menCadVendedorTecnico.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        menCadVendedorTecnico.setText("Vendedores/Técnicos");
        menCadVendedorTecnico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menCadVendedorTecnicoActionPerformed(evt);
            }
        });
        menCad.add(menCadVendedorTecnico);

        menStatusOs.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        menStatusOs.setText("Cadastro Status Os");
        menStatusOs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menStatusOsActionPerformed(evt);
            }
        });
        menCad.add(menStatusOs);

        menUsuarios.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        menUsuarios.setText("Usuários");
        menUsuarios.setEnabled(false);
        menUsuarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menUsuariosActionPerformed(evt);
            }
        });
        menCad.add(menUsuarios);

        Menu.add(menCad);

        jMenu4.setText("Financeiro");
        jMenu4.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jMenu4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu4ActionPerformed(evt);
            }
        });

        jMenu5.setText("Cadastros");
        jMenu5.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N

        menClassificacao.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        menClassificacao.setText("Cadastro de Classificação");
        menClassificacao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menClassificacaoActionPerformed(evt);
            }
        });
        jMenu5.add(menClassificacao);

        jMenuItem2.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jMenuItem2.setText("Cadastro cartões");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem2);

        jMenuItem3.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jMenuItem3.setText("Cadastro de contas");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem3);

        jMenu4.add(jMenu5);

        jMenu6.setText("Lancamentos");
        jMenu6.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N

        menLancamentoFinanceiro.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        menLancamentoFinanceiro.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        menLancamentoFinanceiro.setText("Lançamento de contas");
        menLancamentoFinanceiro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menLancamentoFinanceiroActionPerformed(evt);
            }
        });
        jMenu6.add(menLancamentoFinanceiro);

        menMoviCartao.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        menMoviCartao.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        menMoviCartao.setText("Movimento de Cartões");
        menMoviCartao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menMoviCartaoActionPerformed(evt);
            }
        });
        jMenu6.add(menMoviCartao);

        menFatura.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        menFatura.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        menFatura.setText("Controle de Faturas");
        menFatura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menFaturaActionPerformed(evt);
            }
        });
        jMenu6.add(menFatura);

        jMenu4.add(jMenu6);

        Menu.add(jMenu4);

        jMenu2.setText("Movimentações");
        jMenu2.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N

        menOs.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        menOs.setText("OS");
        menOs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menOsActionPerformed(evt);
            }
        });
        jMenu2.add(menOs);

        menOrc2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        menOrc2.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        menOrc2.setText("Venda/OS Prod + Serv");
        menOrc2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menOrc2ActionPerformed(evt);
            }
        });
        jMenu2.add(menOrc2);

        Menu.add(jMenu2);

        menRel.setText("Relatorios");
        menRel.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        menRel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menRelActionPerformed(evt);
            }
        });

        menRelClientes.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        menRelClientes.setText("Relatório de Clientes");
        menRelClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menRelClientesMouseClicked(evt);
            }
        });
        menRelClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menRelClientesActionPerformed(evt);
            }
        });
        menRel.add(menRelClientes);

        menRelProd.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        menRelProd.setText("Relatório de Produto");
        menRelProd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menRelProdActionPerformed(evt);
            }
        });
        menRel.add(menRelProd);

        menRelFinanceiro.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        menRelFinanceiro.setText("Relatório Financeiro");
        menRelFinanceiro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menRelFinanceiroActionPerformed(evt);
            }
        });
        menRel.add(menRelFinanceiro);

        menRelMovimentoCartoes.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        menRelMovimentoCartoes.setText("Rel movimentações Cartoes");
        menRelMovimentoCartoes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menRelMovimentoCartoesActionPerformed(evt);
            }
        });
        menRel.add(menRelMovimentoCartoes);

        Menu.add(menRel);

        menAjuda.setText("Ajuda");
        menAjuda.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        menAjuda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menAjudaActionPerformed(evt);
            }
        });

        menSobre.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        menSobre.setText("Sobre");
        menSobre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menSobreActionPerformed(evt);
            }
        });
        menAjuda.add(menSobre);

        menAnydesk.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        menAnydesk.setText("Any Desk");
        menAnydesk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menAnydeskActionPerformed(evt);
            }
        });
        menAjuda.add(menAnydesk);

        menCalc.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        menCalc.setText("Calculadora");
        menCalc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menCalcActionPerformed(evt);
            }
        });
        menAjuda.add(menCalc);

        Menu.add(menAjuda);

        menSair.setText("Opções");
        menSair.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        menSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menSairActionPerformed(evt);
            }
        });

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_DOWN_MASK));
        jMenuItem1.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jMenuItem1.setText("Sair");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        menSair.add(jMenuItem1);

        menTrocaUsuario.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.ALT_DOWN_MASK));
        menTrocaUsuario.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        menTrocaUsuario.setText("Trocar Usuário");
        menTrocaUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menTrocaUsuarioActionPerformed(evt);
            }
        });
        menSair.add(menTrocaUsuario);

        menBackup.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.ALT_DOWN_MASK));
        menBackup.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        menBackup.setText("Backup");
        menBackup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menBackupActionPerformed(evt);
            }
        });
        menSair.add(menBackup);

        menEmpresa.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.ALT_DOWN_MASK));
        menEmpresa.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        menEmpresa.setText("Empresa");
        menEmpresa.setEnabled(false);
        menEmpresa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menEmpresaActionPerformed(evt);
            }
        });
        menSair.add(menEmpresa);

        menConfig.setVisible(false);
        menConfig.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        menConfig.setText("Configurações");
        menConfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menConfigActionPerformed(evt);
            }
        });
        menSair.add(menConfig);

        Menu.add(menSair);

        menProgramacao.setVisible(false);
        menProgramacao.setText("Suporte");
        menProgramacao.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N

        menVendas.setVisible(false);
        menVendas.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        menVendas.setText("Vendas");
        menVendas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menVendasActionPerformed(evt);
            }
        });
        menProgramacao.add(menVendas);

        menStartImpantacao.setVisible(false);
        menStartImpantacao.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        menStartImpantacao.setText("Start Implantacao");
        menProgramacao.add(menStartImpantacao);

        Menu.add(menProgramacao);

        setJMenuBar(Menu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblCodUsoPrincipal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblUsuario)
                .addGap(176, 176, 176)
                .addComponent(lblcodEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblFantasiaEmpresaprin, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblAviso, javax.swing.GroupLayout.PREFERRED_SIZE, 384, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 27, Short.MAX_VALUE)
                .addComponent(lblData, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(Desktop, javax.swing.GroupLayout.DEFAULT_SIZE, 1052, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(Desktop, javax.swing.GroupLayout.DEFAULT_SIZE, 537, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblUsuario)
                    .addComponent(lblData, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblcodEmpresa)
                    .addComponent(lblFantasiaEmpresaprin)
                    .addComponent(lblCodUsoPrincipal)
                    .addComponent(lblAviso))
                .addGap(2, 2, 2))
        );

        setSize(new java.awt.Dimension(1068, 587));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void menUsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menUsuariosActionPerformed
        // TODO add your handling code here:
        // as linhas abixo abrem o form da tela do usuário.
        //Desktop.getDesktopManager().maximizeFrame(usuario);
        TelaUsuario uso = new TelaUsuario();
        uso.setVisible(true);
        Desktop.add(uso);
        //coiso 
        CentralizaForm c = new CentralizaForm(uso);
        c.centralizaForm();
        //fim coiso 


    }//GEN-LAST:event_menUsuariosActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:

        // String nomeArquivo = "backup.txt";
        Date data = new Date();
        DateFormat formatador = DateFormat.getDateInstance(DateFormat.SHORT);
        lblData.setText(formatador.format(data));
        //geralog();

        File pasta = new File(caminhoexecucao + "\\relatorios");
        if (!pasta.exists()) {
            pasta.mkdirs();
        }

    }//GEN-LAST:event_formWindowActivated

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        saindo();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void menAjudaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menAjudaActionPerformed
        // TODO add your handling code here:
        // Chamando a tela sobre

    }//GEN-LAST:event_menAjudaActionPerformed

    private void menSobreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menSobreActionPerformed
        // TODO add your handling code here:
        TelaSobre sobre = new TelaSobre();
        sobre.setVisible(true);
        Desktop.add(sobre);
        CentralizaForm c = new CentralizaForm(sobre);
        c.centralizaForm();


    }//GEN-LAST:event_menSobreActionPerformed

    private void menTrocaUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menTrocaUsuarioActionPerformed
        // TODO add your handling code here:
        //int sair = JOptionPane.showOptionDialog(null, "Tem certeza que deseja sair?", "Atenção", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
        int sair = JOptionPane.showOptionDialog(null, "Tem certeza que deseja trocar o usuário?", "Atenção", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
        if (sair == JOptionPane.YES_OPTION) {
            //  System.exit(sair);
            TelaLogin login = new TelaLogin();
            login.setVisible(true);
            this.dispose();
        }
    }//GEN-LAST:event_menTrocaUsuarioActionPerformed

    private void menBackupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menBackupActionPerformed

        GeraBackup backupMySQL = new GeraBackup();
        try {
            backupMySQL.GerarBackup();
        } catch (InterruptedException ex) {
            Logger.getLogger(TelaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_menBackupActionPerformed

    private void menPessoasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menPessoasActionPerformed
        // TODO add your handling code here:

        TelaPessoas pessoas = new TelaPessoas();
        pessoas.setVisible(true);
        Desktop.add(pessoas);
        TelaPessoas.btnIncluir.requestFocus();
        CentralizaForm c = new CentralizaForm(pessoas);
        c.centralizaForm();
        //fim coiso 

    }//GEN-LAST:event_menPessoasActionPerformed

    private void menOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menOsActionPerformed
        // TODO add your handling code here:
        TelaOs os = new TelaOs();
        os.setVisible(true);
        Desktop.add(os);
        CentralizaForm c = new CentralizaForm(os);
        c.centralizaForm();
    }//GEN-LAST:event_menOsActionPerformed

    private void menConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menConfigActionPerformed
        // TODO add your handling code here:
        TelaConfiguracaoes conf = new TelaConfiguracaoes();
        conf.setVisible(true);
        Desktop.add(conf);
        //coiso 
        CentralizaForm c = new CentralizaForm(conf);
        c.centralizaForm();
        //fim coiso 
    }//GEN-LAST:event_menConfigActionPerformed

    private void menEmpresaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menEmpresaActionPerformed
        // TODO add your handling code here:
        TelaEmpresa emp = new TelaEmpresa();
        emp.setVisible(true);
        Desktop.add(emp);
        //coiso 
        CentralizaForm c = new CentralizaForm(emp);
        c.centralizaForm();
        //fim coiso 
    }//GEN-LAST:event_menEmpresaActionPerformed

    private void menCadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menCadActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_menCadActionPerformed

    private void menCadVendedorTecnicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menCadVendedorTecnicoActionPerformed
        // TODO add your handling code here:

        TelaVendedor vendedor = new TelaVendedor();
        vendedor.setVisible(true);
        Desktop.add(vendedor);
        //coiso 
        CentralizaForm c = new CentralizaForm(vendedor);
        c.centralizaForm();
        //fim coiso 
    }//GEN-LAST:event_menCadVendedorTecnicoActionPerformed

    private void menRelClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menRelClientesActionPerformed
        // Gerando relatório de clientes
        relCliente();
    }//GEN-LAST:event_menRelClientesActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:

        setaempresa();
        lerconfiguracao();

        CarregaSaldoContas carrega = new CarregaSaldoContas(tblSaldo);
        carrega.exibesaldo();

        if (pergunta_mov_cartao_automatico != null) {
            if (pergunta_mov_cartao_automatico.equals("Sim")) {
                if (options.length >= 2) {
                    int movicartao = JOptionPane.showOptionDialog(null, "Deseja lançar um movimento de cartão agora?", "Atenção", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
                    if (movicartao == JOptionPane.YES_OPTION) {
                        TelaMovimentacaoCartoes cartao = new TelaMovimentacaoCartoes();
                        cartao.setVisible(true);
                        Desktop.add(cartao);
                        CentralizaForm c = new CentralizaForm(cartao);
                        c.centralizaForm();
                        cartao.toFront();
                        cartao.btnIncluir.requestFocus();
                    }
                } else {
                    // Lógica para lidar com o array options que não tem pelo menos dois elementos
                }
            }
        }
        // gerando bkp em segundo plano

        if (lblUsuario.getText().equals("Programador")) {

        } else {
            SwingUtilities.invokeLater(() -> {
                GeraBackup gb = new GeraBackup();
                try {
                    gb.GerarBackup();
                } catch (InterruptedException ex) {
                    Logger.getLogger(TelaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

        }


    }//GEN-LAST:event_formWindowOpened

    private void menProdutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menProdutoActionPerformed
        // TODO add your handling code here:
        TelaProdutos produtos = new TelaProdutos();
        produtos.setVisible(true);
        Desktop.add(produtos);
        //coiso 
        CentralizaForm c = new CentralizaForm(produtos);
        c.centralizaForm();
        //fim coiso 
    }//GEN-LAST:event_menProdutoActionPerformed

    private void menGruposActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menGruposActionPerformed
        // TODO add your handling code here:
        TelaGrupo grupo = new TelaGrupo();
        grupo.setVisible(true);
        Desktop.add(grupo);
        //coiso 
        CentralizaForm c = new CentralizaForm(grupo);
        c.centralizaForm();
        //fim coiso 
    }//GEN-LAST:event_menGruposActionPerformed

    private void menVendasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menVendasActionPerformed
        // TODO add your handling code here:

        TelaVendas vendas = new TelaVendas();
        vendas.setVisible(true);
        Desktop.add(vendas);
        Desktop.getDesktopManager().maximizeFrame(vendas);
        //coiso 
        CentralizaForm c = new CentralizaForm(vendas);
        c.centralizaForm();
        //fim coiso 

    }//GEN-LAST:event_menVendasActionPerformed

    private void menRelClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menRelClientesMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_menRelClientesMouseClicked

    private void menRelProdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menRelProdActionPerformed
        // TODO add your handling code here:
        relListaProdutos();
    }//GEN-LAST:event_menRelProdActionPerformed

    private void menStatusOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menStatusOsActionPerformed
        // TODO add your handling code here:
        TelaStatusOs status = new TelaStatusOs();
        status.setVisible(true);
        Desktop.add(status);
        //coiso 
        CentralizaForm c = new CentralizaForm(status);
        c.centralizaForm();
        //fim coiso 
    }//GEN-LAST:event_menStatusOsActionPerformed

    private void menOrc2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menOrc2ActionPerformed
        // TODO add your handling code here:
        TelaOsProdeServ osps = new TelaOsProdeServ();
        osps.setVisible(true);
        Desktop.add(osps);
        CentralizaForm c = new CentralizaForm(osps);
        c.centralizaForm();
        //fim coiso 
        TelaOsProdeServ.txtNumOS.requestFocus();
        osps.toFront();

    }//GEN-LAST:event_menOrc2ActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        saindo();
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        saindo();


    }//GEN-LAST:event_formWindowClosing

    private void menRelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menRelActionPerformed
        // TODO add your handling code here:


    }//GEN-LAST:event_menRelActionPerformed

    private void menSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menSairActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_menSairActionPerformed

    private void menCalcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menCalcActionPerformed
        try {
            // TODO add your handling code here:
            Runtime.getRuntime().exec("calc");

        } catch (IOException ex) {
            Logger.getLogger(TelaPrincipal.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_menCalcActionPerformed

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_formMouseClicked

    private void menAnydeskActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menAnydeskActionPerformed
        // TODO add your handling code here:
        try {
            // TODO add your handling code here:

            Runtime.getRuntime().exec("Anydesk");

        } catch (IOException ex) {
            Logger.getLogger(TelaPrincipal.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_menAnydeskActionPerformed

    private void menCadFormaPgtoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menCadFormaPgtoActionPerformed
        // TODO add your handling code here:
        TelaCadFormasPgto pgto = new TelaCadFormasPgto();
        pgto.setVisible(true);
        Desktop.add(pgto);
        //coiso 
        CentralizaForm c = new CentralizaForm(pgto);
        c.centralizaForm();
        pgto.toFront();
        //fim coiso 
    }//GEN-LAST:event_menCadFormaPgtoActionPerformed

    private void menLancamentoFinanceiroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menLancamentoFinanceiroActionPerformed
        // TODO add your handling code here:
        TelaLancamentoFinanceiro fin = new TelaLancamentoFinanceiro();
        fin.setVisible(true);
        Desktop.add(fin);
        CentralizaForm c = new CentralizaForm(fin);
        c.centralizaForm();
        fin.toFront();

    }//GEN-LAST:event_menLancamentoFinanceiroActionPerformed

    private void jMenu4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu4ActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_jMenu4ActionPerformed

    private void menClassificacaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menClassificacaoActionPerformed
        // TODO add your handling code here:
        TelaClassificacaoFinanceira classificacao = new TelaClassificacaoFinanceira();
        classificacao.setVisible(true);
        Desktop.add(classificacao);
        CentralizaForm c = new CentralizaForm(classificacao);
        c.centralizaForm();
        classificacao.toFront();
    }//GEN-LAST:event_menClassificacaoActionPerformed

    private void menRelFinanceiroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menRelFinanceiroActionPerformed
        // TODO add your handling code here:
        TelaRelFinanceiroDespesaReceita rel = new TelaRelFinanceiroDespesaReceita();
        rel.setVisible(true);
        Desktop.add(rel);
        CentralizaForm c = new CentralizaForm(rel);
        c.centralizaForm();
        rel.toFront();
    }//GEN-LAST:event_menRelFinanceiroActionPerformed

    private void menMoviCartaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menMoviCartaoActionPerformed
        // TODO add your handling code here:
        TelaMovimentacaoCartoes cartao = new TelaMovimentacaoCartoes();
        cartao.setVisible(true);
        Desktop.add(cartao);
        CentralizaForm c = new CentralizaForm(cartao);
        c.centralizaForm();

        cartao.toFront();
        cartao.btnIncluir.requestFocus();

    }//GEN-LAST:event_menMoviCartaoActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
        TelaCadastroCartoes cadcartao = new TelaCadastroCartoes();
        cadcartao.setVisible(true);
        Desktop.add(cadcartao);
        CentralizaForm c = new CentralizaForm(cadcartao);
        c.centralizaForm();
        cadcartao.toFront();


    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void menFaturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menFaturaActionPerformed
        // TODO add your handling code here:
        TelaFechaFatura f = new TelaFechaFatura();
        f.setVisible(true);
        Desktop.add(f);
        CentralizaForm c = new CentralizaForm(f);
        c.centralizaForm();
        f.toFront();

    }//GEN-LAST:event_menFaturaActionPerformed

    private void menRelMovimentoCartoesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menRelMovimentoCartoesActionPerformed
        // TODO add your handling code here:
        TelaRelFinanceiroMovCartoes relCartoesAgrupado = new TelaRelFinanceiroMovCartoes();
        relCartoesAgrupado.setVisible(true);
        Desktop.add(relCartoesAgrupado);
        CentralizaForm c = new CentralizaForm(relCartoesAgrupado);
        c.centralizaForm();
        relCartoesAgrupado.toFront();
    }//GEN-LAST:event_menRelMovimentoCartoesActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:
        TelaCadConta conta = new TelaCadConta();
        conta.setVisible(true);
        Desktop.add(conta);
        CentralizaForm c = new CentralizaForm(conta);
        c.centralizaForm();
        conta.toFront();
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        // TODO add your handling code here:
       CarregaSaldoContas c = new CarregaSaldoContas(tblSaldo);
       c.exibesaldo();
    }//GEN-LAST:event_jLabel2MouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TelaPrincipal.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaPrincipal.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaPrincipal.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaPrincipal.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaPrincipal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JDesktopPane Desktop;
    private javax.swing.JMenuBar Menu;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JList<String> jList1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    public static javax.swing.JLabel lblAviso;
    public static javax.swing.JLabel lblClassificacaoFatura;
    public static javax.swing.JLabel lblClassificacaofinanceira;
    public static javax.swing.JLabel lblCodUsoPrincipal;
    public static javax.swing.JLabel lblData;
    public static javax.swing.JLabel lblDispositivo;
    public static javax.swing.JLabel lblFantasiaEmpresaprin;
    public static javax.swing.JLabel lblFoneEmpresa;
    public static javax.swing.JLabel lblFormaPgtoFatura;
    public static javax.swing.JLabel lblInsereFinAuto;
    public static javax.swing.JLabel lblSituacaoosfinaliza;
    public static javax.swing.JLabel lblUsuario;
    public static javax.swing.JLabel lblcodEmpresa;
    public static javax.swing.JLabel lblenviaemailos;
    private javax.swing.JMenu menAjuda;
    private javax.swing.JMenuItem menAnydesk;
    public static javax.swing.JMenuItem menBackup;
    private javax.swing.JMenu menCad;
    private javax.swing.JMenuItem menCadFormaPgto;
    private javax.swing.JMenuItem menCadVendedorTecnico;
    private javax.swing.JMenuItem menCalc;
    private javax.swing.JMenuItem menClassificacao;
    public static javax.swing.JMenuItem menConfig;
    public static javax.swing.JMenuItem menEmpresa;
    private javax.swing.JMenuItem menFatura;
    private javax.swing.JMenuItem menGrupos;
    private javax.swing.JMenuItem menLancamentoFinanceiro;
    private javax.swing.JMenuItem menMoviCartao;
    public static javax.swing.JMenuItem menOrc2;
    public static javax.swing.JMenuItem menOs;
    private javax.swing.JMenuItem menPessoas;
    private javax.swing.JMenuItem menProduto;
    public static javax.swing.JMenu menProgramacao;
    private javax.swing.JMenu menRel;
    public static javax.swing.JMenuItem menRelClientes;
    private javax.swing.JMenuItem menRelFinanceiro;
    private javax.swing.JMenuItem menRelMovimentoCartoes;
    private javax.swing.JMenuItem menRelProd;
    private javax.swing.JMenu menSair;
    private javax.swing.JMenuItem menSobre;
    public static javax.swing.JMenuItem menStartImpantacao;
    private javax.swing.JMenuItem menStatusOs;
    private javax.swing.JMenuItem menTrocaUsuario;
    public static javax.swing.JMenuItem menUsuarios;
    public static javax.swing.JMenuItem menVendas;
    public static javax.swing.JTable tblSaldo;
    // End of variables declaration//GEN-END:variables
}
