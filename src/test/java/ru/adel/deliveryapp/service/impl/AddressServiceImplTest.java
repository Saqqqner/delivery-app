package ru.adel.deliveryapp.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.adel.deliveryapp.dao.AddressDao;
import ru.adel.deliveryapp.entity.Address;
import ru.adel.deliveryapp.util.AddressNotFoundException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AddressServiceImplTest {
    @Mock
    private AddressDao addressDao;

    @InjectMocks
    private AddressServiceImpl addressService;

    @Test
    void getAddressById_shouldGetAddressById() throws SQLException {
        // Arrange
        Long addressId = 1L;
        Address mockAddress = new Address(addressId, "Moscow", "Красная площадь", "1", "1");
        Mockito.when(addressDao.findById(addressId)).thenReturn(Optional.of(mockAddress));
        // Act
        Address result = addressService.getAddressById(addressId);
        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(mockAddress, result);
    }

    @Test
    void getAddressById_shouldThrowAddressNotFoundException() throws SQLException {
        // Arrange
        Long addressId = 1L;
        Mockito.when(addressDao.findById(addressId)).thenReturn(Optional.empty());

        // Act and Assert
        Assertions.assertThrows(AddressNotFoundException.class, () -> addressService.getAddressById(addressId));
    }

    @Test
    void deleteById_shouldDeleteById() throws SQLException {
        // Arrange
        Long addressId = 1L;
        Mockito.when(addressDao.deleteById(addressId)).thenReturn(true);

        // Act
        Assertions.assertDoesNotThrow(() -> addressService.deleteById(addressId));
    }

    @Test
    void deleteById_shouldThrowAddressNotFoundException() throws SQLException {
        // Arrange
        Long addressId = 1L;
        Mockito.when(addressDao.deleteById(addressId)).thenReturn(false);

        // Act and Assert
        Assertions.assertThrows(AddressNotFoundException.class, () -> addressService.deleteById(addressId));
    }

    @Test
    void updateAddress_shouldUpdateAddress() throws SQLException {
        // Arrange
        Long addressId = 1L;
        Address mockAddress = new Address(addressId, "Moscow", "Красная площадь", "1", "1");
        Address newAddress = new Address(addressId, "UpdatedCity", "UpdatedStreet", "2", "2");

        Mockito.when(addressDao.findById(addressId)).thenReturn(Optional.of(mockAddress));

        // Act
        Assertions.assertDoesNotThrow(() -> addressService.update(addressId, newAddress));

        // Assert
        Mockito.verify(addressDao, Mockito.times(1)).update(Mockito.any(Address.class));
    }

    @Test
    void updateAddress_shouldThrowAddressNotFoundException() throws SQLException {
        // Arrange
        Long addressId = 1L;
        Address newAddress = new Address(addressId, "UpdatedCity", "UpdatedStreet", "2", "2");

        Mockito.when(addressDao.findById(addressId)).thenReturn(Optional.empty());

        // Act and Assert
        Assertions.assertThrows(AddressNotFoundException.class, () -> addressService.update(addressId, newAddress));
        Mockito.verify(addressDao, Mockito.never()).update(Mockito.any(Address.class));
    }

    @Test
    void findAll_shouldFindAllAddresses() throws SQLException {
        // Arrange
        List<Address> mockAddresses = new ArrayList<>();
        mockAddresses.add(new Address(1L, "City1", "Street1", "1", "1"));
        mockAddresses.add(new Address(2L, "City2", "Street2", "2", "2"));

        Mockito.when(addressDao.findAll()).thenReturn(mockAddresses);

        // Act
        List<Address> result = addressService.findAll();

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(mockAddresses.size(), result.size());
        Assertions.assertEquals(mockAddresses, result);
    }

    @Test
    void save_shouldSaveAddress() throws SQLException {
        // Arrange
        Address newAddress = new Address(null, "NewCity", "NewStreet", "3", "3");

        // Act
        Assertions.assertDoesNotThrow(() -> addressService.save(newAddress));

        // Assert
        Mockito.verify(addressDao, Mockito.times(1)).save(Mockito.any(Address.class));
    }
}
