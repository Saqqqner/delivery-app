package ru.adel.deliveryapp.dao.mapper.impl;

import ru.adel.deliveryapp.dao.mapper.EntityResultSetMapper;
import ru.adel.deliveryapp.entity.Product;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductResultSetMapper implements EntityResultSetMapper<Product> {

    @Override
    public Product map(ResultSet resultSet) throws SQLException {
        Product product = new Product();
        product.setId(resultSet.getLong("id"));
        product.setName(resultSet.getString("name"));
        product.setDescription(resultSet.getString("description"));
        product.setPrice(resultSet.getBigDecimal("price"));
        product.setStock(resultSet.getLong("stock"));
        return product;
    }
}
