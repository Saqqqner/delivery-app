package ru.adel.deliveryapp.models;

import java.util.List;

public class Customer {
    private Long id;
    private String username;
    private String email;
    private List<Order> order;

    public Customer() {
    }

    public Customer(Long id, String username, String email, List<Order> order) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.order = order;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Order> getOrder() {
        return order;
    }

    public void setOrder(List<Order> order) {
        this.order = order;
    }
}
