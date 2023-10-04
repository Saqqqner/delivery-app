package ru.adel.deliveryapp.dao;

import ru.adel.deliveryapp.entity.OrderItem;

import java.sql.SQLException;
import java.util.List;

public interface OrderItemDao {
    List<OrderItem> findAllByOrderId(Long orderId) throws SQLException;

    Long save(OrderItem orderItem, Long id) throws SQLException;


}
