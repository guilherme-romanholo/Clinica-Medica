package clinica.medica.consultas;

import clinica.medica.database.ConsultaSQL;
import clinica.medica.database.SQLiteConnection;
import clinica.medica.usuarios.Medico;
import clinica.medica.usuarios.Paciente;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Classe referente a consulta
 */
public class Consulta {
    private int id;
    private String data;
    private Medico medico;
    private Paciente paciente;
    private boolean realizada;
    private String descricao;
    private String horario;

    private String motivoCancelamento;

    private boolean encaixe;

    /**
     * Método construtor da consulta
     * @param id Id da consulta
     */
    public Consulta(int id) {
        SQLiteConnection connection = new SQLiteConnection();
        connection.conectar();

        ResultSet rs = ConsultaSQL.selectAllFromConsulta(id, connection.getConn());

        try {
            this.medico = new Medico(rs.getString("medico"));
            this.setPaciente(new Paciente(rs.getString("paciente")));
            this.setData(rs.getString("data"));
            this.setRealizada(rs.getBoolean("realizada"));
            this.setDescricao(rs.getString("descricao"));
            this.setHorario(rs.getString("horario"));
            this.setId(id);
            this.setMotivoCancelamento(rs.getString("motivoCancelamento"));
            this.setEncaixe(rs.getBoolean("encaixe"));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Não foi possível criar a consulta.");
        }

        connection.desconectar();
    }

    /**
     * Método get id
     * @return Id
     */
    public int getId() {
        return id;
    }

    /**
     * Método set id
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Método get data
     * @return Data
     */
    public String getData() {
        return data;
    }

    /**
     * Método set data
     * @param data Data para ser setada
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * Método get médico
     * @return Médico
     */
    public Medico getMedico() {
        return medico;
    }

    /**
     * Método set medico
     * @param medico Médico
     */
    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    /**
     * Método get paciente
     * @return Paciente
     */
    public Paciente getPaciente() {
        return paciente;
    }

    /**
     * Método set paciente
     * @param paciente Paciente
     */
    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    /**
     * Método para verificar se a consulta foi realiza
     * @return True ou False
     */
    public boolean isRealizada() {
        return realizada;
    }

    /**
     * Método para setar a consilta como realizada ou não
     * @param realizada True ou False
     */
    public void setRealizada(boolean realizada) {
        this.realizada = realizada;
    }

    /**
     * Método get descrição
     * @return Descrição
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * Método set descrição
     * @param descricao Descrição
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * Método get horario
     * @return Horario
     */
    public String getHorario() {
        return horario;
    }

    /**
     * Método set horario
     * @param horario Horario
     */
    public void setHorario(String horario) {
        this.horario = horario;
    }

    /**
     * Método get motivo do cancelamento
     * @return Motivo do cancelamento
     */
    public String getMotivoCancelamento() {
        return motivoCancelamento;
    }

    /**
     * Método set motivo do cancelamento
     * @param motivoCancelamento Motivo do cancelamento
     */
    public void setMotivoCancelamento(String motivoCancelamento) {
        this.motivoCancelamento = motivoCancelamento;
    }

    /**
     * Verifica se é um encaixe
     * @return True ou false
     */
    public boolean isEncaixe() {
        return encaixe;
    }

    /**
     * Método set se é um encaixe ou não
     * @param encaixe True ou false
     */
    public void setEncaixe(boolean encaixe) {
        this.encaixe = encaixe;
    }
}
