package ru.adel.deliveryapp.util;

public class DuplicateCustomerException extends RuntimeException {
    public DuplicateCustomerException(String msg) {
        super(msg);
    }
}
