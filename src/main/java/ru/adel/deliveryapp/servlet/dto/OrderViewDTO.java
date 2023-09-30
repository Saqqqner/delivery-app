package ru.adel.deliveryapp.servlet.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class OrderViewDTO implements Serializable {
    private Long customerId;
    private Long addressId;
    private List<OrderItemViewDTO> orderItems;
    private  BigDecimal totalPrice;
    private String status;

    public OrderViewDTO() {
    }

    public OrderViewDTO(Long customerId, Long addressId, List<OrderItemViewDTO> orderItems, BigDecimal totalPrice, String status) {
        this.customerId = customerId;
        this.addressId = addressId;
        this.orderItems = orderItems;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }


    public List<OrderItemViewDTO> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemViewDTO> orderItems) {
        this.orderItems = orderItems;
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
}
