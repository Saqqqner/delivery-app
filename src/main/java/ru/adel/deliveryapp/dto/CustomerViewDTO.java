package ru.adel.deliveryapp.dto;

import java.io.Serializable;
import java.util.List;

public class CustomerViewDTO implements Serializable {
    private String username;
    private String email;
    private List<OrderDTO> orderList;

    public CustomerViewDTO() {
    }

    public CustomerViewDTO(String username, String email, List<OrderDTO> orderList) {
        this.username = username;
        this.email = email;
        this.orderList = orderList;
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

    public List<OrderDTO> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<OrderDTO> orderList) {
        this.orderList = orderList;
    }
}
