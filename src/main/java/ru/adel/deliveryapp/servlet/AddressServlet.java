package ru.adel.deliveryapp.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.adel.deliveryapp.service.AddressService;
import ru.adel.deliveryapp.service.impl.AddressServiceImpl;
import ru.adel.deliveryapp.servlet.dto.AddressDTO;
import ru.adel.deliveryapp.servlet.mapper.AddressMapper;
import ru.adel.deliveryapp.util.AddressNotFoundException;
import ru.adel.deliveryapp.util.CustomerNotFoundException;
import ru.adel.deliveryapp.util.DuplicateException;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/address/*")
public class AddressServlet extends HttpServlet {
    private static final String INTERNAL_MSG = "Internal Server Error";
    private static final String ADDRESS_ID_REQUIRED_MSG = "Address ID is required";
    private static final String INVALID_ID_MSG = "Invalid ID parameter";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private AddressService addressService;
    private AddressMapper addressMapper;

    @Override
    public void init() throws ServletException {
        final Object addressServiceImpl = getServletContext().getAttribute("addressService");
        this.addressMapper = AddressMapper.INSTANCE;
        this.addressService = (AddressServiceImpl) addressServiceImpl;
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
                    resp.getWriter().println("Invalid ID parameter");
                    return;
                }
            }
            if (id != null) {
                try {
                    AddressDTO addressDTO = addressMapper.addressToAddressDTO(addressService.getAddressById(id));
                    String jsonResponse = objectMapper.writeValueAsString(addressDTO);
                    PrintWriter out = resp.getWriter();
                    out.print(jsonResponse);
                    out.flush();
                } catch (AddressNotFoundException e) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter().println(e.getMessage());
                }
            } else {
                List<AddressDTO> addressDTOS = addressMapper.addressListTOAddressDTOList(addressService.findAll());
                String jsonResponse = objectMapper.writeValueAsString(addressDTOS);
                PrintWriter out = resp.getWriter();
                out.print(jsonResponse);
                out.flush();
            }
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println(INTERNAL_MSG);
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        respConfig(resp);
        try {
            AddressDTO addressDTO = objectMapper.readValue(req.getInputStream(), AddressDTO.class);
            addressService.save(addressMapper.addressDTOToAddress(addressDTO));
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (DuplicateException e) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getWriter().println(e.getMessage());
        } catch (IOException | SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println(INTERNAL_MSG);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String pathInfo = req.getPathInfo();
            if (pathInfo != null && !pathInfo.equals("/")) {
                String[] pathParts = pathInfo.split("/");
                if (pathParts.length == 2) {
                    Long addressId = Long.parseLong(pathParts[1]);
                    AddressDTO addressDTO = objectMapper.readValue(req.getInputStream(), AddressDTO.class);
                    addressService.update(addressId, addressMapper.addressDTOToAddress(addressDTO));
                    resp.setStatus(HttpServletResponse.SC_OK);
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println(ADDRESS_ID_REQUIRED_MSG);
            }
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println(INVALID_ID_MSG);
        } catch (AddressNotFoundException e) {
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
                    Long customerId = Long.parseLong(pathParts[1]);
                    addressService.deleteById(customerId);
                    resp.setStatus(HttpServletResponse.SC_OK);
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println(INVALID_ID_MSG);
        } catch (AddressNotFoundException e) {
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
