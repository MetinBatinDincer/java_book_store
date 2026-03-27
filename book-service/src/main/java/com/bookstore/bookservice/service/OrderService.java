package com.bookstore.bookservice.service;

import com.bookstore.bookservice.model.*;
import com.bookstore.bookservice.repository.BookRepository;
import com.bookstore.bookservice.repository.OrderRepository;
import com.bookstore.bookservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    public List<Order> getByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public Optional<Order> getById(Long id) {
        return orderRepository.findById(id);
    }

    public Order create(Long userId, List<OrderItem> items) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Kullanıcı bulunamadı: " + userId));

        Order order = new Order();
        order.setUser(user);

        BigDecimal total = BigDecimal.ZERO;
        for (OrderItem item : items) {
            Book book = bookRepository.findById(item.getBook().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Kitap bulunamadı: " + item.getBook().getId()));
            item.setOrder(order);
            item.setBook(book);
            item.setUnitPrice(book.getPrice());
            total = total.add(book.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        order.setItems(items);
        order.setTotalPrice(total);
        return orderRepository.save(order);
    }

    public Order updateStatus(Long id, Order.Status status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sipariş bulunamadı: " + id));
        order.setStatus(status);
        return orderRepository.save(order);
    }

    public void delete(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new IllegalArgumentException("Sipariş bulunamadı: " + id);
        }
        orderRepository.deleteById(id);
    }
}
