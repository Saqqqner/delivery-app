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
import ru.adel.deliveryapp.entity.Address;
import ru.adel.deliveryapp.entity.Customer;
import ru.adel.deliveryapp.entity.Order;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Testcontainers
@ExtendWith(MockitoExtension.class)
class OrderDaoImplTest {
    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest").withInitScript("scripts-order.sql");

    private SessionManager sessionManager;
    private OrderDaoImpl orderDao;
    private DataSource dataSource;
    private Customer customer;
    private Address address;

    @BeforeAll
    public static void beforeAll() {
        System.setProperty("jdbc.url", postgresContainer.getJdbcUrl());
        System.setProperty("jdbc.username", postgresContainer.getUsername());
        System.setProperty("jdbc.password", postgresContainer.getPassword());
        postgresContainer.start();
        postgresContainer.followOutput(new Slf4jLogConsumer(LoggerFactory.getLogger(OrderItemDaoImplTest.class)));

    }

    @AfterAll
    public static void afterAll() {
        postgresContainer.stop();
    }

    @BeforeEach
    void setUp() throws SQLException {
        dataSource = CustomDataSourceConfig.getHikariDataSource();
        sessionManager = new SessionManagerJdbc(dataSource);
        orderDao = new OrderDaoImpl(sessionManager);
        customer = new Customer();
        address = new Address();
        customer.setId(1L);
        address.setId(1L);

    }

    @Test
    void findById_shouldReturnOrder() throws SQLException {
        Long orderId = saveTestOrder(1L, 1L, BigDecimal.TEN, "PENDING");
        Optional<Order> orderById = orderDao.findById(orderId);
        Assertions.assertTrue(orderById.isPresent());
        Assertions.assertEquals(1L, orderById.get().getCustomer().getId());
        Assertions.assertEquals(1L, orderById.get().getShippingAddress().getId());
        Assertions.assertEquals(0,BigDecimal.TEN.compareTo( orderById.get().getTotalPrice()));
        Assertions.assertEquals("PENDING", orderById.get().getStatus());
    }

    @Test
    void findById_shouldReturnEmptyOptionalForNonExistingOrder() throws SQLException {
        Optional<Order> result = orderDao.findById(-1L);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void deleteById_shouldReturnTrueForExistingOrder() throws SQLException {
        Long orderId = saveTestOrder(1L, 1L, BigDecimal.TEN, "PENDING");
        boolean result = orderDao.deleteById(orderId);
        Assertions.assertTrue(result);
    }

    @Test
    void deleteById_shouldReturnFalseForNonExistingOrder() throws SQLException {
        Assertions.assertThrows(SQLException.class, () -> orderDao.deleteById(-1L));
    }

    @Test
    void findAll_shouldReturnListOfOrders() throws SQLException {
        saveTestOrder(1L, 1L, BigDecimal.TEN, "PENDING");
        saveTestOrder(2L, 2L, BigDecimal.valueOf(20), "PROCESSING");
        List<Order> orders = orderDao.findAll();
        Assertions.assertEquals(2, orders.size());
        Assertions.assertEquals(1L, orders.get(0).getCustomer().getId());
        Assertions.assertEquals(2L, orders.get(1).getCustomer().getId());
    }

    @Test
    void findAllByCustomerId_shouldReturnListOfOrders() throws SQLException {
        saveTestOrder(1L, 1L, BigDecimal.TEN, "PENDING");
        saveTestOrder(1L, 2L, BigDecimal.valueOf(20), "PROCESSING");
        List<Order> orders = orderDao.findAllByCustomerId(1L);
        Assertions.assertEquals(2, orders.size());
        Assertions.assertEquals(1L, orders.get(0).getCustomer().getId());
        Assertions.assertEquals(1L, orders.get(1).getCustomer().getId());
    }

    @Test
    void save_shouldSaveOrder() throws SQLException {
        Order order = new Order();
        order.setCustomer(customer);
        order.setShippingAddress(address);
        order.setTotalPrice(BigDecimal.TEN);
        order.setStatus("PENDING");
        Long orderId = orderDao.save(order);
        Optional<Order> orderById = orderDao.findById(orderId);
        Assertions.assertTrue(orderById.isPresent());
        Assertions.assertEquals(order.getCustomer().getId(), orderById.get().getCustomer().getId());
        Assertions.assertEquals(order.getShippingAddress().getId(), orderById.get().getShippingAddress().getId());
        Assertions.assertEquals(0, order.getTotalPrice().compareTo(orderById.get().getTotalPrice()));
        Assertions.assertEquals("PENDING", orderById.get().getStatus());
    }

    @Test
    void update_shouldUpdateOrderStatus() throws SQLException {
        Long orderId = saveTestOrder(1L, 1L, BigDecimal.TEN, "PENDING");
        Order updatedOrder = new Order();
        updatedOrder.setId(orderId);
        updatedOrder.setStatus("PROCESSING");
        orderDao.update(updatedOrder);
        Optional<Order> orderById = orderDao.findById(orderId);
        Assertions.assertTrue(orderById.isPresent());
        Assertions.assertEquals("PROCESSING", orderById.get().getStatus());
    }

    private Long saveTestOrder(Long customerId, Long shippingAddressId, BigDecimal totalPrice, String status) throws SQLException {
        Order order = new Order();
        Customer customer1 = new Customer();
        Address address1 = new Address();
        customer1.setId(customerId);
        address1.setId(shippingAddressId);
        order.setCustomer(customer1);
        order.setShippingAddress(address1);
        order.setTotalPrice(totalPrice);
        order.setStatus(status);
        return orderDao.save(order);
    }
}
