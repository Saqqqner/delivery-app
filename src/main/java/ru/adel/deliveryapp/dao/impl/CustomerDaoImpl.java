package ru.adel.deliveryapp.dao.impl;

import ru.adel.deliveryapp.dao.CustomerDao;
import ru.adel.deliveryapp.dao.mapper.impl.CustomerResultSetMapper;
import ru.adel.deliveryapp.datasourse.jdbc.sessionmanager.SessionManager;
import ru.adel.deliveryapp.models.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerDaoImpl implements CustomerDao {
    private final SessionManager sessionManager;
    private final CustomerResultSetMapper customerResultSetMapper;

    public CustomerDaoImpl(final SessionManager sessionManager) {
        this.sessionManager = sessionManager;

        customerResultSetMapper = new CustomerResultSetMapper();
    }

    @Override
    public Optional<Customer> findById(Long id) throws SQLException {
        sessionManager.beginSession();
        try (Connection connection = sessionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(SQLTask.GET_CUSTOMER_BY_ID.QUERY)) {
            pst.setLong(1, id);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return Optional.ofNullable(customerResultSetMapper.map(rs));
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
        try (Connection connection = sessionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(SQLTask.DELETE_CUSTOMER_BY_ID.QUERY)) {
            pst.setLong(1, id);
            updatedRows = pst.executeUpdate();
            sessionManager.commitSession();
        } catch (SQLException ex) {
            sessionManager.rollbackSession();
            throw ex;
        }
        return updatedRows > 0;
    }

    @Override
    public List<Customer> findAll() throws SQLException {
        List<Customer> customers = new ArrayList<>();
        sessionManager.beginSession();
        try (Connection connection = sessionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(SQLTask.GET_ALL_CUSTOMERS.QUERY)) {
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    customers.add(customerResultSetMapper.map(rs));
                }
            }
        } catch (SQLException ex) {
            sessionManager.rollbackSession();
            throw ex;
        }
        return customers;
    }

    @Override
    public Long save(Customer customer) throws SQLException {
        sessionManager.beginSession();
        try (Connection connection = sessionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(SQLTask.INSERT_CUSTOMER.QUERY, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, customer.getUsername());
            pst.setString(2, customer.getEmail());

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
    public void update(Customer customer) throws SQLException {
        sessionManager.beginSession();
        try (Connection connection = sessionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(SQLTask.UPDATE_CUSTOMER_BY_ID.QUERY)) {
            pst.setString(1, customer.getUsername());
            pst.setString(2, customer.getEmail());
            pst.setLong(3, customer.getId());

            pst.executeUpdate();
            sessionManager.commitSession();
        } catch (SQLException ex) {
            sessionManager.rollbackSession();
            throw ex;
        }
    }

    @Override
    public boolean existsByEmail(String email) throws SQLException {
        sessionManager.beginSession();
        try (Connection connection = sessionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(SQLTask.CHECK_EXISTENCE_BY_EMAIL.QUERY)) {
            pst.setString(1, email);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            sessionManager.rollbackSession();
            throw ex;
        }
    }

    @Override
    public boolean existsByUsername(String username) throws SQLException {
        sessionManager.beginSession();
        try (Connection connection = sessionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(SQLTask.CHECK_EXISTENCE_BY_USERNAME.QUERY)) {
            pst.setString(1, username);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            sessionManager.rollbackSession();
            throw ex;
        }
    }


    /**
     * SQL queries for customers table.
     */
    enum SQLTask {
        INSERT_CUSTOMER("INSERT INTO customers (username, email) VALUES (?, ?)"),
        GET_CUSTOMER_BY_ID("SELECT customers.id, customer.username, customers.email FROM customers WHERE id = ?"),
        DELETE_CUSTOMER_BY_ID("DELETE FROM customers WHERE id = ?"),
        UPDATE_CUSTOMER_BY_ID("UPDATE customers SET username = ?, email = ? WHERE id = ?"),
        GET_ALL_CUSTOMERS("SELECT customers.id, customers.username, customers.email FROM customers"),
        CHECK_EXISTENCE_BY_EMAIL("SELECT 1 FROM customers WHERE email = ?"),
        CHECK_EXISTENCE_BY_USERNAME("SELECT 1 FROM customers WHERE username = ?");

        String QUERY;

        SQLTask(String QUERY) {
            this.QUERY = QUERY;
        }
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }
}
