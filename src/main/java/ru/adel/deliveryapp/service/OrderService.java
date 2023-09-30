package ru.adel.deliveryapp.service;

import ru.adel.deliveryapp.servlet.dto.OrderDTO;
import ru.adel.deliveryapp.entity.Order;
import ru.adel.deliveryapp.util.AddressNotFoundException;
import ru.adel.deliveryapp.util.CustomerNotFoundException;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

public interface OrderService extends Serializable {
    Order getOrderById(Long id) throws SQLException;

    List<Order> getAllOrders() throws SQLException;

    Long createOrder(OrderDTO orderDTO) throws SQLException , CustomerNotFoundException, AddressNotFoundException;

    void deleteOrder(Long id) throws SQLException;

    List<Order> findAllByCustomerId(Long id) throws SQLException;

    void updateOrderStatusDelivered(Long orderId) throws SQLException;

}
