package ru.adel.deliveryapp.service;

import ru.adel.deliveryapp.entity.Customer;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

public interface CustomerService extends Serializable {
    Customer getCustomerById(Long id) throws SQLException;

    void deleteById(Long id) throws SQLException;

    void update(Long id, Customer customer) throws SQLException;

    List<Customer> findAll() throws SQLException;

    void save(Customer customer) throws SQLException;
}
