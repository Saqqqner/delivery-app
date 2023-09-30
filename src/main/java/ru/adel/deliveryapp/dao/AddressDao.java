package ru.adel.deliveryapp.dao;

import ru.adel.deliveryapp.entity.Address;

import java.sql.SQLException;

public interface AddressDao extends JdbcRepository<Address, Long> {
    Long save(Address address) throws SQLException;
}
