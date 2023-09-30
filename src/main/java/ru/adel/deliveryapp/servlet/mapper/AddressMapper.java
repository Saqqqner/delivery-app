package ru.adel.deliveryapp.servlet.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.adel.deliveryapp.servlet.dto.AddressDTO;
import ru.adel.deliveryapp.entity.Address;

import java.util.List;

@Mapper
public interface AddressMapper {
    AddressMapper INSTANCE = Mappers.getMapper(AddressMapper.class);

    AddressDTO addressToAddressDTO(Address address);
    List<AddressDTO> addressListTOAddressDTOList(List<Address>addressList);


    Address addressDTOToAddress(AddressDTO addressDTO);
}

