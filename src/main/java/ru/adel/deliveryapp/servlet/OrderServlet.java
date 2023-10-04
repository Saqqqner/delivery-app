package ru.adel.deliveryapp.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.adel.deliveryapp.service.OrderService;
import ru.adel.deliveryapp.service.impl.OrderServiceImpl;
import ru.adel.deliveryapp.servlet.dto.OrderDTO;
import ru.adel.deliveryapp.servlet.dto.OrderViewDTO;
import ru.adel.deliveryapp.servlet.mapper.OrderMapper;
import ru.adel.deliveryapp.util.AddressNotFoundException;
import ru.adel.deliveryapp.util.CustomerNotFoundException;
import ru.adel.deliveryapp.util.DuplicateException;
import ru.adel.deliveryapp.util.OrderNotFoundException;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/orders/*")
public class OrderServlet extends HttpServlet {
    private static final String INTERNAL_MSG = "Internal Server Error";
    private static final String ORDER_ID_REQUIRED_MSG = "Order ID is required";
    private static final String INVALID_ID_MSG = "Invalid ID parameter";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private OrderService orderService;
    private OrderMapper orderMapper;

    @Override
    public void init() throws ServletException {
        final Object orderService1 = getServletContext().getAttribute("orderService");

        this.orderService = (OrderServiceImpl) orderService1;
        this.orderMapper = OrderMapper.INSTANCE;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        respConfig(resp);
        try {
            String requestURI = req.getRequestURI();
            String[] parts = requestURI.split("/");
            Long id = null;
            if (parts.length > 2) {
                try {
                    id = Long.parseLong(parts[2]);
                } catch (NumberFormatException e) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().println(INVALID_ID_MSG);
                    return;
                }
            }
            if (id != null) {
                try {
                    OrderViewDTO orderViewDTO = orderMapper.orderToOrderViewDTO(orderService.getOrderById(id));
                    String jsonResponse = objectMapper.writeValueAsString(orderViewDTO);
                    PrintWriter out = resp.getWriter();
                    out.print(jsonResponse);
                    out.flush();
                } catch (OrderNotFoundException e) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter().println(e.getMessage());
                }
            } else {
                List<OrderViewDTO> orderViewDTOS = orderMapper.orderListToOrderDTOList(orderService.getAllOrders());
                String jsonResponse = objectMapper.writeValueAsString(orderViewDTOS);
                PrintWriter out = resp.getWriter();
                out.print(jsonResponse);
                out.flush();
            }
        } catch (SQLException | RuntimeException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println(INTERNAL_MSG);
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
        respConfig(resp);
        try {
            OrderDTO orderDTO = objectMapper.readValue(request.getInputStream(), OrderDTO.class);
            orderService.createOrder(orderDTO);
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (CustomerNotFoundException | AddressNotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().println(e.getMessage());
        } catch (IOException | SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println(INTERNAL_MSG);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        respConfig(resp);
        try {
            String pathInfo = req.getPathInfo();
            if (pathInfo != null && !pathInfo.equals("/")) {
                String[] pathParts = pathInfo.split("/");
                if (pathParts.length == 2) {
                    Long id = Long.parseLong(pathParts[1]);
                    orderService.updateOrderStatusDelivered(id);
                    resp.setStatus(HttpServletResponse.SC_OK);
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println(ORDER_ID_REQUIRED_MSG);
            }
        } catch (OrderNotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().println(e.getMessage());
        } catch (DuplicateException e) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getWriter().println(e.getMessage());
        } catch (IOException | SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println(INTERNAL_MSG);

        }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String pathInfo = req.getPathInfo();
            if (pathInfo != null && !pathInfo.equals("/")) {
                String[] pathParts = pathInfo.split("/");
                if (pathParts.length == 2) {
                    Long orderId = Long.parseLong(pathParts[1]);
                    orderService.deleteOrder(orderId);
                    resp.setStatus(HttpServletResponse.SC_OK);
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println(ORDER_ID_REQUIRED_MSG);
            }
        } catch (OrderNotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().println(e.getMessage());
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println(INTERNAL_MSG);
        }
    }

    private void respConfig(HttpServletResponse resp) {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
    }
}
