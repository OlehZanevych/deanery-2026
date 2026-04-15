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

CREATE TABLE specialities
(
    id            BIGSERIAL PRIMARY KEY,
    name          VARCHAR(128) NOT NULL UNIQUE,
    code          VARCHAR(16)  NOT NULL UNIQUE,
    department_id BIGINT REFERENCES departments (id) NOT NULL,
    info          TEXT
);

CREATE TABLE curricula
(
    id             BIGSERIAL PRIMARY KEY,
    name           VARCHAR(128) NOT NULL,
    speciality_id  BIGINT REFERENCES specialities (id) NOT NULL,
    year           INTEGER      NOT NULL,
    degree         VARCHAR(32)  NOT NULL,
    duration_years INTEGER      NOT NULL DEFAULT 4,
    info           TEXT
);

CREATE TABLE academic_groups
(
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(32) NOT NULL UNIQUE,
    curriculum_id   BIGINT REFERENCES curricula (id) NOT NULL,
    enrollment_year INTEGER     NOT NULL,
    info            TEXT
);

CREATE TABLE lecturers
(
    id            BIGSERIAL PRIMARY KEY,
    first_name    VARCHAR(64) NOT NULL,
    middle_name   VARCHAR(64) NOT NULL,
    last_name     VARCHAR(64) NOT NULL,
    email         VARCHAR(64) NOT NULL,
    phone         VARCHAR(32) NOT NULL,
    title         VARCHAR(64) NOT NULL,
    department_id BIGINT REFERENCES departments (id) NOT NULL,
    info          TEXT
);

CREATE TABLE courses
(
    id            BIGSERIAL PRIMARY KEY,
    name          VARCHAR(128) NOT NULL,
    credits       INTEGER      NOT NULL,
    hours         INTEGER      NOT NULL,
    semester      INTEGER      NOT NULL,
    curriculum_id BIGINT REFERENCES curricula (id)  NOT NULL,
    lecturer_id   BIGINT REFERENCES lecturers (id),
    info          TEXT
);