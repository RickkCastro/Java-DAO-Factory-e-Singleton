package br.com.fiap.dao;

import java.util.List;

import br.com.fiap.model.Filme;

/**
 * Padrao DAO (Data Access Object).
 *
 * Contrato de acesso a dados da entidade Filme, cobrindo o CRUD completo.
 */
public interface FilmeDAO {

    /** CREATE: grava um novo filme e devolve o objeto com o id gerado. */
    Filme salvar(Filme filme);

    /** READ: busca um unico filme pela chave primaria (null se nao existir). */
    Filme buscarPorId(int id);

    /** READ: lista todos os filmes cadastrados. */
    List<Filme> listarTodos();

    /** UPDATE: atualiza os dados de um filme existente. */
    boolean atualizar(Filme filme);

    /** DELETE: remove um filme pelo id. */
    boolean deletar(int id);
}
