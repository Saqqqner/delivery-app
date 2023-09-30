package ru.adel.deliveryapp.dao.mapper;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface EntityResultSetMapper <T> extends Serializable {
    T map(ResultSet resultSet) throws SQLException;
}
