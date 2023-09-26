package ru.adel.deliveryapp.service;

import ru.adel.deliveryapp.dto.ProductByStockDTO;
import ru.adel.deliveryapp.dto.ProductDTO;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

public interface ProductService extends Serializable {

    ProductDTO getProductById(Long id) throws SQLException;

    void deleteById(Long id) throws SQLException;

    void update(Long id, ProductByStockDTO productDTO) throws SQLException;

    List<ProductDTO> findAll() throws SQLException;

    void save(ProductByStockDTO productDTO) throws SQLException;
}
