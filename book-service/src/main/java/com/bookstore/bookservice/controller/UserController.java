package com.bookstore.bookservice.controller;

import com.bookstore.bookservice.dto.ApiResponse;
import com.bookstore.bookservice.model.User;
import com.bookstore.bookservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<User>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(userService.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> getById(@PathVariable Long id) {
        return userService.getById(id)
                .map(u -> ResponseEntity.ok(ApiResponse.success(u)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Kullanıcı bulunamadı: " + id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<User>> create(@RequestBody User user) {
        User saved = userService.create(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Kullanıcı oluşturuldu", saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> update(@PathVariable Long id, @RequestBody User user) {
        User updated = userService.update(id, user);
        return ResponseEntity.ok(ApiResponse.success("Kullanıcı güncellendi", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Kullanıcı silindi", null));
    }
}
