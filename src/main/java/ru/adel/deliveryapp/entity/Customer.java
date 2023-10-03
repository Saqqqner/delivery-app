package ru.adel.deliveryapp.entity;

import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(id, customer.id) && Objects.equals(username, customer.username) && Objects.equals(email, customer.email) && Objects.equals(order, customer.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email, order);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", order=" + order +
                '}';
    }
}
