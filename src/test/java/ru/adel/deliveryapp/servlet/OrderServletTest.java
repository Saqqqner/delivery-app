package ru.adel.deliveryapp.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ReadListener;
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
import ru.adel.deliveryapp.entity.Order;
import ru.adel.deliveryapp.service.OrderService;
import ru.adel.deliveryapp.servlet.dto.OrderDTO;
import ru.adel.deliveryapp.servlet.dto.OrderViewDTO;
import ru.adel.deliveryapp.servlet.mapper.OrderMapper;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class OrderServletTest {

    @Mock
    private OrderService orderService;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderServlet orderServlet;

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
        Long orderId = 1L;
        OrderViewDTO orderViewDTO = new OrderViewDTO();
        Mockito.when(request.getRequestURI()).thenReturn("/orders/" + orderId);
        Mockito.when(orderService.getOrderById(orderId)).thenReturn(new Order());
        Mockito.when(orderMapper.orderToOrderViewDTO(Mockito.any())).thenReturn(orderViewDTO);
        Mockito.when(response.getWriter()).thenReturn(printWriter);
        orderServlet.doGet(request, response);
        Mockito.verify(response).setContentType("application/json");
        Mockito.verify(response).setCharacterEncoding("UTF-8");
        Mockito.verify(response, Mockito.never()).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        Mockito.verify(response).getWriter();
        Mockito.verify(response.getWriter()).print(objectMapper.writeValueAsString(orderViewDTO));
        Mockito.verify(response.getWriter()).flush();
    }

    @Test
    void testDoGetWithInvalidId() throws Exception {
        Mockito.when(request.getRequestURI()).thenReturn("/orders/invalidId");
        Mockito.when(response.getWriter()).thenReturn(printWriter);
        orderServlet.doGet(request, response);
        Mockito.verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        Mockito.verify(response.getWriter()).println("Invalid ID parameter");
    }

    @Test
    void testDoGetAllOrders() throws Exception {
        Mockito.when(request.getRequestURI()).thenReturn("/orders");
        List<Order> orders = Arrays.asList(
                new Order(1L, null, null, new ArrayList<>(), "S"),
                new Order(2L, null, null, new ArrayList<>(), "S")
        );
        List<OrderViewDTO> orderViewDTOS = Arrays.asList(
                new OrderViewDTO(1L, 1L, new ArrayList<>(), BigDecimal.valueOf(100.0), "S"),
                new OrderViewDTO(2L, 2L, new ArrayList<>(), BigDecimal.valueOf(150.0), "S")
        );
        Mockito.when(orderService.getAllOrders()).thenReturn(orders);
        Mockito.when(orderMapper.orderListToOrderDTOList(orders)).thenReturn(orderViewDTOS);
        Mockito.when(response.getWriter()).thenReturn(printWriter);
        orderServlet.doGet(request, response);
        Mockito.verify(response).setContentType("application/json");
        Mockito.verify(response).setCharacterEncoding("UTF-8");
        Mockito.verify(response, Mockito.never()).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        Mockito.verify(response.getWriter()).print(objectMapper.writeValueAsString(orderViewDTOS));
        Mockito.verify(response.getWriter()).flush();
    }

    @Test
    void testDoPost() throws Exception {
        OrderDTO orderDTO = new OrderDTO();
        Mockito.when(request.getInputStream()).thenReturn(getInputStream(orderDTO));
        orderServlet.doPost(request, response);
        Mockito.verify(response).setContentType("application/json");
        Mockito.verify(response).setCharacterEncoding("UTF-8");
        Mockito.verify(response, Mockito.never()).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        Mockito.verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    void testDoPutWithValidId() throws Exception {
        Long orderId = 1L;
        Mockito.lenient().when(response.getWriter()).thenReturn(printWriter);
        Mockito.when(request.getPathInfo()).thenReturn("/" + orderId);
        orderServlet.doPut(request, response);
        verify(orderService).updateOrderStatusDelivered(orderId);
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void testDoPutWithInvalidId() throws Exception {
        Mockito.when(request.getPathInfo()).thenReturn("/");
        Mockito.when(response.getWriter()).thenReturn(printWriter);
        orderServlet.doPut(request, response);
        Mockito.verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        Mockito.verify(response.getWriter()).println("Order ID is required");
    }

    @Test
    void testDoDeleteWithValidId() throws Exception {
        Long orderId = 1L;
        Mockito.when(request.getPathInfo()).thenReturn("/" + orderId);
        orderServlet.doDelete(request, response);
        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void testDoDeleteWithInvalidId() throws Exception {
        Mockito.when(request.getPathInfo()).thenReturn("/");
        Mockito.when(response.getWriter()).thenReturn(printWriter);
        orderServlet.doDelete(request, response);
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

