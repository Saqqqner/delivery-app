package ru.adel.deliveryapp.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class OrderItemDTO implements Serializable {
    private ProductDTO product;
    private Long quantity;
    private BigDecimal totalPrice;

    public OrderItemDTO() {
    }

    public OrderItemDTO(ProductDTO product, Long quantity, BigDecimal totalPrice) {
        this.product = product;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
