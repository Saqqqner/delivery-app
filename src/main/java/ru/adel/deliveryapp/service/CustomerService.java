package ru.adel.deliveryapp.service;

import ru.adel.deliveryapp.dto.CustomerDTO;
import ru.adel.deliveryapp.dto.CustomerViewDTO;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

public interface CustomerService extends Serializable {
    CustomerViewDTO getCustomerById(Long id) throws SQLException;
    void deleteById(Long id)throws SQLException;
    void update(Long id,CustomerDTO customerDTO)throws SQLException;
    List<CustomerViewDTO> findAll()throws SQLException;
    void save(CustomerDTO customerDTO)throws SQLException;
}
