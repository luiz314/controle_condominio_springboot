
# Condomínio Final

## PostgreSQL

CREATE DATABASE condominio;

CREATE USER condominio WITH PASSWORD '123456';

GRANT ALL PRIVILEGES ON DATABASE condominio TO condominio;

## Rodar

mvn spring-boot:run

## Primeiro usuário

http://localhost:8080/usuarios/novo

## Login

http://localhost:8080/login
