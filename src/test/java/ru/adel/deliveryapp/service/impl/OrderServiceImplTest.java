package ru.adel.deliveryapp.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.adel.deliveryapp.dao.CustomerDao;
import ru.adel.deliveryapp.dao.OrderDao;
import ru.adel.deliveryapp.dao.OrderItemDao;
import ru.adel.deliveryapp.entity.*;
import ru.adel.deliveryapp.service.AddressService;
import ru.adel.deliveryapp.service.ProductService;
import ru.adel.deliveryapp.servlet.dto.OrderDTO;
import ru.adel.deliveryapp.servlet.dto.OrderItemDTO;
import ru.adel.deliveryapp.util.AddressNotFoundException;
import ru.adel.deliveryapp.util.CustomerNotFoundException;
import ru.adel.deliveryapp.util.OrderNotFoundException;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private ProductService productService;

    @Mock
    private CustomerDao customerDao;

    @Mock
    private AddressService addressService;

    @Mock
    private OrderItemDao orderItemDao;

    @InjectMocks
    private OrderServiceImpl orderService;

    private OrderDTO orderDTO;

    @BeforeEach
    void setUp() {
        orderDTO = new OrderDTO();
        orderDTO.setCustomerId(1L);
        orderDTO.setAddressId(1L);
        List<OrderItemDTO> orderItems = new ArrayList<>();
        orderItems.add(new OrderItemDTO(1L, 2L));
        orderItems.add(new OrderItemDTO(1L, 3L));
        orderDTO.setOrderItems(orderItems);
    }

    @Test
    void getOrderById_shouldGetOrderById() throws SQLException {
        // Arrange
        Long orderId = 1L;
        Order mockOrder = new Order(orderId, null, null, new ArrayList<>(), "Created");
        List<OrderItem> mockOrderItems = new ArrayList<>();
        Mockito.when(orderDao.findById(orderId)).thenReturn(Optional.of(mockOrder));
        Mockito.when(orderItemDao.findAllByOrderId(orderId)).thenReturn(mockOrderItems);

        // Act
        Order result = orderService.getOrderById(orderId);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(mockOrder, result);
        Assertions.assertEquals(mockOrderItems, result.getOrderItems());
    }

    @Test
    void getOrderById_shouldThrowOrderNotFoundException() throws SQLException {
        // Arrange
        Long orderId = 1L;
        Mockito.when(orderDao.findById(orderId)).thenReturn(Optional.empty());

        // Act and Assert
        Assertions.assertThrows(OrderNotFoundException.class, () -> orderService.getOrderById(orderId));
    }

    @Test
    void getAllOrders_shouldFindAllOrders() throws SQLException {
        // Arrange
        List<Order> mockOrders = new ArrayList<>();
        mockOrders.add(new Order(1L, null, null, new ArrayList<>(), "Created"));
        mockOrders.add(new Order(2L, null, null, new ArrayList<>(), "Delivered"));
        List<OrderItem> mockOrderItems = new ArrayList<>();

        Mockito.when(orderDao.findAll()).thenReturn(mockOrders);
        Mockito.when(orderItemDao.findAllByOrderId(1L)).thenReturn(mockOrderItems);
        Mockito.when(orderItemDao.findAllByOrderId(2L)).thenReturn(mockOrderItems);

        // Act
        List<Order> result = orderService.getAllOrders();

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(mockOrders.size(), result.size());
        Assertions.assertEquals(mockOrders.get(0).getOrderItems(), result.get(0).getOrderItems());
        Assertions.assertEquals(mockOrders.get(1).getOrderItems(), result.get(1).getOrderItems());
    }

    @Test
    void findAllByCustomerId_shouldFindAllOrdersByCustomerId() throws SQLException {
        // Arrange
        Long customerId = 1L;
        List<Order> mockOrders = new ArrayList<>();
        mockOrders.add(new Order(1L, null, null, new ArrayList<>(), "Created"));
        List<OrderItem> mockOrderItems = new ArrayList<>();

        Mockito.when(orderDao.findAllByCustomerId(customerId)).thenReturn(mockOrders);
        Mockito.when(orderItemDao.findAllByOrderId(1L)).thenReturn(mockOrderItems);

        // Act
        List<Order> result = orderService.findAllByCustomerId(customerId);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(mockOrders.size(), result.size());
        Assertions.assertEquals(mockOrders.get(0).getOrderItems(), result.get(0).getOrderItems());
    }

    @Test
    void createOrder_shouldCreateOrder() throws SQLException, CustomerNotFoundException, AddressNotFoundException {
        // Arrange
        Customer mockCustomer = new Customer(1L, "John Doe", "john@example.com", new ArrayList<>());
        Address mockAddress = new Address(1L, "123 Main St", "City", "12345", "2");
        List<OrderItem> mockOrderItems = new ArrayList<>();

        Mockito.when(customerDao.findById(orderDTO.getCustomerId())).thenReturn(Optional.of(mockCustomer));
        Mockito.when(addressService.getAddressById(orderDTO.getAddressId())).thenReturn(mockAddress);
        Mockito.when(orderDao.save(Mockito.any(Order.class))).thenReturn(1L);
        Mockito.lenient().when(productService.getProductById(1L)).thenReturn(new Product(1L, "Product 1", "Description", BigDecimal.TEN, 5L));
        Mockito.lenient().when(productService.getProductById(2L)).thenReturn(new Product(2L, "Product 2", "Description", BigDecimal.TEN, 5L));

        // Act
        Long orderId = orderService.createOrder(orderDTO);

        // Assert
        Assertions.assertNotNull(orderId);
        Mockito.verify(orderDao, Mockito.times(1)).save(Mockito.any(Order.class));
        Mockito.verify(orderItemDao, Mockito.times(2)).save(Mockito.any(OrderItem.class), Mockito.eq(orderId));
        Mockito.verify(productService, Mockito.times(2)).updateProductStock(Mockito.anyList());
    }

    @Test
    void updateOrderStatusDelivered_shouldUpdateOrderStatusDelivered() throws SQLException {
        // Arrange
        Long orderId = 1L;
        Order mockOrder = new Order(orderId, null, null, new ArrayList<>(), "Created");
        Mockito.when(orderDao.findById(orderId)).thenReturn(Optional.of(mockOrder));

        // Act
        Assertions.assertDoesNotThrow(() -> orderService.updateOrderStatusDelivered(orderId));

        // Assert
        Mockito.verify(orderDao, Mockito.times(1)).update(Mockito.any(Order.class));
    }

    @Test
    void updateOrderStatusDelivered_shouldThrowOrderNotFoundException() throws SQLException {
        // Arrange
        Long orderId = 1L;
        Mockito.when(orderDao.findById(orderId)).thenReturn(Optional.empty());

        // Act and Assert
        Assertions.assertThrows(OrderNotFoundException.class, () -> orderService.updateOrderStatusDelivered(orderId));
        Mockito.verify(orderDao, Mockito.never()).update(Mockito.any(Order.class));
    }

    @Test
    void deleteOrder_shouldDeleteOrderById() throws SQLException {
        // Arrange
        Long orderId = 1L;
        Mockito.when(orderDao.deleteById(orderId)).thenReturn(true);

        // Act
        Assertions.assertDoesNotThrow(() -> orderService.deleteOrder(orderId));

        // Assert
        Mockito.verify(orderDao, Mockito.times(1)).deleteById(orderId);
        Mockito.verify(orderItemDao, Mockito.times(1)).deleteAllByOrderId(orderId);
    }

    @Test
    void deleteOrder_shouldThrowOrderNotFoundException() throws SQLException {
        // Arrange
        Long orderId = 1L;
        Mockito.when(orderDao.deleteById(orderId)).thenReturn(false);

        // Act and Assert
        Assertions.assertThrows(OrderNotFoundException.class, () -> orderService.deleteOrder(orderId));
        Mockito.verify(orderItemDao, Mockito.never()).deleteAllByOrderId(orderId);
    }
}

