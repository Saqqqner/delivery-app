package ru.adel.deliveryapp.servlet.dto;

import java.io.Serializable;

public class CreateCustomerDTO implements Serializable {
    private String username;
    private String email;

    public CreateCustomerDTO(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public CreateCustomerDTO() {
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
}
