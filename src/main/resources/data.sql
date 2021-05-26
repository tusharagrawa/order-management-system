DROP TABLE IF EXISTS product;

CREATE TABLE product (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  product_name VARCHAR(250) NOT NULL,
  price DOUBLE NULL,
  quantity INT DEFAULT NULL
);

INSERT INTO product (product_name, price, quantity) VALUES
  ('Mint', 50.0, 20),
  ('TurboTax', 25.3, 10),
  ('QuickBooks', 110.7, 15),
  ('ProConnect', 75.0, 20);