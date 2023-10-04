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
import ru.adel.deliveryapp.entity.Address;
import ru.adel.deliveryapp.service.AddressService;
import ru.adel.deliveryapp.servlet.dto.AddressDTO;
import ru.adel.deliveryapp.servlet.mapper.AddressMapper;
import ru.adel.deliveryapp.util.CustomerNotFoundException;
import ru.adel.deliveryapp.util.DuplicateException;

import java.io.*;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class AddressServletTest {
    @Mock
    private AddressService addressService;

    @Mock
    private AddressMapper addressMapper;

    @InjectMocks
    private AddressServlet addressServlet;

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
    void testDoGetWithValidId() throws ServletException, IOException, CustomerNotFoundException, SQLException {
        // Arrange
        Long addressId = 1L;
        AddressDTO addressDTO = new AddressDTO();
        Mockito.when(request.getRequestURI()).thenReturn("/address/" + addressId);
        Mockito.when(addressService.getAddressById(addressId)).thenReturn(new Address());
        Mockito.when(addressMapper.addressToAddressDTO(Mockito.any())).thenReturn(addressDTO);
        Mockito.when(response.getWriter()).thenReturn(printWriter);

        addressServlet.doGet(request, response);

        Mockito.verify(response).setContentType("application/json");
        Mockito.verify(response).setCharacterEncoding("UTF-8");
        Mockito.verify(response, Mockito.never()).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        Mockito.verify(response).getWriter();
        Mockito.verify(response.getWriter()).print(objectMapper.writeValueAsString(addressDTO));
        Mockito.verify(response.getWriter()).flush();
    }

    @Test
    void testDoGetWithInvalidId() throws ServletException, IOException, CustomerNotFoundException, SQLException {
        // Arrange
        Mockito.when(request.getRequestURI()).thenReturn("/addresses/invalidId");
        Mockito.when(response.getWriter()).thenReturn(printWriter);
        // Act
        addressServlet.doGet(request, response);

        // Assert
        Mockito.verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        Mockito.verify(response.getWriter()).println("Invalid ID parameter");
    }

    @Test
    void testDoGetAllAddresses() throws ServletException, IOException, SQLException {
        // Arrange
        Mockito.when(request.getRequestURI()).thenReturn("/addresses");
        List<Address> addresses = Arrays.asList(
                new Address(1L, "123", "123", "123", "123"),
                new Address(2L, "1232", "1232", "1232", "1232")
        );
        List<AddressDTO> addressDTOS = Arrays.asList(
                new AddressDTO("123", "123", "123", "123"),
                new AddressDTO("1232", "1232", "1232", "1232")
        );
        Mockito.when(addressService.findAll()).thenReturn(addresses);
        Mockito.when(addressMapper.addressListTOAddressDTOList(addresses)).thenReturn(addressDTOS);
        Mockito.when(response.getWriter()).thenReturn(printWriter);

        // Act
        addressServlet.doGet(request, response);

        // Assert
        Mockito.verify(response).setContentType("application/json");
        Mockito.verify(response).setCharacterEncoding("UTF-8");
        Mockito.verify(response, Mockito.never()).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        Mockito.verify(response.getWriter()).print(objectMapper.writeValueAsString(addressDTOS));
        Mockito.verify(response.getWriter()).flush();
    }

    @Test
    void testDoPost() throws ServletException, IOException, DuplicateException, SQLException {
        // Arrange
        AddressDTO addressDTO = new AddressDTO();
        Mockito.when(request.getInputStream()).thenReturn(getInputStream(addressDTO));

        // Act
        addressServlet.doPost(request, response);

        // Assert
        Mockito.verify(response).setContentType("application/json");
        Mockito.verify(response).setCharacterEncoding("UTF-8");
        Mockito.verify(response, Mockito.never()).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        Mockito.verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    void testDoPutWithValidId() throws ServletException, IOException, CustomerNotFoundException, DuplicateException, SQLException {
        // Arrange
        Long addressId = 1L;
        AddressDTO addressDTO = new AddressDTO();
        Mockito.when(request.getPathInfo()).thenReturn("/" + addressId);
        Mockito.when(request.getInputStream()).thenReturn(getInputStream(addressDTO));

        // Act
        addressServlet.doPut(request, response);

        // Assert
        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void testDoPutWithInvalidId() throws ServletException, IOException, CustomerNotFoundException, DuplicateException, SQLException {
        // Arrange
        Mockito.when(request.getPathInfo()).thenReturn("/");
        Mockito.when(response.getWriter()).thenReturn(printWriter);
        // Act
        addressServlet.doPut(request, response);

        // Assert
        Mockito.verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        Mockito.verify(response.getWriter()).println("Address ID is required");
    }

    @Test
    void testDoDeleteWithValidId() throws ServletException, IOException, CustomerNotFoundException, SQLException {
        // Arrange
        Long addressId = 1L;
        Mockito.when(request.getPathInfo()).thenReturn("/" + addressId);
        // Act
        addressServlet.doDelete(request, response);

        // Assert
        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void testDoDeleteWithInvalidId() throws ServletException, IOException, CustomerNotFoundException, SQLException {
        // Arrange
        Mockito.lenient().when(response.getWriter()).thenReturn(printWriter);
        Mockito.lenient().when(request.getPathInfo()).thenReturn("/");


        // Act
        addressServlet.doDelete(request, response);

        // Assert
        Mockito.verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
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
