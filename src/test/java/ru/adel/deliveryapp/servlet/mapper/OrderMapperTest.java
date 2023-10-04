package ru.adel.deliveryapp.servlet.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.adel.deliveryapp.entity.*;
import ru.adel.deliveryapp.servlet.dto.OrderViewDTO;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderMapperTest {
    private Customer customer;
    private Address address;
    private OrderItem orderItem;
    private Order order;
    private Order order1;
    private List<Order> orders;

    private final OrderMapper orderMapper = OrderMapper.INSTANCE;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1L);

        address = new Address();
        address.setId(2L);

        orderItem = new OrderItem();
        orderItem.setProduct(new Product(1L, "AA", "AA", BigDecimal.TEN, 100L));
        orderItem.setQuantity(3L);
        orderItem.calculateTotalPrice();

        order = new Order();
        order1 = new Order();
        order.setCustomer(customer);
        order.setShippingAddress(address);
        order.setOrderItems(Arrays.asList(orderItem));
        order.setStatus("PROCESSING");
        order1.setCustomer(customer);
        order1.setShippingAddress(address);
        order1.setOrderItems(Arrays.asList(orderItem));
        order1.setStatus("DELIVERED");
        orders = Arrays.asList(order, order1);
    }

    @Test
    void orderToOrderViewDTO() {

        OrderViewDTO orderViewDTO = orderMapper.orderToOrderViewDTO(order);

        Assertions.assertEquals(order.getCustomer().getId(), orderViewDTO.getCustomerId());
        Assertions.assertEquals(order.getShippingAddress().getId(), orderViewDTO.getAddressId());

    }

    @Test
    void orderListToOrderDTOList() {
        List<OrderViewDTO> orderViewDTOS = orderMapper.orderListToOrderDTOList(orders);
        Assertions.assertEquals(2, orderViewDTOS.size());
        Assertions.assertEquals(order.getShippingAddress().getId(), orderViewDTOS.get(0).getAddressId());
        Assertions.assertEquals(order1.getShippingAddress().getId(), orderViewDTOS.get(1).getAddressId());
        Assertions.assertEquals(order1.getStatus(), orderViewDTOS.get(1).getStatus());

    }


}
