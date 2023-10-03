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
                                        name VARCHAR(255) NOT NULL,
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
                                       FOREIGN KEY (customer_id) REFERENCES Customers(id),
                                       FOREIGN KEY (shipping_address_id) REFERENCES Address(id)
);
CREATE TABLE IF NOT EXISTS OrderItems (
                                          id SERIAL PRIMARY KEY,
                                          order_id BIGINT NOT NULL,
                                          product_id BIGINT NOT NULL,
                                          quantity BIGINT NOT NULL,
                                          product_total_price DECIMAL(10, 2) NOT NULL,
                                          FOREIGN KEY (order_id) REFERENCES Orders(id),
                                          FOREIGN KEY (product_id) REFERENCES Products(id)
);
insert into Customers (username, email) VALUES ('Adel','AdelMail.ru');
insert into Customers (username, email) VALUES ('Adel1','Adel1Mail.ru');
insert into Address (city, street, house_number, apartment_number) VALUES ('Kazan','123','1','2');
insert into Address (city, street, house_number, apartment_number) VALUES ('Kazan1','123','1','2');
insert into Orders (customer_id, shipping_address_id, status, total_price) VALUES (1,1,'Delivered',18.00);
insert into Orders (customer_id, shipping_address_id, status, total_price) VALUES (2,2,'Delivered',12.00);
insert into Products (name, description, price, stock) VALUES ('Potato','Картошка',2.0,300);
insert into Products (name, description, price, stock) VALUES ('Orange','Апельсин',4.0,400);