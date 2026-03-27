package com.bookstore.bookservice.controller;

import com.bookstore.bookservice.dto.ApiResponse;
import com.bookstore.bookservice.model.Book;
import com.bookstore.bookservice.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Book>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(bookService.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Book>> getById(@PathVariable Long id) {
        return bookService.getById(id)
                .map(b -> ResponseEntity.ok(ApiResponse.success(b)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Kitap bulunamadı: " + id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Book>> create(@RequestBody Book book) {
        Book saved = bookService.create(book);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Kitap oluşturuldu", saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Book>> update(@PathVariable Long id, @RequestBody Book book) {
        Book updated = bookService.update(id, book);
        return ResponseEntity.ok(ApiResponse.success("Kitap güncellendi", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Kitap silindi", null));
    }
}
