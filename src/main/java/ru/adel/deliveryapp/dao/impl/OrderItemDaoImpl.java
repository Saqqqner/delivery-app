package ru.adel.deliveryapp.dao.impl;

import ru.adel.deliveryapp.dao.OrderItemDao;
import ru.adel.deliveryapp.dao.mapper.impl.OrderItemResultSetMapper;
import ru.adel.deliveryapp.datasourse.jdbc.sessionmanager.SessionManager;
import ru.adel.deliveryapp.entity.OrderItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderItemDaoImpl implements OrderItemDao {
    private final SessionManager sessionManager;
    private final OrderItemResultSetMapper orderItemResultSetMapper;

    public OrderItemDaoImpl(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        this.orderItemResultSetMapper = new OrderItemResultSetMapper();
    }





    @Override
    public Long save(OrderItem orderItem, Long orderId) throws SQLException {
        sessionManager.beginSession();
        try (Connection connection = sessionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(SQLTask.INSERT_ORDER_ITEM.QUERY, Statement.RETURN_GENERATED_KEYS)) {
            pst.setLong(1, orderId);
            pst.setLong(2, orderItem.getProduct().getId());
            pst.setLong(3, orderItem.getQuantity());
            pst.setBigDecimal(4, orderItem.getProductTotalPrice());

            pst.executeUpdate();
            try (ResultSet rs = pst.getGeneratedKeys()) {
                rs.next();
                Long id = rs.getLong(1);
                sessionManager.commitSession();
                return id;
            }
        } catch (SQLException ex) {
            sessionManager.rollbackSession();
            throw ex;
        }
    }


    @Override
    public List<OrderItem> findAllByOrderId(Long orderId) throws SQLException {
        List<OrderItem> orderItems = new ArrayList<>();
        sessionManager.beginSession();
        try (Connection connection = sessionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(SQLTask.GET_ALL_ORDER_ITEMS_BY_ORDER_ID.QUERY)) {
            pst.setLong(1, orderId); // Устанавливаем orderId в запрос
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    orderItems.add(orderItemResultSetMapper.map(rs));
                }
            }
        } catch (SQLException ex) {
            sessionManager.rollbackSession();
            throw ex;
        }
        return orderItems;
    }

    enum SQLTask {
        INSERT_ORDER_ITEM("INSERT INTO order_items (order_id, product_id, quantity, product_total_price) VALUES (?, ?, ?, ?)"),

        GET_ALL_ORDER_ITEMS_BY_ORDER_ID("SELECT order_items.id, order_items.order_id, order_items.product_id, order_items.quantity, order_items.product_total_price FROM order_items WHERE order_id = ?");

        String QUERY;

        SQLTask(String QUERY) {
            this.QUERY = QUERY;
        }
    }
}


