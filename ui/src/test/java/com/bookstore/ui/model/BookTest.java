package com.bookstore.ui.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    @Test
    void defaultConstructor_fieldsAreNull() {
        Book book = new Book();
        assertNull(book.getId());
        assertNull(book.getTitle());
    }

    @Test
    void paramConstructor_storesValues() {
        Book book = new Book("Clean Code", "Robert C. Martin",
                "9780132350884", new BigDecimal("49.90"), 10);
        assertEquals("Clean Code", book.getTitle());
        assertEquals("Robert C. Martin", book.getAuthor());
        assertEquals(new BigDecimal("49.90"), book.getPrice());
        assertEquals(10, book.getStockQuantity());
    }

    @Test
    void settersAndGetters_workCorrectly() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Effective Java");
        book.setAuthor("Joshua Bloch");
        book.setIsbn("9780134685991");
        book.setPrice(new BigDecimal("79.90"));
        book.setStockQuantity(5);

        assertEquals(1L,    book.getId());
        assertEquals("Effective Java",   book.getTitle());
        assertEquals("Joshua Bloch",     book.getAuthor());
        assertEquals("9780134685991",    book.getIsbn());
        assertEquals(new BigDecimal("79.90"), book.getPrice());
        assertEquals(5,     book.getStockQuantity());
    }

    @Test
    void toStringContainsTitleAndAuthor() {
        Book book = new Book();
        book.setId(42L);
        book.setTitle("Java Concurrency");
        book.setAuthor("Goetz");
        assertTrue(book.toString().contains("Java Concurrency"));
        assertTrue(book.toString().contains("Goetz"));
    }
}
