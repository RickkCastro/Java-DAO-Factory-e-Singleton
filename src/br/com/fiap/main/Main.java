package br.com.fiap.main;

import java.util.List;

import br.com.fiap.connection.ConnectionManager;
import br.com.fiap.dao.FilmeDAO;
import br.com.fiap.factory.DAOFactory;
import br.com.fiap.model.Filme;

/**
 * Classe principal: demonstra o uso integrado dos tres padroes
 * (Singleton + Factory + DAO) exercitando todo o CRUD de Filme.
 */
public class Main {

    public static void main(String[] args) {

        System.out.println("========= SISTEMA DE CATALOGO DE FILMES =========\n");

        // FACTORY -> cria o DAO ja com a conexao vinda do SINGLETON
        FilmeDAO dao = DAOFactory.getFilmeDAO();

        // ---------- CREATE ----------
        System.out.println("--- 1) CREATE: cadastrando filmes ---");
        Filme matrix = dao.salvar(new Filme("Matrix", "Lana Wachowski", "Ficcao Cientifica", 1999, 136));
        Filme cidadeDeDeus = dao.salvar(new Filme("Cidade de Deus", "Fernando Meirelles", "Drama", 2002, 130));
        dao.salvar(new Filme("A Origem", "Christopher Nolan", "Ficcao Cientifica", 2010, 148));

        // ---------- READ (por id) ----------
        System.out.println("\n--- 2) READ: buscando o filme de id " + matrix.getId() + " ---");
        Filme encontrado = dao.buscarPorId(matrix.getId());
        System.out.println(encontrado != null ? encontrado : "Filme nao encontrado.");

        // ---------- READ (listar todos) ----------
        System.out.println("\n--- 3) READ: listando todos os filmes ---");
        listar(dao.listarTodos());

        // ---------- UPDATE ----------
        System.out.println("\n--- 4) UPDATE: alterando o filme de id " + matrix.getId() + " ---");
        matrix.setTitulo("Matrix Reloaded");
        matrix.setAnoLancamento(2003);
        matrix.setDuracaoMinutos(138);
        dao.atualizar(matrix);
        System.out.println("Depois do update: " + dao.buscarPorId(matrix.getId()));

        // ---------- DELETE ----------
        System.out.println("\n--- 5) DELETE: removendo o filme de id " + cidadeDeDeus.getId() + " ---");
        dao.deletar(cidadeDeDeus.getId());
        System.out.println("Busca apos o delete: " + dao.buscarPorId(cidadeDeDeus.getId()));

        // ---------- Estado final ----------
        System.out.println("\n--- 6) Catalogo final ---");
        listar(dao.listarTodos());

        // Prova de que o Singleton entrega sempre a MESMA instancia
        System.out.println("\n--- Prova do Singleton ---");
        ConnectionManager c1 = ConnectionManager.getInstance();
        ConnectionManager c2 = ConnectionManager.getInstance();
        System.out.println("Mesma instancia do gerenciador? " + (c1 == c2));
        System.out.println("Mesma conexao com o banco?     " + (c1.getConnection() == c2.getConnection()));

        // Encerra a conexao unica da aplicacao
        ConnectionManager.getInstance().fecharConexao();

        System.out.println("\n================ FIM DO PROGRAMA ================");
    }

    private static void listar(List<Filme> filmes) {
        if (filmes.isEmpty()) {
            System.out.println("Nenhum filme cadastrado.");
            return;
        }
        for (Filme filme : filmes) {
            System.out.println(filme);
        }
        System.out.println("Total: " + filmes.size() + " filme(s).");
    }
}
