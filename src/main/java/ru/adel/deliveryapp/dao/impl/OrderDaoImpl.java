package ru.adel.deliveryapp.dao.impl;

import ru.adel.deliveryapp.dao.OrderDao;
import ru.adel.deliveryapp.dao.mapper.impl.OrderResultSetMapper;
import ru.adel.deliveryapp.datasourse.jdbc.sessionmanager.SessionManager;
import ru.adel.deliveryapp.entity.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderDaoImpl implements OrderDao {
    private final SessionManager sessionManager;
    private final OrderResultSetMapper orderResultSetMapper;

    public OrderDaoImpl(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        orderResultSetMapper = new OrderResultSetMapper();
    }

    @Override
    public Optional<Order> findById(Long id) throws SQLException {
        sessionManager.beginSession();
        try (Connection connection = sessionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(SQLTask.GET_ORDER_BY_ID.QUERY)) {
            pst.setLong(1, id);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return Optional.ofNullable(orderResultSetMapper.map(rs));
                }
            }
        } catch (SQLException ex) {
            sessionManager.rollbackSession();
            throw ex;
        }
        return Optional.empty();
    }


    @Override
    public boolean deleteById(Long id) throws SQLException {
        int updatedRows;
        sessionManager.beginSession();
        try (Connection connection = sessionManager.getCurrentSession()) {
            if (id < 0) {
                throw new SQLException("ID can't be negative");
            }
            try (PreparedStatement pst = connection.prepareStatement(SQLTask.DELETE_ORDER_BY_ID.QUERY)) {
                pst.setLong(1, id);
                updatedRows = pst.executeUpdate();
                sessionManager.commitSession();
            } catch (SQLException ex) {
                sessionManager.rollbackSession();
                throw ex;
            }
            return updatedRows > 0;
        }
    }

    @Override
    public List<Order> findAll() throws SQLException {
        List<Order> orders = new ArrayList<>();
        sessionManager.beginSession();
        try (Connection connection = sessionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(SQLTask.GET_ALL_ORDERS.QUERY)) {
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    orders.add(orderResultSetMapper.map(rs));
                }
            }
        } catch (SQLException ex) {
            sessionManager.rollbackSession();
            throw ex;
        }
        return orders;
    }

    @Override
    public List<Order> findAllByCustomerId(Long customerId) throws SQLException {
        List<Order> orders = new ArrayList<>();
        sessionManager.beginSession();
        try (Connection connection = sessionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(SQLTask.GET_ALL_ORDERS_BY_CUSTOMER_ID.QUERY)) {
            pst.setLong(1, customerId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    orders.add(orderResultSetMapper.map(rs));
                }
            }
        } catch (SQLException ex) {
            sessionManager.rollbackSession();
            throw ex;
        }
        return orders;
    }


    @Override
    public Long save(Order order) throws SQLException {
        sessionManager.beginSession();
        try (Connection connection = sessionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(SQLTask.INSERT_ORDER.QUERY, Statement.RETURN_GENERATED_KEYS)) {
            pst.setLong(1, order.getCustomer().getId());
            pst.setLong(2, order.getShippingAddress().getId());
            pst.setBigDecimal(3, order.getTotalPrice());
            pst.setString(4, order.getStatus());

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
    public void update(Order order) throws SQLException {
        sessionManager.beginSession();
        try (Connection connection = sessionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(SQLTask.UPDATE_STATUS_ORDER_BY_ID.QUERY)) {
            pst.setString(1, order.getStatus());
            pst.setLong(2, order.getId());
            pst.executeUpdate();
            sessionManager.commitSession();
        } catch (SQLException ex) {
            sessionManager.rollbackSession();
            throw ex;
        }
    }


    enum SQLTask {
        INSERT_ORDER("INSERT INTO orders (customer_id, shipping_address_id,total_price,status) VALUES (?, ?, ?,?)"),
        GET_ORDER_BY_ID("SELECT orders.id, orders.customer_id, orders.shipping_address_id,orders.status," +
                "orders.total_price FROM orders WHERE id = ?"),
        DELETE_ORDER_BY_ID("DELETE FROM orders WHERE id = ?"),
        UPDATE_STATUS_ORDER_BY_ID("UPDATE orders SET status = ? WHERE id = ?"),
        GET_ALL_ORDERS("SELECT orders.id, orders.customer_id, orders.shipping_address_id,orders.status," +
                "orders.total_price FROM orders"),
        GET_ALL_ORDERS_BY_CUSTOMER_ID("SELECT orders.id, orders.customer_id, orders.shipping_address_id,orders.status," +
                "orders.total_price FROM orders WHERE customer_id = ? ");


        String QUERY;

        SQLTask(String QUERY) {
            this.QUERY = QUERY;
        }
    }
}

