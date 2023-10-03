package ru.adel.deliveryapp.servlet.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import ru.adel.deliveryapp.servlet.dto.OrderItemDTO;
import ru.adel.deliveryapp.servlet.dto.OrderItemViewDTO;
import ru.adel.deliveryapp.entity.OrderItem;
import ru.adel.deliveryapp.entity.Product;

@Mapper
public interface OrderItemMapper {
    OrderItemMapper INSTANCE = Mappers.getMapper(OrderItemMapper.class);

    @Mapping(target = "productId", source = "product.id")
    OrderItemDTO orderItemToOrderItemDTO(OrderItem orderItem);

    @Mapping(target = "productId", source = "product.id")
    OrderItemViewDTO orderItemToOrderItemViewDTO(OrderItem orderItem);

    @Mapping(target = "productId", source = "product.id")
    void updateOrderItemFromProduct(Product product, @MappingTarget OrderItemDTO orderItemDTO);
    @Mapping(target = "productId", source = "product.id")
    void updateOrderItemFromProduct(Product product, @MappingTarget OrderItemViewDTO orderItemDTO);
}
