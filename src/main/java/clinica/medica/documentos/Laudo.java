package clinica.medica.documentos;

import clinica.medica.database.LaudosSQL;
import clinica.medica.database.SQLiteConnection;
import clinica.medica.usuarios.Medico;
import clinica.medica.usuarios.Paciente;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Classe referente ao laudo
 */
public class Laudo implements Imprimivel {
    private Exame exame;
    private String clinica;
    private Medico medicoSolicitante;
    private Paciente paciente;
    private Date data;
    private String conteudo;

    /**
     * Método construtor do laudo
     * @param id Id do laudo
     */
    public Laudo(int id) {
        SQLiteConnection connection = new SQLiteConnection();

        connection.conectar();

        ResultSet rs = LaudosSQL.selectAllFromLaudos(id, connection.getConn());

        try {
            this.setExame(new Exame(rs.getInt("exame")));
            this.setClinica("Clinica UNESP");
            this.setPaciente(new Paciente(rs.getString("paciente")));
            this.setMedicoSolicitante(new Medico(rs.getString("medicoSolicitante")));
            this.setData(rs.getDate("data"));
            this.setConteudo(rs.getString("conteudo"));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        connection.desconectar();
    }

    // --------------------- Métodos da Interface ---------------------
    @Override
    public String imprimeTipo() {
        return "Laudo";
    }
    @Override
    public String imprimeInfo() {
        return "Laudo referente ao exame: " + exame.getTipo() + " - id: " + exame.getId();
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
        return "Data do laudo: " + data;
    }
    @Override
    public String imprimeComentarios() {
        return "Informações:\n" + conteudo;
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
     * Get exame
     * @return Exame
     */
    public Exame getExame() {
        return exame;
    }

    /**
     * Set exame
     * @param exame Exame
     */
    public void setExame(Exame exame) {
        this.exame = exame;
    }

    /**
     * Get clinica
     * @return Nome da clinica
     */
    public String getClinica() {
        return clinica;
    }

    /**
     * Set clinica
     * @param clinica Nome da clinica
     */
    public void setClinica(String clinica) {
        this.clinica = clinica;
    }

    /**
     * Set medico solicitante
     * @param medicoSolicitante Medico solicitante
     */
    public void setMedicoSolicitante(Medico medicoSolicitante) {
        this.medicoSolicitante = medicoSolicitante;
    }

    /**
     * Get paciente
     * @return Paciente
     */
    public Paciente getPaciente() {
        return paciente;
    }

    /**
     * Set paciente
     * @param paciente Paciente
     */
    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    /**
     * Get data
     * @return Data
     */
    public Date getData() {
        return data;
    }

    /**
     * Set data
     * @param data Data
     */
    public void setData(Date data) {
        this.data = data;
    }

    /**
     * Set conteudo
     * @param conteudo Conteudo
     */
    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }
}
