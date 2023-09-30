package ru.adel.deliveryapp.servlet.dto;

import java.io.Serializable;
import java.util.List;

public class CreateOrderDTO implements Serializable {
    private Long customerId;
    private AddressDTO address;
    private List<OrderItemDTO> orderItems;

    public CreateOrderDTO() {
    }

    public CreateOrderDTO(Long customerId, AddressDTO address, List<OrderItemDTO> orderItems) {
        this.customerId = customerId;
        this.address = address;
        this.orderItems = orderItems;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
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
}
