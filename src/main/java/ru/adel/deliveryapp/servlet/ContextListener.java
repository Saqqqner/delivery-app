package ru.adel.deliveryapp.servlet;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import ru.adel.deliveryapp.dao.CustomerDao;
import ru.adel.deliveryapp.dao.ProductDao;
import ru.adel.deliveryapp.dao.impl.CustomerDaoImpl;
import ru.adel.deliveryapp.dao.impl.ProductDaoImpl;
import ru.adel.deliveryapp.datasourse.DataSourceHikariPostgreSQL;
import ru.adel.deliveryapp.datasourse.jdbc.sessionmanager.SessionManager;
import ru.adel.deliveryapp.datasourse.jdbc.sessionmanager.SessionManagerJdbc;
import ru.adel.deliveryapp.service.CustomerService;
import ru.adel.deliveryapp.service.ProductService;
import ru.adel.deliveryapp.service.impl.CustomerServiceImpl;
import ru.adel.deliveryapp.service.impl.ProductServiceImpl;

import javax.sql.DataSource;

@WebListener
public class ContextListener implements ServletContextListener {
    private CustomerDao customerDaoJdbc;
    private ProductDao productDaoJdbc;
    private CustomerService customerService;
    private ProductService productService;


    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        final ServletContext servletContext =
                servletContextEvent.getServletContext();

        DataSource dataSource = DataSourceHikariPostgreSQL.getHikariDataSource();
        SessionManager sessionManager = new SessionManagerJdbc(dataSource);
        this.productDaoJdbc = new ProductDaoImpl(sessionManager);
        this.customerDaoJdbc = new CustomerDaoImpl(sessionManager);
        this.customerService = new CustomerServiceImpl(customerDaoJdbc);
        this.productService = new ProductServiceImpl(productDaoJdbc);


        servletContext.setAttribute("customerService", customerService);
        servletContext.setAttribute("productService", productService);


    }
}
