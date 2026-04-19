package com.bookstore.bookservice.service;

import com.bookstore.bookservice.exception.ResourceNotFoundException;
import com.bookstore.bookservice.model.*;
import com.bookstore.bookservice.repository.BookRepository;
import com.bookstore.bookservice.repository.OrderRepository;
import com.bookstore.bookservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Order> getByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public Optional<Order> getById(Long id) {
        return orderRepository.findById(id);
    }

    public Order create(Long userId, List<OrderItem> items) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı bulunamadı: " + userId));

        // Tüm kitapları tek sorguda çek — N+1'i önler
        List<Long> bookIds = items.stream()
                .map(i -> i.getBook().getId()).toList();
        Map<Long, Book> bookMap = bookRepository.findAllById(bookIds).stream()
                .collect(Collectors.toMap(Book::getId, Function.identity()));

        Order order = new Order();
        order.setUser(user);

        BigDecimal total = BigDecimal.ZERO;
        for (OrderItem item : items) {
            Book book = bookMap.get(item.getBook().getId());
            if (book == null) throw new ResourceNotFoundException(
                    "Kitap bulunamadı: " + item.getBook().getId());
            if (book.getStockQuantity() < item.getQuantity()) throw new IllegalArgumentException(
                    "Yetersiz stok: " + book.getTitle());

            item.setOrder(order);
            item.setBook(book);
            item.setUnitPrice(book.getPrice());
            total = total.add(book.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));

            // Stok düş
            book.setStockQuantity(book.getStockQuantity() - item.getQuantity());
        }

        bookRepository.saveAll(bookMap.values());
        order.setItems(items);
        order.setTotalPrice(total);
        return orderRepository.save(order);
    }

    public Order updateStatus(Long id, Order.Status status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sipariş bulunamadı: " + id));
        if (order.getStatus() == status) return order;
        order.setStatus(status);
        return orderRepository.save(order);
    }

    public void delete(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sipariş bulunamadı: " + id));
        orderRepository.delete(order);
    }
}
