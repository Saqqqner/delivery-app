package ru.adel.deliveryapp.dto;

import java.math.BigDecimal;
import java.util.List;

public class OrderDTO {
    private CustomerViewDTO customer;
    private AddressDTO address;
    private List<OrderItemDTO> orderItems;
    private  BigDecimal totalPrice;

    public OrderDTO() {
    }

    public OrderDTO(CustomerViewDTO customer, AddressDTO address, List<OrderItemDTO> orderItems, BigDecimal totalPrice) {
        this.customer = customer;
        this.address = address;
        this.orderItems = orderItems;
        this.totalPrice = totalPrice;
    }

    public CustomerViewDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerViewDTO customer) {
        this.customer = customer;
    }

    public AddressDTO getAddress() {
        return address;
    }

    public void setAddress(AddressDTO address) {
        this.address = address;
    }

    public List<OrderItemDTO> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemDTO> orderItems) {
        this.orderItems = orderItems;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
