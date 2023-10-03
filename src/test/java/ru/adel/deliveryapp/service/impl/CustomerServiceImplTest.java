package ru.adel.deliveryapp.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.adel.deliveryapp.dao.CustomerDao;
import ru.adel.deliveryapp.entity.Customer;
import ru.adel.deliveryapp.entity.Order;
import ru.adel.deliveryapp.entity.OrderItem;
import ru.adel.deliveryapp.service.OrderService;
import ru.adel.deliveryapp.util.CustomerNotFoundException;
import ru.adel.deliveryapp.util.DuplicateException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerDao customerDao;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Test
    void getCustomerById_shouldGetCustomerById() throws SQLException {
        // Arrange
        List<Order> mockOrders = new ArrayList<>();
        Long customerId = 1L;
        Customer mockCustomer = new Customer(customerId, "John Doe", "john@example.com", mockOrders);
        mockOrders.add(new Order(1L, mockCustomer, null, new ArrayList<OrderItem>(), null));
        Mockito.when(customerDao.findById(customerId)).thenReturn(Optional.of(mockCustomer));
        Mockito.when(orderService.findAllByCustomerId(customerId)).thenReturn(mockOrders);

        // Act
        Customer result = customerService.getCustomerById(customerId);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(mockCustomer, result);
        Assertions.assertEquals(mockOrders, result.getOrder());
    }

    @Test
    void getCustomerById_shouldThrowCustomerNotFoundException() throws SQLException {
        // Arrange
        Long customerId = 1L;
        Mockito.when(customerDao.findById(customerId)).thenReturn(Optional.empty());

        // Act and Assert
        Assertions.assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomerById(customerId));
    }

    @Test
    void deleteById_shouldDeleteCustomerById() throws SQLException {
        // Arrange
        Long customerId = 1L;
        Mockito.when(customerDao.deleteById(customerId)).thenReturn(true);

        // Act
        Assertions.assertDoesNotThrow(() -> customerService.deleteById(customerId));
    }

    @Test
    void deleteById_shouldThrowCustomerNotFoundException() throws SQLException {
        // Arrange
        Long customerId = 1L;
        Mockito.when(customerDao.deleteById(customerId)).thenReturn(false);

        // Act and Assert
        Assertions.assertThrows(CustomerNotFoundException.class, () -> customerService.deleteById(customerId));
    }

    @Test
    void update_shouldUpdateCustomerById() throws SQLException {
        // Arrange
        Long customerId = 1L;
        Customer mockCustomer = new Customer(customerId, "John Doe", "john@example.com", new ArrayList<>());
        Customer updateCustomer = new Customer(customerId, "Updated John Doe", "updatedjohn@example.com", new ArrayList<>());

        Mockito.when(customerDao.findById(customerId)).thenReturn(Optional.of(mockCustomer));

        // Act
        Assertions.assertDoesNotThrow(() -> customerService.update(customerId, updateCustomer));

        // Assert
        Mockito.verify(customerDao, Mockito.times(1)).update(Mockito.any(Customer.class));
    }

    @Test
    void update_shouldThrowCustomerNotFoundException() throws SQLException {
        // Arrange
        Long customerId = 1L;
        Customer updateCustomer = new Customer(customerId, "Updated John Doe", "updatedjohn@example.com", new ArrayList<>());

        Mockito.when(customerDao.findById(customerId)).thenReturn(Optional.empty());

        // Act and Assert
        Assertions.assertThrows(CustomerNotFoundException.class, () -> customerService.update(customerId, updateCustomer));
        Mockito.verify(customerDao, Mockito.never()).update(Mockito.any(Customer.class));
    }

    @Test
    void findAll_shouldFindAllCustomers() throws SQLException {
        // Arrange
        List<Customer> mockCustomers = new ArrayList<>();
        mockCustomers.add(new Customer(1L, "John Doe", "john@example.com", new ArrayList<>()));
        mockCustomers.add(new Customer(2L, "Jane Doe", "jane@example.com", new ArrayList<>()));

        Mockito.when(customerDao.findAll()).thenReturn(mockCustomers);
        Mockito.when(orderService.findAllByCustomerId(1L)).thenReturn(new ArrayList<>());
        Mockito.when(orderService.findAllByCustomerId(2L)).thenReturn(new ArrayList<>());

        // Act
        List<Customer> result = customerService.findAll();

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(mockCustomers.size(), result.size());
        Assertions.assertEquals(mockCustomers, result);
    }

    @Test
    void save_shouldSaveCustomer() throws SQLException {
        // Arrange
        Customer newCustomer = new Customer(null, "New John Doe", "newjohn@example.com", new ArrayList<>());

        Mockito.when(customerDao.existsByEmail(newCustomer.getEmail())).thenReturn(false);
        Mockito.when(customerDao.existsByUsername(newCustomer.getUsername())).thenReturn(false);

        // Act
        Assertions.assertDoesNotThrow(() -> customerService.save(newCustomer));

        // Assert
        Mockito.verify(customerDao, Mockito.times(1)).save(Mockito.any(Customer.class));
    }

    @Test
    void saveByDuplicateEmail_shouldThrowDuplicateException() throws SQLException {
        // Arrange
        Customer newCustomer = new Customer(null, "New John Doe", "john@example.com", new ArrayList<>());

        Mockito.when(customerDao.existsByEmail(newCustomer.getEmail())).thenReturn(true);

        // Act and Assert
        Assertions.assertThrows(DuplicateException.class, () -> customerService.save(newCustomer));
        Mockito.verify(customerDao, Mockito.never()).save(Mockito.any(Customer.class));
    }

    @Test
    void saveByDuplicateUsername_shouldThrowDuplicateException() throws SQLException {
        // Arrange
        Customer newCustomer = new Customer(null, "New John Doe", "newjohn@example.com", new ArrayList<>());

        Mockito.when(customerDao.existsByEmail(newCustomer.getEmail())).thenReturn(false);
        Mockito.when(customerDao.existsByUsername(newCustomer.getUsername())).thenReturn(true);

        // Act and Assert
        Assertions.assertThrows(DuplicateException.class, () -> customerService.save(newCustomer));
        Mockito.verify(customerDao, Mockito.never()).save(Mockito.any(Customer.class));
    }
}


