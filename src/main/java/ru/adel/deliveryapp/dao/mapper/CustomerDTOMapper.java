package ru.adel.deliveryapp.dao.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.adel.deliveryapp.dto.CustomerDTO;
import ru.adel.deliveryapp.dto.CustomerViewDTO;
import ru.adel.deliveryapp.models.Customer;

import java.io.Serializable;

@Mapper
public interface CustomerDTOMapper extends Serializable {
    CustomerDTOMapper INSTANCE = Mappers.getMapper(CustomerDTOMapper.class);

    @Mapping(target = "orderList", ignore = true)
    CustomerViewDTO customerToCustomerViewDTO(Customer customer);

    CustomerDTO customerToCustomerDTO(Customer customer);

    Customer customerDTOToCustomer(CustomerDTO customerDTO);
}

