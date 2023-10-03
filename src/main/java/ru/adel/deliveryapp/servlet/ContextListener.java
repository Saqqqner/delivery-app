package ru.adel.deliveryapp.servlet;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import ru.adel.deliveryapp.dao.*;
import ru.adel.deliveryapp.dao.impl.*;
import ru.adel.deliveryapp.datasourse.DataSourceHikariPostgreSQL;
import ru.adel.deliveryapp.datasourse.jdbc.sessionmanager.SessionManager;
import ru.adel.deliveryapp.datasourse.jdbc.sessionmanager.SessionManagerJdbc;
import ru.adel.deliveryapp.service.AddressService;
import ru.adel.deliveryapp.service.CustomerService;
import ru.adel.deliveryapp.service.OrderService;
import ru.adel.deliveryapp.service.ProductService;
import ru.adel.deliveryapp.service.impl.AddressServiceImpl;
import ru.adel.deliveryapp.service.impl.CustomerServiceImpl;
import ru.adel.deliveryapp.service.impl.OrderServiceImpl;
import ru.adel.deliveryapp.service.impl.ProductServiceImpl;

import javax.sql.DataSource;

@WebListener
public class ContextListener implements ServletContextListener {
    private CustomerDao customerDao;
    private ProductDao productDao;
    private OrderDao orderDao;
    private AddressDao addressDao;
    private OrderItemDao orderItemDao;
    private CustomerService customerService;
    private ProductService productService;
    private OrderService orderService;
    private AddressService addressService;


    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        final ServletContext servletContext =
                servletContextEvent.getServletContext();

        DataSource dataSource = DataSourceHikariPostgreSQL.getHikariDataSource();
        SessionManager sessionManager = new SessionManagerJdbc(dataSource);

        this.productDao = new ProductDaoImpl(sessionManager);
        this.customerDao = new CustomerDaoImpl(sessionManager);
        this.addressDao = new AddressDaoImpl(sessionManager);
        this.orderDao = new OrderDaoImpl(sessionManager);
        this.orderItemDao = new OrderItemDaoImpl(sessionManager);
        this.addressService = new AddressServiceImpl(addressDao);
        this.productService = new ProductServiceImpl(productDao);
        this.orderService = new OrderServiceImpl(orderDao, productService, customerDao, addressService, orderItemDao);
        this.customerService = new CustomerServiceImpl(customerDao, orderService);

        servletContext.setAttribute("orderService", orderService);
        servletContext.setAttribute("addressService", addressService);
        servletContext.setAttribute("customerService", customerService);
        servletContext.setAttribute("productService", productService);


    }
}
