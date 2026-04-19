package com.bookstore.bookservice;

import com.bookstore.bookservice.model.Book;
import com.bookstore.bookservice.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BookSaveTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    void kitapKaydetVeGetir() {
        Book book = new Book();
        book.setTitle("Clean Code");
        book.setAuthor("Robert C. Martin");
        book.setIsbn("978-0132350884");
        book.setPrice(new BigDecimal("149.90"));
        book.setStockQuantity(10);

        Book saved = bookRepository.save(book);

        assertNotNull(saved.getId());
        System.out.println("Kaydedilen kitap ID: " + saved.getId());
        System.out.println("Kitap: " + saved.getTitle() + " - " + saved.getAuthor());

        List<Book> books = bookRepository.findAll();
        System.out.println("DB'deki toplam kitap sayisi: " + books.size());
        assertFalse(books.isEmpty());
    }
}
