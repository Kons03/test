CREATE TABLE IF NOT EXISTS client (
	id int8 NOT NULL,
	date_of_birth date NOT NULL,
	"name" varchar(255) NOT NULL,
	"password" varchar(500) NOT NULL,
	CONSTRAINT client_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS email_data (
	id int8 NOT NULL,
	mail varchar(255) NULL,
	client_id int8 NULL,
	CONSTRAINT email_data_pkey PRIMARY KEY (id)
);

ALTER TABLE email_data ADD CONSTRAINT email_client FOREIGN KEY (client_id) REFERENCES client(id);

CREATE TABLE IF NOT EXISTS phone_data (
	id int8 NOT NULL,
	phone varchar(255) NULL,
	client_id int8 NULL,
	CONSTRAINT phone_data_pkey PRIMARY KEY (id)
);

ALTER TABLE phone_data ADD CONSTRAINT phone_client FOREIGN KEY (client_id) REFERENCES client(id);

CREATE TABLE IF NOT EXISTS account (
	id int8 NOT NULL,
	balance numeric(19,2) NULL,
	client_id int8 NULL,
	CONSTRAINT account_pkey PRIMARY KEY (id)
);

ALTER TABLE account ADD CONSTRAINT account_client FOREIGN KEY (client_id) REFERENCES client(id);

CREATE SEQUENCE IF NOT EXISTS MY_CUSTOM_SEQ MINVALUE 1 START WITH 1 INCREMENT BY 1;