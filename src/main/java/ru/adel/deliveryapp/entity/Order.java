package ru.adel.deliveryapp.entity;


import java.math.BigDecimal;
import java.util.List;

public class Order  {
    private Long id;
    private Customer customer;
    private Address shippingAddress;
    private List<OrderItem> orderItems;
    private String status;
    private BigDecimal totalPrice;

    public Order() {
    }

    public Order(Long id, Customer customer, Address shippingAddress, List<OrderItem> orderItems, String status) {
        this.id = id;
        this.customer = customer;
        this.shippingAddress = shippingAddress;
        this.orderItems = orderItems;
        this.status = status;
        this.calculateTotalPrice();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
        this.calculateTotalPrice();
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }



    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    private void calculateTotalPrice() {
        BigDecimal total = BigDecimal.ZERO;
        for (OrderItem item : orderItems) {
            total = total.add(item.getProductTotalPrice());
        }
        this.totalPrice = total;
    }
}
