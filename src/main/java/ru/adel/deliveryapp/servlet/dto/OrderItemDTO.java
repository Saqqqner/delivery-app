package ru.adel.deliveryapp.servlet.dto;

import java.io.Serializable;

public class OrderItemDTO implements Serializable {
    private Long productId;
    private Long quantity;


    public OrderItemDTO() {
    }

    public OrderItemDTO(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;

    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

}
