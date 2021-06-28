DROP TABLE IF EXISTS users;
CREATE TABLE users
(
  id       INT PRIMARY KEY,
  user_name VARCHAR(64) NOT NULL,
  password VARCHAR(64)
);

DROP TABLE IF EXISTS student;
CREATE TABLE student
(
  id       INT PRIMARY KEY,
  name     VARCHAR(64) NOT NULL
);


DROP TABLE IF EXISTS category;
CREATE TABLE category
(
  category_id   INT PRIMARY KEY,
  category_name VARCHAR(50) NOT NULL,
  description   VARCHAR(100)
);

DROP TABLE IF EXISTS product;
CREATE TABLE product
(
  product_id   INT PRIMARY KEY auto_increment,
  category_id  INT         NOT NULL,
  product_name VARCHAR(50) NOT NULL,
  price        DECIMAL,
  description   VARCHAR(100)
);

DROP TABLE IF EXISTS bug;
CREATE TABLE bug
(
  id       INT PRIMARY KEY auto_increment,
  title    VARCHAR(255) NOT NULL,
  assignTo VARCHAR(255) NOT NULL
);

DELETE
FROM users;
INSERT INTO users (id, user_name, password)
VALUES
  (1, 'usr1', 'bigSecret'),
  (2, 'usr2', 'topSecret');


DELETE
FROM category;
INSERT INTO category (category_id, category_name, description)
VALUES
  (1, 'Beverages', 'test'),
  (2, 'Condiments', 'test'),
  (3, 'Oil', 'test');

DELETE
FROM product;
INSERT INTO product (product_id, category_id, product_name, price, description)
VALUES
  (1, 1, 'Northwind Traders Chai', 18.0000, 'p1'),
  (2, 2, 'Northwind Traders Syrup', 7.5000, 'p2'),
  (3, 2, 'Northwind Traders Cajun Seasoning', 16.5000, 'p3'),
  (4, 3, 'Northwind Traders Olive Oil', 16.5000, 'p4'),
  (5, 3, 'Northwind Traders xxxx Oil', 16.5000, 'p5');

DELETE
FROM bug;
INSERT INTO bug (id, title, assignTo)
VALUES
  (1, 'TEST_1', 'FRANK'),
  (2, 'TEST_2', 'Jack');

DELETE
FROM student;
INSERT INTO student (id, name)
VALUES
  (1, 'Ernest Emerson'),
  (2, 'Rosemary Ernest'),
  (3, 'Prima Ramsden'),
  (4, 'Haley Noyes'),
  (5, 'Mildred Juliet'),
  (6, 'Elvira Daisy'),
  (7, 'Monica Robeson'),
  (8, 'Katherine Eliot'),
  (9, 'Hamiltion Hamlet'),
