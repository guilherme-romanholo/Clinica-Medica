package clinica.medica.database;

import clinica.medica.documentos.Receita;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
/**
 * Classe para realizar a consulta das informações referentes as
 * receitas no banco de dados.
 */
public class ReceitasSQL {
    /**
     * Método que retorna todas as receitas que um paciente possui
     * @param cpfPaciente CPF do paciente
     * @return retorna uma lista de receitas do paciente
     */
    public static ArrayList<Receita> selectAllReceitasFromPaciente(String cpfPaciente) {
        String query = "SELECT * FROM receitas where paciente = ?";
        ArrayList<Receita> receitas = new ArrayList<>();
        ResultSet rs = null;

        SQLiteConnection connection = new SQLiteConnection();
        connection.conectar();

        try {
            PreparedStatement pstmt = connection.getConn().prepareStatement(query);
            pstmt.setString(1, cpfPaciente);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                receitas.add(new Receita(rs.getInt("id")));
            }
        } catch (SQLException e) {
            System.out.println("Receita não foi encontrada.");
        }

        return receitas;
    }

    /**
     * Método que retorna todas as receitas feitas por um médico
     * @param cpfMedico CPF médico
     * @return retorna uma lista de receitas feitas pelo médico
     */
    public static ArrayList<Receita> selectAllReceitasFromMedico(String cpfMedico) {
        String query = "SELECT * FROM receitas where medico = ?";
        ArrayList<Receita> receitas = new ArrayList<>();
        ResultSet rs = null;

        SQLiteConnection connection = new SQLiteConnection();
        connection.conectar();

        try {
            PreparedStatement pstmt = connection.getConn().prepareStatement(query);
            pstmt.setString(1, cpfMedico);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                receitas.add(new Receita(rs.getInt("id")));
            }
        } catch (SQLException e) {
            System.out.println("Receita não foi encontrada.");
        }

        return receitas;
    }

    /**
     * Método que retorna todos os dados de uma receita específica
     * @param id ID da receita desejada
     * @param conn conexão com o banco de dados
     * @return retorna o resultado da consulta do banco de dados
     */
    public static ResultSet selectAllFromReceitas(int id, Connection conn) {
        String query = "SELECT * FROM receitas WHERE id = ?";
        ResultSet rs = null;

        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
        } catch (SQLException e) {
            System.out.println("Receita não foi encontrada.");
        }

        return rs;
    }

    /**
     * Método que salva uma nova receita no banco de dados
     * @param receita Objeto da receita que será salva
     * @param panel painel da interface de cadastro da receita
     * @return retorna verdadeiro se o cadastro der certo, ou falso se houver algum erro
     */
    public static boolean cadastrarReceita(Receita receita, JPanel panel){
        boolean cadastro = false;

        if(receita.getNomeDoRemedio().matches("[0-9]+")){
            JOptionPane.showMessageDialog(panel, "Não foi possível cadastrar a receita, tente novamente!", "ERRO", JOptionPane.ERROR_MESSAGE);
            return false;
        }else if(receita.getNomeDoRemedio().equals("") || receita.getDetalhes().equals("")){
            JOptionPane.showMessageDialog(panel, "O cadastro de receita não pode conter campos vazios !", "ERRO", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String queryVerificacao = "SELECT * FROM pacientes WHERE cpf = ?";
        String query = "INSERT INTO receitas (nomeDoRemedio, detalhes, medico, paciente, data) VALUES (?, ?, ?, ?, ?)";

        SQLiteConnection connection = new SQLiteConnection();
        connection.conectar();

        try {
            PreparedStatement pstmt = connection.getConn().prepareStatement(queryVerificacao);
            pstmt.setString(1, receita.getPaciente().getCpf());
            ResultSet rs = pstmt.executeQuery();
            if(!rs.next()){
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        try {
            PreparedStatement pstmt = connection.getConn().prepareStatement(query);
            pstmt.setString(1, receita.getNomeDoRemedio());
            pstmt.setString(2, receita.getDetalhes());
            pstmt.setString(3, receita.getMedico().getCpf());
            pstmt.setString(4, receita.getPaciente().getCpf());
            pstmt.setDate(5, receita.getDataReceita());
            pstmt.executeUpdate();
            cadastro = true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        connection.desconectar();

        return cadastro;
    }
}
