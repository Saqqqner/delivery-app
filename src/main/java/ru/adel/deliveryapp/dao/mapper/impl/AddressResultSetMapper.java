package ru.adel.deliveryapp.dao.mapper.impl;

import ru.adel.deliveryapp.dao.mapper.EntityResultSetMapper;
import ru.adel.deliveryapp.entity.Address;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AddressResultSetMapper implements EntityResultSetMapper<Address> {
    @Override
    public Address map(ResultSet resultSet) throws SQLException {
        Address address = new Address();
        address.setId(resultSet.getLong("id"));
        address.setCity(resultSet.getString("city"));
        address.setStreet(resultSet.getString("street"));
        address.setHouseNumber(resultSet.getString("house_number"));
        address.setApartmentNumber(resultSet.getString("apartment_number"));
        return address;
    }
}
