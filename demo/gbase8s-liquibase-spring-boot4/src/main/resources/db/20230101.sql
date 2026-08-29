--liquibase formatted sql
--changeset admin:1
CREATE TABLE IF NOT EXISTS COMPANY(
   ID             SERIAL         PRIMARY KEY,
   NAME           VARCHAR(40)    NOT NULL,
   AGE            INT            NOT NULL,
   ADDRESS        CHAR(50),
   SALARY         DECIMAL(10,2)
);

insert into COMPANY values('1', 'company', 12, 'Address', 22.3);
