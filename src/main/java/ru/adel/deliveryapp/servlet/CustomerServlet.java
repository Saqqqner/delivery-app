package ru.adel.deliveryapp.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.adel.deliveryapp.servlet.dto.CreateCustomerDTO;
import ru.adel.deliveryapp.servlet.mapper.CustomerMapper;
import ru.adel.deliveryapp.servlet.dto.CustomerViewDTO;
import ru.adel.deliveryapp.service.CustomerService;
import ru.adel.deliveryapp.service.impl.CustomerServiceImpl;
import ru.adel.deliveryapp.util.CustomerNotFoundException;
import ru.adel.deliveryapp.util.DuplicateException;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/customers/*")
public class CustomerServlet extends HttpServlet {
    private  static final String INTERNAL_MSG ="Internal Server Error";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private CustomerService customerService;
    private CustomerMapper customerMapper;

    @Override
    public void init() throws ServletException {
        final Object customerServiceImpl = getServletContext().getAttribute("customerService");
        this.customerMapper = CustomerMapper.INSTANCE;
        this.customerService = (CustomerServiceImpl) customerServiceImpl;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try {
            String requestURI = req.getRequestURI();
            String[] parts = requestURI.split("/");
            Long id = null;
            if (parts.length > 2) {
                try {
                    id = Long.parseLong(parts[2]);
                } catch (NumberFormatException e) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().println("Invalid ID parameter");
                    return;
                }
            }
            if (id != null) {
                try {
                    CustomerViewDTO customer = customerMapper.customerToCustomerViewDTO(customerService.getCustomerById(id));
                    String jsonResponse = objectMapper.writeValueAsString(customer);
                    PrintWriter out = resp.getWriter();
                    out.print(jsonResponse);
                    out.flush();
                } catch (CustomerNotFoundException e) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter().println(e.getMessage());
                }
            } else {
                List<CustomerViewDTO> customers = customerMapper.customersToCustomersViewDTO(customerService.findAll());
                String jsonResponse = objectMapper.writeValueAsString(customers);
                PrintWriter out = resp.getWriter();
                out.print(jsonResponse);
                out.flush();
            }
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println(INTERNAL_MSG);
            e.printStackTrace();
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            CreateCustomerDTO newCustomerDTO = objectMapper.readValue(request.getInputStream(), CreateCustomerDTO.class);
            customerService.save(customerMapper.customerDTOToCustomer(newCustomerDTO));
            response.setStatus(HttpServletResponse.SC_CREATED);
        } catch (DuplicateException e) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            response.getWriter().println(e.getMessage());
        } catch (IOException | SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println(INTERNAL_MSG);
            e.printStackTrace();
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String pathInfo = req.getPathInfo();
            if (pathInfo != null && !pathInfo.equals("/")) {
                String[] pathParts = pathInfo.split("/");
                if (pathParts.length == 2) {
                    Long customerId = Long.parseLong(pathParts[1]);
                    CreateCustomerDTO updatedCustomer = objectMapper.readValue(req.getInputStream(), CreateCustomerDTO.class);
                    customerService.update(customerId, customerMapper.customerDTOToCustomer(updatedCustomer));
                    resp.setStatus(HttpServletResponse.SC_OK);
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println("Customer ID is required");
            }
        } catch (CustomerNotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().println(e.getMessage());
        } catch (DuplicateException e) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getWriter().println(e.getMessage());
        } catch (IOException | SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println(INTERNAL_MSG);
            e.printStackTrace();
        }
    }


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String pathInfo = req.getPathInfo();
            if (pathInfo != null && !pathInfo.equals("/")) {
                String[] pathParts = pathInfo.split("/");
                if (pathParts.length == 2) {
                    Long customerId = Long.parseLong(pathParts[1]);
                    customerService.deleteById(customerId);
                    resp.setStatus(HttpServletResponse.SC_OK);
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (CustomerNotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().println(e.getMessage());
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println(INTERNAL_MSG);
            e.printStackTrace();
        }
    }

}
