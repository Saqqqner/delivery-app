package ru.adel.deliveryapp.dao;

import ru.adel.deliveryapp.entity.OrderItem;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface OrderItemDao  {
    List<OrderItem> findAllByOrderId(Long orderId) throws SQLException;
    Long save(OrderItem orderItem , Long id) throws SQLException;
    boolean deleteAllByOrderId(Long orderId) throws SQLException;


}
