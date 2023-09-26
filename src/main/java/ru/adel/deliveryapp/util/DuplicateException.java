package ru.adel.deliveryapp.util;

public class DuplicateException extends RuntimeException {
    public DuplicateException(String msg){
        super(msg);
    }
}
