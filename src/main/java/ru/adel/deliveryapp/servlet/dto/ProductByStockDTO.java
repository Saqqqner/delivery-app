package ru.adel.deliveryapp.servlet.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class ProductByStockDTO implements Serializable {
    private String name;
    private String description;
    private BigDecimal price;
    private Long stock;


    public ProductByStockDTO() {
    }

    public ProductByStockDTO( String name, String description, BigDecimal price, Long stock) {

        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;

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

    public Long getStock() {
        return stock;
    }

    public void setStock(Long stock) {
        this.stock = stock;
    }


}

