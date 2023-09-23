package ru.adel.deliveryapp.models;


import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private Long id;
    private Customer customer;
    private LocalDateTime createdAt;
    private Address shippingAddress;
    private List<OrderItem> orderItems;

    public Order() {
    }

    public Order(Long id, Customer customer, LocalDateTime createdAt, Address shippingAddress, List<OrderItem> orderItems) {
        this.id = id;
        this.customer = customer;
        this.createdAt = createdAt;
        this.shippingAddress = shippingAddress;
        this.orderItems = orderItems;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
