package ru.adel.deliveryapp.dao.mapper.impl;

import ru.adel.deliveryapp.dao.mapper.EntityResultSetMapper;
import ru.adel.deliveryapp.models.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerResultSetMapper implements EntityResultSetMapper<Customer> {
    @Override
    public Customer map(ResultSet resultSet)throws SQLException {
        Customer customer = new Customer();
        customer.setId(resultSet.getLong("id"));
        customer.setUsername(resultSet.getString("username"));
        customer.setEmail(resultSet.getString("email"));
        return customer;
    }
}
