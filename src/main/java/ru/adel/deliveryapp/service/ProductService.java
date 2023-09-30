package ru.adel.deliveryapp.service;

import ru.adel.deliveryapp.entity.OrderItem;
import ru.adel.deliveryapp.entity.Product;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

public interface ProductService extends Serializable {

    Product getProductById(Long id) throws SQLException;

    void deleteById(Long id) throws SQLException;

    void update(Long id, Product product) throws SQLException;

    List<Product> findAll() throws SQLException;

    void save(Product product) throws SQLException;
    void updateProductStock(List<OrderItem> orderItems) throws SQLException;
}
