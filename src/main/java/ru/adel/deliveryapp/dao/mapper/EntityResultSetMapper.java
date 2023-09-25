package ru.adel.deliveryapp.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface EntityResultSetMapper <T>{
    T map(ResultSet resultSet)throws SQLException;
}
