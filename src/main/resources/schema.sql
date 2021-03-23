-- Creation du schema du projet
DROP TABLE IF EXISTS products;
CREATE TABLE products(id INTEGER PRIMARY KEY AUTO_INCREMENT, name VARCHAR, price NUMERIC, weight NUMERIC);