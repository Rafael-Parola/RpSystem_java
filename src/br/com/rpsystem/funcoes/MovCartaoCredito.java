/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.rpsystem.funcoes;

import br.com.rpsystem.dal.ModuloConexao;
import br.com.rpsystem.telas.TelaPrincipal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

/**
 *
 * @author Rafael Veiga
 */
public class MovCartaoCredito {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    private Date data;
    private int classificacao;
    private int cartao;
    private int nparcelas;
    private int nrepreticoes;
    private Double valor;
    private int dfchamento;
    private int dvencimento;
    private String obs;
    private String desc;

    public MovCartaoCredito(Date data, int classificacao, int cartao, int nparcelas, int nrepreticoes, double valor, int dfchamento, int dvencimento, String obs, String desc) {
        this.data = data;
        this.classificacao = classificacao;
        this.cartao = cartao;
        this.nparcelas = nparcelas;
        this.nrepreticoes = nrepreticoes;
        this.valor = valor;
        this.dfchamento = dfchamento;
        this.dvencimento = dvencimento;
        this.obs = obs;
        this.desc = desc;

    }

    public void lancacartao() {
        Date dataCompra = data;  // Data da compra
        int categoria = classificacao;  // Categoria da compra
        int cartaoUtilizado = cartao;  // Cartão utilizado
        int numeroParcelas = nparcelas;  // Número de parcelas
        int numeroRepeticoes = nrepreticoes;  // Número de repetições
        double valorTotal = valor;  // Valor total da compra
        String observacao = obs;

        String descricao = desc;
        //descricao = desc;

        int diaFechamento = dfchamento;  // Dia de fechamento da fatura
        int diaVencimento = dvencimento;  // Dia de vencimento da fatura

        Calendar calCompra = Calendar.getInstance();
        calCompra.setTime(dataCompra);
        int diaCompra = calCompra.get(Calendar.DAY_OF_MONTH);

        Calendar calVencimento = Calendar.getInstance();
        calVencimento.setTime(dataCompra);

        if (diaCompra <= diaFechamento) {
            calVencimento.set(Calendar.DAY_OF_MONTH, diaVencimento);
            calVencimento.add(Calendar.MONTH, 0);
        } else {
            calVencimento.set(Calendar.DAY_OF_MONTH, diaVencimento);
            calVencimento.add(Calendar.MONTH, 1);
        }

        Date vencimento = calVencimento.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String vencimentoFormatado = dateFormat.format(vencimento);

        // Calcula o valor de cada parcela
        double valorParcela = valorTotal / numeroParcelas;

        boolean success = true;

        // Simula a inserção no banco das parcelas
        for (int i = 0; i < numeroParcelas; i++) {
            Calendar calParcela = Calendar.getInstance();
            calParcela.setTime(vencimento);
            calParcela.add(Calendar.MONTH, i);

            //pego a data da parcela para inseir na tabela de fatura 
            String dataParcela = dateFormat.format(calParcela.getTime());

            // Calcula o valor da parcelas para inserir na tabela de faturas 
            double valorParcelaIndividual = valorTotal / numeroParcelas;

            if (!inserirNoBanco(dataCompra, categoria, cartaoUtilizado, dateFormat.format(calParcela.getTime()), valorParcela, observacao.concat("- Parcela: " + (i + 1) + " de " + numeroParcelas), descricao)) {

                success = false;

            }
            // Adiciona a chamada do método insere_fatura para cada parcela
            insere_fatura(valorParcelaIndividual, dataParcela, cartaoUtilizado);

        }

        for (int i = 0; i < numeroRepeticoes; i++) {
            Calendar calRecorrencia = Calendar.getInstance();
            calRecorrencia.setTime(vencimento);
            calRecorrencia.add(Calendar.MONTH, 1);  // Increment the month by 1

            String datarecorrencia = dateFormat.format(calRecorrencia.getTime());
            //inserirNoBanco(dataCompra, categoria, cartaoUtilizado, dateFormat.format(calRecorrencia.getTime()), valorTotal, observacao);
            if (!inserirNoBanco(dataCompra, categoria, cartaoUtilizado, dateFormat.format(calRecorrencia.getTime()), valorTotal, observacao, descricao)) {
                success = false; // If any insertion fails, set success to false

            }
            vencimento = calRecorrencia.getTime();
            insere_fatura(valorTotal, datarecorrencia, cartaoUtilizado);
        }

        //Simula a inserção no banco da opção à vista
        if (numeroParcelas == 0) {
            inserirNoBanco(dataCompra, categoria, cartaoUtilizado, vencimentoFormatado, valorTotal, observacao, descricao);
            insere_fatura(valorTotal, vencimentoFormatado, cartaoUtilizado);
        }
        if (success || (numeroParcelas == 0 && numeroRepeticoes == 0)) {
            JOptionPane.showMessageDialog(null, "Inserido com sucesso"); // Display the success message if all insertions were successful or there are no insertions
        }

    }

