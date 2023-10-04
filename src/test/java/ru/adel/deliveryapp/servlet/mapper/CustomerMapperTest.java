package ru.adel.deliveryapp.servlet.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.adel.deliveryapp.entity.Customer;
import ru.adel.deliveryapp.servlet.dto.CreateCustomerDTO;
import ru.adel.deliveryapp.servlet.dto.CustomerViewDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomerMapperTest {
    private Customer customer;
    private CustomerViewDTO customerViewDTO;
    private CreateCustomerDTO createCustomerDTO;
    private final CustomerMapper mapper = CustomerMapper.INSTANCE;
    @BeforeEach
     void setUp(){
        customer=new Customer();
        createCustomerDTO=new CreateCustomerDTO();
        customerViewDTO=new CustomerViewDTO();
        customer.setUsername("Alfred");
        customer.setEmail("Alfred@mail.ru");
        customer.setOrder(new ArrayList<>());
        customerViewDTO.setUsername("Alfred");
        customerViewDTO.setEmail("Alfred@mail.ru");
        customerViewDTO.setOrder(new ArrayList<>());
        createCustomerDTO.setUsername("Alfred");
        createCustomerDTO.setEmail("Alfred@mail.ru");
    }
    @Test
    void customerToCustomerViewDTO() {
        // Act
        CustomerViewDTO customerViewDTO1 = mapper.customerToCustomerViewDTO(customer);

        Assertions.assertEquals(customer.getUsername(),customerViewDTO1.getUsername());
        Assertions.assertEquals(customer.getEmail(),customerViewDTO1.getEmail());
        Assertions.assertEquals(customer.getOrder(),customerViewDTO1.getOrder());
    }

    @Test
    void customersToCustomersViewDTO() {
        // Arrange

        Customer customer2 = new Customer();
        customer2.setUsername("Alfred123");
        customer2.setEmail("Alfred123@mail.ru");
        customer2.setOrder(new ArrayList<>());

        List<Customer> customers = Arrays.asList(customer, customer2);

        // Act
        List<CustomerViewDTO> customersViewDTOList = mapper.customersToCustomersViewDTO(customers);
        Assertions.assertEquals(2,customersViewDTOList.size());
        Assertions.assertEquals(customer2.getEmail(),customersViewDTOList.get(1).getEmail());
        Assertions.assertEquals(customer.getEmail(),customersViewDTOList.get(0).getEmail());
    }

    @Test
    void customerDTOToCustomer() {
        // Act
        Customer customer1 = mapper.customerDTOToCustomer(createCustomerDTO);

        Assertions.assertEquals(customer1.getUsername(),createCustomerDTO.getUsername());
        Assertions.assertEquals(customer1.getEmail(),createCustomerDTO.getEmail());
    }
}