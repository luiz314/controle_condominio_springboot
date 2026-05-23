
AJUSTES IMPLEMENTADOS

1. Controle principal agora mostra:
- STATUS DENTRO/FORA
- botão LOG

2. Histórico removido da tela principal

3. Nova tela:
- /controle/log/{id}

4. Nada do restante do sistema foi removido



5. Criação de tabelas no banco:

CREATE DATABASE controle_condominio;
USE controle_condominio;

-- =========================
-- TABELA DE USUÁRIOS
-- =========================
CREATE TABLE usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

-- =========================
-- TABELA DE PESSOAS
-- =========================
CREATE TABLE pessoas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255),
    documento VARCHAR(255),
    telefone VARCHAR(255)
);

-- =========================
-- TABELA DE ACESSOS
-- =========================
CREATE TABLE acessos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    pessoa_id BIGINT NOT NULL,

    entrada DATETIME,
    saida DATETIME,

    CONSTRAINT fk_acesso_pessoa
        FOREIGN KEY (pessoa_id)
        REFERENCES pessoas(id)
        ON DELETE CASCADE
);


6. Criação de usuário/admin:
INSERT INTO usuarios (username, password)
VALUES (
    'admin',
    '$2a$10$DowJonesExampleHash123456789'
);


