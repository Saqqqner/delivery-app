package ru.adel.deliveryapp.dao;

import ru.adel.deliveryapp.entity.Customer;

import java.io.Serializable;
import java.sql.SQLException;

public interface CustomerDao extends JdbcRepository<Customer, Long> , Serializable {
    boolean existsByEmail(String email) throws SQLException;

    boolean existsByUsername(String username) throws SQLException;


}
