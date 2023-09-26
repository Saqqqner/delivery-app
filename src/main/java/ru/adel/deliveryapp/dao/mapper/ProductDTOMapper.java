package ru.adel.deliveryapp.dao.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.adel.deliveryapp.dto.ProductByStockDTO;
import ru.adel.deliveryapp.dto.ProductDTO;
import ru.adel.deliveryapp.models.Product;

import java.io.Serializable;

@Mapper
public interface ProductDTOMapper extends Serializable {
    ProductDTOMapper INSTANCE = Mappers.getMapper(ProductDTOMapper.class);



    ProductDTO productToProductDTO(Product product);
    @Mapping(target = "stock", ignore = true)
    ProductByStockDTO productToProductByStockDTO(Product product);

    Product productDTOToProduct(ProductByStockDTO product);
}

