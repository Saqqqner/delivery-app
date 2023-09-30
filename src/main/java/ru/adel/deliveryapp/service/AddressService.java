package ru.adel.deliveryapp.service;

import ru.adel.deliveryapp.entity.Address;

import java.sql.SQLException;
import java.util.List;

public interface AddressService {
    Address getAddressById(Long id) throws SQLException;

    void deleteById(Long id) throws SQLException;

    void update(Long id, Address address) throws SQLException;

    List<Address> findAll() throws SQLException;

    void save(Address address) throws SQLException;
}
