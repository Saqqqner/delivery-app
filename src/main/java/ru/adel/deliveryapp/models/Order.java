package ru.adel.deliveryapp.models;


import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private Long id;
    private Customer customer;
    private LocalDateTime createdAt;
    private Address shippingAddress;
    private List<Product> products;

    public Order() {
    }

    public Order(Long id, Customer customer, LocalDateTime createdAt, Address shippingAddress, List<Product> products) {
        this.id = id;
        this.customer = customer;
        this.createdAt = createdAt;
        this.shippingAddress = shippingAddress;
        this.products = products;
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

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
