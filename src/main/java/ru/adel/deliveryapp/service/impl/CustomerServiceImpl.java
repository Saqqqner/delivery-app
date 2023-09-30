package ru.adel.deliveryapp.service.impl;

import ru.adel.deliveryapp.dao.CustomerDao;
import ru.adel.deliveryapp.dao.OrderDao;
import ru.adel.deliveryapp.entity.Customer;
import ru.adel.deliveryapp.entity.Order;
import ru.adel.deliveryapp.service.CustomerService;
import ru.adel.deliveryapp.util.CustomerNotFoundException;
import ru.adel.deliveryapp.util.DuplicateException;
import ru.adel.deliveryapp.util.OrderNotFoundException;

import java.sql.SQLException;
import java.util.List;

public class CustomerServiceImpl implements CustomerService {
    private static final String CUSTOMER_NOT_FOUND_MSG = "Customer not found with ID: ";
    private static final String CUSTOMER_DUPLICATE_EMAIL_MSG = "Customer with the provided email already exists";
    private static final String CUSTOMER_DUPLICATE_USERNAME_MSG = "Customer with the provided username already exists";
    private final CustomerDao customerDao;
    private final OrderDao orderDao;


    public CustomerServiceImpl(CustomerDao customerDao, OrderDao orderDao) {
        this.customerDao = customerDao;
        this.orderDao = orderDao;
    }

    @Override
    public Customer getCustomerById(Long id) throws SQLException {
        Customer customer = customerDao.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(CUSTOMER_NOT_FOUND_MSG + id));
        customer.setOrder(orderDao.findAllByCustomerId(customer.getId()));
        return customer;
    }

    @Override
    public void deleteById(Long id) throws SQLException {
        if (!customerDao.deleteById(id)) {
            throw new CustomerNotFoundException(CUSTOMER_NOT_FOUND_MSG + id);
        }
    }

    @Override
    public void update(Long id, Customer updateCustomer) throws SQLException {
        Customer customer = customerDao.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(CUSTOMER_NOT_FOUND_MSG + id));
        checkAndUpdateEmailAndUsername(customer, updateCustomer.getEmail(), updateCustomer.getUsername());
        customerDao.update(customer);
    }

    @Override
    public List<Customer> findAll() throws SQLException {
        List<Customer> customers = customerDao.findAll();
        for (Customer customer : customers) {
            List<Order> orders = orderDao.findAllByCustomerId(customer.getId());
            customer.setOrder(orders);
        }
        return customers;
    }

    @Override
    public void save(Customer customer) throws SQLException {
        if (customerDao.existsByEmail(customer.getEmail())||customerDao.existsByUsername(customer.getUsername())) {
            throw new DuplicateException(CUSTOMER_DUPLICATE_EMAIL_MSG);
        }
        // Проверка существования пользователя по username
        if (customerDao.existsByUsername(customer.getUsername())) {
            throw new DuplicateException(CUSTOMER_DUPLICATE_USERNAME_MSG);
        }
        customerDao.save(customer);

    }

    private void checkAndUpdateEmailAndUsername(Customer existingUser, String newEmail, String newUsername) throws SQLException {
        if (!existingUser.getEmail().equals(newEmail)) {
            boolean emailExists = customerDao.existsByEmail(newEmail);
            if (emailExists) {
                throw new DuplicateException(CUSTOMER_DUPLICATE_EMAIL_MSG);
            }
            existingUser.setEmail(newEmail);
        }
        if (!existingUser.getUsername().equals(newUsername)) {
            boolean usernameExists = customerDao.existsByUsername(newUsername);
            if (usernameExists) {
                throw new DuplicateException(CUSTOMER_DUPLICATE_USERNAME_MSG);
            }
            existingUser.setUsername(newUsername);
        }
    }


}

