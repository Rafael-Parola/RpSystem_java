/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package br.com.rpsystem.telas;

import java.sql.*;
import br.com.rpsystem.dal.ModuloConexao;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URL;
import javax.swing.JOptionPane;
import org.mindrot.jbcrypt.BCrypt;
import static org.mozilla.javascript.Context.exit;

/**
 *
 * @author Rafael Veiga
 */
public class TelaLogin extends javax.swing.JFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public void Logar() {
        String caminho = System.getProperty("user.dir");
        File permissao = new File(caminho + "\\exitmenu.txt");
        String usuario = txtUsuario.getText();
        if (permissao.exists() && usuario.equals("Prog")) {

            TelaPrincipal prin = new TelaPrincipal();
            prin.setVisible(true);
            TelaPrincipal.menUsuarios.setEnabled(true);
            TelaPrincipal.lblCodUsoPrincipal.setText("1");
            TelaPrincipal.lblCodUsoPrincipal.setVisible(true);
            TelaPrincipal.lblUsuario.setText("Programador");
            TelaPrincipal.menUsuarios.setEnabled(true);
            TelaPrincipal.menConfig.setVisible(true);
            TelaPrincipal.menEmpresa.setEnabled(true);
            TelaPrincipal.menVendas.setVisible(true);
            TelaPrincipal.menStartImpantacao.setVisible(true);
            TelaPrincipal.menBackup.setVisible(true);
            TelaPrincipal.menProgramacao.setVisible(true);

            this.dispose();
        } else {

            String sql = "select * from usuarios where login = ? and empresa_codempresa = " + lblCodempresa.getText();
            try {
                pst = conexao.prepareStatement(sql);

                pst.setString(1, usuario);
                rs = pst.executeQuery();

                //Checa se o usuario existe 
                if (rs.next()) {
                    //Captura hash armazenado no banco 
                    String hashSenhaArmazenadaNoBanco = rs.getString("senha"); // Coluna onde o hash da senha é armazenado
                    //captura o valor digitado no txt 
                    String senhaDoUsuarioDigitada = new String(txtSenha.getPassword());
                    //Compara o hash do banco com o hash que foi capturado do txt
                    if (BCrypt.checkpw(senhaDoUsuarioDigitada, hashSenhaArmazenadaNoBanco)) {
                        String perfil = rs.getString(6);
                        if (perfil.equals("Admin")) {
                            permissao.delete();
                            TelaPrincipal principal = new TelaPrincipal();
                            principal.setVisible(true);
                            TelaPrincipal.menUsuarios.setEnabled(true);
                            TelaPrincipal.menEmpresa.setEnabled(true);
                            TelaPrincipal.lblCodUsoPrincipal.setText(rs.getString(1));
                            TelaPrincipal.lblUsuario.setText(rs.getString(2));
                            TelaPrincipal.menConfig.setVisible(true);
                            this.dispose();
                            //conexao.close();
                        }
                        if (perfil.equals("User")) {
                            permissao.delete();
                            TelaPrincipal principal = new TelaPrincipal();
                            principal.setVisible(true);
                            TelaPrincipal.menUsuarios.setEnabled(false);
                            TelaPrincipal.lblCodUsoPrincipal.setText(rs.getString(1));
                            TelaPrincipal.lblUsuario.setText(rs.getString(2));
                            this.dispose();
                            conexao.close();
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Senha incorreta! Verifique os dados.");
                        txtSenha.requestFocus();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Usuario não encontrado! Verifique os dados.");
                    txtUsuario.requestFocus();
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    public void empresa() {
        String empresa = "select * from empresa ";
        conexao = ModuloConexao.conector();
        try {
            pst = conexao.prepareStatement(empresa);
            rs = pst.executeQuery();
            if (rs.next()) {
                do {
                    cboEmpresa.addItem(rs.getString(1).concat(" - ").concat(rs.getString(2)));
                } while (rs.next());

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    public void v_codempresa() {
        String codempresa = cboEmpresa.getSelectedItem().toString();
        int emp = Integer.parseInt(codempresa.substring(0, 2).replace(" ", "").replace("-", ""));

        lblCodempresa.setText(String.valueOf(emp));
        // System.out.println(emp);
    }

    public void atualizaBanco() {

        int versaoTela = Integer.parseInt(lblVersaoTela.getText());
        int versaoAtual = 0;
        String verificaversao = "Select versaobd from configuracoes";
        do {

            //  System.out.println("Versao da tela " + versaoTela);
            try {

                pst = conexao.prepareStatement(verificaversao);
                rs = pst.executeQuery();
                if (rs.next()) {
                    versaoAtual = rs.getInt(1);
                    //System.out.println("Versão do banco " + versaoAtual);
                    if (versaoAtual == versaoTela) {
                        //  System.out.println("FAZ NADA");
                        //  System.out.println("---------------------");

                    }
                    if (versaoAtual > versaoTela) {
                        JOptionPane.showMessageDialog(null, "Atualize a Versão antes de continuar");
                        dispose();

                    } else {

                        switch (versaoAtual) {
                            case 0:
                                //System.out.println("caso Versão 0");
                                String v = "update configuracoes set versaobd = '1' where empresa_codempresa = " + lblCodempresa.getText();
                                try {
                                    pst = conexao.prepareStatement(v);
                                    pst.execute();
                                    //System.out.println("Alterou para versão 1");
                                    //System.out.println("---------------------");
                                } catch (Exception e) {
                                    JOptionPane.showMessageDialog(null, e);
                                }
                                break;
                            case 1:
                                //System.out.println("Caso a Versão 1");
                                String v1 = "ALTER TABLE `produtos` \n"
                                        + "ADD COLUMN `margemlucro` DOUBLE NULL DEFAULT 0 AFTER `ultimaalteracao` ";
                                try {
                                    pst = conexao.prepareStatement(v1);
                                    pst.execute();
                                    String vatualiza = "update configuracoes set versaobd = 2 ";
                                    try {
                                        pst = conexao.prepareStatement(vatualiza);
                                        pst.execute();
                                    } catch (Exception e) {
                                        JOptionPane.showMessageDialog(null, e);
                                    }
                                    //System.out.println("Alterou para versão 2");
                                    //System.out.println("---------------------");
                                } catch (Exception e) {
                                    JOptionPane.showMessageDialog(null, e);
                                }
                                break;

                            case 2:

                                String v2 = "ALTER TABLE `itensorcamento` \n"
                                        + "ADD COLUMN `vlrdesconto` DOUBLE NULL DEFAULT 0 AFTER `vltotal`,\n"
                                        + "ADD COLUMN `vlrfinal` DOUBLE NULL DEFAULT 0 AFTER `vlrdesconto`;";

                                String v2_1 = "ALTER TABLE `ordemservico` \n"
                                        + "ADD COLUMN `totaldescontos` DOUBLE NULL DEFAULT 0 AFTER `usuarios_id`;";

                                String v2_2 = "update itensorcamento set vlrfinal = vltotal where vlrfinal = 0 ";

                                String vatualiza = "update configuracoes set versaobd = 3 ";
                                try {
                                    pst = conexao.prepareStatement(v2);
                                    pst.execute();

                                    try {
                                        pst = conexao.prepareStatement(v2_1);
                                        pst.execute();
                                        try {
                                            pst = conexao.prepareStatement(v2_2);
                                            pst.execute();
                                            try {
                                                pst = conexao.prepareStatement(vatualiza);
                                                pst.execute();
                                            } catch (Exception e) {
                                                JOptionPane.showMessageDialog(null, e);
                                            }
                                        } catch (Exception e) {
                                            JOptionPane.showMessageDialog(null, e);
                                        }
                                    } catch (Exception e) {
                                        JOptionPane.showMessageDialog(null, e);
                                    }
                                } catch (Exception e) {
                                    JOptionPane.showMessageDialog(null, e);

                                }

                                break;
                            case 3:
                                String v3 = "ALTER TABLE `itensorcamento` \n"
                                        + "CHANGE COLUMN `qtde` `qtde` DOUBLE NULL DEFAULT '0.00' ,\n"
                                        + "CHANGE COLUMN `vlunitario` `vlunitario` DOUBLE NULL DEFAULT '0.00' ,\n"
                                        + "CHANGE COLUMN `vltotal` `vltotal` DOUBLE NULL DEFAULT '0.00' ;";

                                try {
                                    pst = conexao.prepareStatement(v3);
                                    pst.execute();
                                    String vatualiza4 = "update configuracoes set versaobd = 4";
                                    try {
                                        pst = conexao.prepareStatement(vatualiza4);
                                        pst.execute();
                                    } catch (Exception e) {
                                        JOptionPane.showMessageDialog(null, e);

                                    }

                                } catch (Exception e) {
                                    JOptionPane.showMessageDialog(null, e);

                                }

                                break;
                            case 4:
                                String v4 = " ALTER TABLE `ordemservico` \n"
                                        + "ADD COLUMN `totalbruto` DOUBLE NULL AFTER `totaldescontos`;";
                                String v4_1 = "update ordemservico set totalbruto =  valor where totalbruto is null  ";
                                try {
                                    pst = conexao.prepareStatement(v4);
                                    pst.execute();
                                    try {
                                        pst = conexao.prepareStatement(v4_1);
                                        pst.execute();
                                        try {
                                            String vatualiza5 = "update configuracoes set versaobd = 5";

                                            pst = conexao.prepareStatement(vatualiza5);
                                            pst.execute();
                                        } catch (Exception e) {
                                            JOptionPane.showMessageDialog(null, e);

                                        }
                                    } catch (Exception e) {
                                        JOptionPane.showMessageDialog(null, e);

                                    }
                                } catch (Exception e) {
                                    JOptionPane.showMessageDialog(null, e);

                                }
                                break;
                            case 5:
                                String v5 = "ALTER TABLE `clientes` CHANGE COLUMN `endereco` `endereco` VARCHAR(60) "
                                        + "NULL DEFAULT NULL ";
                                try {
                                    pst = conexao.prepareStatement(v5);
                                    pst.execute();
                                    String vatualiza6 = "update configuracoes set versaobd = 6";
                                    try {
                                        pst = conexao.prepareStatement(vatualiza6);
                                        pst.execute();
                                    } catch (Exception e) {
                                        JOptionPane.showMessageDialog(null, e);
                                    }
                                } catch (Exception e) {
                                    JOptionPane.showMessageDialog(null, e);
                                }

                                break;
                            case 6:
                                String v6 = "ALTER TABLE `configuracoes` \n"
                                        + "ADD COLUMN `emailenvio` VARCHAR(45) NULL AFTER `versaobd`,\n"
                                        + "ADD COLUMN `senhaenvio` VARCHAR(45) NULL AFTER `emailenvio`;";
                                String v6a = "update configuracoes set versaobd = 7";
                                try {
                                    pst = conexao.prepareStatement(v6);
                                    pst.execute();

                                    pst = conexao.prepareStatement(v6a);
                                    pst.execute();
                                } catch (Exception e) {
                                    JOptionPane.showMessageDialog(null, e);
                                }
                                break;
                            case 7:
                                String v7 = "ALTER TABLE `empresa` \n"
                                        + "ADD COLUMN `telefone` VARCHAR(15) NULL AFTER `ultimaalteracao`";
                                String v7a = "update configuracoes set versaobd = 8";
                                try {
                                    pst = conexao.prepareStatement(v7);
                                    pst.execute();

                                    pst = conexao.prepareStatement(v7a);
                                    pst.execute();
                                } catch (Exception e) {
                                    JOptionPane.showMessageDialog(null, e);
                                }
                                break;
                            case 8:
                                String v8 = "ALTER TABLE `configuracoes` \n"
                                        + "ADD COLUMN `smtp` VARCHAR(30) NULL AFTER `senhaenvio`,\n"
                                        + "ADD COLUMN `porta` INT NULL AFTER `smtp`;";
                                String v8a = "update configuracoes set versaobd = 9";
                                try {
                                    pst = conexao.prepareStatement(v8);
                                    pst.execute();

                                    pst = conexao.prepareStatement(v8a);
                                    pst.execute();
                                } catch (Exception e) {
                                    JOptionPane.showMessageDialog(null, e);

                                }
                                break;
                            case 9:
                                String v9 = "ALTER TABLE `configuracoes` \n"
                                        + "ADD COLUMN `smtpautenticado` VARCHAR(6) NULL AFTER `porta`;";
                                String v9a = "update configuracoes set versaobd = 10";
                                try {
                                    pst = conexao.prepareStatement(v9);
                                    pst.execute();

                                    pst = conexao.prepareStatement(v9a);
                                    pst.execute();
                                } catch (Exception e) {
                                    JOptionPane.showMessageDialog(null, e);

                                }
                                break;
                            case 10:
                                String v10 = "ALTER TABLE `log` \n"
                                        + "ADD COLUMN `telaAlterada` VARCHAR(60) NULL DEFAULT NULL AFTER `id`;";
                                String v10a = "update configuracoes set versaobd = 11";
                                try {
                                    pst = conexao.prepareStatement(v10);
                                    pst.execute();

                                    pst = conexao.prepareStatement(v10a);
                                    pst.execute();

                                } catch (Exception e) {
                                    JOptionPane.showMessageDialog(null, e);

                                }
                                break;
                            case 11:
                                String v11 = "ALTER TABLE `log` \n"
                                        + "ADD COLUMN `registro` INT NULL AFTER `empresa_codempresa`;";
                                String v11a = "update configuracoes set versaobd = 12";
                                try {
                                    pst = conexao.prepareStatement(v11);
                                    pst.execute();

                                    pst = conexao.prepareStatement(v11a);
                                    pst.execute();

                                } catch (Exception e) {
                                    JOptionPane.showMessageDialog(null, e);

                                }
                                break;
                            case 12:
                                String v12 = "ALTER TABLE `configuracoes` \n"
                                        + "ADD COLUMN `enviaemailos` VARCHAR(3) NULL DEFAULT 'Sim' AFTER `smtpautenticado`;";
                                String v12a = "update configuracoes set versaobd = 13";
                                try {
                                    pst = conexao.prepareStatement(v12);
                                    pst.execute();

                                    pst = conexao.prepareStatement(v12a);
                                    pst.execute();

                                } catch (Exception e) {
                                    JOptionPane.showMessageDialog(null, e);

                                }
                                break;
                            case 13:
                                String v13 = "CREATE TABLE IF NOT EXISTS `formaspagamento` (\n"
                                        + "  `codigo` INT NOT NULL AUTO_INCREMENT,\n"
                                        + "  `descricao` VARCHAR(45) NOT NULL,\n"
                                        + "  `status` TINYINT NOT NULL DEFAULT 0,\n"
                                        + "  `tipo` VARCHAR(45) NULL,\n"
                                        + "  `vencimentoprimeiraparcela` INT NULL,\n"
                                        + "  `vencimentodemaisparcelas` INT NULL,\n"
                                        + "  `nummaximoparcelas` INT NULL,\n"
                                        + "  `valorminimoparcelas` DOUBLE NULL,\n"
                                        + "  `taxaprimeiraparcela` DOUBLE NULL,\n"
                                        + "  `taxademaisparcelas` DOUBLE NULL,\n"
                                        + "  `fornecedor` INT NULL,\n"
                                        + "  `banco` INT NULL,\n"
                                        + "  `contacorrente` INT NULL,\n"
                                        + "  `usuarios_id` INT UNSIGNED NOT NULL,\n"
                                        + "  `empresa_codempresa` INT NOT NULL,\n"
                                        + "  PRIMARY KEY (`codigo`),\n"
                                        + "  INDEX `fk_formaspagamento_usuarios_idx` (`usuarios_id` ASC) VISIBLE,\n"
                                        + "  INDEX `fk_formaspagamento_empresa1_idx` (`empresa_codempresa` ASC) VISIBLE,\n"
                                        + "  CONSTRAINT `fk_formaspagamento_usuarios`\n"
                                        + "    FOREIGN KEY (`usuarios_id`)\n"
                                        + "    REFERENCES `usuarios` (`id`)\n"
                                        + "    ON DELETE NO ACTION\n"
                                        + "    ON UPDATE NO ACTION,\n"
                                        + "  CONSTRAINT `fk_formaspagamento_empresa1`\n"
                                        + "    FOREIGN KEY (`empresa_codempresa`)\n"
                                        + "    REFERENCES `empresa` (`codempresa`)\n"
                                        + "    ON DELETE NO ACTION\n"
                                        + "    ON UPDATE NO ACTION)\n"
                                        + "ENGINE = InnoDB  DEFAULT CHARSET=utf8mb4";

                                String v13_1 = "ALTER TABLE `configuracoes` \n"
                                        + "ADD COLUMN `usuarios_id` INT UNSIGNED NULL DEFAULT 1 AFTER `enviaemailos`";

                                String v13a = "update configuracoes set versaobd = 14";
                                try {
                                    pst = conexao.prepareStatement(v13);
                                    pst.execute();
                                    pst = conexao.prepareStatement(v13_1);
                                    pst.execute();

                                    pst = conexao.prepareStatement(v13a);
                                    pst.execute();

                                } catch (Exception e) {
                                    JOptionPane.showMessageDialog(null, e);

                                }
                                break;
                            case 14:
                                String v14 = "ALTER TABLE `configuracoes` \n"
                                        + "ADD CONSTRAINT `fk_configuracoes_usuarios`\n"
                                        + "  FOREIGN KEY (`usuarios_id`)\n"
                                        + "  REFERENCES `usuarios` (`id`)";
                                String v14a = "update configuracoes set versaobd = 15";
                                try {
                                    pst = conexao.prepareStatement(v14);
                                    pst.execute();
                                    pst = conexao.prepareStatement(v14a);
                                    pst.execute();

                                } catch (Exception e) {
                                    JOptionPane.showMessageDialog(null, e);
                                    System.out.println(e);
                                }

                                break;
                            case 15:
                                String v15 = "ALTER TABLE `clientes` \n"
                                        + "ADD COLUMN `cliente` TINYINT NULL AFTER `ultimaalteracao`,\n"
                                        + "ADD COLUMN `fornecedor` TINYINT NULL AFTER `cliente`,\n"
                                        + "ADD COLUMN `funcionario` TINYINT NULL AFTER `fornecedor`, RENAME TO  `pessoas` ;";
                                String v15a = "update configuracoes set versaobd = 16";
                                try {
                                    pst = conexao.prepareStatement(v15);
                                    pst.execute();
                                    pst = conexao.prepareStatement(v15a);
                                    pst.execute();

                                } catch (Exception e) {
                                    JOptionPane.showMessageDialog(null, e);

                                }
                                break;
                            case 16:
                                String v16 = "update pessoas set cliente = 1, fornecedor = 0,funcionario = 0";
                                String v16a = "update configuracoes set versaobd = 17";
                                try {
                                    pst = conexao.prepareStatement(v16);
                                    pst.execute();
                                    pst = conexao.prepareStatement(v16a);
                                    pst.execute();

                                } catch (Exception e) {
                                    JOptionPane.showMessageDialog(null, e);

                                }

                                break;
                            case 17:
                                String v17 = "ALTER TABLE `pessoas` \n"
                                        + "CHANGE COLUMN `idclientes` `id` INT NOT NULL AUTO_INCREMENT ,\n"
                                        + "CHANGE COLUMN `nomeCliente` `nome` VARCHAR(100) NOT NULL ;";
                                String v17a = "update configuracoes set versaobd = 18";
                                try {
                                    pst = conexao.prepareStatement(v17);
                                    pst.execute();
                                    pst = conexao.prepareStatement(v17a);
                                    pst.execute();

                                } catch (Exception e) {
                                    JOptionPane.showMessageDialog(null, e);

                                }
                                break;
                            case 18:
                                String v18 = "ALTER TABLE `pessoas` \n"
                                        + "ADD COLUMN `observacoes` VARCHAR(3000) NULL AFTER `funcionario`;";
                                String v18a = "update configuracoes set versaobd = 19";
                                try {
                                    pst = conexao.prepareStatement(v18);
                                    pst.execute();
                                    pst = conexao.prepareStatement(v18a);
                                    pst.execute();

                                } catch (Exception e) {
                                    JOptionPane.showMessageDialog(null, e);

                                }
                                break;

                            case 19:
                                String v19 = "update pessoas set cliente = 1,fornecedor=0,funcionario=0 where cliente is null ";
                                String v19_1 = "update pessoas set estado='SP' where estado is null ";
                                String v19a = "update configuracoes set versaobd = 20";
                                try {
                                    pst = conexao.prepareStatement(v19);
                                    pst.execute();
                                    pst = conexao.prepareStatement(v19_1);
                                    pst.execute();
                                    pst = conexao.prepareStatement(v19a);
                                    pst.execute();

                                } catch (Exception e) {
                                    JOptionPane.showMessageDialog(null, e);

                                }
                                break;
                            case 20:
                                String v20 = "ALTER TABLE `ordemservico` \n"
                                        + "ADD COLUMN `formaspagamento_codigo` INT  NULL AFTER `totalbruto`;";

                                String v20_1 = " ALTER TABLE ordemservico"
                                        + " ADD CONSTRAINT `fk_ordemservico_formaspagamento1`\n"
                                        + " FOREIGN KEY (`formaspagamento_codigo`)\n"
                                        + " REFERENCES `formaspagamento` (`codigo`)";

                                String v20_2 = "INSERT INTO `formaspagamento`\n"
                                        + "(`codigo`,`descricao`,`status`,`tipo`,`vencimentoprimeiraparcela`,`vencimentodemaisparcelas`,`nummaximoparcelas`,`valorminimoparcelas`,\n"
                                        + "`taxaprimeiraparcela`,`taxademaisparcelas`,`fornecedor`,`banco`,`usuarios_id`,`empresa_codempresa`)\n"
                                        + "VALUES\n"
                                        + "(1,'Dinheiro',0,'Dinheiro',0,0,0,0,0,0,1,0,1,1)";
                                String v20a = "update configuracoes set versaobd = 21";

                                try {
                                    pst = conexao.prepareStatement(v20);
                                    pst.execute();
                                    pst = conexao.prepareStatement(v20_1);
                                    pst.execute();
                                    pst = conexao.prepareStatement(v20_2);
                                    pst.execute();
                                    pst = conexao.prepareStatement(v20a);
                                    pst.execute();

                                } catch (Exception e) {
                                    JOptionPane.showMessageDialog(null, e);

                                }
                                break;

                            case 21:

                                String v21 = "CREATE TABLE `classificacaofinanceira` (\n"
                                        + "  `cod` int NOT NULL AUTO_INCREMENT,\n"
                                        + "  `descricao` varchar(45) NOT NULL,\n"
                                        + "  `excluido` tinyint NOT NULL DEFAULT '0',\n"
                                        + "  `usuarios_id` int unsigned NOT NULL,\n"
                                        + "  `empresa_codempresa` int NOT NULL,\n"
                                        + "  `utlimaalteracao` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n"
                                        + "  PRIMARY KEY (`cod`),\n"
                                        + "  KEY `fk_classfin_usuarios1_idx` (`usuarios_id`),\n"
                                        + "  KEY `fk_classfin_empresa1_idx` (`empresa_codempresa`),\n"
                                        + "  CONSTRAINT `fk_classfin_empresa1` FOREIGN KEY (`empresa_codempresa`) REFERENCES `empresa` (`codempresa`),\n"
                                        + "  CONSTRAINT `fk_classfin_usuarios1` FOREIGN KEY (`usuarios_id`) REFERENCES `usuarios` (`id`)\n"
                                        + ")";
                                String v21a = "update configuracoes set versaobd = 22";

                                try {
                                    pst = conexao.prepareStatement(v21);
                                    pst.execute();

                                    pst = conexao.prepareStatement(v21a);
                                    pst.execute();

                                } catch (Exception e) {
                                    JOptionPane.showMessageDialog(null, e);

                                }
                                break;
                            case 22:
                                String v22 = "CREATE TABLE IF NOT EXISTS `lancamentofinanceiro` (\n"
                                        + "  `cod` INT NOT NULL AUTO_INCREMENT,\n"
                                        + "  `descricao` varchar(100) NOT NULL,\n"
                                        + "  `dtemissao` DATE NOT NULL,\n"
                                        + "  `dtvencimento` DATE NOT NULL,\n"
                                        + "  `valor` DOUBLE NOT NULL DEFAULT 0.00,\n"
                                        + "  `ultimaalteracao` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n"
                                        + "  `formaspagamento_codigo` INT NOT NULL,\n"
                                        + "  `classificacaofinanceira_cod` INT NOT NULL,\n"
                                        + "  `pessoas_id` INT NOT NULL,\n"
                                        + "  `usuarios_id` INT UNSIGNED NOT NULL,\n"
                                        + "  `empresa_codempresa` INT NOT NULL,\n"
                                        + "  PRIMARY KEY (`cod`),\n"
                                        + "  INDEX `fk_lancamentofinanceiro_formaspagamento_idx` (`formaspagamento_codigo` ASC) VISIBLE,\n"
                                        + "  INDEX `fk_lancamentofinanceiro_classificacaofinanceira1_idx` (`classificacaofinanceira_cod` ASC) VISIBLE,\n"
                                        + "  INDEX `fk_lancamentofinanceiro_pessoas1_idx` (`pessoas_id` ASC) VISIBLE,\n"
                                        + "  INDEX `fk_lancamentofinanceiro_usuarios1_idx` (`usuarios_id` ASC) VISIBLE,\n"
                                        + "  INDEX `fk_lancamentofinanceiro_empresa1_idx` (`empresa_codempresa` ASC) VISIBLE,\n"
                                        + "  CONSTRAINT `fk_lancamentofinanceiro_formaspagamento`\n"
                                        + "    FOREIGN KEY (`formaspagamento_codigo`)\n"
                                        + "    REFERENCES `formaspagamento` (`codigo`)\n"
                                        + "    ON DELETE NO ACTION\n"
                                        + "    ON UPDATE NO ACTION,\n"
                                        + "  CONSTRAINT `fk_lancamentofinanceiro_classificacaofinanceira1`\n"
                                        + "    FOREIGN KEY (`classificacaofinanceira_cod`)\n"
                                        + "    REFERENCES `classificacaofinanceira` (`cod`)\n"
                                        + "    ON DELETE NO ACTION\n"
                                        + "    ON UPDATE NO ACTION,\n"
                                        + "  CONSTRAINT `fk_lancamentofinanceiro_pessoas1`\n"
                                        + "    FOREIGN KEY (`pessoas_id`)\n"
                                        + "    REFERENCES `pessoas` (`id`)\n"
                                        + "    ON DELETE NO ACTION\n"
                                        + "    ON UPDATE NO ACTION,\n"
                                        + "  CONSTRAINT `fk_lancamentofinanceiro_usuarios1`\n"
                                        + "    FOREIGN KEY (`usuarios_id`)\n"
                                        + "    REFERENCES `usuarios` (`id`)\n"
                                        + "    ON DELETE NO ACTION\n"
                                        + "    ON UPDATE NO ACTION,\n"
                                        + "  CONSTRAINT `fk_lancamentofinanceiro_empresa1`\n"
                                        + "    FOREIGN KEY (`empresa_codempresa`)\n"
                                        + "    REFERENCES `empresa` (`codempresa`)\n"
                                        + "    ON DELETE NO ACTION\n"
                                        + "    ON UPDATE NO ACTION)";
                                String v22a = "update configuracoes set versaobd = 23";

                                try {
                                    pst = conexao.prepareStatement(v22);
                                    pst.execute();

                                    pst = conexao.prepareStatement(v22a);
                                    pst.execute();

                                } catch (Exception e) {
                                    JOptionPane.showMessageDialog(null, e);

                                }
                                break;
                            case 23:
                                String v23 = "ALTER TABLE `lancamentofinanceiro` \n"
                                        + "ADD COLUMN `tipo` VARCHAR(10) NOT NULL AFTER `cod`;";
                                String v23a = "update configuracoes set versaobd = 24";
                                try {
                                    pst = conexao.prepareStatement(v23);
                                    pst.execute();

                                    pst = conexao.prepareStatement(v23a);
                                    pst.execute();

                                } catch (Exception e) {
                                    JOptionPane.showMessageDialog(null, e);

                                }
                                break;

                            case 24:
                                String v24 = "ALTER TABLE `lancamentofinanceiro` \n"
                                        + "ADD COLUMN `excluido` TINYINT NOT NULL AFTER `empresa_codempresa`;";

                                String v24a = "update configuracoes set versaobd = 25";
                                try {
                                    pst = conexao.prepareStatement(v24);
                                    pst.execute();

                                    pst = conexao.prepareStatement(v24a);
                                    pst.execute();

                                } catch (Exception e) {
                                    JOptionPane.showMessageDialog(null, e);

                                }
                                break;
                            case 25:
                                String v25 = "ALTER TABLE `lancamentofinanceiro` \n"
                                        + "ADD COLUMN `observacao` VARCHAR(3000) NULL AFTER `excluido`;";
                                String v25a = "update configuracoes set versaobd = 26";
                                try {
                                    pst = conexao.prepareStatement(v25);
                                    pst.execute();

                                    pst = conexao.prepareStatement(v25a);
                                    pst.execute();

                                } catch (Exception e) {
                                    JOptionPane.showMessageDialog(null, e);

                                }
                                break;
                            case 26:
                                String v26 = "ALTER TABLE `configuracoes` \n"
                                        + "ADD COLUMN `inserefinanceiro_os_auto` TINYINT NULL AFTER `usuarios_id`,\n"
                                        + "ADD COLUMN `classificacao_padrao_os` INT NULL AFTER `inserefinanceiro_os_auto`";

                                String v26_1 = " ALTER TABLE configuracoes\n"
                                        + " ADD CONSTRAINT fk_classificacao_padrao\n"
                                        + "FOREIGN KEY (`classificacao_padrao_os`)\n"
                                        + " REFERENCES `classificacaofinanceira` (`cod`);";

                                String v26a = "update configuracoes set versaobd = 27";
                                try {
                                    pst = conexao.prepareStatement(v26);
                                    pst.execute();
                                    pst = conexao.prepareStatement(v26_1);
                                    pst.execute();
                                    pst = conexao.prepareStatement(v26a);
                                    pst.execute();

                                } catch (Exception e) {
                                    JOptionPane.showMessageDialog(null, e);

                                }
                                break;
                            case 27:
                                String v27 = "ALTER TABLE `ordemservico` \n"
                                        + "ADD COLUMN `concluido` TINYINT NULL AFTER `formaspagamento_codigo`;";

                                String v27_1 = "ALTER TABLE `lancamentofinanceiro` \n"
                                        + "ADD COLUMN `dtpagamento` DATE NULL AFTER `observacao`;";

                                String v27a = "update configuracoes set versaobd = 28";
                                try {
                                    pst = conexao.prepareStatement(v27);
                                    pst.execute();
                                    pst = conexao.prepareStatement(v27_1);
                                    pst.execute();
                                    pst = conexao.prepareStatement(v27a);
                                    pst.execute();

                                } catch (Exception e) {
                                    JOptionPane.showMessageDialog(null, e);

                                }
                                break;
                            case 28:
                                String v28 = "update lancamentofinanceiro set dtpagamento = dtvencimento where dtpagamento is null;";
                                String v28a = "update configuracoes set versaobd = 29";
                                try {
                                    pst = conexao.prepareStatement(v28);
                                    pst.execute();

                                    pst = conexao.prepareStatement(v28a);
                                    pst.execute();

                                } catch (Exception e) {
                                    JOptionPane.showMessageDialog(null, e);

                                }
                                break;
                            case 29:
                                String v29 = "ALTER TABLE `configuracoes` \n"
                                        + "ADD COLUMN `situacaoos` VARCHAR(45) NULL AFTER `classificacao_padrao_os`;";
                                String v29a = "update configuracoes set versaobd = 30";
                                try {
                                    pst = conexao.prepareStatement(v29);
                                    pst.execute();

                                    pst = conexao.prepareStatement(v29a);
                                    pst.execute();

                                } catch (Exception e) {
                                    JOptionPane.showMessageDialog(null, e);
                                }
                                break;
                            case 30:
                                String v30 = "ALTER TABLE `lancamentofinanceiro` \n"
                                        + "ADD COLUMN `numos` INT NULL AFTER `dtpagamento`;";
                                String v30a = "update configuracoes set versaobd = 31";
                                try {
                                    pst = conexao.prepareStatement(v30);
                                    pst.execute();

                                    pst = conexao.prepareStatement(v30a);
                                    pst.execute();

                                } catch (Exception e) {
                                    JOptionPane.showMessageDialog(null, e);
                                }
                                break;
                            case 31:
                                String v31 = "CREATE TABLE `cadcartoes` (\n"
                                        + "  `codigo` INT NOT NULL AUTO_INCREMENT,\n"
                                        + "  `nomecartao` VARCHAR(45) NOT NULL,\n"
                                        + "  `fornecedor` VARCHAR(45) NOT NULL,\n"
                                        + "  `diafechamentofatura` INT NOT NULL DEFAULT 0,\n"
                                        + "  `diavencimentofatura` INT NOT NULL DEFAULT 0,\n"
                                        + "  `usuario` INT UNSIGNED NOT NULL,\n"
                                        + "  `cod_empresa` INT NOT NULL,\n"
                                        + "  `ultimaalteracao` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n"
                                        + "  PRIMARY KEY (`codigo`),\n"
                                        + "  INDEX `usuario_idx` (`usuario` ASC) VISIBLE,\n"
                                        + "  INDEX `empresa_idx` (`cod_empresa` ASC) VISIBLE,\n"
                                        + "  CONSTRAINT `empresa`\n"
                                        + "    FOREIGN KEY (`cod_empresa`)\n"
                                        + "    REFERENCES `empresa` (`codempresa`)\n"
                                        + "    ON DELETE NO ACTION\n"
                                        + "    ON UPDATE NO ACTION,\n"
                                        + "  CONSTRAINT `usuario`\n"
                                        + "    FOREIGN KEY (`usuario`)\n"
                                        + "    REFERENCES `usuarios` (`id`)\n"
                                        + "    ON DELETE NO ACTION\n"
                                        + "    ON UPDATE NO ACTION\n"
                                        + ");";
                                String v31a = "update configuracoes set versaobd = 32";
                                try {
                                    pst = conexao.prepareStatement(v31);
                                    pst.execute();

                                    pst = conexao.prepareStatement(v31a);
                                    pst.execute();

                                } catch (Exception e) {
                                    System.out.println(e);
                                    JOptionPane.showMessageDialog(null, e);
                                }
                                break;
                            case 32:
                                String v32 = "ALTER TABLE `cadcartoes` \n"
                                        + "ADD COLUMN `excluido` TINYINT NULL AFTER `ultimaalteracao`;";
                                String v32_1 = "update cadcartoes set excluido = 0 where excluido is null;";
                                String v32a = "update configuracoes set versaobd = 33";
                                try {
                                    pst = conexao.prepareStatement(v32);
                                    pst.execute();
                                    pst = conexao.prepareStatement(v32_1);
                                    pst.execute();
                                    pst = conexao.prepareStatement(v32a);
                                    pst.execute();

                                } catch (Exception e) {
                                    System.out.println(e);
                                    JOptionPane.showMessageDialog(null, e);
                                }
                                break;
                            case 33:
                                String v33 = "CREATE TABLE `movimentocartoes` (\n"
                                        + "  `codigo` INT NOT NULL AUTO_INCREMENT,\n"
                                        + "  `dataemissao` DATE NOT NULL,\n"
                                        + "  `datavencimento` DATE NOT NULL,\n"
                                        + "  `classificacao` INT NOT NULL,\n"
                                        + "  `cartao` INT NOT NULL,\n"
                                        + "  `nparcelas` INT NOT NULL,\n"
                                        + "  `valor` DOUBLE NOT NULL,\n"
                                        + "  `obs` VARCHAR(3000) NOT NULL,\n"
                                        + "  `empresa_codempresa` INT NOT NULL,\n"
                                        + "  `usuario_idusuario` INT UNSIGNED NOT NULL,\n"
                                        + "  PRIMARY KEY (`codigo`),\n"
                                        + "  INDEX `classificacao_idx` (`classificacao` ASC) VISIBLE,\n"
                                        + "  INDEX `cartao_idx` (`cartao` ASC) VISIBLE,\n"
                                        + "  INDEX `empresa_codempresa_idx` (`empresa_codempresa` ASC) VISIBLE,\n"
                                        + "  INDEX `usuario_idusuario_idx` (`usuario_idusuario` ASC) VISIBLE,\n"
                                        + "  CONSTRAINT `classificacao`\n"
                                        + "    FOREIGN KEY (`classificacao`)\n"
                                        + "    REFERENCES `classificacaofinanceira` (`cod`)\n"
                                        + "    ON DELETE NO ACTION\n"
                                        + "    ON UPDATE NO ACTION,\n"
                                        + "  CONSTRAINT `cartao`\n"
                                        + "    FOREIGN KEY (`cartao`)\n"
                                        + "    REFERENCES `cadcartoes` (`codigo`)\n"
                                        + "    ON DELETE NO ACTION\n"
                                        + "    ON UPDATE NO ACTION,\n"
                                        + "  CONSTRAINT `empresa_codempresa`\n"
                                        + "    FOREIGN KEY (`empresa_codempresa`)\n"
                                        + "    REFERENCES `empresa` (`codempresa`)\n"
                                        + "    ON DELETE NO ACTION\n"
                                        + "    ON UPDATE NO ACTION,\n"
                                        + "  CONSTRAINT `usuario_idusuario`\n"
                                        + "    FOREIGN KEY (`usuario_idusuario`)\n"
                                        + "    REFERENCES `usuarios` (`id`)\n"
                                        + "    ON DELETE NO ACTION\n"
                                        + "    ON UPDATE NO ACTION);";
                                String v33a = "update configuracoes set versaobd = 34";
                                try {
                                    pst = conexao.prepareStatement(v33);
                                    pst.execute();

                                    pst = conexao.prepareStatement(v33a);
                                    pst.execute();

                                } catch (Exception e) {
                                    System.out.println(e);
                                    JOptionPane.showMessageDialog(null, e);
                                }
                                break;
                            case 34:
                                String v34 = "ALTER TABLE `movimentocartoes` \n"
                                        + "ADD COLUMN `descricao` VARCHAR(100) NOT NULL AFTER `usuario_idusuario`;";
                                String v34a = "update configuracoes set versaobd = 35";
                                try {
                                    pst = conexao.prepareStatement(v34);
                                    pst.execute();

                                    pst = conexao.prepareStatement(v34a);
                                    pst.execute();

                                } catch (Exception e) {
                                    System.out.println(e);
                                    JOptionPane.showMessageDialog(null, e);
                                }
                                break;
                            case 35:
                                String v35 = "ALTER TABLE `movimentocartoes` \n"
                                        + "ADD COLUMN `excluido` TINYINT NULL DEFAULT 0 AFTER `descricao`,\n"
                                        + "CHANGE COLUMN `obs` `obs` VARCHAR(3100) NOT NULL ;";
                                String v35a = "update configuracoes set versaobd = 36";
                                try {
                                    pst = conexao.prepareStatement(v35);
                                    pst.execute();

                                    pst = conexao.prepareStatement(v35a);
                                    pst.execute();

                                } catch (Exception e) {
                                    System.out.println(e);
                                    JOptionPane.showMessageDialog(null, e);
                                }
                                break;
                            case 36:
                                String v36 = "	ALTER TABLE `configuracoes` \n"
                                        + "	ADD COLUMN `classificacao_padrao_fatura` INT NULL AFTER `situacaoos`,\n"
                                        + "	ADD COLUMN `forma_pgto_fatura` INT NULL AFTER `classificacao_padrao_fatura`;";
                                String v36a = "update configuracoes set versaobd = 37";
                                try {
                                    pst = conexao.prepareStatement(v36);
                                    pst.execute();

                                    pst = conexao.prepareStatement(v36a);
                                    pst.execute();

                                } catch (Exception e) {
                                    System.out.println(e);
                                    JOptionPane.showMessageDialog(null, e);
                                }
                                break;
                            case 37:
                                String v37 = "ALTER TABLE `movimentocartoes` \n"
                                        + "ADD COLUMN `pago` TINYINT NULL DEFAULT 0 AFTER `excluido`;";
                                String v37_1 = "update movimentocartoes set pago = 0 where pago is null";
                                String v37a = "update configuracoes set versaobd = 38";
                                try {
                                    pst = conexao.prepareStatement(v37);
                                    pst.execute();
                                    pst = conexao.prepareStatement(v37_1);
                                    pst.execute();
                                    pst = conexao.prepareStatement(v37a);
                                    pst.execute();

                                } catch (Exception e) {
                                    System.out.println(e);
                                    JOptionPane.showMessageDialog(null, e);
                                }
                                break;
                            case 38:
                                String v38 = "ALTER TABLE `lancamentofinanceiro` \n"
                                        + "ADD COLUMN `codCartao` INT NULL AFTER `numos`,\n"
                                        + "ADD INDEX `fk_vincula_cartao_idx` (`codCartao` ASC) VISIBLE";
                                String v38_1
                                        = "ALTER TABLE `lancamentofinanceiro` \n"
                                        + "ADD CONSTRAINT `fk_vincula_cartao`\n"
                                        + "  FOREIGN KEY (`codCartao`)\n"
                                        + "  REFERENCES `cadcartoes` (`codigo`)\n"
                                        + "  ON DELETE NO ACTION\n"
                                        + "  ON UPDATE NO ACTION;";
                                String v38a = "update configuracoes set versaobd = 39";
                                try {
                                    pst = conexao.prepareStatement(v38);
                                    pst.execute();
                                    pst = conexao.prepareStatement(v38_1);
                                    pst.execute();
                                    pst = conexao.prepareStatement(v38a);
                                    pst.execute();

                                } catch (Exception e) {
                                    System.out.println(e);
                                    JOptionPane.showMessageDialog(null, e);
                                }
                                break;
                            case 39:
                                String v39 = "ALTER TABLE `usuarios` \n"
                                        + "CHANGE COLUMN `senha` `senha` VARCHAR(200) NOT NULL ;";
                                String v39a = "update configuracoes set versaobd = 40";
                                try {
                                    pst = conexao.prepareStatement(v39);
                                    pst.execute();

                                    pst = conexao.prepareStatement(v39a);
                                    pst.execute();

                                } catch (Exception e) {
                                    System.out.println(e);
                                    JOptionPane.showMessageDialog(null, e);
                                }
                                break;
                            case 40:
                                String v40 = "ALTER TABLE `cadcartoes` \n"
                                        + "ADD COLUMN `limite` DOUBLE NULL AFTER `excluido`";
                                String v40a = "update configuracoes set versaobd = 41";
                                try {
                                    pst = conexao.prepareStatement(v40);
                                    pst.execute();

                                    pst = conexao.prepareStatement(v40a);
                                    pst.execute();

                                } catch (Exception e) {
                                    System.out.println(e);
                                    JOptionPane.showMessageDialog(null, e);
                                }
                                break;
                            case 41:
                                String v41 = "ALTER TABLE `configuracoes` \n"
                                        + "ADD COLUMN `pergunta_movimento_cartao_abertura` VARCHAR(3) NULL AFTER `forma_pgto_fatura`;";
                                String v41a = "update configuracoes set versaobd = 42";
                                try {
                                    pst = conexao.prepareStatement(v41);
                                    pst.execute();
                                    pst = conexao.prepareStatement(v41a);
                                    pst.execute();
                                } catch (Exception e) {
                                    System.out.println(e);
                                    JOptionPane.showMessageDialog(null, e);
                                }
                                break;
                            case 42:
                                String v42 = "CREATE TABLE `cadastrocontas` (\n"
                                        + "  `codigo` INT NOT NULL AUTO_INCREMENT,\n"
                                        + "  `nome` VARCHAR(45) NOT NULL,\n"
                                        + "  `tipo` VARCHAR(15) NOT NULL,\n"
                                        + "  `codfornecedor` INT NULL,\n"
                                        + "  `exibetelaincial` TINYINT NULL,\n"
                                        + "  `alteracao` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n"
                                        + "  `codusuario` INT UNSIGNED NOT NULL,\n"
                                        + "  `codempresa` INT NOT NULL,\n"
                                        + "  PRIMARY KEY (`codigo`),\n"
                                        + "  INDEX `codemprsa_idx` (`codempresa` ASC) VISIBLE,\n"
                                        + "  INDEX `usuario_idx` (`codusuario` ASC) VISIBLE,\n"
                                        + "  INDEX `fornecedor_idx` (`codfornecedor` ASC) VISIBLE,\n"
                                        + "  CONSTRAINT `emprsa`\n"
                                        + "    FOREIGN KEY (`codempresa`)\n"
                                        + "    REFERENCES `empresa` (`codempresa`)\n"
                                        + "    ON DELETE NO ACTION\n"
                                        + "    ON UPDATE NO ACTION,\n"
                                        + "  CONSTRAINT `usuario_cadconta`\n"
                                        + "    FOREIGN KEY (`codusuario`)\n"
                                        + "    REFERENCES `usuarios` (`id`)\n"
                                        + "    ON DELETE NO ACTION\n"
                                        + "    ON UPDATE NO ACTION,\n"
                                        + "  CONSTRAINT `fornecedor`\n"
                                        + "    FOREIGN KEY (`codfornecedor`)\n"
                                        + "    REFERENCES `pessoas` (`id`)\n"
                                        + "    ON DELETE NO ACTION\n"
                                        + "    ON UPDATE NO ACTION);";
                                String v42a = "update configuracoes set versaobd = 43";
                                try {
                                    pst = conexao.prepareStatement(v42);
                                    pst.execute();
                                    pst = conexao.prepareStatement(v42a);
                                    pst.execute();
                                } catch (Exception e) {
                                    System.out.println(e);
                                    JOptionPane.showMessageDialog(null, e);
                                }
                                break;
                            case 43:
                                String v43 = "ALTER TABLE `cadastrocontas` \n"
                                        + "ADD COLUMN `status` TINYINT NOT NULL DEFAULT 0 AFTER `codempresa`";
                                String v43a = "update configuracoes set versaobd = 44";
                                try {
                                    pst = conexao.prepareStatement(v43);
                                    pst.execute();
                                    pst = conexao.prepareStatement(v43a);
                                    pst.execute();
                                } catch (Exception e) {
                                    System.out.println(e);
                                    JOptionPane.showMessageDialog(null, e);
                                }
                                break;
                            case 44:
                                String v44 = "ALTER TABLE `lancamentofinanceiro` \n"
                                        + "ADD COLUMN `codConta` INT NULL AFTER `codCartao`";
                                String v44a = "update configuracoes set versaobd = 45";
                                try {
                                    pst = conexao.prepareStatement(v44);
                                    pst.execute();
                                    pst = conexao.prepareStatement(v44a);
                                    pst.execute();
                                } catch (Exception e) {
                                    System.out.println(e);
                                    JOptionPane.showMessageDialog(null, e);
                                }
                                break;
                            case 45:
                                String v45 = "ALTER TABLE `lancamentofinanceiro` \n"
                                        + "ADD CONSTRAINT `codconta`\n"
                                        + "  FOREIGN KEY (`codConta`)\n"
                                        + "  REFERENCES `cadastrocontas` (`codigo`)\n"
                                        + "  ON DELETE NO ACTION\n"
                                        + "  ON UPDATE NO ACTION;";
                                String v45a = "update configuracoes set versaobd = 46";
                                try {
                                    pst = conexao.prepareStatement(v45);
                                    pst.execute();
                                    pst = conexao.prepareStatement(v45a);
                                    pst.execute();
                                } catch (Exception e) {
                                    System.out.println(e);
                                    JOptionPane.showMessageDialog(null, e);
                                }
                                break;
                            case 46:
                                String v46 = "ALTER TABLE `cadastrocontas` \n"
                                        + "ADD COLUMN `saldo` DOUBLE NOT NULL DEFAULT 0 AFTER `status`;";
                                String v46a = "update configuracoes set versaobd = 47";
                                try {
                                    pst = conexao.prepareStatement(v46);
                                    pst.execute();
                                    pst = conexao.prepareStatement(v46a);
                                    pst.execute();
                                } catch (Exception e) {
                                    System.out.println(e);
                                    JOptionPane.showMessageDialog(null, e);
                                }
                                break;
                            case 47:
                                String v47 = "CREATE TABLE `fatura` (\n"
                                        + "  `cod` INT NOT NULL AUTO_INCREMENT,\n"
                                        + "  `cartao` INT NULL,\n"
                                        + "  `valor` DOUBLE NULL,\n"
                                        + "  `vencimento` DATE NULL,\n"
                                        + "  `observacao` VARCHAR(200) NULL,\n"
                                        + "  `status` VARCHAR(1) NULL ,\n"
                                        + "  `codempresa` INT NOT NULL,\n"
                                        + "  `codusuario` INT UNSIGNED NOT NULL,\n"
                                        + "  PRIMARY KEY (`cod`),\n"
                                        + "  INDEX `fk_cod_cartao_idx` (`cartao` ASC) VISIBLE,\n"
                                        + "  INDEX `fk_cod_empresa_idx` (`codempresa` ASC) VISIBLE,\n"
                                        + "  INDEX `fk_cod_usuario_idx` (`codusuario` ASC) VISIBLE,\n"
                                        + "  CONSTRAINT `fk_cod_cartao`\n"
                                        + "    FOREIGN KEY (`cartao`)\n"
                                        + "    REFERENCES `cadcartoes` (`codigo`)\n"
                                        + "    ON DELETE NO ACTION\n"
                                        + "    ON UPDATE NO ACTION,\n"
                                        + "  CONSTRAINT `fk_cod_empresa`\n"
                                        + "    FOREIGN KEY (`codempresa`)\n"
                                        + "    REFERENCES `empresa` (`codempresa`)\n"
                                        + "    ON DELETE NO ACTION\n"
                                        + "    ON UPDATE NO ACTION,\n"
                                        + "  CONSTRAINT `fk_cod_usuario`\n"
                                        + "    FOREIGN KEY (`codusuario`)\n"
                                        + "    REFERENCES `usuarios` (`id`)\n"
                                        + "    ON DELETE NO ACTION\n"
                                        + "    ON UPDATE NO ACTION);";

                                String v47a = "update configuracoes set versaobd = 48";
                                try {
                                    pst = conexao.prepareStatement(v47);
                                    pst.execute();
                                    pst = conexao.prepareStatement(v47a);
                                    pst.execute();
                                } catch (Exception e) {
                                    System.out.println(e);
                                    JOptionPane.showMessageDialog(null, e);
                                }
                                break;
                            case 48:
                                String v48 = "CREATE TABLE `pagamentos_faturas` (\n"
                                        + "  `codigo` INT NOT NULL AUTO_INCREMENT,\n"
                                        + "  `cod_fatura` INT NOT NULL,\n"
                                        + "  `valorpago` DOUBLE NOT NULL,\n"
                                        + "  `dtpagamento` DATE NOT NULL,\n"
                                        + "  `status_pagamento` TINYINT NOT NULL DEFAULT 0,\n"
                                        + "  `obs` VARCHAR(100) NULL,\n"
                                        + "  `codempresa` INT NOT NULL,\n"
                                        + "  `codusuario` INT UNSIGNED NOT NULL,\n"
                                        + "  PRIMARY KEY (`codigo`),\n"
                                        + "  INDEX `codempresa_idx` (`codempresa` ASC) VISIBLE,\n"
                                        + "  INDEX `codusuario_idx` (`codusuario` ASC) VISIBLE,\n"
                                        + "  INDEX `codfatura_idx` (`cod_fatura` ASC) VISIBLE,\n"
                                        + "  CONSTRAINT `codempresa`\n"
                                        + "    FOREIGN KEY (`codempresa`)\n"
                                        + "    REFERENCES `empresa` (`codempresa`)\n"
                                        + "    ON DELETE NO ACTION\n"
                                        + "    ON UPDATE NO ACTION,\n"
                                        + "  CONSTRAINT `codusuario`\n"
                                        + "    FOREIGN KEY (`codusuario`)\n"
                                        + "    REFERENCES `usuarios` (`id`)\n"
                                        + "    ON DELETE NO ACTION\n"
                                        + "    ON UPDATE NO ACTION,\n"
                                        + "  CONSTRAINT `codfatura`\n"
                                        + "    FOREIGN KEY (`cod_fatura`)\n"
                                        + "    REFERENCES `fatura` (`cod`)\n"
                                        + "    ON DELETE NO ACTION\n"
                                        + "    ON UPDATE NO ACTION)";
                                String v48a = "update configuracoes set versaobd = 49";
                                try {
                                    pst = conexao.prepareStatement(v48);
                                    pst.execute();
                                    pst = conexao.prepareStatement(v48a);
                                    pst.execute();
                                } catch (Exception e) {
                                    System.out.println(e);
                                    JOptionPane.showMessageDialog(null, e);
                                }
                                break;
                            case 49:
                                String v49 = "ALTER TABLE `pagamentos_faturas` \n"
                                        + "ADD COLUMN `dtvencimento` DATE NOT NULL AFTER `dtpagamento`;";
                                String v49a = "update configuracoes set versaobd = 50";
                                try {
                                    pst = conexao.prepareStatement(v49);
                                    pst.execute();
                                    pst = conexao.prepareStatement(v49a);
                                    pst.execute();
                                } catch (Exception e) {
                                    System.out.println(e);
                                    JOptionPane.showMessageDialog(null, e);
                                }
                                break;
                            case 50:
                                String v50 = "ALTER TABLE `lancamentofinanceiro` \n"
                                        + "DROP FOREIGN KEY `fk_lancamentofinanceiro_pessoas1`;\n";
                                String v50_1 = "ALTER TABLE `lancamentofinanceiro` \n"
                                        + "CHANGE COLUMN `pessoas_id` `pessoas_id` INT NULL ;\n";
                                String v50_2 = "ALTER TABLE `lancamentofinanceiro` \n"
                                        + "ADD CONSTRAINT `fk_lancamentofinanceiro_pessoas1`\n"
                                        + "  FOREIGN KEY (`pessoas_id`)\n"
                                        + "  REFERENCES `pessoas` (`id`);";

                                String v50a = "update configuracoes set versaobd = 51";
                                try {
                                    pst = conexao.prepareStatement(v50);
                                    pst.execute();
                                    pst = conexao.prepareStatement(v50_1);
                                    pst.execute();
                                    pst = conexao.prepareStatement(v50_2);
                                    pst.execute();
                                    pst = conexao.prepareStatement(v50a);
                                    pst.execute();
                                } catch (Exception e) {
                                    System.out.println(e);
                                    JOptionPane.showMessageDialog(null, e);
                                }
                                break;
                            case 51:
                                String v51 = "ALTER TABLE `lancamentofinanceiro` \n"
                                        + "DROP FOREIGN KEY `fk_lancamentofinanceiro_formaspagamento`;\n";

                                String v51_1 = "ALTER TABLE `lancamentofinanceiro` \n"
                                        + "CHANGE COLUMN `formaspagamento_codigo` `formaspagamento_codigo` INT NULL ;\n";

                                String v51_2 = "ALTER TABLE `lancamentofinanceiro` \n"
                                        + "ADD CONSTRAINT `fk_lancamentofinanceiro_formaspagamento`\n"
                                        + "  FOREIGN KEY (`formaspagamento_codigo`)\n"
                                        + "  REFERENCES `formaspagamento` (`codigo`);";
                                String v51a = "update configuracoes set versaobd = 52";
                                try {
                                    pst = conexao.prepareStatement(v51);
                                    pst.execute();
                                    pst = conexao.prepareStatement(v51_1);
                                    pst.execute();
                                    pst = conexao.prepareStatement(v51_2);
                                    pst.execute();
                                    pst = conexao.prepareStatement(v51a);
                                    pst.execute();
                                } catch (Exception e) {
                                    System.out.println(e);
                                    JOptionPane.showMessageDialog(null, e);
                                }
                                break;
                            case 52:
                                String v52 = "ALTER TABLE `formaspagamento` \n"
                                        + "ADD INDEX `fk_conta_idx` (`contacorrente` ASC) VISIBLE;\n";

                                String v52_1 = "ALTER TABLE `formaspagamento` \n"
                                        + "ADD CONSTRAINT `fk_conta`\n"
                                        + "  FOREIGN KEY (`contacorrente`)\n"
                                        + "  REFERENCES `cadastrocontas` (`codigo`)\n"
                                        + "  ON DELETE NO ACTION\n"
                                        + "  ON UPDATE NO ACTION;";

                                String v52a = "update configuracoes set versaobd = 53";
                                try {
                                    pst = conexao.prepareStatement(v52);
                                    pst.execute();
                                    pst = conexao.prepareStatement(v52_1);
                                    pst.execute();

                                    pst = conexao.prepareStatement(v52a);
                                    pst.execute();
                                } catch (Exception e) {
                                    System.out.println(e);
                                    JOptionPane.showMessageDialog(null, e);
                                }
                                break;

                            case 53:
                                String v53 = "CREATE TABLE `acessos` (\n"
                                        + "  `codigo` INT NOT NULL AUTO_INCREMENT,\n"
                                        + "  `mostraiconeos` TINYINT NULL DEFAULT 1,\n"
                                        + "  PRIMARY KEY (`codigo`));";
                                String v53a = "update configuracoes set versaobd = 54";
                                try {
                                    pst = conexao.prepareStatement(v53);
                                    pst.execute();
                                    pst = conexao.prepareStatement(v53a);
                                    pst.execute();
                                } catch (Exception e) {
                                    System.out.println(e);
                                    JOptionPane.showMessageDialog(null, e);
                                }
                                break;
                            case 54:
                                String v54 = "ALTER TABLE `configuracoes` \n"
                                        + "ADD COLUMN `copiabkp` VARCHAR(3) NULL AFTER `pergunta_movimento_cartao_abertura`,\n"
                                        + "ADD COLUMN `caminhoparacopia` VARCHAR(100) NULL AFTER `copiabkp`;";
                                String v54a = "update configuracoes set versaobd = 55";
                                try {
                                    pst = conexao.prepareStatement(v54);
                                    pst.execute();
                                    pst = conexao.prepareStatement(v54a);
                                    pst.execute();
                                } catch (Exception e) {
                                    System.out.println(e);
                                    JOptionPane.showMessageDialog(null, e);
                                }
                                break;
                            case 55:
                                String v55 = "ALTER TABLE `classificacaofinanceira` \n"
                                        + "ADD COLUMN `tipo` VARCHAR(1) NULL AFTER `utlimaalteracao`";
                                String v55_1 = "update classificacaofinanceira set tipo = '='";
                                String v55a = "update configuracoes set versaobd = 56";
                                try {
                                    pst = conexao.prepareStatement(v55);
                                    pst.execute();
                                    pst = conexao.prepareStatement(v55_1);
                                    pst.execute();
                                    pst = conexao.prepareStatement(v55a);
                                    pst.execute();
                                } catch (Exception e) {
                                    System.out.println(e);
                                    JOptionPane.showMessageDialog(null, e);
                                }
                                break;

                        }
                    }
                    //aqui
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        } while (versaoAtual < versaoTela);

    }

    /**
     * Creates new form TelaLogin
     */
    public TelaLogin() {
        initComponents();

        URL icone = this.getClass().getResource("/br/com/rpsystem/icones/login.png");
        Image imgImage = Toolkit.getDefaultToolkit().getImage(icone);
        this.setIconImage(imgImage);

        String caminho = System.getProperty("user.dir");
        File permissao = new File(caminho + "\\exitmenu.txt");
        if (permissao.exists()) {
            txtUsuario.setText("Prog");
            txtSenha.setText("Prog");
            //     Logar();

        }

        conexao = ModuloConexao.conector();
        if (conexao != null) {
        } else {
            // System.out.println(conexao);
            JOptionPane.showMessageDialog(null, "Erro ao conectar ao banco de dados \n Entre em contato com o Suporte", "Erro", JOptionPane.ERROR_MESSAGE);
            exit();

            btnLogin.setEnabled(false);
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

        jLabel5 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        lblSistema = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtUsuario = new javax.swing.JTextField();
        txtSenha = new javax.swing.JPasswordField();
        jLabel3 = new javax.swing.JLabel();
        cboEmpresa = new javax.swing.JComboBox<>();
        btnSair = new javax.swing.JButton();
        btnLogin = new javax.swing.JButton();
        lblCodempresa = new javax.swing.JLabel();
        lblVersãoSis = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblVersaoTela = new javax.swing.JLabel();

        jLabel5.setText("jLabel5");

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("RP Finanças - Login");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setUndecorated(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));
        jPanel1.setPreferredSize(new java.awt.Dimension(362, 336));

        lblSistema.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        lblSistema.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSistema.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/rpsystem/icones/logo_novoSF.png"))); // NOI18N
        lblSistema.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblSistemaMouseClicked(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(153, 153, 153));
        jLabel1.setText("Usuario:");

        jLabel2.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(153, 153, 153));
        jLabel2.setText("Senha:");

        txtUsuario.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUsuarioActionPerformed(evt);
            }
        });
        txtUsuario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtUsuarioKeyPressed(evt);
            }
        });

        txtSenha.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtSenha.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSenhaKeyPressed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(153, 153, 153));
        jLabel3.setText("Empresa:");

        cboEmpresa.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        cboEmpresa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cboEmpresaMouseClicked(evt);
            }
        });
        cboEmpresa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboEmpresaActionPerformed(evt);
            }
        });
        cboEmpresa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cboEmpresaKeyPressed(evt);
            }
        });

        btnSair.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        btnSair.setText("Sair");
        btnSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSairActionPerformed(evt);
            }
        });
        btnSair.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnSairKeyPressed(evt);
            }
        });

        btnLogin.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        btnLogin.setText("Entrar");
        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginActionPerformed(evt);
            }
        });
        btnLogin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnLoginKeyPressed(evt);
            }
        });

        lblCodempresa.setVisible(false);
        lblCodempresa.setText("codempresa");

        lblVersãoSis.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblVersãoSis.setText("113.4");

        jLabel6.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel6.setText("Versão Sistema:");

        jLabel4.setVisible(false);
        jLabel4.setText("Versão Banco:");
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel4MouseClicked(evt);
            }
        });

        lblVersaoTela.setVisible(false);
        lblVersaoTela.setText("56");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblSistema, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblCodempresa)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtSenha)
                    .addComponent(cboEmpresa, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtUsuario))
                .addGap(37, 37, 37))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 100, Short.MAX_VALUE)
                        .addComponent(btnLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblVersaoTela, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSair, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblVersãoSis, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(lblSistema)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSenha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cboEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLogin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSair, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(20, 20, 20)
                .addComponent(lblCodempresa)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblVersãoSis)
                        .addComponent(jLabel6))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(lblVersaoTela)))
                .addGap(8, 8, 8))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 377, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 353, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        setSize(new java.awt.Dimension(377, 353));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
        // TODO add your handling code here:
        Logar();
        //  lerconfiguracao();

        //   setaempresa();
    }//GEN-LAST:event_btnLoginActionPerformed

    private void txtUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUsuarioActionPerformed
        // TODO add your handling code here:        
    }//GEN-LAST:event_txtUsuarioActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:
        empresa();
        if (lblCodempresa.getText().equals("")) {

        } else {
            atualizaBanco();
        }


    }//GEN-LAST:event_formWindowActivated

    private void btnSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSairActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnSairActionPerformed

    private void lblSistemaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSistemaMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_lblSistemaMouseClicked

    private void txtUsuarioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUsuarioKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtSenha.requestFocus();
        }
    }//GEN-LAST:event_txtUsuarioKeyPressed

    private void txtSenhaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSenhaKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cboEmpresa.requestFocus();
        }
    }//GEN-LAST:event_txtSenhaKeyPressed

    private void btnLoginKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnLoginKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            Logar();
