package clinica.medica.consultas;

import clinica.medica.database.ConsultaSQL;
import clinica.medica.database.SQLiteConnection;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Classe referente ao encaixe
 */
public class Encaixe extends Consulta {
   private String motivoEmergencia;

    /**
     * Método construtor do encaixe
     * @param id Id do encaixe
     */
    public Encaixe(int id) {
        super(id);
        SQLiteConnection connection = new SQLiteConnection();
        connection.conectar();

        ResultSet rs = ConsultaSQL.selectAllFromConsulta(id, connection.getConn());

        try {
            this.setMotivoEmergencia(rs.getString("motivoEmergencia"));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Não foi possível criar o encaixe.");
        }

        connection.desconectar();
    }

    /**
     * Método get do motivo de emergência
     * @return Motivo de emergência
     */
    public String getMotivoEmergencia() {
        return motivoEmergencia;
    }

    /**
     * Método set do motivo de emergência
     * @param motivoEmergencia Motivo de emergência
     */
    public void setMotivoEmergencia(String motivoEmergencia) {
        this.motivoEmergencia = motivoEmergencia;
    }
}
