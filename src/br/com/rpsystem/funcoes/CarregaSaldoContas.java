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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Rafael Veiga
 */
public class CarregaSaldoContas {
    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    
    private JTable table;
    
    public CarregaSaldoContas(JTable table){
        this.table = table;
        
    }
    
    
    public void exibesaldo() {
        conexao = ModuloConexao.conector();
        String consulta_saldo = "SELECT exibetelaincial,nome as 'Descrição', saldo\n"
                + "FROM cadastrocontas\n"
                + "WHERE exibetelaincial = 1 and codempresa = " + TelaPrincipal.lblcodEmpresa.getText() ;
        try {
            pst = conexao.prepareStatement(consulta_saldo);
            rs = pst.executeQuery();

            // System.out.println("caiu aqui cu");
            // Criar o modelo da tabela
            DefaultTableModel model = (DefaultTableModel) table.getModel();

            // Limpar o modelo da tabela
            model.setRowCount(0);

            // Adicionar dados ao modelo
            while (rs.next()) {
                String descricao = rs.getString("Descrição");
                double saldo = rs.getDouble("Saldo");
                String saldoFormatado = String.format("R$ %.2f", saldo); // Formatar o saldo conforme necessário
                model.addRow(new Object[]{descricao, saldoFormatado});
            }

            // Painel (ou contêiner) que envolve a tabela
            JScrollPane scrollPane = (JScrollPane) table.getParent().getParent();

            // Verificar se há ou não linhas na tabela
            if (model.getRowCount() == 0) {
                // Se não houver linhas, ocultar o painel
                scrollPane.setVisible(false);
                // Adicione aqui qualquer outra lógica que você queira executar quando a tabela estiver vazia
            } else {
                // Se houver linhas, tornar o painel visível
                scrollPane.setVisible(true);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
}
