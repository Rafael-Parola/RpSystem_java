/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.rpsystem.funcoes;

import br.com.rpsystem.dal.ModuloConexao;
import br.com.rpsystem.telas.TelaPrincipal;
import java.awt.List;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Rafael Veiga
 */
public class GeraBackup {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public String caminhocopia = null;
    public String caminhoexecucao = System.getProperty("user.dir");

    public void GerarBackup() throws InterruptedException {
        conexao = ModuloConexao.conector();

        String copiabkp = null;
        String caminhodump = null;
        String sql = "select copiabkp, caminhoparacopia,caminho_backup from configuracoes where empresa_codempresa = " + TelaPrincipal.lblcodEmpresa.getText();
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {

                copiabkp = rs.getString(1);
                //System.out.println(copiabkp);
                caminhocopia = rs.getString(2);
                //System.out.println(caminhocopia);
                caminhodump = rs.getString(3);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        File pasta = new File(caminhoexecucao + "\\backup");
        if (!pasta.exists()) {
            pasta.mkdirs();
        }

        String ArqConfig = caminhoexecucao + "\\config.con";
        String conteudo = Arquivo.Read(ArqConfig);
        String caminho = conteudo.split(";")[0];
        String banco = conteudo.split(";")[1];
        String usuario = conteudo.split(";")[2];

        if (caminho.equals("localhost") || caminho.equals("127.0.0.1")) {

            try {
                String mysqlPath = caminhodump.concat("\\mysqldump.exe");
                String dbHost = caminho;
                String dbPort = "3306";
                String dbUser = usuario;
                String dbPass = "dev_rp_sys";
                String dbName = banco;

                // Adicione o caminho da pasta de destino
                String backupDestination = caminhoexecucao + "\\backup";

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmm");
                String currentDate = dateFormat.format(new Date());
                // Construa o nome do arquivo de backup
                String backupFilename = backupDestination + "\\backup_" + currentDate + ".backup";

                // Comando para executar o mysqldump usando um array de strings
                String[] command = {
                    mysqlPath,
                    "--host=" + dbHost,
                    "--port=" + dbPort,
                    "--user=" + dbUser,
                    "--password=" + dbPass,
                    "--databases",
                    dbName,
                    "--routines",
                    "--events",
                    "--single-transaction",
                    "--verbose",
                    "--result-file=" + backupFilename,
                    "--add-drop-database",
                    "--create-options"
                };

                // Use ProcessBuilder para executar o comando e defina o diretório de trabalho
                ProcessBuilder processBuilder = new ProcessBuilder(command);
                processBuilder.directory(new File(backupDestination));
                Process process = processBuilder.start();

                if (copiabkp.equals("Sim")) {
                    // Crie uma thread para aguardar o término do processo em segundo plano
                    new Thread(() -> {
                        try {
                            // Aguarde o término do processo
                            int exitCode = process.waitFor();

                            if (exitCode == 0) {
                                //System.out.println("Backup concluído com sucesso.");

                                // Exemplo de caminho de destino
                                //String destino = caminhocopia + "\\" + File.separator + "backup_" + currentDate + ".backup";
                                String destino = caminhocopia + File.separator + "backup_" + currentDate + ".backup";

                                // Cria um objeto Path para o arquivo de destino
                                Path destinoPath = Path.of(destino);

                                // Copia o arquivo para o destino com a opção de substituir caso já exista
                                Files.copy(Path.of(backupFilename), destinoPath, StandardCopyOption.REPLACE_EXISTING);
                                // System.out.println("Cópia do backup concluída para: " + destinoPath);
                                ApagaArquivosAntigos();

                            } else {
                                //   System.out.println("Erro durante o backup. Código de saída: " + exitCode);
                            }
                        } catch (InterruptedException | IOException e) {
                            e.printStackTrace();
                        }
                    }).start();
                    // Continue executando outras tarefas enquanto o backup está em andamento
                } else {
                    System.out.println("Não vai copiar");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //****************//
    }

    //**********************************************************************************************************************,
    //**********************************************************************************************************************
    public void ApagaArquivosAntigos() {
        apagaArquivosPorPasta(caminhoexecucao + "\\backup");
        //String pasta = caminhoexecucao + "\\backup";
        apagaArquivosPorPasta(caminhocopia);
      //  System.out.println("O caminho que de cópia é : " + caminhocopia);
    }

    private void apagaArquivosPorPasta(String pasta) {
        int diasLimite = 3;

        File diretorio = new File(pasta);

        if (!diretorio.exists() || !diretorio.isDirectory()) {
            // A pasta especificada não existe ou não é um diretório.
            return;
        }

        File[] arquivos = diretorio.listFiles((dir, nome) -> {
            if (nome.startsWith("backup_") && nome.endsWith(".backup")) {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmm");
                    Date dataBackup = dateFormat.parse(nome.substring(7, 21));

                    long diff = new Date().getTime() - dataBackup.getTime();
                    long diasPassados = diff / (24 * 60 * 60 * 1000);

                    return diasPassados > diasLimite;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            return false;
        });

        if (arquivos != null) {
            for (File arquivo : arquivos) {
                if (arquivo.delete()) {
                    // Arquivo apagado com sucesso.
                 //   System.out.println("Apagou arquivos ");
                } else {
                    // Falha ao apagar o arquivo.
                }
            }
        }
    }

}
