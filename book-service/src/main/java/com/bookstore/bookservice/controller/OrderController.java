package com.bookstore.bookservice.controller;

import com.bookstore.bookservice.dto.ApiResponse;
import com.bookstore.bookservice.model.Order;
import com.bookstore.bookservice.model.OrderItem;
import com.bookstore.bookservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Order>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(orderService.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Order>> getById(@PathVariable Long id) {
        return orderService.getById(id)
                .map(o -> ResponseEntity.ok(ApiResponse.success(o)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Sipariş bulunamadı: " + id)));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<Order>>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success(orderService.getByUserId(userId)));
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<Order>> create(@PathVariable Long userId,
                                                      @RequestBody List<OrderItem> items) {
        Order order = orderService.create(userId, items);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Sipariş oluşturuldu", order));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Order>> updateStatus(@PathVariable Long id,
                                                            @RequestParam Order.Status status) {
        Order updated = orderService.updateStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success("Sipariş durumu güncellendi", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        orderService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Sipariş silindi", null));
    }
}
