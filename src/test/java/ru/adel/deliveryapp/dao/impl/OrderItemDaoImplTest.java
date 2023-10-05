package ru.adel.deliveryapp.dao.impl;

import org.junit.jupiter.api.*;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.adel.deliveryapp.dao.OrderItemDao;
import ru.adel.deliveryapp.datasourse.jdbc.sessionmanager.SessionManager;
import ru.adel.deliveryapp.datasourse.jdbc.sessionmanager.SessionManagerJdbc;
import ru.adel.deliveryapp.entity.Order;
import ru.adel.deliveryapp.entity.OrderItem;
import ru.adel.deliveryapp.entity.Product;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

@Testcontainers
class OrderItemDaoImplTest {
    @Container
    private  final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest").withInitScript("scripts-orderitem.sql");

    private SessionManager sessionManager;
    private OrderItemDao orderItemDao;
    private DataSource dataSource;
    private Product product;
    private Product product1;
    private Order order;
    private Order order1;


    @BeforeEach
    void setUp() throws SQLException{
        postgresContainer.start();
        postgresContainer.followOutput(new Slf4jLogConsumer(LoggerFactory.getLogger(AddressDaoImplTest.class)));

        System.setProperty("jdbc.url", postgresContainer.getJdbcUrl());
        System.setProperty("jdbc.username", postgresContainer.getUsername());
        System.setProperty("jdbc.password", postgresContainer.getPassword());

        CustomDataSourceConfig.updateDataSourceProperties();

        dataSource = CustomDataSourceConfig.getHikariDataSource();
        sessionManager = new SessionManagerJdbc(dataSource);
        orderItemDao = new OrderItemDaoImpl(sessionManager);
        product = new Product(1L, "Potato", "Картошка", BigDecimal.valueOf(2.00), 300L);
        product1 = new Product(2L, "Orange", "Апельсин", BigDecimal.valueOf(4.00), 400L);
        order = new Order();
        order1 = new Order();
        order.setId(1L);
        order1.setId(2L);
        createOrderItems();
    }

    @AfterEach
    void tearDown() {
        postgresContainer.stop();
    }



    @Test
    void findAllByOrderId_shouldReturnOrder() throws SQLException {
        List<OrderItem> orderItems = orderItemDao.findAllByOrderId(1L);
        Assertions.assertEquals(2, orderItems.size());
        Assertions.assertEquals(1L, orderItems.get(0).getProduct().getId());
        Assertions.assertEquals(2L, orderItems.get(1).getProduct().getId());
        Assertions.assertEquals(0, BigDecimal.valueOf(6.00).compareTo(orderItems.get(0).getProductTotalPrice()));
        Assertions.assertEquals(0, BigDecimal.valueOf(12.00).compareTo(orderItems.get(1).getProductTotalPrice()));
    }


    @Test
    void saveOrderItemsByOrderId_shouldSaveOrderItems() throws SQLException {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(4L);
        orderItem.calculateTotalPrice();
        orderItemDao.save(orderItem, order.getId());
        List<OrderItem> orderItems = orderItemDao.findAllByOrderId(1L);
        Assertions.assertEquals(3, orderItems.size());
        Assertions.assertEquals(0, BigDecimal.valueOf(8.00).compareTo(orderItems.get(2).getProductTotalPrice()));
    }


    private void createOrderItems() throws SQLException {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(3L);
        orderItem.calculateTotalPrice();
        OrderItem orderItem1 = new OrderItem();
        orderItem1.setOrder(order1);
        orderItem1.setProduct(product1);
        orderItem1.setQuantity(3L);
        orderItem1.calculateTotalPrice();
        OrderItem orderItem2 = new OrderItem();
        orderItem2.setOrder(order);
        orderItem2.setProduct(product1);
        orderItem2.setQuantity(3L);
        orderItem2.calculateTotalPrice();
        orderItemDao.save(orderItem, order.getId());
        orderItemDao.save(orderItem1, order1.getId());
        orderItemDao.save(orderItem2, order.getId());
    }


}
