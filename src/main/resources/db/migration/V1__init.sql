CREATE SCHEMA CRM;


CREATE TABLE crm."customer" (
    id bigserial PRIMARY KEY,
    name VARCHAR (50) NOT NULL,
    surname VARCHAR (100) NOT NULL,
    picture_id integer,
    editor_id uuid NOT NULL
);