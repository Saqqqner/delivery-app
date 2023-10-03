package ru.adel.deliveryapp.entity;


import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) && Objects.equals(customer, order.customer) && Objects.equals(shippingAddress, order.shippingAddress) && Objects.equals(orderItems, order.orderItems) && Objects.equals(status, order.status) && Objects.equals(totalPrice, order.totalPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customer, shippingAddress, orderItems, status, totalPrice);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", customer=" + customer +
                ", shippingAddress=" + shippingAddress +
                ", orderItems=" + orderItems +
                ", status='" + status + '\'' +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
