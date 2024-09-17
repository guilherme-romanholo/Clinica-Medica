package clinica.medica.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe para realizar a conexão ao banco de dados SQLite.
 */
public class SQLiteConnection {
    /**
     * Conexão com o banco de dados.
     */
    private Connection conn;
    /**
     * Caminho para o banco de dados.
     */
    private final String databasePath;

    /**
     * Contrutor da classe.
     */
    public SQLiteConnection(){
        this.conn = null;
        this.databasePath = "src/main/resources/database/clinica.db";
    }

    /**
     * Método para realizar a conexão com o banco de dados.
     */
    public void conectar() {
        try {
            this.conn = DriverManager.getConnection("jdbc:sqlite:" + this.databasePath);
        } catch (SQLException e) {
            System.out.println("Não foi possível realizar a conexão com o Banco de Dados.");
        }
    }

    /**
     * Método para desconectar do banco de dados.
     */
    public void desconectar() {
        try {
            if (this.conn != null)
                this.conn.close();
        } catch (SQLException e) {
            System.out.println("Não foi possível fechar a conexão com o Banco de Dados.");
        }
    }

    /**
     * Método getter para a conexão estabelecida.
         * @return Connection para o banco de dados.
     */
    public Connection getConn() {
        return this.conn;
    }
}