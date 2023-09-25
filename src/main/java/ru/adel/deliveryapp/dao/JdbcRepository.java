package ru.adel.deliveryapp.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface JdbcRepository<T, K> {
    Optional<T> findById(K id) throws SQLException;

    boolean deleteById(K id)throws SQLException;

    List<T> findAll()throws SQLException;

    Long save(T t)throws SQLException;

    void update(T t)throws SQLException;
}
