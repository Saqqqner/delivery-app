package ru.adel.deliveryapp.dao.impl;


import org.junit.jupiter.api.*;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.adel.deliveryapp.datasourse.jdbc.sessionmanager.SessionManager;
import ru.adel.deliveryapp.datasourse.jdbc.sessionmanager.SessionManagerJdbc;
import ru.adel.deliveryapp.entity.Product;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Testcontainers
class ProductDaoImplTest {

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest").withInitScript("scripts.sql");
    private SessionManager sessionManager;
    private ProductDaoImpl productDao;
    private DataSource dataSource;

    @BeforeAll
    public static void beforeAll() {
        System.setProperty("jdbc.url", postgresContainer.getJdbcUrl());
        System.setProperty("jdbc.username", postgresContainer.getUsername());
        System.setProperty("jdbc.password", postgresContainer.getPassword());
        postgresContainer.start();
        postgresContainer.followOutput(new Slf4jLogConsumer(LoggerFactory.getLogger(AddressDaoImplTest.class)));
    }

    @AfterAll
    public static void afterAll() {
        postgresContainer.stop();
    }

    @BeforeEach
    void setUp() throws SQLException {
        dataSource = CustomDataSourceConfig.getHikariDataSource();
        sessionManager = new SessionManagerJdbc(dataSource);
        productDao = new ProductDaoImpl(sessionManager);
    }


    @Test
    void findById_shouldReturnProduct() throws SQLException {
        // Arrange
        Long productId = saveTestProduct("TestProduct", "TestDescription", BigDecimal.TEN, 100L);

        // Act
        Optional<Product> result = productDao.findById(productId);

        // Assert
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("TestProduct", result.get().getName());
        Assertions.assertEquals("TestDescription", result.get().getDescription());
    }

    @Test
    void findById_shouldReturnEmptyOptionalForNonExistingProduct() throws SQLException {
        // Act
        Optional<Product> result = productDao.findById(-1L);

        // Assert
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void deleteById_shouldReturnTrueForExistingProduct() throws SQLException {
        // Arrange
        Long productId = saveTestProduct("TestProduct", "TestDescription", BigDecimal.TEN, 100L);

        // Act
        boolean result = productDao.deleteById(productId);

        // Assert
        Assertions.assertTrue(result);
    }

    @Test
    void save_shouldSaveProduct() throws SQLException {
        Product product = new Product();
        product.setName("Product");
        product.setDescription("Description");
        product.setPrice(BigDecimal.valueOf(15));
        product.setStock(120L);
        Long productId = productDao.save(product);
        Optional<Product> productById = productDao.findById(productId);
        Assertions.assertTrue(productById.isPresent());
        Assertions.assertEquals(1L, productById.get().getId());
        Assertions.assertEquals(product.getName(), productById.get().getName());
        Assertions.assertEquals(product.getDescription(), productById.get().getDescription());
        Assertions.assertEquals(0, product.getPrice().compareTo(productById.get().getPrice()));
        Assertions.assertEquals(product.getStock(), productById.get().getStock());

    }

    @Test
    void deleteById_shouldReturnFalseForNonExistingProduct() throws SQLException {
        Assertions.assertThrows(SQLException.class, () -> productDao.deleteById(-1L));
    }

    @Test
    void findAll_shouldReturnListOfProducts() throws SQLException {
        saveTestProduct("Product1", "Description1", BigDecimal.valueOf(20), 50L);
        saveTestProduct("Product2", "Description2", BigDecimal.valueOf(30), 70L);
        List<Product> products = productDao.findAll();
        Assertions.assertEquals(2, products.size());

    }

    @Test
    void update_shouldUpdateProductDetails() throws SQLException {
        Long productId = saveTestProduct("TestProduct", "TestDescription", BigDecimal.TEN, 100L);
        Product updatedProduct = new Product();
        updatedProduct.setId(productId);
        updatedProduct.setName("UpdatedProduct");
        updatedProduct.setDescription("UpdatedDescription");
        updatedProduct.setPrice(BigDecimal.valueOf(15));
        updatedProduct.setStock(120L);
        productDao.update(updatedProduct);
        Optional<Product> result = productDao.findById(productId);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(updatedProduct.getId(), result.get().getId());
        Assertions.assertEquals(updatedProduct.getName(), result.get().getName());
        Assertions.assertEquals(updatedProduct.getDescription(), result.get().getDescription());
        Assertions.assertEquals(0, updatedProduct.getPrice().compareTo(result.get().getPrice()));
        Assertions.assertEquals(updatedProduct.getStock(), result.get().getStock());
    }

    @Test
    void updateStockById_shouldUpdateStockForExistingProduct() throws SQLException {
        Long productId = saveTestProduct("TestProduct", "TestDescription", BigDecimal.TEN, 100L);
        productDao.updateStockById(productId, 150L);
        Optional<Product> result = productDao.findById(productId);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(150L, result.get().getStock());
    }

    @Test
    void updateStockById_shouldThrowExceptionForNonExistingProduct() {
        Assertions.assertThrows(SQLException.class, () -> productDao.updateStockById(-1L, 150L));
    }


    private Long saveTestProduct(String name, String description, BigDecimal price, Long stock) throws SQLException {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStock(stock);

        return productDao.save(product);
    }
}
