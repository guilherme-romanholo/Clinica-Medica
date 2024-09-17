package clinica.medica.database;

import clinica.medica.documentos.Exame;
import clinica.medica.documentos.Laudo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
/**
 * Classe para realizar a consulta das informações referentes aos
 * pacientes no banco de dados.
 */
public class PacientesSQL {
    /**
     * Método que retorna todos os exames que um paciente possui
     * @param cpf CPF do paciente
     * @return retorna uma lista de exames do paciente
     */
    public static ArrayList<Exame> verificarExamesPaciente(String cpf){
        ArrayList<Exame> exames = new ArrayList();
        String query = "SELECT * FROM exames WHERE paciente = ?";

        SQLiteConnection connection = new SQLiteConnection();
        connection.conectar();

        try{
            PreparedStatement pstmt = connection.getConn().prepareStatement(query);
            pstmt.setString(1, cpf);
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

    /**
     * Método que retorna todos os laudos que um paciente possui
     * @param cpf CPF do paciente
     * @return retorna uma lista de laudos do paciente
     */
    public static ArrayList<Laudo> verificarLaudos(String cpf){
        ArrayList<Laudo> laudos = new ArrayList();
        String query = "SELECT * FROM laudos WHERE paciente = ?";


        SQLiteConnection connection = new SQLiteConnection();
        connection.conectar();

        try{
            PreparedStatement pstmt = connection.getConn().prepareStatement(query);
            pstmt.setString(1, cpf);
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
