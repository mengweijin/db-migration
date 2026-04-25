--liquibase formatted sql
--changeset admin:5 splitStatements:true
CREATE TABLE IF NOT EXISTS COMPANY_TEST(
   ID             SERIAL         PRIMARY KEY,
   NAME           VARCHAR(40)    NOT NULL,
   AGE            INT            NOT NULL,
   ADDRESS        CHAR(50),
   SALARY         DECIMAL(10,2)
);
