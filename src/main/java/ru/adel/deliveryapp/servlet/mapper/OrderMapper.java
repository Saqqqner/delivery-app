package ru.adel.deliveryapp.servlet.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.adel.deliveryapp.servlet.dto.OrderViewDTO;
import ru.adel.deliveryapp.entity.Address;
import ru.adel.deliveryapp.entity.Customer;
import ru.adel.deliveryapp.entity.Order;

import java.util.List;

@Mapper(uses = OrderItemMapper.class)
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);


    // Остальные маппинги остаются неизменными

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "shippingAddress.id", target = "addressId")
    @Mapping(source = "orderItems", target = "orderItems")
        // Маппируем поле orderItems напрямую, так как типы совпадают
    OrderViewDTO orderToOrderViewDTO(Order order);


    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "shippingAddress.id", target = "addressId")
    @Mapping(source = "orderItems", target = "orderItems")
        // Маппируем поле orderItems напрямую, так как типы совпадают
    List<OrderViewDTO> orderListToOrderDTOList(List<Order> orders);


    default Long map(Customer customer) {
        return customer.getId();
    }


    default Long map(Address address) {
        return address.getId();
    }
}

