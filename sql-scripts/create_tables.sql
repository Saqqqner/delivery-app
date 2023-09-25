CREATE TABLE IF NOT EXISTS Address (
                                       id SERIAL PRIMARY KEY,
                                       city VARCHAR(255) NOT NULL,
    street VARCHAR(255) NOT NULL,
    house_number VARCHAR(20) NOT NULL,
    apartment_number VARCHAR(20)
    );
CREATE TABLE IF NOT EXISTS Customer (
                                        id SERIAL PRIMARY KEY,
                                        username VARCHAR(255) NOT NULL unique,
    email VARCHAR(255) NOT NULL unique
    );
CREATE TABLE IF NOT EXISTS Product (
                                       id SERIAL PRIMARY KEY,
                                       name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    stock BIGINT NOT NULL
    );
CREATE TABLE IF NOT EXISTS Orders  (
                                     id SERIAL PRIMARY KEY,
                                     customer_id BIGINT NOT NULL,
                                     shippingAddress_id BIGINT NOT NULL,
                                     total_price DECIMAL(10, 2) NOT NULL,
                                     FOREIGN KEY (customer_id) REFERENCES Customer(id),
    FOREIGN KEY (shippingAddress_id) REFERENCES Address(id)
    );
CREATE TABLE IF NOT EXISTS OrderItem (
                                         id SERIAL PRIMARY KEY,
                                         order_id BIGINT NOT NULL,
                                         product_id BIGINT NOT NULL,
                                         quantity BIGINT NOT NULL,
                                         total_price DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES Orders(id),
    FOREIGN KEY (product_id) REFERENCES Product(id)
    );