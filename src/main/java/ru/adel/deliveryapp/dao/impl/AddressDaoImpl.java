package ru.adel.deliveryapp.dao.impl;

import ru.adel.deliveryapp.dao.AddressDao;
import ru.adel.deliveryapp.dao.mapper.impl.AddressResultSetMapper;
import ru.adel.deliveryapp.datasourse.jdbc.sessionmanager.SessionManager;
import ru.adel.deliveryapp.entity.Address;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AddressDaoImpl implements AddressDao {
    private final SessionManager sessionManager;
    private final AddressResultSetMapper addressResultSetMapper;

    public AddressDaoImpl(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        addressResultSetMapper = new AddressResultSetMapper();
    }

    @Override
    public Optional<Address> findById(Long id) throws SQLException {
        sessionManager.beginSession();
        try (Connection connection = sessionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(SQLTask.GET_ADDRESS_BY_ID.QUERY)) {
            pst.setLong(1, id);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return Optional.ofNullable(addressResultSetMapper.map(rs));
                }
            }
        } catch (SQLException ex) {
            sessionManager.rollbackSession();
            throw ex;
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteById(Long id) throws SQLException {
        int updatedRows;
        sessionManager.beginSession();
        try (Connection connection = sessionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(SQLTask.DELETE_ADDRESS_BY_ID.QUERY)) {
            pst.setLong(1, id);
            updatedRows = pst.executeUpdate();
            sessionManager.commitSession();
        } catch (SQLException ex) {
            sessionManager.rollbackSession();
            throw ex;
        }
        return updatedRows > 0;
    }


    @Override
    public List<Address> findAll() throws SQLException {
        List<Address> addresses = new ArrayList<>();
        sessionManager.beginSession();
        try (Connection connection = sessionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(SQLTask.GET_ALL_ADDRESSES.QUERY)) {
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    addresses.add(addressResultSetMapper.map(rs));
                }
            }
        } catch (SQLException ex) {
            sessionManager.rollbackSession();
            throw ex;
        }
        return addresses;
    }

    @Override
    public Long save(Address address) throws SQLException {
        sessionManager.beginSession();
        try (Connection connection = sessionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(SQLTask.INSERT_ADDRESS.QUERY, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, address.getCity());
            pst.setString(2, address.getStreet());
            pst.setString(3, address.getHouseNumber());
            pst.setString(4, address.getApartmentNumber());
            pst.executeUpdate();
            try (ResultSet rs = pst.getGeneratedKeys()) {
                rs.next();
                Long id = rs.getLong(1);
                sessionManager.commitSession();
                return id;
            }
        } catch (SQLException ex) {
            sessionManager.rollbackSession();
            throw ex;
        }
    }

    @Override
    public void update(Address address) throws SQLException {
        sessionManager.beginSession();
        try (Connection connection = sessionManager.getCurrentSession();
             PreparedStatement pst = connection.prepareStatement(SQLTask.UPDATE_ADDRESS_BY_ID.QUERY)) {
            pst.setString(1, address.getCity());
            pst.setString(2, address.getStreet());
            pst.setString(3, address.getHouseNumber());
            pst.setString(4, address.getApartmentNumber());
            pst.setLong(5, address.getId());
            pst.executeUpdate();
            sessionManager.commitSession();
        } catch (SQLException ex) {
            sessionManager.rollbackSession();
            throw ex;
        }
    }

    enum SQLTask {
        INSERT_ADDRESS("INSERT INTO address (city, street,house_number,apartment_number) VALUES (?, ?, ?, ?)"),
        GET_ADDRESS_BY_ID("SELECT address.id, address.city, address.street,address.house_number," +
                "address.apartment_number FROM address WHERE id = ?"),
        DELETE_ADDRESS_BY_ID("DELETE FROM address WHERE id = ?"),
        UPDATE_ADDRESS_BY_ID("UPDATE address SET city = ?, street = ?,house_number = ?, apartment_number= ? WHERE id = ?"),
        GET_ALL_ADDRESSES("SELECT address.id, address.city, address.street,address.house_number," +
                "address.apartment_number FROM address");

        String QUERY;

        SQLTask(String QUERY) {
            this.QUERY = QUERY;
        }
    }
}
