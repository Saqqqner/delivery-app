package ru.adel.deliveryapp.dao;

import ru.adel.deliveryapp.entity.Order;

import java.sql.SQLException;
import java.util.List;

public interface OrderDao extends JdbcRepository<Order,Long> {
    List<Order> findAllByCustomerId(Long customerId) throws SQLException;

}
