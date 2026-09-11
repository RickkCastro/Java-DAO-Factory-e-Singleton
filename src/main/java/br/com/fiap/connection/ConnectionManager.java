package br.com.fiap.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Padrao SINGLETON.
 *
 * Garante que exista uma unica instancia desta classe (e, por consequencia,
 * uma unica conexao com o banco de dados) compartilhada por toda a aplicacao.
 */
public class ConnectionManager {

    private static final String URL = "jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL";
    private static final String USER = "RM564560";
    private static final String PASSWORD = "250505";

    /** Unica instancia da classe (o coracao do Singleton). */
    private static ConnectionManager instancia;

    /** Conexao unica reaproveitada por toda a aplicacao. */
    private Connection conexao;

    /** Construtor privado: ninguem consegue dar "new ConnectionManager()". */
    private ConnectionManager() {
    }

    /** Unico ponto de acesso a instancia. */
    public static synchronized ConnectionManager getInstance() {
        if (instancia == null) {
            instancia = new ConnectionManager();
        }
        return instancia;
    }

    /**
     * Devolve sempre a MESMA conexao. So abre uma nova se ainda nao existir
     * ou se a anterior tiver sido fechada.
     */
    public Connection getConnection() {
        try {
            if (conexao == null || conexao.isClosed()) {
                Class.forName("oracle.jdbc.driver.OracleDriver");
                conexao = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println(">> Conexao com o Oracle aberta com sucesso!");
            }
            return conexao;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver JDBC do Oracle nao encontrado no classpath.", e);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar no banco de dados: " + e.getMessage(), e);
        }
    }

    /** Fecha a conexao unica (chamar apenas ao encerrar a aplicacao). */
    public void fecharConexao() {
        try {
            if (conexao != null && !conexao.isClosed()) {
                conexao.close();
                System.out.println(">> Conexao com o Oracle encerrada.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao fechar a conexao: " + e.getMessage());
        }
    }
}
