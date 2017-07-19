IF OBJECT_ID('users', 'U') IS NOT NULL
  DROP TABLE users;
CREATE TABLE users (
  id          INT PRIMARY KEY,
  username    VARCHAR(64) NOT NULL,
  password    VARCHAR(64)
);

IF OBJECT_ID('category', 'U') IS NOT NULL
  DROP TABLE category;
CREATE TABLE category (
  category_id   INT PRIMARY KEY,
  category_name VARCHAR (50) NOT NULL,
  description   VARCHAR (100)
);

IF OBJECT_ID('product', 'U') IS NOT NULL
  DROP TABLE product;
CREATE TABLE product (
  product_id    INT PRIMARY KEY,
  category_id   INT NOT NULL,
  product_name  VARCHAR (50) NOT NULL,
  price         DECIMAL
);

DELETE FROM users;
INSERT INTO users (id, username, password) VALUES
  (1, 'usr1', 'bigSecret'),
  (2, 'usr2', 'topSecret');


DELETE FROM category;
INSERT INTO category (category_id, category_name, description) VALUES
  (1, 'Beverages', 'test'),
  (2, 'Condiments', 'test'),
  (3, 'Oil', 'test');

DELETE FROM product;
INSERT INTO product (product_id, category_id, product_name, price) VALUES
  (1, 1, 'Northwind Traders Chai', 18.0000),
  (2, 2, 'Northwind Traders Syrup', 7.5000),
  (3, 2, 'Northwind Traders Cajun Seasoning', 16.5000),
  (4, 3, 'Northwind Traders Olive Oil', 16.5000);
