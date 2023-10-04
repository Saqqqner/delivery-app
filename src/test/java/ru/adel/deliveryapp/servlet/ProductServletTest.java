package ru.adel.deliveryapp.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.adel.deliveryapp.entity.Product;
import ru.adel.deliveryapp.service.ProductService;
import ru.adel.deliveryapp.servlet.dto.ProductByStockDTO;
import ru.adel.deliveryapp.servlet.dto.ProductDTO;
import ru.adel.deliveryapp.servlet.mapper.ProductMapper;
import ru.adel.deliveryapp.util.DuplicateException;
import ru.adel.deliveryapp.util.ProductNotFoundException;

import java.io.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ProductServletTest {
    @Mock
    private ProductService productService;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServlet productServlet;

    @Mock
    private HttpServletRequest request;
    @Mock
    private PrintWriter printWriter;

    @Mock
    private HttpServletResponse response;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void testDoGetWithValidId() throws Exception {
        // Arrange
        Long productId = 1L;
        ProductDTO productDTO = new ProductDTO();
        Mockito.when(request.getRequestURI()).thenReturn("/products/" + productId);
        Mockito.when(productService.getProductById(productId)).thenReturn(new Product());
        Mockito.when(productMapper.productToProductDTO(Mockito.any())).thenReturn(productDTO);
        Mockito.when(response.getWriter()).thenReturn(printWriter);

        // Act
        productServlet.doGet(request, response);

        // Assert
        Mockito.verify(response).setContentType("application/json");
        Mockito.verify(response).setCharacterEncoding("UTF-8");
        Mockito.verify(response, Mockito.never()).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        Mockito.verify(response).getWriter();
        Mockito.verify(response.getWriter()).print(objectMapper.writeValueAsString(productDTO));
        Mockito.verify(response.getWriter()).flush();
    }

    @Test
    void testDoGetWithInvalidId() throws Exception {
        // Arrange
        Mockito.when(request.getRequestURI()).thenReturn("/products/invalidId");
        Mockito.when(response.getWriter()).thenReturn(printWriter);

        // Act
        productServlet.doGet(request, response);

        // Assert
        Mockito.verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        Mockito.verify(response.getWriter()).println("Invalid ID parameter");
    }

    @Test
    void testDoGetAllProducts() throws Exception {
        // Arrange
        Mockito.when(request.getRequestURI()).thenReturn("/orders");
        List<Product> products = Arrays.asList(
                new Product(1L, "Name", "Description", BigDecimal.TEN, 100L),
                new Product(1L, "Name1", "Description1", BigDecimal.TEN, 200L)
        );
        List<ProductDTO> productDTOS = Arrays.asList(
                new ProductDTO("Name", "Description", BigDecimal.TEN),
                new ProductDTO("Name1", "Description1", BigDecimal.TEN)
        );
        Mockito.when(productService.findAll()).thenReturn(products);
        Mockito.when(productMapper.productsToProductsDTO(products)).thenReturn(productDTOS);
        Mockito.when(response.getWriter()).thenReturn(printWriter);

        // Act
        productServlet.doGet(request, response);

        // Assert
        Mockito.verify(response).setContentType("application/json");
        Mockito.verify(response).setCharacterEncoding("UTF-8");
        Mockito.verify(response, Mockito.never()).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        Mockito.verify(response.getWriter()).print(objectMapper.writeValueAsString(productDTOS));
        Mockito.verify(response.getWriter()).flush();
    }

    @Test
    void testDoPost() throws Exception {
        // Arrange
        ProductByStockDTO productByStockDTO = new ProductByStockDTO();
        Mockito.when(request.getInputStream()).thenReturn(getInputStream(productByStockDTO));

        // Act
        productServlet.doPost(request, response);

        // Assert
        Mockito.verify(response).setContentType("application/json");
        Mockito.verify(response).setCharacterEncoding("UTF-8");
        Mockito.verify(response, Mockito.never()).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        Mockito.verify(productService).save(Mockito.any());
        Mockito.verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    void testDoDeleteWithValidId() throws ServletException, IOException, ProductNotFoundException, SQLException {
        // Arrange
        Long productId = 1L;
        Mockito.when(request.getPathInfo()).thenReturn("/" + productId);

        // Act
        productServlet.doDelete(request, response);

        // Assert
        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void testDoDeleteWithInvalidId() throws ServletException, IOException, ProductNotFoundException, SQLException {
        // Arrange
        Mockito.when(request.getPathInfo()).thenReturn("/");
        Mockito.when(response.getWriter()).thenReturn(printWriter);


        // Act
        productServlet.doDelete(request, response);

        // Assert
        Mockito.verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void testDoPutWithValidId() throws ServletException, IOException, ProductNotFoundException, DuplicateException, SQLException {
        // Arrange
        Long productId = 1L;
        ProductByStockDTO productByStockDTO = new ProductByStockDTO();
        Mockito.when(request.getPathInfo()).thenReturn("/" + productId);
        Mockito.when(request.getInputStream()).thenReturn(getInputStream(productByStockDTO));

        // Act
        productServlet.doPut(request, response);

        // Assert
        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void testDoPutWithInvalidId() throws ServletException, IOException, ProductNotFoundException, DuplicateException, SQLException {
        // Arrange
        Mockito.when(request.getPathInfo()).thenReturn("/");
        Mockito.when(response.getWriter()).thenReturn(printWriter);

        // Act
        productServlet.doPut(request, response);

        // Assert
        Mockito.verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        Mockito.verify(response.getWriter()).println("Product ID is required");
    }


    private ServletInputStream getInputStream(Object obj) throws IOException {
        StringWriter writer = new StringWriter();
        objectMapper.writeValue(writer, obj);
        return new TestServletInputStream(new ByteArrayInputStream(writer.toString().getBytes()));
    }

    private static class TestServletInputStream extends ServletInputStream {
        private final InputStream inputStream;

        public TestServletInputStream(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        public int read() throws IOException {
            return inputStream.read();
        }

        @Override
        public boolean isFinished() {
            return false;
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setReadListener(ReadListener readListener) {
        }
    }
}
