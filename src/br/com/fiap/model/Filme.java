package br.com.fiap.model;

/**
 * Entidade de dominio que representa um filme do catalogo.
 * Espelha a tabela TB_FILME do banco de dados.
 */
public class Filme {

    private int id;
    private String titulo;
    private String diretor;
    private String genero;
    private int anoLancamento;
    private int duracaoMinutos;

    public Filme() {
    }

    public Filme(String titulo, String diretor, String genero, int anoLancamento, int duracaoMinutos) {
        this.titulo = titulo;
        this.diretor = diretor;
        this.genero = genero;
        this.anoLancamento = anoLancamento;
        this.duracaoMinutos = duracaoMinutos;
    }

    public Filme(int id, String titulo, String diretor, String genero, int anoLancamento, int duracaoMinutos) {
        this.id = id;
        this.titulo = titulo;
        this.diretor = diretor;
        this.genero = genero;
        this.anoLancamento = anoLancamento;
        this.duracaoMinutos = duracaoMinutos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDiretor() {
        return diretor;
    }

    public void setDiretor(String diretor) {
        this.diretor = diretor;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public int getAnoLancamento() {
        return anoLancamento;
    }

    public void setAnoLancamento(int anoLancamento) {
        this.anoLancamento = anoLancamento;
    }

    public int getDuracaoMinutos() {
        return duracaoMinutos;
    }

    public void setDuracaoMinutos(int duracaoMinutos) {
        this.duracaoMinutos = duracaoMinutos;
    }

    @Override
    public String toString() {
        return "Filme [id=" + id
                + ", titulo=" + titulo
                + ", diretor=" + diretor
                + ", genero=" + genero
                + ", anoLancamento=" + anoLancamento
                + ", duracaoMinutos=" + duracaoMinutos + "]";
    }
}
