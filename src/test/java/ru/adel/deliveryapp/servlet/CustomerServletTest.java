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
import ru.adel.deliveryapp.entity.Customer;
import ru.adel.deliveryapp.service.CustomerService;
import ru.adel.deliveryapp.servlet.dto.CreateCustomerDTO;
import ru.adel.deliveryapp.servlet.dto.CustomerViewDTO;
import ru.adel.deliveryapp.servlet.mapper.CustomerMapper;
import ru.adel.deliveryapp.util.CustomerNotFoundException;
import ru.adel.deliveryapp.util.DuplicateException;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@ExtendWith(MockitoExtension.class)
class CustomerServletTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerServlet customerServlet;

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

        Long customerId = 1L;
        CustomerViewDTO customerViewDTO = new CustomerViewDTO();
        customerViewDTO.setEmail("Email@Mail");
        Mockito.when(request.getRequestURI()).thenReturn("/customers/" + customerId);
        Mockito.when(customerService.getCustomerById(customerId)).thenReturn(new Customer());
        Mockito.when(customerMapper.customerToCustomerViewDTO(Mockito.any())).thenReturn(customerViewDTO);
        Mockito.when(response.getWriter()).thenReturn(printWriter);
        customerServlet.doGet(request, response);
        Mockito.verify(response).setContentType("application/json");
        Mockito.verify(response).setCharacterEncoding("UTF-8");
        Mockito.verify(response, Mockito.never()).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        Mockito.verify(response).getWriter();
        Mockito.verify(response.getWriter()).print(objectMapper.writeValueAsString(customerViewDTO));
        Mockito.verify(response.getWriter()).flush();
    }


    @Test
    void testDoGetWithInvalidId() throws ServletException, IOException, CustomerNotFoundException, SQLException {

        Mockito.when(request.getRequestURI()).thenReturn("/customers/invalidId");
        Mockito.when(response.getWriter()).thenReturn(printWriter);

        customerServlet.doGet(request, response);


        Mockito.verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        Mockito.verify(response.getWriter()).println("Invalid ID parameter");
    }

    @Test
    void testDoGetAllCustomers() throws ServletException, IOException, SQLException {

        Mockito.when(request.getRequestURI()).thenReturn("/customers");
        List<Customer> customers = Arrays.asList(
                new Customer(1L, "john.doe@example.com", "John Doe", new ArrayList<>()),
                new Customer(2L, "jane.smith@example.com", "Jane Smith", new ArrayList<>())
        );
        List<CustomerViewDTO> customerViewDTOS = Arrays.asList(
                new CustomerViewDTO("john.doe@example.com", "John Doe", new ArrayList<>()),
                new CustomerViewDTO("jane.smith@example.com", "Jane Smith", new ArrayList<>())
        );
        Mockito.when(customerService.findAll()).thenReturn(customers);
        Mockito.when(customerMapper.customersToCustomersViewDTO(customers)).thenReturn(customerViewDTOS);
        Mockito.when(response.getWriter()).thenReturn(printWriter);


        customerServlet.doGet(request, response);


        Mockito.verify(response).setContentType("application/json");
        Mockito.verify(response).setCharacterEncoding("UTF-8");
        Mockito.verify(response, Mockito.never()).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        Mockito.verify(response.getWriter()).print(objectMapper.writeValueAsString(customerViewDTOS));
        Mockito.verify(response.getWriter()).flush();
    }

    @Test
    void testDoPost() throws ServletException, IOException, DuplicateException, SQLException {
        CreateCustomerDTO createCustomerDTO = new CreateCustomerDTO();
        createCustomerDTO.setUsername("John Doe");
        Mockito.when(request.getInputStream()).thenReturn(getInputStream(createCustomerDTO));
        customerServlet.doPost(request, response);
        Mockito.verify(response).setContentType("application/json");
        Mockito.verify(response).setCharacterEncoding("UTF-8");
        Mockito.verify(response, Mockito.never()).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        Mockito.verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    void testDoPutWithValidId() throws ServletException, IOException, CustomerNotFoundException, DuplicateException, SQLException {
        Long customerId = 1L;
        CreateCustomerDTO updatedCustomerDTO = new CreateCustomerDTO();
        Mockito.when(request.getPathInfo()).thenReturn("/" + customerId);
        Mockito.when(request.getInputStream()).thenReturn(getInputStream(updatedCustomerDTO));
        customerServlet.doPut(request, response);
        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void testDoPutWithInvalidId() throws ServletException, IOException, CustomerNotFoundException, DuplicateException, SQLException {
        Mockito.when(request.getPathInfo()).thenReturn("/");
        Mockito.when(response.getWriter()).thenReturn(printWriter);
        customerServlet.doPut(request, response);
        Mockito.verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        Mockito.verify(response.getWriter()).println("Customer ID is required");
    }

    @Test
    void testDoDeleteWithValidId() throws ServletException, IOException, CustomerNotFoundException, SQLException {
        Long customerId = 1L;
        Mockito.when(request.getPathInfo()).thenReturn("/" + customerId);
        customerServlet.doDelete(request, response);
        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void testDoDeleteWithInvalidId() throws ServletException, IOException, CustomerNotFoundException, SQLException {
        Mockito.when(request.getPathInfo()).thenReturn("/");
        Mockito.when(response.getWriter()).thenReturn(printWriter);
        customerServlet.doDelete(request, response);
        Mockito.verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        Mockito.verify(response.getWriter()).println("Customer ID is required");
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