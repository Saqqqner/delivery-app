package ru.adel.deliveryapp.dao.mapper.impl;

import ru.adel.deliveryapp.dao.mapper.EntityResultSetMapper;
import ru.adel.deliveryapp.entity.Address;
import ru.adel.deliveryapp.entity.Customer;
import ru.adel.deliveryapp.entity.Order;
import ru.adel.deliveryapp.entity.OrderItem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderResultSetMapper implements EntityResultSetMapper<Order> {
    @Override
    public Order map(ResultSet resultSet) throws SQLException {
        Order order = new Order();
        order.setId(resultSet.getLong("id"));
        order.setStatus(resultSet.getString("status"));

        // Создаем объекты Customer и Address и устанавливаем их в заказ
        Customer customer = new Customer();
        customer.setId(resultSet.getLong("customer_id"));
        order.setCustomer(customer);

        Long addressId = resultSet.getLong("shipping_address_id");
        Address address = new Address();
        address.setId(addressId);
        order.setShippingAddress(address);
        List<OrderItem> orderItems = new ArrayList<>();
        order.setOrderItems(orderItems);
        order.setTotalPrice(resultSet.getBigDecimal("total_price"));


        return order;
    }
}
