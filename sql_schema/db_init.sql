-- Cleanup existing objects
DROP TABLE IF EXISTS sellers;

DROP TABLE IF EXISTS products;

DROP TABLE IF EXISTS sales;

CREATE TABLE sellers (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    contact_info TEXT
);

CREATE TABLE products (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price DECIMAL(10, 2) NOT NULL
);

CREATE TABLE sales (
    id SERIAL PRIMARY KEY,
    seller_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    sale_date DATE DEFAULT CURRENT_DATE,
    CONSTRAINT fk_seller FOREIGN KEY (seller_id) REFERENCES sellers (id),
    CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES products (id)
);

INSERT INTO sellers (name, contact_info) VALUES
('Alice', 'alice@example.com'),
('Bob', 'bob@example.com');

INSERT INTO products (name, price) VALUES
('Laptop', 1000.00),
('Mouse', 25.00),
('Keyboard', 50.00);

INSERT INTO sales (seller_id, product_id, quantity, sale_date) VALUES
(1, 1, 2, '2024-12-20'),
(2, 2, 5, '2024-12-19');