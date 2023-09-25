package ru.adel.deliveryapp.servlet;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import ru.adel.deliveryapp.dao.CustomerDao;
import ru.adel.deliveryapp.dao.impl.CustomerDaoImpl;
import ru.adel.deliveryapp.datasourse.DataSourceHikariPostgreSQL;
import ru.adel.deliveryapp.datasourse.jdbc.sessionmanager.SessionManager;
import ru.adel.deliveryapp.datasourse.jdbc.sessionmanager.SessionManagerJdbc;
import ru.adel.deliveryapp.service.CustomerService;
import ru.adel.deliveryapp.service.impl.CustomerServiceImpl;

import javax.sql.DataSource;

@WebListener
public class ContextListener implements ServletContextListener {
    private CustomerDao customerDaoJdbc;
    private CustomerService customerService;


    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        final ServletContext servletContext =
                servletContextEvent.getServletContext();

        DataSource dataSource = DataSourceHikariPostgreSQL.getHikariDataSource();
        SessionManager sessionManager = new SessionManagerJdbc(dataSource);
        this.customerDaoJdbc = new CustomerDaoImpl(sessionManager);
        this.customerService = new CustomerServiceImpl(customerDaoJdbc);


        servletContext.setAttribute("customerService", customerService);
        servletContext.setAttribute("customerDao", customerDaoJdbc);


    }
}
