package ru.adel.deliveryapp.dao.impl;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.adel.deliveryapp.datasourse.jdbc.sessionmanager.SessionManager;
import ru.adel.deliveryapp.datasourse.jdbc.sessionmanager.SessionManagerJdbc;
import ru.adel.deliveryapp.entity.Customer;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@ExtendWith(MockitoExtension.class)
public class CustomerDaoImplTest {
    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest").withInitScript("scripts.sql");


    private SessionManager sessionManager;


    private CustomerDaoImpl customerDao;

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
        customerDao = new CustomerDaoImpl(sessionManager);
    }


    @Test
    void findById_shouldReturnCustomer() throws SQLException {
        Long customerId = saveTestCustomer("TestCustomer", "TestCustomer@mail.ru");
        Optional<Customer> result = customerDao.findById(customerId);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("TestCustomer", result.get().getUsername());
        Assertions.assertEquals("TestCustomer@mail.ru", result.get().getEmail());
    }

    @Test
    void findById_shouldReturnEmptyOptionalForNonExistingCustomer() throws SQLException {
        Optional<Customer> result = customerDao.findById(-1L);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void deleteById_shouldReturnTrueForExistingCustomer() throws SQLException {
        Long customerId = saveTestCustomer("TestCustomer", "TestCustomer@mail.ru");
        boolean result = customerDao.deleteById(customerId);
        Assertions.assertTrue(result);
    }

    @Test
    void deleteById_shouldReturnFalseForNonExistingProduct() throws SQLException {
        // Act
        Assertions.assertThrows(SQLException.class, () -> customerDao.deleteById(-1L));
    }
    @Test
    void findAll_shouldReturnListOfProducts() throws SQLException {
        saveTestCustomer("TestCustomer", "TestCustomer@mail.ru");
        saveTestCustomer("TestCustomer1", "TestCustomer1@mail.ru");
        List<Customer> customers = customerDao.findAll();
        Assertions.assertEquals(2, customers.size());
        Assertions.assertEquals("TestCustomer",customers.get(0).getUsername());
        Assertions.assertEquals("TestCustomer1",customers.get(1).getUsername());
    }


    @Test
    void save_shouldSaveCustomer() throws SQLException {
        Customer customer = new Customer();
        customer.setEmail("UpdateCustomer@mail.ru");
        customer.setUsername("UpdateCustomer");
        Long customerId = customerDao.save(customer);
        Optional<Customer> customerById = customerDao.findById(customerId);
        Assertions.assertTrue(customerById.isPresent());
        Assertions.assertEquals(1L, customerById.get().getId());
        Assertions.assertEquals(customer.getUsername(), customerById.get().getUsername());
        Assertions.assertEquals(customer.getEmail(), customerById.get().getEmail());
    }
    @Test
    void update_shouldUpdateProductDetails() throws SQLException {
        Long customerId = saveTestCustomer("TestCustomer", "TestCustomer@mail.ru");
        Customer updatedCustomer = new Customer();
        updatedCustomer.setId(customerId);
        updatedCustomer.setEmail("UpdateCustomer@mail.ru");
        updatedCustomer.setUsername("UpdateCustomer");
        customerDao.update(updatedCustomer);
        Optional<Customer> result = customerDao.findById(customerId);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(updatedCustomer.getId(), result.get().getId());
        Assertions.assertEquals(updatedCustomer.getUsername(), result.get().getUsername());
        Assertions.assertEquals(updatedCustomer.getEmail(), result.get().getEmail());
    }


    private Long saveTestCustomer(String username, String email) throws SQLException {
        Customer customer = new Customer();
        customer.setUsername(username);
        customer.setEmail(email);
        return customerDao.save(customer);
    }
}
