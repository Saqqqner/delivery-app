package ru.adel.deliveryapp.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.adel.deliveryapp.dto.ProductByStockDTO;
import ru.adel.deliveryapp.dto.ProductDTO;
import ru.adel.deliveryapp.service.ProductService;
import ru.adel.deliveryapp.service.impl.ProductServiceImpl;
import ru.adel.deliveryapp.util.DuplicateException;
import ru.adel.deliveryapp.util.ProductNotFoundException;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/products/*")
public class ProductServlet extends HttpServlet {
    private static final String INTERNAL_MSG = "Internal Server Error";
    private ProductService productService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init() throws ServletException {
        final Object productServiceImpl = getServletContext().getAttribute("productService");

        this.productService = (ProductServiceImpl) productServiceImpl;
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
                    // Обработка ошибки, если "id" не является числом
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().println("Invalid ID parameter");
                    return;
                }
            }
            if (id != null) {
                try {
                    ProductDTO product = productService.getProductById(id);
                    String jsonResponse = objectMapper.writeValueAsString(product);
                    PrintWriter out = resp.getWriter();
                    out.print(jsonResponse);
                    out.flush();
                } catch (ProductNotFoundException e) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter().println(e.getMessage());
                }
            } else {
                List<ProductDTO> products = productService.findAll();
                String jsonResponse = objectMapper.writeValueAsString(products);
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
            ProductByStockDTO productByStockDTO = objectMapper.readValue(request.getInputStream(), ProductByStockDTO.class);
            productService.save(productByStockDTO);
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
                    Long productId = Long.parseLong(pathParts[1]);
                    ProductByStockDTO productByStockDTO = objectMapper.readValue(req.getInputStream(), ProductByStockDTO.class);
                    productService.update(productId, productByStockDTO);
                    resp.setStatus(HttpServletResponse.SC_OK);
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (ProductNotFoundException e) {
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
                    Long productId = Long.parseLong(pathParts[1]);
                    productService.deleteById(productId);
                    resp.setStatus(HttpServletResponse.SC_OK);
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (ProductNotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().println(e.getMessage());
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println(INTERNAL_MSG);
            e.printStackTrace();
        }
    }
}
