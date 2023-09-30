package ru.adel.deliveryapp.servlet.dto;

import java.io.Serializable;
import java.util.List;

public class CustomerViewDTO implements Serializable {
    private String username;
    private String email;
    private List<OrderViewDTO> order;

    public CustomerViewDTO() {
    }

    public CustomerViewDTO(String username, String email, List<OrderViewDTO> order) {
        this.username = username;
        this.email = email;
        this.order = order;
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

    public List<OrderViewDTO> getOrder() {
        return order;
    }

    public void setOrder(List<OrderViewDTO> order) {
        this.order = order;
    }
}
