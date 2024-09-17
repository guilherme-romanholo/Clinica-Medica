
package clinica.medica.database;

import clinica.medica.documentos.Exame;
import clinica.medica.documentos.Laudo;
import clinica.medica.usuarios.Medico;
import clinica.medica.usuarios.Paciente;

import javax.swing.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Classe para realizar a consulta das informações referentes aos
 * médicos no banco de dados.
 */
public class MedicosSQL {
    /**
     * Método que salva um novo exame no banco de dados
     * @param tipo tipo do exame
     * @param cpf CPF do paciente
     * @param cpfMedico CPF do médico
     * @param data datd do exame
     * @param comentario comentário do exame
     * @param panel painel da interface do cadastro de exame
     * @return retorna verdadeiro se o cadastro der certo, ou falso se houver algum erro
     */
    public static boolean cadastrarNovoExame(String tipo, String cpf, String cpfMedico, Date data, String comentario, JPanel panel){
        boolean cadastro = false;

        if(tipo.matches("[0-9]+")){
            JOptionPane.showMessageDialog(panel, "Não foi possível cadastrar o exame, tente novamente!", "ERRO", JOptionPane.ERROR_MESSAGE);
            return false;
        }else if(tipo.equals("") || comentario.equals("")){
            JOptionPane.showMessageDialog(panel, "O cadastro de exame não pode conter campos vazios !", "ERRO", JOptionPane.ERROR_MESSAGE);
            return false;
        }else if(cpf.contains("_")){
            JOptionPane.showMessageDialog(panel, "CPF inválido!", "ERRO", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String queryVerificacao = "SELECT * FROM pacientes WHERE cpf = ?";
        String query = "INSERT INTO exames (tipo, paciente, medicoSolicitante, comentario, data) VALUES (?, ?, ?, ?, ?)";
        
        SQLiteConnection connection = new SQLiteConnection();
        connection.conectar();

        try {
            PreparedStatement pstmt = connection.getConn().prepareStatement(queryVerificacao);
            pstmt.setString(1, cpf);  
            ResultSet rs = pstmt.executeQuery();
            if(!rs.next()){
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        try {
            PreparedStatement pstmt = connection.getConn().prepareStatement(query);
            pstmt.setString(1, tipo);
            pstmt.setString(2, cpf);
            pstmt.setString(3, cpfMedico);
            pstmt.setString(4, comentario);
            pstmt.setDate(5, data);
            pstmt.executeUpdate();
            cadastro = true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        connection.desconectar();

        return cadastro;
    }

    /**
     * Método que salva um novo laudo no banco de dados
     * @param idExame ID do exame utilizado para fazer o laudo
     * @param cpf CPF do paciente
     * @param cpfMedico CPF do médico que realiza o laudo
     * @param data data do laudo
     * @param conteudo conteudo do laudo
     * @param panel painel da interface de cadastro do laudo
     * @return retorna verdadeiro se o cadastro der certo, ou falso se houver algum erro
     */
    public static boolean cadastrarNovoLaudo(int idExame, String cpf, String cpfMedico, Date data, String conteudo, JPanel panel){
        boolean cadastro = false;

        if(conteudo.equals("")){
            JOptionPane.showMessageDialog(panel, "O cadastro de laudo não pode conter campos vazios !", "ERRO", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        String query = "INSERT INTO laudos (exame, medicoSolicitante, paciente, data, conteudo) VALUES (?, ?, ?, ?, ?)";

        SQLiteConnection connection = new SQLiteConnection();
        connection.conectar();

        try {
            PreparedStatement pstmt = connection.getConn().prepareStatement(query);
            pstmt.setInt(1, idExame);
            pstmt.setString(2, cpfMedico);
            pstmt.setString(3, cpf);
            pstmt.setDate(4, data);
            pstmt.setString(5, conteudo);
            pstmt.executeUpdate();
            cadastro = true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        connection.desconectar();

        return cadastro;
    }

}
