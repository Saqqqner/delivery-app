package ru.adel.deliveryapp.servlet.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.adel.deliveryapp.entity.Address;
import ru.adel.deliveryapp.servlet.dto.AddressDTO;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AddressMapperTest {
    private Address address;
    private Address address1;
    private AddressDTO addressDTO;
    private final AddressMapper mapper = AddressMapper.INSTANCE;

    @BeforeEach
    void setUp(){
        addressDTO = new AddressDTO();
        addressDTO.setStreet("123 Main St");
        addressDTO.setCity("City");
        address = new Address();
        address.setId(1L);
        address.setStreet("123 Main St");
        address.setCity("City");
        address1 = new Address();
        address1.setId(2L);
        address1.setStreet("123123 Main St");
        address1.setCity("City");

    }
    @Test
    void addressToAddressDTO() {
        // Arrange
        Address address = new Address();
        address.setId(1L);
        address.setStreet("123 Main St");
        address.setCity("City");

        // Act
        AddressDTO addressDTO = mapper.addressToAddressDTO(address);

        // Assert;
        Assertions.assertEquals(address.getStreet(), addressDTO.getStreet());
        Assertions.assertEquals(address.getCity(), addressDTO.getCity());
        Assertions.assertEquals(address.getApartmentNumber(), addressDTO.getApartmentNumber());
        Assertions.assertEquals(address.getHouseNumber(), addressDTO.getHouseNumber());
    }

    @Test
    void addressListTOAddressDTOList() {
        // Arrange


        List<Address> addressList = Arrays.asList(address1, address);

        // Act
        List<AddressDTO> addressDTOList = mapper.addressListTOAddressDTOList(addressList);

        // Assert
        Assertions.assertEquals(addressList.size(), addressDTOList.size());
    }

    @Test
    void addressDTOToAddress() {
        Address address = mapper.addressDTOToAddress(addressDTO);

        // Assert
        assertEquals(addressDTO.getStreet(), address.getStreet());
        assertEquals(addressDTO.getCity(), address.getCity());
        assertEquals(addressDTO.getApartmentNumber(), address.getApartmentNumber());
        assertEquals(addressDTO.getHouseNumber(), address.getHouseNumber());

    }
}
