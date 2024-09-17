package clinica.medica.database;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
/**
 * Classe para realizar a consulta das informações referentes aos
 * horários no banco de dados.
 */
public class HorariosSQL {
    /**
     * Método que verifica os horários disponíveis paa o agendamento de uma consulta
     * @param medicoCpf CPF do médico escolhido para a consulta
     * @param data data escolhida para a consulta
     * @return retorna um vetor de String com os horários disponíveis
     */
    public static String[] selectHorariosDisponiveis(String medicoCpf, Date data) {
        String query = "SELECT * FROM horarios WHERE medico = ?";
        String queryVerificacao = "SELECT * FROM consultas WHERE medico = ? AND data = ?";

        SQLiteConnection connection = new SQLiteConnection();
        connection.conectar();

        ResultSet rs = null;
        ResultSet rs2 = null;

        try {
            PreparedStatement pstmt = connection.getConn().prepareStatement(query);
            pstmt.setString(1, medicoCpf);
            rs = pstmt.executeQuery();
        } catch (SQLException e){
            e.printStackTrace();
        }

        LocalDate localDate = data.toLocalDate();
        String dataFormatada = localDate.getYear() + "-" + localDate.getMonthValue() + "-" + localDate.getDayOfMonth();

        try {
            PreparedStatement pstmt = connection.getConn().prepareStatement(queryVerificacao);
            pstmt.setString(1, medicoCpf);
            pstmt.setString(2, dataFormatada);
            rs2 = pstmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<String> horariosDisponiveis = new ArrayList<>();

        try {
            while (rs.next()) {
                horariosDisponiveis.add(rs.getString("horario"));
            }
            while (rs2.next()) {
                if (horariosDisponiveis.contains(rs2.getString("horario"))){
                    horariosDisponiveis.remove(rs2.getString("horario"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        connection.desconectar();

        String[] lista = horariosDisponiveis.toArray(new String[horariosDisponiveis.size()]);

        if (horariosDisponiveis.size() == 0) {
            String[] ret = new String[1];
            ret[0] = "Não existem horários disponíveis nesse dia.";
            return ret;
        } else {
            return lista;
        }
    }

    /**
     * Método que salva os horários disponíveis de um médico
     * @param horario Horário de consulta do médico
     * @param cpfMedico CPF do médico que está sendo cadastrado
     */
    public static void salvarHorarios(String horario, String cpfMedico){
        String query = "INSERT INTO horarios (horario, medico) VALUES (?, ?)";

        SQLiteConnection connection = new SQLiteConnection();
        connection.conectar();

        try {
            PreparedStatement pstmt = connection.getConn().prepareStatement(query);
            pstmt.setString(1, horario);
            pstmt.setString(2, cpfMedico);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        connection.desconectar();
    }
}
