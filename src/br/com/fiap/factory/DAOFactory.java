package br.com.fiap.factory;

import java.sql.Connection;

import br.com.fiap.connection.ConnectionManager;
import br.com.fiap.dao.FilmeDAO;
import br.com.fiap.dao.impl.FilmeDAOImpl;

/**
 * Padrao FACTORY.
 *
 * Centraliza a criacao dos DAOs da aplicacao e injeta neles a conexao
 * obtida do Singleton (ConnectionManager). A classe Main nunca precisa
 * saber qual e a implementacao concreta do DAO nem de onde vem a conexao.
 */
public class DAOFactory {

    private DAOFactory() {
    }

    public static FilmeDAO getFilmeDAO() {
        Connection conexao = ConnectionManager.getInstance().getConnection();
        return new FilmeDAOImpl(conexao);
    }
}
