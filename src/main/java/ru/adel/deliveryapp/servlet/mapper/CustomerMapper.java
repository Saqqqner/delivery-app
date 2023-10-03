package ru.adel.deliveryapp.servlet.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.adel.deliveryapp.servlet.dto.CreateCustomerDTO;
import ru.adel.deliveryapp.servlet.dto.CustomerViewDTO;
import ru.adel.deliveryapp.entity.Customer;

import java.io.Serializable;
import java.util.List;

@Mapper(uses = OrderMapper.class)
public interface CustomerMapper extends Serializable {
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);


    @Mapping(source = "order", target = "order")
    CustomerViewDTO customerToCustomerViewDTO(Customer customer);
    @Mapping(source = "order", target = "order")
    List<CustomerViewDTO> customersToCustomersViewDTO(List<Customer> customers);


    Customer customerDTOToCustomer(CreateCustomerDTO customerDTO);
}

