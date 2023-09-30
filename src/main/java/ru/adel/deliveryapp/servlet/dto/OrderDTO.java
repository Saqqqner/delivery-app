package ru.adel.deliveryapp.servlet.dto;

import java.io.Serializable;
import java.util.List;

public class OrderDTO implements Serializable {
    private Long customerId;
    private Long addressId;
    private List<OrderItemDTO> orderItems;

    private String status;

    public OrderDTO() {
    }

    public OrderDTO(Long customerId, Long addressId, List<OrderItemDTO> orderItems, String status) {
        this.customerId = customerId;
        this.addressId = addressId;
        this.orderItems = orderItems;

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

    public List<OrderItemDTO> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemDTO> orderItems) {
        this.orderItems = orderItems;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
