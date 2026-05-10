package com.bookstore.bookservice.exception;

// RuntimeException'dan türetildi; e-posta çakışması gibi 409 durumlarında fırlatılır.
public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
