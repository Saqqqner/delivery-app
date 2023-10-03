package ru.adel.deliveryapp.service.impl;

import ru.adel.deliveryapp.dao.AddressDao;
import ru.adel.deliveryapp.entity.Address;
import ru.adel.deliveryapp.service.AddressService;
import ru.adel.deliveryapp.util.AddressNotFoundException;

import java.sql.SQLException;
import java.util.List;

public class AddressServiceImpl implements AddressService {
    private static final String ADDRESS_NOT_FOUND_MSG = "Address not found with ID: ";
    private final AddressDao addressDao;


    public AddressServiceImpl(AddressDao addressDao) {
        this.addressDao = addressDao;
    }

    @Override
    public Address getAddressById(Long id) throws SQLException {
        return addressDao.findById(id)
                .orElseThrow(() -> new AddressNotFoundException(ADDRESS_NOT_FOUND_MSG + id));
    }


    @Override
    public void deleteById(Long id) throws SQLException {
        if (!addressDao.deleteById(id)) {
            throw new AddressNotFoundException(ADDRESS_NOT_FOUND_MSG + id);
        }
    }


    @Override
    public void update(Long id, Address newAddress) throws SQLException {
        Address address = addressDao.findById(id)
                .orElseThrow(() -> new AddressNotFoundException(ADDRESS_NOT_FOUND_MSG + id));
        updateField(newAddress, address);
        addressDao.update(address);
    }


    @Override
    public List<Address> findAll() throws SQLException {
        return addressDao.findAll();

    }


    @Override
    public void save(Address address) throws SQLException {
        addressDao.save(address);
    }

    private void updateField(Address newAddress, Address address) {
        address.setCity(newAddress.getCity());
        address.setStreet(newAddress.getStreet());
        address.setHouseNumber(newAddress.getApartmentNumber());
        address.setApartmentNumber(newAddress.getApartmentNumber());
    }
}
