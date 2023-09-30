package ru.adel.deliveryapp.dao;

import ru.adel.deliveryapp.entity.Product;

import java.io.Serializable;
import java.sql.SQLException;

public interface ProductDao extends JdbcRepository<Product,Long> , Serializable {
    boolean existsByName(String name) throws SQLException;
    void updateStockById(Long id, Long newStock) throws SQLException;
}
