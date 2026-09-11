package br.com.fiap.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.com.fiap.dao.FilmeDAO;
import br.com.fiap.model.Filme;

/**
 * Implementacao JDBC do FilmeDAO.
 *
 * A conexao NAO e aberta aqui: ela chega pronta pelo construtor, injetada
 * pela DAOFactory a partir do Singleton ConnectionManager.
 */
public class FilmeDAOImpl implements FilmeDAO {

    private final Connection conexao;

    public FilmeDAOImpl(Connection conexao) {
        this.conexao = conexao;
    }

    @Override
    public Filme salvar(Filme filme) {
        String sql = "INSERT INTO TB_FILME "
                + "(id_filme, titulo, diretor, genero, ano_lancamento, duracao_minutos) "
                + "VALUES (SEQ_FILME.NEXTVAL, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conexao.prepareStatement(sql, new String[] { "ID_FILME" })) {
            stmt.setString(1, filme.getTitulo());
            stmt.setString(2, filme.getDiretor());
            stmt.setString(3, filme.getGenero());
            stmt.setInt(4, filme.getAnoLancamento());
            stmt.setInt(5, filme.getDuracaoMinutos());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    filme.setId(rs.getInt(1));
                }
            }

            System.out.println(">> Filme salvo com sucesso! Id gerado: " + filme.getId());
            return filme;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar o filme: " + e.getMessage(), e);
        }
    }

    @Override
    public Filme buscarPorId(int id) {
        String sql = "SELECT id_filme, titulo, diretor, genero, ano_lancamento, duracao_minutos "
                + "FROM TB_FILME WHERE id_filme = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return montarFilme(rs);
                }
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar o filme de id " + id + ": " + e.getMessage(), e);
        }
    }

    @Override
    public List<Filme> listarTodos() {
        String sql = "SELECT id_filme, titulo, diretor, genero, ano_lancamento, duracao_minutos "
                + "FROM TB_FILME ORDER BY id_filme";

        List<Filme> filmes = new ArrayList<>();

        try (Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                filmes.add(montarFilme(rs));
            }
            return filmes;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar os filmes: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean atualizar(Filme filme) {
        String sql = "UPDATE TB_FILME SET titulo = ?, diretor = ?, genero = ?, "
                + "ano_lancamento = ?, duracao_minutos = ? WHERE id_filme = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, filme.getTitulo());
            stmt.setString(2, filme.getDiretor());
            stmt.setString(3, filme.getGenero());
            stmt.setInt(4, filme.getAnoLancamento());
            stmt.setInt(5, filme.getDuracaoMinutos());
            stmt.setInt(6, filme.getId());

            int linhas = stmt.executeUpdate();
            System.out.println(">> Filmes atualizados: " + linhas);
            return linhas > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar o filme: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean deletar(int id) {
        String sql = "DELETE FROM TB_FILME WHERE id_filme = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);

            int linhas = stmt.executeUpdate();
            System.out.println(">> Filmes removidos: " + linhas);
            return linhas > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar o filme de id " + id + ": " + e.getMessage(), e);
        }
    }

    /** Converte a linha atual do ResultSet em um objeto Filme. */
    private Filme montarFilme(ResultSet rs) throws SQLException {
        Filme filme = new Filme();
        filme.setId(rs.getInt("id_filme"));
        filme.setTitulo(rs.getString("titulo"));
        filme.setDiretor(rs.getString("diretor"));
        filme.setGenero(rs.getString("genero"));
        filme.setAnoLancamento(rs.getInt("ano_lancamento"));
        filme.setDuracaoMinutos(rs.getInt("duracao_minutos"));
        return filme;
    }
}