//aqui vai o q voce deseja fazer quando o usuario clicar enter naquele jtextfield
        }
    }//GEN-LAST:event_btnLoginKeyPressed

    private void cboEmpresaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cboEmpresaKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnLogin.requestFocus();
//aqui vai o q voce deseja fazer quando o usuario clicar enter naquele jtextfield
        }
    }//GEN-LAST:event_cboEmpresaKeyPressed

    private void btnSairKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnSairKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            dispose();
        }
    }//GEN-LAST:event_btnSairKeyPressed

    private void jLabel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_jLabel4MouseClicked

    private void cboEmpresaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cboEmpresaMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_cboEmpresaMouseClicked

    private void cboEmpresaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboEmpresaActionPerformed
        // TODO add your handling code here:
        v_codempresa();
    }//GEN-LAST:event_cboEmpresaActionPerformed

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
            java.util.logging.Logger.getLogger(TelaLogin.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaLogin.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaLogin.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaLogin.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TelaLogin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLogin;
    private javax.swing.JButton btnSair;
    public static javax.swing.JComboBox<String> cboEmpresa;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTabbedPane jTabbedPane1;
    public static javax.swing.JLabel lblCodempresa;
    private javax.swing.JLabel lblSistema;
    public static javax.swing.JLabel lblVersaoTela;
    private javax.swing.JLabel lblVersãoSis;
    private javax.swing.JPasswordField txtSenha;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables
}
