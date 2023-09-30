package ru.adel.deliveryapp.dao.mapper.impl;

import ru.adel.deliveryapp.dao.mapper.EntityResultSetMapper;
import ru.adel.deliveryapp.entity.Order;
import ru.adel.deliveryapp.entity.OrderItem;
import ru.adel.deliveryapp.entity.Product;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderItemResultSetMapper implements EntityResultSetMapper<OrderItem> {
    @Override
    public OrderItem map(ResultSet resultSet) throws SQLException {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(resultSet.getLong("id"));
        Order order = new Order();
        order.setId(resultSet.getLong("order_id"));
        orderItem.setOrder(order);
        // Создаем объект Product и устанавливаем его в OrderItem
        Product product = new Product();
        product.setId(resultSet.getLong("product_id"));
        orderItem.setProduct(product);

        // Устанавливаем количество товара и общую стоимость для OrderItem
        orderItem.setQuantity(resultSet.getLong("quantity"));
        orderItem.setProductTotalPrice(resultSet.getBigDecimal("product_total_price"));

        return orderItem;
    }
}