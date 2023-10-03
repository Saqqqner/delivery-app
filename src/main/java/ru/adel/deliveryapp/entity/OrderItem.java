package ru.adel.deliveryapp.entity;

import java.math.BigDecimal;
import java.util.Objects;

public class OrderItem  {
    private Long id;
    private Order order;
    private Product product;
    private Long quantity;
    private BigDecimal productTotalPrice;

    public OrderItem() {
    }

    public OrderItem(Long id, Order order, Product product, Long quantity, BigDecimal totalPrice) {
        this.id = id;
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.productTotalPrice = totalPrice;
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

    public BigDecimal getProductTotalPrice() {
        return productTotalPrice;
    }

    public void setProductTotalPrice(BigDecimal productTotalPrice) {
        this.productTotalPrice = productTotalPrice;
    }

    public void calculateTotalPrice() {
        this.productTotalPrice = this.product.getPrice().multiply(BigDecimal.valueOf(this.quantity));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return Objects.equals(id, orderItem.id) && Objects.equals(order, orderItem.order) && Objects.equals(product, orderItem.product) && Objects.equals(quantity, orderItem.quantity) && Objects.equals(productTotalPrice, orderItem.productTotalPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, order, product, quantity, productTotalPrice);
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", order=" + order +
                ", product=" + product +
                ", quantity=" + quantity +
                ", productTotalPrice=" + productTotalPrice +
                '}';
    }
}
