CREATE TABLE IF NOT EXISTS Address (
                                       id SERIAL PRIMARY KEY,
                                       city VARCHAR(255) NOT NULL,
                                       street VARCHAR(255) NOT NULL,
                                       house_number VARCHAR(20) NOT NULL,
                                       apartment_number VARCHAR(20)
);
CREATE TABLE IF NOT EXISTS Customers (
                                         id SERIAL PRIMARY KEY,
                                         username VARCHAR(255) NOT NULL unique,
                                         email VARCHAR(255) NOT NULL unique
);
CREATE TABLE IF NOT EXISTS Products (
                                        id SERIAL PRIMARY KEY,
                                        name VARCHAR(255) NOT NULL unique,
                                        description TEXT,
                                        price DECIMAL(10, 2) NOT NULL,
                                        stock BIGINT NOT NULL
);
CREATE TABLE IF NOT EXISTS Orders  (
                                       id SERIAL PRIMARY KEY,
                                       customer_id BIGINT NOT NULL,
                                       shipping_address_id BIGINT NOT NULL,
                                       status VARCHAR(255) NOT NULL,
                                       total_price DECIMAL(10, 2) NOT NULL,
                                       FOREIGN KEY (customer_id) REFERENCES Customers(id)  on DELETE set NULL ,
                                       FOREIGN KEY (shipping_address_id) REFERENCES Address(id) on DELETE set NULL
);
CREATE TABLE IF NOT EXISTS Order_Items (
                                           id SERIAL PRIMARY KEY,
                                           order_id BIGINT NOT NULL,
                                           product_id BIGINT NOT NULL,
                                           quantity BIGINT NOT NULL,
                                           product_total_price DECIMAL(10, 2) NOT NULL,
                                           FOREIGN KEY (order_id) REFERENCES Orders(id)  on DELETE CASCADE,
                                           FOREIGN KEY (product_id) REFERENCES Products(id) on DELETE CASCADE
);
insert into Customers (username, email) VALUES ('Adel','AdelMail.ru');
insert into Customers (username, email) VALUES ('Adel1','Adel1Mail.ru');
insert into Address (city, street, house_number, apartment_number) VALUES ('Kazan','123','1','2');
insert into Address (city, street, house_number, apartment_number) VALUES ('Kazan1','123','1','2');