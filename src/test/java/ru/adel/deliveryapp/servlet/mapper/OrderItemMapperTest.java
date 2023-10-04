package ru.adel.deliveryapp.servlet.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.adel.deliveryapp.entity.OrderItem;
import ru.adel.deliveryapp.entity.Product;
import ru.adel.deliveryapp.servlet.dto.OrderItemDTO;
import ru.adel.deliveryapp.servlet.dto.OrderItemViewDTO;

import java.math.BigDecimal;

class OrderItemMapperTest {

    private final OrderItemMapper mapper = OrderItemMapper.INSTANCE;

    @Test
    void orderItemToOrderItemDTO() {
        // Arrange
        OrderItem orderItem = new OrderItem();
        Product product = new Product();
        product.setId(1L);
        orderItem.setProduct(product);
        orderItem.setQuantity(2L);

        // Act
        OrderItemDTO orderItemDTO = mapper.orderItemToOrderItemDTO(orderItem);

        // Assert
        Assertions.assertEquals(product.getId(), orderItemDTO.getProductId());
        Assertions.assertEquals(orderItem.getQuantity(), orderItemDTO.getQuantity());
    }

    @Test
    void orderItemToOrderItemViewDTO() {
        // Arrange
        OrderItem orderItem = new OrderItem();
        Product product = new Product();
        product.setId(1L);
        product.setPrice(BigDecimal.TEN);
        orderItem.setProduct(product);
        orderItem.setQuantity(2L);
        orderItem.calculateTotalPrice();

        // Act
        OrderItemViewDTO orderItemViewDTO = mapper.orderItemToOrderItemViewDTO(orderItem);

        // Assert
        Assertions.assertEquals(product.getId(), orderItemViewDTO.getProductId());
        Assertions.assertEquals(orderItem.getQuantity(), orderItemViewDTO.getQuantity());
        Assertions.assertEquals(orderItem.getProductTotalPrice(), orderItemViewDTO.getProductTotalPrice());
    }

    @Test
    void updateOrderItemFromProduct() {
        // Arrange
        Product product = new Product();
        product.setId(1L);

        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setProductId(1L);
        orderItemDTO.setQuantity(2L);

        // Act
        mapper.updateOrderItemFromProduct(product, orderItemDTO);

        // Assert
        Assertions.assertEquals(product.getId(), orderItemDTO.getProductId());

    }

    @Test
    void updateOrderItemFromProductViewDTO() {
        // Arrange
        Product product = new Product();
        product.setId(1L);

        OrderItemViewDTO orderItemViewDTO = new OrderItemViewDTO();
        orderItemViewDTO.setProductId(1L);
        orderItemViewDTO.setQuantity(2L);

        // Act
        mapper.updateOrderItemFromProduct(product, orderItemViewDTO);

        // Assert
        Assertions.assertEquals(product.getId(), orderItemViewDTO.getProductId());
        // You may add more assertions based on your mapping logic
    }
}