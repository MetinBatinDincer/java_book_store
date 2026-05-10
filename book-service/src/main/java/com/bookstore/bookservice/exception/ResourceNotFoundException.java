package com.bookstore.bookservice.exception;

// RuntimeException'dan türetildi; bulunamayan kayıtlar için 404 döndürür.
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
