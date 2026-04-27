package com.bookstore.bookservice.service;

import com.bookstore.bookservice.exception.ResourceNotFoundException;
import com.bookstore.bookservice.model.Book;
import com.bookstore.bookservice.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class BookService {

    private final BookRepository bookRepository;
    private final ActivityLogService activityLogService;

    @Transactional(readOnly = true)
    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Book> getById(Long id) {
        return bookRepository.findById(id);
    }

    public Book create(Book book) {
        Book saved = bookRepository.save(book);
        activityLogService.log("CREATE", "Book", saved.getId(), saved.getTitle() + " eklendi");
        return saved;
    }

    public Book update(Long id, Book updated) {
        Book existing = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kitap bulunamadı: " + id));
        existing.setTitle(updated.getTitle());
        existing.setAuthor(updated.getAuthor());
        existing.setIsbn(updated.getIsbn());
        existing.setPrice(updated.getPrice());
        existing.setStockQuantity(updated.getStockQuantity());
        Book saved = bookRepository.save(existing);
        activityLogService.log("UPDATE", "Book", saved.getId(), saved.getTitle() + " güncellendi");
        return saved;
    }

    public void delete(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kitap bulunamadı: " + id));
        bookRepository.delete(book);
        activityLogService.log("DELETE", "Book", id, book.getTitle() + " silindi");
    }
}