    public boolean inserirNoBanco(Date dataCompra, int categoria, int cartaoUtilizado, String vencimento, double valor, String observacao, String descricao) {
        conexao = ModuloConexao.conector();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        Date datavencimento = null;
        try {
            datavencimento = sdf.parse(vencimento);

        } catch (ParseException ex) {
            Logger.getLogger(MovCartaoCredito.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        // Converte o objeto Date para um objeto SQL Date.
        var dtvenc = new java.sql.Date(datavencimento.getTime());

        String sql = "INSERT INTO `movimentocartoes`\n"
                + "(`codigo`,`dataemissao`,`datavencimento`,`classificacao`,`cartao`,`nparcelas`,`valor`,`obs`,`empresa_codempresa`,`usuario_idusuario`,descricao)\n"
                + "select 1+ coalesce ((select max(codigo) from movimentocartoes),0),?,?,?,?,?,?,?,?,?,?;";

        try {

            pst = conexao.prepareStatement(sql);

            pst.setDate(1, new java.sql.Date(dataCompra.getTime()));
            pst.setDate(2, dtvenc);
            pst.setInt(3, categoria);
            pst.setInt(4, cartaoUtilizado);
            pst.setInt(5, 0);
            pst.setDouble(6, valor);
            pst.setString(7, observacao);
            pst.setInt(8, Integer.parseInt(TelaPrincipal.lblcodEmpresa.getText()));
            pst.setInt(9, Integer.parseInt(TelaPrincipal.lblCodUsoPrincipal.getText()));
            pst.setString(10, descricao);
            int inserido = pst.executeUpdate();
            if (inserido > 0) {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }

        return false;
    }

    public void insere_fatura(double valor, String vencimento, int cartao) {
        boolean exclusao = false;
        conexao = ModuloConexao.conector();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        Date datavencimento = null;
        try {
            datavencimento = sdf.parse(vencimento);

        } catch (ParseException ex) {
            Logger.getLogger(MovCartaoCredito.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        // Converte o objeto Date para um objeto SQL Date.
        var dtvenc = new java.sql.Date(datavencimento.getTime());

        String existeRegistro = "select * from fatura where vencimento = ? and codempresa = ? and cartao = ?";

        String realizarUpdate = "update fatura set valor = valor + " + valor + ""
                + " where vencimento = ? and cartao = ? and codempresa = ?";

        String realizarInsert = "INSERT INTO `fatura`\n"
                + "(`cod`,`cartao`,`valor`,`vencimento`,`status`,`codempresa`,`codusuario`)\n"
                + "select 1+coalesce (( select max(cod) from fatura),0),?,?,?,?,?,?;";

        String realizarExclusao = "update fatura set valor = valor - " + valor + ""
                + " where vencimento = ? and cartao = ? and codempresa = ?";

        try {
            pst = conexao.prepareStatement(existeRegistro);
            pst.setDate(1, dtvenc);
            pst.setString(2, TelaPrincipal.lblcodEmpresa.getText());
            pst.setInt(3, cartao);

            rs = pst.executeQuery();

            if (rs.next()) {
                if (exclusao) {
                    try {
                        pst = conexao.prepareStatement(realizarExclusao);
                        pst.setDate(1, dtvenc);
                        pst.setInt(2, cartao);
                        pst.setInt(3, Integer.parseInt(TelaPrincipal.lblcodEmpresa.getText()));
                        pst.executeUpdate();
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e);
                        System.out.println("Erro na exclusão do SALDO");
                    }
                } else {
                    try {
                        pst = conexao.prepareStatement(realizarUpdate);
                        pst.setDate(1, dtvenc);
                        pst.setInt(2, cartao);
                        pst.setInt(3, Integer.parseInt(TelaPrincipal.lblcodEmpresa.getText()));
                        pst.executeUpdate();
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e);
                        System.out.println("Erro na atualização do SALDO");
                    }
                }

            } else {
                try {
                    pst = conexao.prepareStatement(realizarInsert);
                    pst.setInt(1, cartao);
                    pst.setDouble(2, valor);
                    pst.setDate(3, dtvenc);
                    pst.setString(4, "A");
                    pst.setInt(5, Integer.parseInt(TelaPrincipal.lblcodEmpresa.getText()));
                    pst.setInt(6, Integer.parseInt(TelaPrincipal.lblCodUsoPrincipal.getText()));

                    pst.executeUpdate();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
                    System.out.println("Erro na criação do SALDO");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

}
