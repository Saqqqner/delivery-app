package ru.adel.deliveryapp.datasourse.jdbc.sessionmanager;

import java.io.Serializable;
import java.sql.Connection;

public interface SessionManager extends AutoCloseable, Serializable {
    void beginSession();

    void commitSession();

    void rollbackSession();

    void close();

    Connection getCurrentSession();

}