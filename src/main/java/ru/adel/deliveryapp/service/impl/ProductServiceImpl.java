package ru.adel.deliveryapp.service.impl;

import ru.adel.deliveryapp.dao.ProductDao;
import ru.adel.deliveryapp.dao.mapper.ProductDTOMapper;
import ru.adel.deliveryapp.dto.ProductByStockDTO;
import ru.adel.deliveryapp.dto.ProductDTO;
import ru.adel.deliveryapp.models.Product;
import ru.adel.deliveryapp.service.ProductService;
import ru.adel.deliveryapp.util.DuplicateException;
import ru.adel.deliveryapp.util.ProductNotFoundException;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class ProductServiceImpl implements ProductService {
    private static final String PRODUCT_NOT_FOUND_MSG = "Customer not found with ID: ";
    private static final String PRODUCT_DUPLICATE_NAME_MSG = "Customer with the provided username already exists";

    private final ProductDao productDao;
    private final ProductDTOMapper productDTOMapper;

    public ProductServiceImpl(ProductDao productDao) {
        this.productDao = productDao;
        productDTOMapper = ProductDTOMapper.INSTANCE;
    }

    @Override
    public ProductDTO getProductById(Long id) throws SQLException {
        return productDao.findById(id)
                .map(productDTOMapper::productToProductDTO)
                .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND_MSG + id));
    }


    @Override
    public void deleteById(Long id) throws SQLException {
        productDao.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND_MSG + id));
        productDao.deleteById(id);

    }

    @Override
    public void update(Long id, ProductByStockDTO productDTO) throws SQLException {
        Product product = productDao.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND_MSG + id));
        checkAndUpdate(product, productDTO);
        productDao.update(product);
    }


    @Override
    public List<ProductDTO> findAll() throws SQLException {
        return productDao.findAll()
                .stream()
                .map(productDTOMapper::productToProductDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void save(ProductByStockDTO productDTO) throws SQLException {
        if (productDao.existsByName(productDTO.getName())) {
            throw new DuplicateException(PRODUCT_DUPLICATE_NAME_MSG);
        }
        Product product = productDTOMapper.productDTOToProduct(productDTO);
        productDao.save(product);
    }

    private void checkAndUpdate(Product existingProduct, ProductByStockDTO productDTO) throws SQLException {
        if (!existingProduct.getName().equals(productDTO.getName())) {
            boolean nameExists = productDao.existsByName(productDTO.getName());
            if (nameExists) {
                throw new DuplicateException(PRODUCT_DUPLICATE_NAME_MSG);
            }
        }
        existingProduct.setName(productDTO.getName());
        if (productDTO.getPrice() != null) {
            existingProduct.setPrice(productDTO.getPrice());
        }
        if (productDTO.getDescription() != null) {
            existingProduct.setDescription(productDTO.getDescription());
        }
        if (productDTO.getStock() != null) {
            existingProduct.setStock(productDTO.getStock());
        }


    }
}
