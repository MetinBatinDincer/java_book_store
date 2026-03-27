package com.bookstore.bookservice.service;

import com.bookstore.bookservice.model.User;
import com.bookstore.bookservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public Optional<User> getById(Long id) {
        return userRepository.findById(id);
    }

    public User create(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Bu e-posta zaten kayıtlı: " + user.getEmail());
        }
        if (user.getRole() == null) {
            user.setRole(User.Role.CUSTOMER);
        }
        return userRepository.save(user);
    }

    public User update(Long id, User updated) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Kullanıcı bulunamadı: " + id));
        existing.setName(updated.getName());
        existing.setEmail(updated.getEmail());
        existing.setRole(updated.getRole());
        return userRepository.save(existing);
    }

    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("Kullanıcı bulunamadı: " + id);
        }
        userRepository.deleteById(id);
    }
}
