DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

CREATE TABLE faculties
(
    id      BIGSERIAL PRIMARY KEY,
    name    VARCHAR(64)  NOT NULL UNIQUE,
    website VARCHAR(24)  NOT NULL,
    email   VARCHAR(32)  NOT NULL,
    phone   VARCHAR(32)  NOT NULL,
    address VARCHAR(128) NOT NULL,
    info    TEXT
);

CREATE TABLE departments
(
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(64)                      NOT NULL UNIQUE,
    faculty_id BIGINT REFERENCES faculties (id) NOT NULL,
    email      VARCHAR(32)                      NOT NULL,
    phone      VARCHAR(32)                      NOT NULL,
    info       TEXT
);

CREATE TABLE users
(
    id            BIGSERIAL PRIMARY KEY,
    username      VARCHAR(32) UNIQUE NOT NULL,
    password_hash VARCHAR(128)       NOT NULL,
    is_admin      BOOLEAN            NOT NULL,
    first_name    VARCHAR(64)        NOT NULL,
    middle_name   VARCHAR(64)        NOT NULL,
    last_name     VARCHAR(64)        NOT NULL,
    phone         VARCHAR(16)        NOT NULL,
    email         VARCHAR(32)        NOT NULL,
    info          TEXT
);