package ru.adel.deliveryapp.servlet.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.adel.deliveryapp.servlet.dto.ProductByStockDTO;
import ru.adel.deliveryapp.servlet.dto.ProductDTO;
import ru.adel.deliveryapp.entity.Product;

import java.io.Serializable;
import java.util.List;

@Mapper
public interface ProductMapper extends Serializable {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);



    ProductDTO productToProductDTO(Product product);

    ProductByStockDTO productToProductByStockDTO(Product product);
    List<ProductByStockDTO> productsToProductsByStockDTO(List<Product> list);
    @Mapping(target = "stock", ignore = true)
    List<ProductDTO> productsToProductsDTO(List<Product> list);

    Product productByStockDTOToProduct(ProductByStockDTO product);
}

