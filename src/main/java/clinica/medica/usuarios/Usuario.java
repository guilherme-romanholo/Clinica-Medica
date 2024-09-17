package clinica.medica.usuarios;

import clinica.medica.database.SQLiteConnection;
import clinica.medica.database.UsuariosSQL;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Classe abstrata para implementação dos usuários
 */
public abstract class Usuario {
    private String nome;
    private String cpf;
    private String email;
    private String senha;

    /**
     * Método construtor dos usuários
     * @param userCpf Cpf do usuário
     */
    public Usuario(String userCpf) {
        SQLiteConnection connection = new SQLiteConnection();
        connection.conectar();

        ResultSet rs = UsuariosSQL.selectAllFromUsuario(userCpf, connection.getConn());

        try {
            this.nome = rs.getString("nome");
            this.cpf = rs.getString("cpf");
            this.email = rs.getString("email");
            this.senha = rs.getString("senha");
        } catch (SQLException e) {
            System.out.println("Não foi possível instanciar o usuário.");
        }

        connection.desconectar();
    }

    /**
     * Método get nome do usuário
     * @return Nome do usuário
     */
    public String getNome() {
        return this.nome;
    }

    /**
     * Método get cpf
     * @return Cpf do usuário
     */
    public String getCpf() {
        return this.cpf;
    }
}
