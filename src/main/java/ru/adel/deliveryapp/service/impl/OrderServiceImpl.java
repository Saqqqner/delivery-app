package ru.adel.deliveryapp.service.impl;


import ru.adel.deliveryapp.dao.CustomerDao;
import ru.adel.deliveryapp.dao.OrderDao;
import ru.adel.deliveryapp.dao.OrderItemDao;
import ru.adel.deliveryapp.entity.*;
import ru.adel.deliveryapp.service.AddressService;
import ru.adel.deliveryapp.service.OrderService;
import ru.adel.deliveryapp.service.ProductService;
import ru.adel.deliveryapp.servlet.dto.OrderDTO;
import ru.adel.deliveryapp.servlet.dto.OrderItemDTO;
import ru.adel.deliveryapp.util.AddressNotFoundException;
import ru.adel.deliveryapp.util.CustomerNotFoundException;
import ru.adel.deliveryapp.util.OrderNotFoundException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderServiceImpl implements OrderService {
    private static final String ORDER_NOT_FOUND_MSG = "Order not found with ID: ";
    private final OrderDao orderDao;
    private final ProductService productService;
    private final CustomerDao customerDao;
    private final AddressService addressService;
    private final OrderItemDao orderItemDao;

    public OrderServiceImpl(OrderDao orderDao, ProductService productService, CustomerDao customerDao, AddressService addressService, OrderItemDao orderItemDao) {
        this.orderDao = orderDao;
        this.productService = productService;
        this.customerDao = customerDao;
        this.addressService = addressService;
        this.orderItemDao = orderItemDao;
    }

    public Order getOrderById(Long id) throws SQLException {
        Order order = orderDao.findById(id).orElseThrow(() -> new OrderNotFoundException(ORDER_NOT_FOUND_MSG + id));
        List<OrderItem> orderItems = orderItemDao.findAllByOrderId(id);
        order.setOrderItems(orderItems);
        return order;
    }

    public List<Order> getAllOrders() throws SQLException {
        List<Order> orders = orderDao.findAll();
        for (Order order : orders) {
            List<OrderItem> orderItems = orderItemDao.findAllByOrderId(order.getId());
            order.setOrderItems(orderItems);
        }
        return orders;
    }

    @Override
    public List<Order> findAllByCustomerId(Long id) throws SQLException {
        List<Order> orders = orderDao.findAllByCustomerId(id);
        for (Order order : orders) {
            List<OrderItem> orderItems = orderItemDao.findAllByOrderId(order.getId());
            order.setOrderItems(orderItems);
        }
        return orders;
    }

    public Long createOrder(OrderDTO orderDTO) throws SQLException, CustomerNotFoundException, AddressNotFoundException {
        Customer customer = customerDao.findById(orderDTO.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: "));
        Address address = addressService.getAddressById(orderDTO.getAddressId());
        Order order = new Order();
        order.setShippingAddress(address);
        order.setCustomer(customer);
        order.setStatus("Created");
        List<OrderItem> orderItems = getOrderItemsByRepo(orderDTO, order);
        order.setOrderItems(orderItems);
        Long orderId = orderDao.save(order);
        order.setId(orderId);
        for (OrderItem orderItem : orderItems) {
            orderItem.setOrder(order);
            orderItemDao.save(orderItem, orderId);
        }

        return orderId;
    }

    @Override
    public void updateOrderStatusDelivered(Long orderId) throws SQLException {
        Order existingOrder = orderDao.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(ORDER_NOT_FOUND_MSG + orderId));
        existingOrder.setStatus("Delivered");
        orderDao.update(existingOrder);
    }

    private List<OrderItem> getOrderItemsByRepo(OrderDTO orderDTO, Order order) throws SQLException {
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemDTO orderItemDTO : orderDTO.getOrderItems()) {
            Product product = productService.getProductById(orderItemDTO.getProductId());
            OrderItem newOrderItem = new OrderItem();
            newOrderItem.setOrder(order);
            newOrderItem.setProduct(product);
            newOrderItem.setQuantity(orderItemDTO.getQuantity());
            newOrderItem.calculateTotalPrice();
            orderItems.add(newOrderItem);
            productService.updateProductStock(orderItems);
        }
        return orderItems;
    }


    public void deleteOrder(Long id) throws SQLException {
        if (!orderDao.deleteById(id)) {
            throw new OrderNotFoundException(ORDER_NOT_FOUND_MSG + id);
        }
        orderItemDao.deleteAllByOrderId(id);
    }
}