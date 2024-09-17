package clinica.medica.database;

import clinica.medica.documentos.Laudo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
/**
 * Classe para realizar a consulta das informações referentes aos
 * laudos no banco de dados.
 */
public class LaudosSQL {
    /**
     * Método que retorna todos os dados de um laudo
     * @param id ID do laudo desejado
     * @param conn conexão com o banco de dados
     * @return retorna o resultado da consulta com o banco de dados
     */
    public static ResultSet selectAllFromLaudos(int id, Connection conn) {
        String query = "SELECT * FROM laudos WHERE exame = ?";
        ResultSet rs = null;

        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
        } catch (SQLException e) {
            System.out.println("Laudo não foi encontrado.");
        }

        return rs;
    }

    /**
     * Método que retorna todos os laudos realizados por um médico
     * @param cpfMedico CPF do médico que realizou os laudos
     * @return retorna uma lista de laudos feitos pelo médico solicitante
     */
    public static ArrayList<Laudo> verificarLaudos(String cpfMedico){
        ArrayList<Laudo> laudos = new ArrayList();
        String query = "SELECT * FROM laudos WHERE medicoSolicitante = ?";

        SQLiteConnection connection = new SQLiteConnection();
        connection.conectar();

        try{
            PreparedStatement pstmt = connection.getConn().prepareStatement(query);
            pstmt.setString(1, cpfMedico);
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()) {
                laudos.add(new Laudo(rs.getInt("exame")));
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

        connection.desconectar();

        return laudos;
    }
}
