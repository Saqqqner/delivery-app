package ru.adel.deliveryapp.service.impl;

import ru.adel.deliveryapp.dao.CustomerDao;
import ru.adel.deliveryapp.dao.mapper.CustomerDTOMapper;
import ru.adel.deliveryapp.dto.CustomerDTO;
import ru.adel.deliveryapp.dto.CustomerViewDTO;
import ru.adel.deliveryapp.models.Customer;
import ru.adel.deliveryapp.service.CustomerService;
import ru.adel.deliveryapp.util.CustomerNotFoundException;
import ru.adel.deliveryapp.util.DuplicateException;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerServiceImpl implements CustomerService {
    private static final String CUSTOMER_NOT_FOUND_MSG = "Customer not found with ID: ";
    private static final String CUSTOMER_DUPLICATE_EMAIL_MSG = "Customer with the provided email already exists";
    private static final String CUSTOMER_DUPLICATE_USERNAME_MSG = "Customer with the provided username already exists";
    private final CustomerDao customerDao;
    private final CustomerDTOMapper customerDTOMapper;

    public CustomerServiceImpl(CustomerDao customerDao) {
        this.customerDao = customerDao;
        customerDTOMapper = CustomerDTOMapper.INSTANCE;
    }

    @Override
    public CustomerViewDTO getCustomerById(Long id) throws SQLException {
        return customerDao.findById(id)
                .map(customerDTOMapper::customerToCustomerViewDTO)
                .orElseThrow(() -> new CustomerNotFoundException(CUSTOMER_NOT_FOUND_MSG + id));
    }

    @Override
    public void deleteById(Long id) throws SQLException {
        customerDao.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(CUSTOMER_NOT_FOUND_MSG + id));
        customerDao.deleteById(id);
    }

    @Override
    public void update(Long id, CustomerDTO customerDTO) throws SQLException {
        Customer customer = customerDao.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(CUSTOMER_NOT_FOUND_MSG + id));
        checkAndUpdateEmailAndUsername(customer, customerDTO.getEmail(), customerDTO.getUsername());
        customerDao.update(customer);
    }

    @Override
    public List<CustomerViewDTO> findAll() throws SQLException {
        return customerDao.findAll()
                .stream()
                .map(customerDTOMapper::customerToCustomerViewDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void save(CustomerDTO customerDTO) throws SQLException {
        if (customerDao.existsByEmail(customerDTO.getEmail())) {
            throw new DuplicateException(CUSTOMER_DUPLICATE_EMAIL_MSG);
        }
        // Проверка существования пользователя по username
        if (customerDao.existsByUsername(customerDTO.getUsername())) {
            throw new DuplicateException(CUSTOMER_DUPLICATE_USERNAME_MSG);
        }
        Customer customer = customerDTOMapper.customerDTOToCustomer(customerDTO);
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

