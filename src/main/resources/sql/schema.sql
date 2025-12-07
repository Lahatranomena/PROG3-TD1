\c product_management_db

CREATE TABLE IF NOT EXISTS product (
  id SERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  price NUMERIC(12,2) NOT NULL,
  creation_datetime TIMESTAMP NOT NULL DEFAULT now()
);


CREATE TABLE Product_category (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    product_id INT REFERENCES Product(id)
);