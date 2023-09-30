package ru.adel.deliveryapp.servlet.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class OrderItemViewDTO implements Serializable {
    private Long productId;
    private Long quantity;
    private BigDecimal productTotalPrice;

    public OrderItemViewDTO() {
    }

    public OrderItemViewDTO(Long productId, Long quantity, BigDecimal totalPrice) {
        this.productId = productId;
        this.quantity = quantity;
        this.productTotalPrice = totalPrice;
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

    public BigDecimal getProductTotalPrice() {
        return productTotalPrice;
    }

    public void setProductTotalPrice(BigDecimal productTotalPrice) {
        this.productTotalPrice = productTotalPrice;
    }
}
