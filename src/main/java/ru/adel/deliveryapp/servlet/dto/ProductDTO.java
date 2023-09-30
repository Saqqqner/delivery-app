package ru.adel.deliveryapp.servlet.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class ProductDTO implements Serializable {
    private String name;
    private String description;
    private BigDecimal price;


    public ProductDTO() {
    }

    public ProductDTO(String name, String description, BigDecimal price) {
        this.name = name;
        this.description = description;
        this.price = price;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }



}