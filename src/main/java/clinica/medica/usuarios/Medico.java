package clinica.medica.usuarios;

import clinica.medica.consultas.Consulta;
import clinica.medica.database.SQLiteConnection;
import clinica.medica.database.UsuariosSQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 * Classe do médico
 */
public class Medico extends Usuario {
    private String areaAtuacao;
    private String CRM;
    private LinkedList<Consulta> consultas;

    /**
     * Método construtor do médico
     * @param medicoCpf Cpf do médico que sera criado
     */
    public Medico(String medicoCpf) {
        super(medicoCpf);

        SQLiteConnection connection = new SQLiteConnection();
        connection.conectar();

        ResultSet rs = UsuariosSQL.selectAllFromMedico(medicoCpf, connection.getConn());

        try {
            this.areaAtuacao = rs.getString("areaAtuacao");
            this.CRM = rs.getString("CRM");
            this.consultas = null;
        } catch (SQLException e) {
            System.out.println("Não foi possível instanciar o medico.");
        }

        connection.desconectar();
    }

    /**
     * Getter área de atuação
     * @return Área de atuação
     */
    public String getAreaAtuacao() {
        return this.areaAtuacao;
    }

    /**
     * Getter do Crm
     * @return CRM
     */
    public String getCRM(){
        return this.CRM;
    }
}
