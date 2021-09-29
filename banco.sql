create database banco;
use banco;
CREATE USER 'banker'@'localhost' IDENTIFIED BY '1r0nH@ck3r';

GRANT ALL PRIVILEGES ON *.* TO 'banker'@'localhost';

FLUSH PRIVILEGES;

show tables;

select * from accounts;
select * from role;

INSERT INTO user (password, username) VALUES
("$2a$10$VOUPX4qZYPz5h/kC22.eGu6K40DsFQ6s31e8uB.IW2p31cntd9FwG", "accountholder1"),
("$2a$10$N8StR4wEHvV7I0ToJkcEAO5kG58tqomVDT905NGdDLui7VOvmOFFu", "accountholder2"),
("$2a$10$ir18LDcsaNvVEFpacbioVeGpHDrqLz0d7BDxrPy.II.esvV1AGyS2", "admin");

INSERT INTO third_party (hashed_key, name) VALUES
("ghyretns6734klyi8650", "Pizza Hut"),
("ghyrfgts6767klyi1900", "TK Maxx");

INSERT INTO role (name, user_id) VALUES
("ACCOUNT_HOLDER", 59),
("ACCOUNT_HOLDER", 60),
("ADMIN", 61);

INSERT INTO admin (id, name) VALUES
(61, "admin");

select max(amount) from transaction group by transaction_time;
