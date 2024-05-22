/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.rpsystem.funcoes;

import br.com.rpsystem.dal.ModuloConexao;
import br.com.rpsystem.telas.TelaPrincipal;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.JOptionPane;

/**
 *
 * @author Rafael Veiga
 */
public class EnviarEmail {

    private String destinatario;
    private String assunto;
    private String mensagem;
    private String arquivo;
//    private String starttls = "true";

    public EnviarEmail(String destinatario, String assunto, String mensagem, String arquivo) {

        this.destinatario = destinatario;
        this.assunto = assunto;
        this.mensagem = mensagem;
        this.arquivo = arquivo;

    }

    public void enviar() throws IOException {

        Connection conexao = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        conexao = ModuloConexao.conector();

        String buscaemail = "select emailenvio,senhaenvio,smtp,porta,smtpautenticado from configuracoes "
                + "where empresa_codempresa = " + TelaPrincipal.lblcodEmpresa.getText() + " and emailenvio <> ''";

        try {
            pst = conexao.prepareStatement(buscaemail);
            rs = pst.executeQuery();

            if (rs.next()) {
                String emailenvio = rs.getString(1);
                String senahenvio = rs.getString(2);
                String smtp = rs.getString(3);
                String porta = rs.getString(4);
                String smtpautenticado = rs.getString(5);
                String remetente = emailenvio;
                String senha = senahenvio;

                Properties props = new Properties();
                props.put("mail.smtp.host", smtp);
                props.put("mail.smtp.port", porta);
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.auth", smtpautenticado);
                
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.ssl.trust", smtp);


                Session session;
                System.setProperty("javax.net.debug", "all");
                session = Session.getInstance(props,
                        new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(remetente, senha);
                    }
                });

                try {

                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(remetente));

                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));

                    message.setSubject(assunto);

                    //message.setText(mensagem);
                    MimeBodyPart messageBodyPart = new MimeBodyPart();
                    messageBodyPart.setContent(mensagem, "text/html; charset=utf-8 ");
//
                    Multipart multipart = new MimeMultipart();
                    MimeBodyPart attachPart = new MimeBodyPart();
                    attachPart.attachFile(arquivo);
                    multipart.addBodyPart(attachPart);
                    multipart.addBodyPart(messageBodyPart);

                    message.setContent(multipart);
                    Transport.send(message);

                    JOptionPane.showMessageDialog(null, "Enviado com sucesso.");
                } catch (Exception e) {
                    System.out.println(e);
                    JOptionPane.showMessageDialog(null, "Verifique as configurações do e-mail\n Caso o problema persista acione o Suporte", "Erro E-mail", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Não exite email configurado", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

}
