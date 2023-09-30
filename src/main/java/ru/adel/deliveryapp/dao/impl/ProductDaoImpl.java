package ru.adel.deliveryapp.dao.impl;

import ru.adel.deliveryapp.dao.ProductDao;
import ru.adel.deliveryapp.dao.mapper.impl.ProductResultSetMapper;
import ru.adel.deliveryapp.datasourse.jdbc.sessionmanager.SessionManager;
import ru.adel.deliveryapp.entity.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductDaoImpl implements ProductDao {
    private final SessionManager sessionManager;
    private final ProductResultSetMapper productResultSetMapper;

    public ProductDaoImpl(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        productResultSetMapper = new ProductResultSetMapper();
    }

    @Override
    public Optional<Product> findById(Long id) throws SQLException {
        sessionManager.beginSession();
        try (Connection connection = sessionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(SQLTask.GET_PRODUCT_BY_ID.QUERY)) {
            pst.setLong(1, id);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return Optional.ofNullable(productResultSetMapper.map(rs));
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
             PreparedStatement pst = connection.prepareStatement(SQLTask.DELETE_PRODUCT_BY_ID.QUERY)) {
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
    public List<Product> findAll() throws SQLException {
        List<Product> products = new ArrayList<>();
        sessionManager.beginSession();
        try (Connection connection = sessionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(SQLTask.GET_ALL_PRODUCTS.QUERY)) {
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    products.add(productResultSetMapper.map(rs));
                }
            }
        } catch (SQLException ex) {
            sessionManager.rollbackSession();
            throw ex;
        }
        return products;
    }

    @Override
    public Long save(Product product) throws SQLException {
        sessionManager.beginSession();
        try (Connection connection = sessionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(SQLTask.INSERT_PRODUCT.QUERY, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, product.getName());
            pst.setString(2, product.getDescription());
            pst.setBigDecimal(3, product.getPrice());
            pst.setLong(4, product.getStock());

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
    public void update(Product product) throws SQLException {
        sessionManager.beginSession();
        try (Connection connection = sessionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(SQLTask.UPDATE_PRODUCT_BY_ID.QUERY)) {
            pst.setString(1, product.getName());
            pst.setString(2, product.getDescription());
            pst.setBigDecimal(3, product.getPrice());
            pst.setLong(4, product.getStock());
            pst.setLong(5, product.getId());

            pst.executeUpdate();
            sessionManager.commitSession();
        } catch (SQLException ex) {
            sessionManager.rollbackSession();
            throw ex;
        }
    }

    @Override
    public void updateStockById(Long id, Long newStock) throws SQLException {
        sessionManager.beginSession();
        try (Connection connection = sessionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(SQLTask.UPDATE_STOCK_BY_ID.QUERY)) {
            pst.setLong(1, newStock);
            pst.setLong(2, id);

            pst.executeUpdate();
            sessionManager.commitSession();
        } catch (SQLException ex) {
            sessionManager.rollbackSession();
            throw ex;
        }
    }

    @Override
    public boolean existsByName(String name) throws SQLException {
        sessionManager.beginSession();
        try (Connection connection = sessionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(SQLTask.CHECK_EXISTENCE_BY_NAME.QUERY)) {
            pst.setString(1, name);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            sessionManager.rollbackSession();
            throw ex;
        }
    }

    enum SQLTask {
        INSERT_PRODUCT("INSERT INTO products (name, description,price,stock) VALUES (?, ?, ?, ?)"),
        GET_PRODUCT_BY_ID("SELECT products.id, products.name, products.description,products.price," +
                "products.stock FROM products WHERE id = ?"),
        DELETE_PRODUCT_BY_ID("DELETE FROM products WHERE id = ?"),
        UPDATE_PRODUCT_BY_ID("UPDATE products SET name = ?, description = ?,price = ?, stock= ? WHERE id = ?"),
        UPDATE_STOCK_BY_ID("UPDATE products SET stock = ? WHERE id = ?"),
        GET_ALL_PRODUCTS("SELECT products.id, products.name, products.description,products.price," +
                "products.stock FROM products"),
        CHECK_EXISTENCE_BY_NAME("SELECT 1 FROM products WHERE name = ?");


        String QUERY;

        SQLTask(String QUERY) {
            this.QUERY = QUERY;
        }
    }
}
