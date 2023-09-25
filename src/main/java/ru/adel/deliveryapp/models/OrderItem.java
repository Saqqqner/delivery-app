package ru.adel.deliveryapp.models;

import java.math.BigDecimal;

public class OrderItem {
    private Long id;
    private Order order;
    private Product product;
    private Long quantity;
    private BigDecimal totalPrice;

    public OrderItem() {
    }

    public OrderItem(Long id, Order order, Product product, Long quantity, BigDecimal totalPrice) {
        this.id = id;
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
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
        this.totalPrice = this.product.getPrice().multiply(BigDecimal.valueOf(this.quantity));
    }
}
