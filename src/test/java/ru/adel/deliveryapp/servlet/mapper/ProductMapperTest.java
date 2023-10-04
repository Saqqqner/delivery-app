package ru.adel.deliveryapp.servlet.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.adel.deliveryapp.entity.Product;
import ru.adel.deliveryapp.servlet.dto.ProductByStockDTO;
import ru.adel.deliveryapp.servlet.dto.ProductDTO;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductMapperTest {

    private ProductMapper productMapper;
    private Product product;

    @BeforeEach
    void setUp() {
        productMapper = Mappers.getMapper(ProductMapper.class);
        product = createProduct();
    }

    @Test
    void productToProductDTO() {

        ProductDTO productDTO = productMapper.productToProductDTO(product);

        Assertions.assertEquals(product.getName(), productDTO.getName());
        Assertions.assertEquals(product.getDescription(), productDTO.getDescription());
        Assertions.assertEquals(product.getPrice(), productDTO.getPrice());
    }

    @Test
    void productToProductByStockDTO() {
        // Act
        ProductByStockDTO productByStockDTO = productMapper.productToProductByStockDTO(product);

        Assertions.assertEquals(product.getName(), productByStockDTO.getName());
        Assertions.assertEquals(product.getDescription(), productByStockDTO.getDescription());
        Assertions.assertEquals(product.getPrice(), productByStockDTO.getPrice());
        Assertions.assertEquals(product.getStock(), productByStockDTO.getStock());
    }

    @Test
    void productsToProductsByStockDTO() {
        // Arrange
        List<Product> productList = Arrays.asList(product, product);

        // Act
        List<ProductByStockDTO> productsByStockDTOList = productMapper.productsToProductsByStockDTO(productList);

        // Assert
        Assertions.assertEquals(productList.size(), productsByStockDTOList.size());
        Assertions.assertEquals(productList.get(0).getStock(),productsByStockDTOList.get(0).getStock());
        Assertions.assertEquals(productList.get(1).getStock(),productsByStockDTOList.get(1).getStock());
        Assertions.assertEquals(productList.get(0).getPrice(),productsByStockDTOList.get(0).getPrice());
        Assertions.assertEquals(productList.get(1).getPrice(),productsByStockDTOList.get(1).getPrice());
    }

    @Test
    void productsToProductsDTO() {

        List<Product> productList = Arrays.asList(product, product);


        List<ProductDTO> productsDTOList = productMapper.productsToProductsDTO(productList);


        Assertions.assertEquals(productList.size(), productsDTOList.size());
        Assertions.assertEquals(productList.get(0).getPrice(),productsDTOList.get(0).getPrice());
        Assertions.assertEquals(productList.get(1).getPrice(),productsDTOList.get(1).getPrice());

    }

    private Product createProduct() {
        // Create sample data for the Product
        Product product = new Product();
        product.setName("Test Product");
        product.setDescription("Product Description");
        product.setPrice(BigDecimal.TEN);
        product.setStock(100L);

        return product;
    }
}
