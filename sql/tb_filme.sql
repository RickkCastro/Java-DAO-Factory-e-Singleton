-- =====================================================================
-- Script de criacao da tabela e da sequence usadas pelo projeto.
-- Banco: Oracle (oracle.fiap.com.br)
-- Executar no SQL Developer conectado com o usuario da aplicacao.
-- =====================================================================

-- Descomente as duas linhas abaixo para recriar o objeto do zero:
-- DROP TABLE TB_FILME;
-- DROP SEQUENCE SEQ_FILME;

CREATE TABLE TB_FILME (
    id_filme         NUMBER(5)       NOT NULL,
    titulo           VARCHAR2(100)   NOT NULL,
    diretor          VARCHAR2(80)    NOT NULL,
    genero           VARCHAR2(50)    NOT NULL,
    ano_lancamento   NUMBER(4)       NOT NULL,
    duracao_minutos  NUMBER(4)       NOT NULL,
    CONSTRAINT pk_filme PRIMARY KEY (id_filme)
);

CREATE SEQUENCE SEQ_FILME
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

COMMIT;

-- Consulta de conferencia:
-- SELECT * FROM TB_FILME ORDER BY id_filme;
