package ru.adel.deliveryapp.dao;

import ru.adel.deliveryapp.models.Product;

public interface ProductDao extends JdbcRepository<Product,Long> {
    void updateStock(Long stock);
}
