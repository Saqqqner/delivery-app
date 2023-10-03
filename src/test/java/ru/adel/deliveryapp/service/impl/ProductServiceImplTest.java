package ru.adel.deliveryapp.service.impl;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.adel.deliveryapp.dao.ProductDao;
import ru.adel.deliveryapp.entity.OrderItem;
import ru.adel.deliveryapp.entity.Product;
import ru.adel.deliveryapp.util.DuplicateException;
import ru.adel.deliveryapp.util.ProductNotFoundException;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void getProductById_shouldGetProductById() throws SQLException {
        // Arrange
        Long productId = 1L;
        Product mockProduct = new Product(productId, "Product 1",  "Description 1",BigDecimal.valueOf(20.0), 10L);
        Mockito.when(productDao.findById(productId)).thenReturn(Optional.of(mockProduct));

        // Act
        Product result = productService.getProductById(productId);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(mockProduct, result);
    }

    @Test
    void getProductById_shouldThrowProductNotFoundException() throws SQLException {
        // Arrange
        Long productId = 1L;
        Mockito.when(productDao.findById(productId)).thenReturn(Optional.empty());

        // Act and Assert
        Assertions.assertThrows(ProductNotFoundException.class, () -> productService.getProductById(productId));
    }

    @Test
    void deleteById_shouldDeleteProductById() throws SQLException {
        // Arrange
        Long productId = 1L;
        Mockito.when(productDao.deleteById(productId)).thenReturn(true);

        // Act
        Assertions.assertDoesNotThrow(() -> productService.deleteById(productId));
    }

    @Test
    void deleteById_shouldThrowProductNotFoundException() throws SQLException {
        // Arrange
        Long productId = 1L;
        Mockito.when(productDao.deleteById(productId)).thenReturn(false);

        // Act and Assert
        Assertions.assertThrows(ProductNotFoundException.class, () -> productService.deleteById(productId));
    }

    @Test
    void update_shouldUpdateByProductId() throws SQLException {
        // Arrange
        Long productId = 1L;
        Product mockProduct = new Product(productId, "Product 1",  "Description 1",BigDecimal.valueOf(20.0), 10L);
        Product updateProduct = new Product(productId, "Updated Product 1",  "Updated Description 1",BigDecimal.valueOf(25.0), 15L);

        Mockito.when(productDao.findById(productId)).thenReturn(Optional.of(mockProduct));

        // Act
        Assertions.assertDoesNotThrow(() -> productService.update(productId, updateProduct));

        // Assert
        Mockito.verify(productDao,  Mockito.times(1)).update( Mockito.any(Product.class));
    }

    @Test
    void  update_shouldThrowProductNotFoundException() throws SQLException {
        // Arrange
        Long productId = 1L;
        Product updateProduct = new Product(productId, "Updated Product 1",  "Updated Description 1",BigDecimal.valueOf(25.0), 15L);

        Mockito.when(productDao.findById(productId)).thenReturn(Optional.empty());

        // Act and Assert
        Assertions.assertThrows(ProductNotFoundException.class, () -> productService.update(productId, updateProduct));
        Mockito.verify(productDao,  Mockito.never()).update( Mockito.any(Product.class));
    }

    @Test
    void findAll_shouldFindAllProducts() throws SQLException {
        // Arrange
        List<Product> mockProducts = new ArrayList<>();
        mockProducts.add(new Product(1L, "Product 1",  "Description 1",BigDecimal.valueOf(20.0), 10L));
        mockProducts.add(new Product(2L, "Product 2",  "Description 2",BigDecimal.valueOf(30.0), 15L));

        Mockito.when(productDao.findAll()).thenReturn(mockProducts);

        // Act
        List<Product> result = productService.findAll();

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(mockProducts.size(), result.size());
        Assertions.assertEquals(mockProducts, result);
    }

    @Test
    void save_shouldSaveProduct() throws SQLException {
        // Arrange
        Product newProduct = new Product(null, "New Product", "New Description", BigDecimal.valueOf(15.0), 5L);

        Mockito.when(productDao.existsByName(newProduct.getName())).thenReturn(false);

        // Act
        Assertions.assertDoesNotThrow(() -> productService.save(newProduct));

        // Assert
        Mockito.verify(productDao,  Mockito.times(1)).save( Mockito.any(Product.class));
    }

    @Test
    void saveByDuplicateName_shouldThrowDuplicateException() throws SQLException {
        // Arrange
        Product newProduct = new Product(null, "Product 1", "Description 1", BigDecimal.valueOf(20.0), 10L);

        Mockito.when(productDao.existsByName(newProduct.getName())).thenReturn(true);

        // Act and Assert
        Assertions.assertThrows(DuplicateException.class, () -> productService.save(newProduct));
        Mockito.verify(productDao,  Mockito.never()).save( Mockito.any(Product.class));
    }

    @Test
    void updateProductStock_shouldUpdateProductStock() throws SQLException {
        // Arrange
        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(new OrderItem(1L,null, new Product(1L, "Product 1",  "Description 1",BigDecimal.valueOf(20.0), 10L), 2L,BigDecimal.TEN));

        // Act
        Assertions.assertDoesNotThrow(() -> productService.updateProductStock(orderItems));

        // Assert
        Mockito.verify(productDao,  Mockito.times(1)).updateStockById( Mockito.anyLong(),  Mockito.anyLong());
    }
}
