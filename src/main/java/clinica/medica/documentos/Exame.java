package clinica.medica.documentos;

import clinica.medica.database.ExamesSQL;
import clinica.medica.database.SQLiteConnection;
import clinica.medica.usuarios.Medico;
import clinica.medica.usuarios.Paciente;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Classe referente ao exame
 */
public class Exame implements Imprimivel {
    private int id;
    private String tipo;
    private Paciente paciente;
    private Medico medicoSolicitante;
    private Date data;
    private String comentario;

    /**
     * Método construtor do exame
     * @param id Id do exame
     */
    public Exame(int id) {
        SQLiteConnection connection = new SQLiteConnection();

        connection.conectar();

        ResultSet rs = ExamesSQL.selectAllFromExames(id, connection.getConn());

        try {
            this.id = rs.getInt("id");
            this.tipo = rs.getString("tipo");
            this.paciente = new Paciente(rs.getString("paciente"));
            this.medicoSolicitante = new Medico(rs.getString("medicoSolicitante"));
            this.data = rs.getDate("data");
            this.comentario = rs.getString("comentario");
        } catch (SQLException e) {
            System.out.println("Não foi possível instanciar o exame.");
        }

        connection.desconectar();
    }

    // --------------------- Métodos da Interface ---------------------
    @Override
    public String imprimeTipo() {
        return "Exame";
    }
    @Override
    public String imprimeInfo() {
        return tipo + " - id: " + id;
    }
    @Override
    public String imprimeNomePaciente() {
        return "Nome: " + paciente.getNome();
    }
    @Override
    public String imprimeIdade() {
        return "Idade: " + paciente.getIdade();
    }
    @Override
    public String imprimeSexo() {
        return "Sexo: " + paciente.getSexo();
    }
    @Override
    public String imprimeData() {
        return "Data do exame: " + data;
    }
    @Override
    public String imprimeComentarios() {
        return "Informações:\n" + comentario;
    }
    @Override
    public String imprimeNomeMedico() {
        return "Dr. " + medicoSolicitante.getNome();
    }
    @Override
    public String imprimeCrmAtuacaoMedico() {
        return "CRM " + medicoSolicitante.getCRM() + " - " + medicoSolicitante.getAreaAtuacao();
    }

    // --------------------- Getters e Setters ---------------------
    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the tipo
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * @return the paciente
     */
    public Paciente getPaciente() {
        return paciente;
    }

    /**
     * @param paciente the paciente to set
     */
    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    /**
     * @return the medicoSolicitante
     */
    public Medico getMedicoSolicitante() {
        return medicoSolicitante;
    }

    /**
     * @return the data
     */
    public Date getData() {
        return data;
    }

    /**
     * @return the comentario
     */
    public String getComentario() {
        return comentario;
    }
}
