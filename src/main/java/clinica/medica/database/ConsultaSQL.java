package clinica.medica.database;

import clinica.medica.consultas.Consulta;
import clinica.medica.consultas.Encaixe;
import clinica.medica.documentos.Receita;

import javax.swing.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Classe para realizar a consulta das informações referentes as
 * consultas no banco de dados.
 */
public class ConsultaSQL {
    /**
     * Método que retorna todas as consultas marcadas por um paciente
     * @param cpfPaciente CPF do paciente que marcou as consultas
     * @return retorna uma lista de consultas marcadas pelo paciente
     */
    public static ArrayList<Consulta> selectAllConsultasFromPaciente(String cpfPaciente) {
        String query = "SELECT * FROM consultas where paciente = ? ORDER BY data ASC";
        ArrayList<Consulta> consultas = new ArrayList<>();
        ResultSet rs = null;

        SQLiteConnection connection = new SQLiteConnection();
        connection.conectar();

        try {
            PreparedStatement pstmt = connection.getConn().prepareStatement(query);
            pstmt.setString(1, cpfPaciente);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                consultas.add(new Consulta(rs.getInt("id")));
            }
        } catch (SQLException e) {
            System.out.println("Consulta não foi encontrada.");
        }

        return consultas;
    }

    /**
     * Método que retorna as consultas de um paciente em uma data específica
     * @param cpfPaciente CPF do paciente que marcou as consultas
     * @param data data para verificar as consultas em um dia específico
     * @return retorna uma lista das consultas marcadas no dia passado como parâmetro
     */
    public static ArrayList<Consulta> selectAllConsultasFromPaciente(String cpfPaciente, Date data) {
        String query = "SELECT * FROM consultas where paciente = ? AND data = ? ORDER BY data ASC";
        ArrayList<Consulta> consultas = new ArrayList<>();
        ResultSet rs = null;

        SQLiteConnection connection = new SQLiteConnection();
        connection.conectar();

        LocalDate localDate = data.toLocalDate();
        String dataFormatada = localDate.getYear() + "-" + localDate.getMonthValue() + "-" + localDate.getDayOfMonth();

        try {
            PreparedStatement pstmt = connection.getConn().prepareStatement(query);
            pstmt.setString(1, cpfPaciente);
            pstmt.setString(2 , dataFormatada);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                consultas.add(new Consulta(rs.getInt("id")));
            }
        } catch (SQLException e) {
            System.out.println("Consulta não foi encontrada.");
        }

        return consultas;
    }

    /**
     * Método que retorna as consultas de um médico em um dia específico
     * @param cpfMedico CPF do médico que possui consultas marcadas
     * @param data data para verificar as consultas em um dia específico
     * @return retorna uma lista com as consultas que o médico deve realizar no dia específicado pela data
     */
    public static ArrayList<Consulta> selectAllConsultasFromMedico(String cpfMedico, Date data) {
        String query = "SELECT * FROM consultas where medico = ? AND data = ? ORDER BY data ASC";
        ArrayList<Consulta> consultas = new ArrayList<>();
        ResultSet rs = null;

        SQLiteConnection connection = new SQLiteConnection();
        connection.conectar();

        LocalDate localDate = data.toLocalDate();
        String dataFormatada = localDate.getYear() + "-" + localDate.getMonthValue() + "-" + localDate.getDayOfMonth();

        try {
            PreparedStatement pstmt = connection.getConn().prepareStatement(query);
            pstmt.setString(1, cpfMedico);
            pstmt.setString(2, dataFormatada);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                consultas.add(new Consulta(rs.getInt("id")));
            }
        } catch (SQLException e) {
            System.out.println("Receita não foi encontrada.");
        }

        return consultas;
    }

    /**
     * Método que retorna todas as consultas que o médico deve realizar
     * @param cpfMedico CPF do médico que deve realizar as consultas
     * @return retorna uma lista de consultas que o médico deve realizar
     */
    public static ArrayList<Consulta> selectAllConsultasFromMedico(String cpfMedico) {
        String query = "SELECT * FROM consultas where medico = ? ORDER  BY data ASC";
        ArrayList<Consulta> consultas = new ArrayList<>();
        ResultSet rs = null;

        SQLiteConnection connection = new SQLiteConnection();
        connection.conectar();

        try {
            PreparedStatement pstmt = connection.getConn().prepareStatement(query);
            pstmt.setString(1, cpfMedico);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                consultas.add(new Consulta(rs.getInt("id")));
            }
        } catch (SQLException e) {
            System.out.println("Receita não foi encontrada.");
        }

        return consultas;
    }

    /**
     * Método que retorna todas as informações de uma consulta
     * @param id ID da consulta desejada
     * @param conn Conexão com o banco de dados
     * @return retorna o resultado da pesquisa do banco de dados
     */
    public static ResultSet selectAllFromConsulta(int id, Connection conn) {
        String query = "SELECT * FROM consultas WHERE id = ?";
        ResultSet rs = null;

        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
        } catch (SQLException e) {
            System.out.println("Consulta não foi encontrada.");
        }

        return rs;
    }

    /**
     * Método usado para salvar uma consulta no banco de dados
     * @param data data da consulta
     * @param cpfMedico CPF do médico que irá realizar a consulta
     * @param cpfPaciente CPF do paciente que marcou a consulta
     * @param descricao descrição da consulta
     * @param horario horário da consulta
     * @param painel painel da interface do cadastro da consulta
     * @return retorna verdadeiro, se a consulta for salva com sucesso, ou falso se há algum erro
     */
    public static boolean salvarConsulta(Date data, String cpfMedico, String cpfPaciente, String descricao, String horario, JPanel painel){
        boolean cadastro = false;
        String query = "INSERT INTO consultas (medico, paciente, data, descricao, realizada, horario, motivoCancelamento, motivoEmergencia, encaixe) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        if(descricao.equals("")){
            JOptionPane.showMessageDialog(painel, "O cadastro de consulta não pode conter campos vazios !", "ERRO", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        SQLiteConnection connection = new SQLiteConnection();
        connection.conectar();

        LocalDate localDate = data.toLocalDate();
        String dataFormatada = localDate.getYear() + "-" + localDate.getMonthValue() + "-" + localDate.getDayOfMonth();

        try {
            PreparedStatement pstmt = connection.getConn().prepareStatement(query);
            pstmt.setString(1, cpfMedico);
            pstmt.setString(2, cpfPaciente);
            pstmt.setString(3, dataFormatada);
            pstmt.setString(4, descricao);
            pstmt.setBoolean(5, false);
            pstmt.setString(6, horario);
            pstmt.setString(7, "");
            pstmt.setString(8, "");
            pstmt.setBoolean(9, false);
            pstmt.executeUpdate();
            cadastro = true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        connection.desconectar();

        return cadastro;
    }

    /**
     * Método para salvar um encaixe no banco de dados
     * @param data data do encaixe
     * @param cpfMedico CPF do médico que marcou o encaixe
     * @param cpfPaciente CPF do paciente que será atendido no encaixe
     * @param descricao descrição do encaixe
     * @param motivo motivo do encaixe
     * @param horario horário do encaixe
     * @param painel painel da interface de cadastro do encaixe
     * @return retorna verdadeiro, se a consulta for salva com sucesso, ou falso se há algum erro
     */
    public static boolean salvarEncaixe(Date data, String cpfMedico, String cpfPaciente, String descricao,String motivo, String horario, JPanel painel){
        boolean cadastro = false;
        String queryVerificacao = "SELECT * FROM pacientes WHERE cpf = ?";
        String query = "INSERT INTO consultas (medico, paciente, data, descricao, realizada, horario, motivoCancelamento, motivoEmergencia, encaixe) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        if(descricao.equals("") || motivo.equals("")){
            JOptionPane.showMessageDialog(painel, "O cadastro encaixe não pode conter campos vazios !", "ERRO", JOptionPane.ERROR_MESSAGE);
            return false;
        }else if(cpfPaciente.contains("_")){
            JOptionPane.showMessageDialog(painel, "CPF do paciente inválido !", "ERRO", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        SQLiteConnection connection = new SQLiteConnection();
        connection.conectar();

        try {
            PreparedStatement pstmt = connection.getConn().prepareStatement(queryVerificacao);
            pstmt.setString(1, cpfPaciente);
            ResultSet rs = pstmt.executeQuery();
            if(!rs.next()){
                JOptionPane.showMessageDialog(painel, "Esse paciente não existe !", "ERRO", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        LocalDate localDate = data.toLocalDate();
        String dataFormatada = localDate.getYear() + "-" + localDate.getMonthValue() + "-" + localDate.getDayOfMonth();

        try {
            PreparedStatement pstmt = connection.getConn().prepareStatement(query);
            pstmt.setString(1, cpfMedico);
            pstmt.setString(2, cpfPaciente);
            pstmt.setString(3, dataFormatada);
            pstmt.setString(4, descricao);
            pstmt.setBoolean(5, false);
            pstmt.setString(6, horario);
            pstmt.setString(7, "");
            pstmt.setString(8, motivo);
            pstmt.setBoolean(9, true);
            pstmt.executeUpdate();
            cadastro = true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        connection.desconectar();

        return cadastro;
    }



    /**
     * Método que atualiza a situação de uma consulta
     * @param id ID da consulta que será atualizada
     */
    public static void atualizaConsulta(int id){
        String query = "UPDATE consultas SET realizada = 1 WHERE id = ?";

        SQLiteConnection connection = new SQLiteConnection();
        connection.conectar();

        try {
            PreparedStatement pstmt = connection.getConn().prepareStatement(query);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        connection.desconectar();
    }

    /**
     * Método que atualizará uma consulta para marcá-la como cancelada
     * @param id ID da consulta cancelada
     * @param motivoCancelamento moticvo do cancelamento da consulta
     */
    public static void atualizaConsulta(int id, String motivoCancelamento){
        String query = "UPDATE consultas SET realizada = true, motivoCancelamento = ? WHERE id = ?";

        SQLiteConnection connection = new SQLiteConnection();
        connection.conectar();

        try {
            PreparedStatement pstmt = connection.getConn().prepareStatement(query);
            pstmt.setString(1,motivoCancelamento);
            pstmt.setInt(2,id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        connection.desconectar();
    }
}
