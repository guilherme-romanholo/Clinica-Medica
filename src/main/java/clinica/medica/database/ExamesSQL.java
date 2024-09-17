package clinica.medica.database;

import clinica.medica.documentos.Exame;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
/**
 * Classe para realizar a consulta das informações referentes aos
 * exames no banco de dados.
 */
public class ExamesSQL {
    /**
     * Método que seleciona todas as informações de um exame
     * @param id ID do exame desejado
     * @param conn conexão com o banco de dados
     * @return retorna o resultado da consulta no banco
     */
    public static ResultSet selectAllFromExames(int id, Connection conn) {
        String query = "SELECT * FROM exames WHERE id = ?";
        ResultSet rs = null;

        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
        } catch (SQLException e) {
            System.out.println("Exame não foi encontrado.");
        }

        return rs;
    }

    /**
     * Método que retorna todos os exames feitos por um médico
     * @param cpfMedico CPF do médico que realizou os exames
     * @return retorna uma lista de exames realizados pelo médico
     */
    public static ArrayList<Exame> verificarExames(String cpfMedico){
        ArrayList<Exame> exames = new ArrayList();
        String query = "SELECT * FROM exames WHERE medicoSolicitante = ?";

        SQLiteConnection connection = new SQLiteConnection();
        connection.conectar();

        try{
            PreparedStatement pstmt = connection.getConn().prepareStatement(query);
            pstmt.setString(1, cpfMedico);
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()) {
                exames.add(new Exame(rs.getInt("id")));
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

        connection.desconectar();

        return exames;
    }
}
