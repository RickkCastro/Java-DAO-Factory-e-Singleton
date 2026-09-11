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

Projeto Java puro (sem Maven, sem frameworks) — apenas `src/` e o driver em `lib/`.

```
src/br/com/fiap/
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

lib/ojdbc11.jar                     # Driver JDBC do Oracle (já incluído)
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

O driver **`lib/ojdbc11.jar` já vem no projeto**, então basta colocá-lo no
classpath. Não é necessário Maven.

### Eclipse

1. `File > Import > Existing Projects into Workspace` (ou `File > New > Java Project`
   apontando para esta pasta).
2. Clique com o botão direito no projeto → `Build Path > Configure Build Path...`
   → aba `Libraries` → `Add JARs...` → selecione `lib/ojdbc11.jar` → `Apply and Close`.
3. Botão direito em `Main.java` → `Run As > Java Application`.

### IntelliJ IDEA

1. `Open` nesta pasta e marque `src` como *Sources Root* (botão direito → `Mark Directory as`).
2. `File > Project Structure > Libraries > +  > Java` → selecione `lib/ojdbc11.jar`.
3. Rode a classe `Main`.

### VS Code

Com o *Extension Pack for Java* instalado, o `src/` e o `lib/*.jar` são detectados
automaticamente — é só abrir a pasta e clicar em `Run` acima do `main`.

### Terminal (javac / java)

```bash
# Linux / macOS
javac -encoding UTF-8 -d bin $(find src -name "*.java")
java -cp bin:lib/ojdbc11.jar br.com.fiap.main.Main
```

```bat
:: Windows (cmd)
dir /s /b src\*.java > sources.txt
javac -encoding UTF-8 -d bin @sources.txt
java -cp bin;lib\ojdbc11.jar br.com.fiap.main.Main
```

### Se der erro ao rodar

| Erro | Causa / solução |
|---|---|
| `ClassNotFoundException: oracle.jdbc.driver.OracleDriver` | O `ojdbc11.jar` não está no classpath — refaça o passo do *Build Path* / `-cp`. |
| `ORA-00942: table or view does not exist` | A tabela não foi criada — rode o `sql/tb_filme.sql`. |
| `ORA-01017: invalid username/password` | Usuário/senha em `ConnectionManager` diferentes do seu RM. |
| `IO Error: The Network Adapter could not establish the connection` (ou trava) | O `oracle.fiap.com.br` só é acessível da rede da FIAP ou pela VPN. |
| `ORA-00001: unique constraint (PK_FILME)` | A sequence está atrasada em relação à tabela — recrie a sequence. |

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
