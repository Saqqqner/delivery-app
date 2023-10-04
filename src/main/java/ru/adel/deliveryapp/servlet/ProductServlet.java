package ru.adel.deliveryapp.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.adel.deliveryapp.service.ProductService;
import ru.adel.deliveryapp.service.impl.ProductServiceImpl;
import ru.adel.deliveryapp.servlet.dto.ProductByStockDTO;
import ru.adel.deliveryapp.servlet.dto.ProductDTO;
import ru.adel.deliveryapp.servlet.mapper.ProductMapper;
import ru.adel.deliveryapp.util.DuplicateException;
import ru.adel.deliveryapp.util.ProductNotFoundException;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/products/*")
public class ProductServlet extends HttpServlet {
    private static final String INTERNAL_MSG = "Internal Server Error";
    private static final String PRODUCT_ID_IS_REQUIRED_MSG = "Product ID is required";
    private static final String INVALID_ID_MSG = "Invalid ID parameter";
    private ProductService productService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private ProductMapper productMapper;


    @Override
    public void init() throws ServletException {
        final Object productServiceImpl = getServletContext().getAttribute("productService");

        this.productService = (ProductServiceImpl) productServiceImpl;
        this.productMapper = ProductMapper.INSTANCE;
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
                    ProductDTO product = productMapper.productToProductDTO(productService.getProductById(id));
                    String jsonResponse = objectMapper.writeValueAsString(product);
                    PrintWriter out = resp.getWriter();
                    out.print(jsonResponse);
                    out.flush();
                } catch (ProductNotFoundException e) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter().println(e.getMessage());
                }
            } else {
                List<ProductDTO> products = productMapper.productsToProductsDTO(productService.findAll());
                String jsonResponse = objectMapper.writeValueAsString(products);
                PrintWriter out = resp.getWriter();
                out.print(jsonResponse);
                out.flush();
            }
        } catch (SQLException | IOException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println(INTERNAL_MSG);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        respConfig(resp);
        try {
            ProductByStockDTO productByStockDTO = objectMapper.readValue(req.getInputStream(), ProductByStockDTO.class);
            productService.save(productMapper.productByStockDTOToProduct(productByStockDTO));
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
                    Long productId = Long.parseLong(pathParts[1]);
                    ProductByStockDTO productByStockDTO = objectMapper.readValue(req.getInputStream(), ProductByStockDTO.class);
                    productService.update(productId, productMapper.productByStockDTOToProduct(productByStockDTO));
                    resp.setStatus(HttpServletResponse.SC_OK);
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println(PRODUCT_ID_IS_REQUIRED_MSG);
            }
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println(INVALID_ID_MSG);
        } catch (ProductNotFoundException e) {
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
                    Long productId = Long.parseLong(pathParts[1]);
                    productService.deleteById(productId);
                    resp.setStatus(HttpServletResponse.SC_OK);
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println(PRODUCT_ID_IS_REQUIRED_MSG);
            }
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println(INVALID_ID_MSG);
        } catch (ProductNotFoundException e) {
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
