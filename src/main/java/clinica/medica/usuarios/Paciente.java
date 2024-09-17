package clinica.medica.usuarios;

import clinica.medica.database.SQLiteConnection;
import clinica.medica.database.UsuariosSQL;
import clinica.medica.documentos.Exame;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;

/**
 * Classe do paciente
 */
public class Paciente extends Usuario{
    private String endereco;
    private ArrayList<Exame> exames;
    private String sexo;
    private int idade;
    private double altura;
    private double peso;

    /**
     * Método construtor do paciente
     * @param pacienteCpf Cpf do paciente
     */
    public Paciente(String pacienteCpf){
        super(pacienteCpf);

        SQLiteConnection connection = new SQLiteConnection();
        connection.conectar();

        ResultSet rs = UsuariosSQL.selectAllFromPaciente(pacienteCpf, connection.getConn());

        try {
            this.setEndereco(rs.getString("endereco"));
            this.setSexo(rs.getString("sexo"));
            this.setIdade(rs.getInt("idade"));
            this.setAltura(rs.getDouble("altura"));
            this.setPeso(rs.getDouble("peso"));
            this.setExames(null);
        } catch (SQLException e) {
            System.out.println("Não foi possível instanciar o paciente.");
        }

        connection.desconectar();
    }


    /**
     * Método set do endereõ
     * @param endereco Endereço
     */
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }


    /**
     * Método set dos exames
     * @param exames Lista de exames
     */
    public void setExames(ArrayList<Exame> exames) {
        this.exames = exames;
    }


    /**
     * Método get sexo
     * @return sexo
     */
    public String getSexo() {
        return sexo;
    }

    /**
     * Método set sexo
     * @param sexo Sexo para ser setado
     */
    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    /**
     * Método get idade
     * @return Idade
     */
    public int getIdade() {
        return idade;
    }

    /**
     * Método set idade
     * @param idade Idade para ser setada
     */
    public void setIdade(int idade) {
        this.idade = idade;
    }

    /**
     * Método set altura
     * @param altura Altura para ser setada
     */
    public void setAltura(double altura) {
        this.altura = altura;
    }

    /**
     * Método set peso
     * @param peso Peso para ser setado
     */
    public void setPeso(double peso) {
        this.peso = peso;
    }
}
