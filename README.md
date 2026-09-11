# Catálogo de Filmes — DAO, Factory e Singleton

Exercício de fixação da disciplina **Domain Driven Design with Java** (FIAP).
Aplicação Java simples, sem frameworks, que integra os três padrões de projeto
estudados na Aula 05 — **Singleton**, **Factory** e **DAO** — em torno da
entidade **Filme**, persistida em uma tabela Oracle via JDBC.

## Entidade escolhida: Filme

A entidade representa um filme de um catálogo. A tabela `TB_FILME` tem 6 colunas,
sendo `id_filme` a chave primária (gerada pela sequence `SEQ_FILME`):

| Coluna            | Tipo          | Descrição                      |
|-------------------|---------------|--------------------------------|
| `id_filme`        | NUMBER(5)     | Chave primária                 |
| `titulo`          | VARCHAR2(100) | Título do filme                |
| `diretor`         | VARCHAR2(80)  | Nome do diretor                |
| `genero`          | VARCHAR2(50)  | Gênero (Drama, Ficção...)      |
| `ano_lancamento`  | NUMBER(4)     | Ano de lançamento              |
| `duracao_minutos` | NUMBER(4)     | Duração em minutos             |

## Estrutura de pacotes

```
src/main/java/br/com/fiap/
├── model/
│   └── Filme.java                  # Entidade: atributos privados + getters/setters
├── connection/
│   └── ConnectionManager.java      # SINGLETON: instância única de conexão
├── dao/
│   ├── FilmeDAO.java               # DAO: interface com o CRUD completo
│   └── impl/
│       └── FilmeDAOImpl.java       # DAO: implementação JDBC das 5 operações
├── factory/
│   └── DAOFactory.java             # FACTORY: cria o DAO injetando a conexão
└── main/
    └── Main.java                   # Demonstração integrada dos três padrões

sql/tb_filme.sql                    # Script de criação da tabela e da sequence
```

### Como os padrões se encaixam

- **Singleton** (`ConnectionManager`): construtor privado, campo estático `instancia`
  e método `getInstance()` sincronizado. A mesma `Connection` é reaproveitada por
  toda a aplicação — a `Main` inclusive imprime a prova de que `getInstance()` e
  `getConnection()` devolvem sempre o mesmo objeto.
- **Factory** (`DAOFactory`): único ponto de criação do DAO. Pega a conexão do
  Singleton e injeta no `FilmeDAOImpl` pelo construtor, devolvendo o tipo
  `FilmeDAO` (a `Main` não conhece a implementação concreta).
- **DAO** (`FilmeDAO` / `FilmeDAOImpl`): isola todo o SQL do restante do sistema.
  Operações implementadas e funcionais:
  - `salvar(Filme)` — **create** (id gerado por `SEQ_FILME` e recuperado com `getGeneratedKeys`)
  - `buscarPorId(int)` — **read** por chave primária
  - `listarTodos()` — **read** de todos os registros
  - `atualizar(Filme)` — **update**
  - `deletar(int)` — **delete**

## Banco de dados

Conexão configurada em `ConnectionManager`:

```java
String url = "jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL";
String user = "RM564560";
String password = "250505";
```

Antes de rodar, execute o script `sql/tb_filme.sql` no SQL Developer para criar
a tabela `TB_FILME` e a sequence `SEQ_FILME`.

## Como executar

### Opção 1 — Maven (baixa o driver automaticamente)

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass=br.com.fiap.main.Main
```

### Opção 2 — javac/java com o ojdbc no classpath

Baixe o `ojdbc11.jar` (ou use o que já vem com o Oracle SQL Developer) e coloque
na pasta `lib/`:

```bash
# Linux/macOS
javac -d bin $(find src/main/java -name "*.java")
java -cp bin:lib/ojdbc11.jar br.com.fiap.main.Main

# Windows
javac -d bin src\main\java\br\com\fiap\**\*.java
java -cp bin;lib\ojdbc11.jar br.com.fiap.main.Main
```

### Opção 3 — Eclipse / IntelliJ

Importe o projeto, adicione o `ojdbc11.jar` ao *Build Path* (ou deixe o Maven
resolver) e rode a classe `br.com.fiap.main.Main`.

## O que a Main demonstra

1. **CREATE** — cadastra três filmes e mostra os ids gerados
2. **READ** — busca um filme por id
3. **READ** — lista todos os filmes da tabela
4. **UPDATE** — altera título, ano e duração de um filme e reconsulta
5. **DELETE** — remove um filme e confirma que a busca passa a retornar vazio
6. Lista o catálogo final e imprime a prova de que o Singleton entrega sempre
   a mesma instância e a mesma conexão, encerrando-a ao final.

---

**Aluno:** RM564560 · **Disciplina:** Domain Driven Design with Java · **Professor:** Evando Borges
