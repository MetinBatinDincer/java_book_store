package com.bookstore.bookservice.service;

// TDD - Red-Green-Refactor
// Tarih: 2026-04-27
// Aşama: GREEN (testler geçiyor)

import com.bookstore.bookservice.exception.ResourceNotFoundException;
import com.bookstore.bookservice.model.Book;
import com.bookstore.bookservice.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private ActivityLogService activityLogService;

    @InjectMocks
    private BookService bookService;

    private Book testBook;

    @BeforeEach
    void setUp() {
        testBook = new Book();
        testBook.setId(1L);
        testBook.setTitle("Clean Code");
        testBook.setAuthor("Robert C. Martin");
        testBook.setIsbn("978-0132350884");
        testBook.setPrice(new BigDecimal("149.90"));
        testBook.setStockQuantity(10);
    }

    // --- RED: önce test yaz, sonra kodu yaz ---

    @Test
    @DisplayName("Tüm kitaplar listelenmeli")
    void getAll_ReturnsAllBooks() {
        when(bookRepository.findAll()).thenReturn(List.of(testBook));

        List<Book> result = bookService.getAll();

        assertEquals(1, result.size());
        assertEquals("Clean Code", result.get(0).getTitle());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("ID ile kitap bulunmalı")
    void getById_ExistingId_ReturnsBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));

        Optional<Book> result = bookService.getById(1L);

        assertTrue(result.isPresent());
        assertEquals("Clean Code", result.get().getTitle());
    }

    @Test
    @DisplayName("Olmayan ID için boş döner")
    void getById_NonExistingId_ReturnsEmpty() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Book> result = bookService.getById(99L);

        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Kitap kaydedilmeli ve log atılmalı")
    void create_ValidBook_SavesAndLogs() {
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);

        Book result = bookService.create(testBook);

        assertNotNull(result);
        assertEquals("Clean Code", result.getTitle());
        verify(bookRepository, times(1)).save(testBook);
        verify(activityLogService, times(1)).log(eq("CREATE"), eq("Book"), any(), any());
    }

    @Test
    @DisplayName("Olmayan kitap güncellenince 404 fırlatmalı")
    void update_NonExistingId_ThrowsResourceNotFoundException() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookService.update(99L, testBook));
    }

    @Test
    @DisplayName("Olmayan kitap silinince 404 fırlatmalı")
    void delete_NonExistingId_ThrowsResourceNotFoundException() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookService.delete(99L));
    }

    // --- REFACTOR: testler geçiyor, kod temizlendi ---

    @Test
    @DisplayName("Kitap güncellenince log atılmalı")
    void update_ExistingBook_UpdatesAndLogs() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);

        Book updated = new Book();
        updated.setTitle("Clean Code 2");
        updated.setAuthor("Robert C. Martin");
        updated.setIsbn("999");
        updated.setPrice(new BigDecimal("199.90"));
        updated.setStockQuantity(5);

        bookService.update(1L, updated);

        verify(activityLogService, times(1)).log(eq("UPDATE"), eq("Book"), any(), any());
    }
}
