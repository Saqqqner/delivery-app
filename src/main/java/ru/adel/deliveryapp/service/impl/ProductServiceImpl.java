package ru.adel.deliveryapp.service.impl;

import ru.adel.deliveryapp.dao.ProductDao;
import ru.adel.deliveryapp.entity.OrderItem;
import ru.adel.deliveryapp.entity.Product;
import ru.adel.deliveryapp.service.ProductService;
import ru.adel.deliveryapp.util.DuplicateException;
import ru.adel.deliveryapp.util.ProductNotFoundException;

import java.sql.SQLException;
import java.util.List;

public class ProductServiceImpl implements ProductService {
    private static final String PRODUCT_NOT_FOUND_MSG = "Customer not found with ID: ";
    private static final String PRODUCT_DUPLICATE_NAME_MSG = "Customer with the provided username already exists";

    private final ProductDao productDao;


    public ProductServiceImpl(ProductDao productDao) {
        this.productDao = productDao;

    }

    @Override
    public Product getProductById(Long id) throws SQLException {
        return productDao.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND_MSG + id));
    }


    @Override
    public void deleteById(Long id) throws SQLException {
        if (!productDao.deleteById(id)) {
            throw new ProductNotFoundException(PRODUCT_NOT_FOUND_MSG + id);

        }
    }

    @Override
    public void update(Long id, Product product) throws SQLException {
        Product existingProduct = productDao.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND_MSG + id));
        checkAndUpdate(existingProduct, product);
        productDao.update(product);
    }


    @Override
    public List<Product> findAll() throws SQLException {
        return productDao.findAll();
    }

    @Override
    public void save(Product product) throws SQLException {
        if (productDao.existsByName(product.getName())) {
            throw new DuplicateException(PRODUCT_DUPLICATE_NAME_MSG);
        }
        productDao.save(product);
    }

    @Override
    public void updateProductStock(List<OrderItem> orderItems) throws SQLException {
        for (OrderItem orderItem : orderItems) {
            Long productId = orderItem.getProduct().getId();
            Long newStock = orderItem.getProduct().getStock() - orderItem.getQuantity();
            productDao.updateStockById(productId, newStock);
        }
    }

    private void checkAndUpdate(Product existingProduct, Product product) throws SQLException {
        if (!existingProduct.getName().equals(product.getName())) {
            boolean nameExists = productDao.existsByName(product.getName());
            if (nameExists) {
                throw new DuplicateException(PRODUCT_DUPLICATE_NAME_MSG);
            }
        }
        existingProduct.setName(product.getName());
        if (product.getPrice() != null) {
            existingProduct.setPrice(product.getPrice());
        }
        if (product.getDescription() != null) {
            existingProduct.setDescription(product.getDescription());
        }
        if (product.getStock() != null) {
            existingProduct.setStock(product.getStock());
        }


    }
}
