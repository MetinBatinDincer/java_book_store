package com.bookstore.bookservice.service;

// TDD - Red-Green-Refactor
// Tarih: 2026-04-27
// Aşama: GREEN (testler geçiyor)

import com.bookstore.bookservice.exception.ConflictException;
import com.bookstore.bookservice.exception.ResourceNotFoundException;
import com.bookstore.bookservice.model.User;
import com.bookstore.bookservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("Ahmet Yilmaz");
        testUser.setEmail("ahmet@test.com");
        testUser.setPassword("123456");
        testUser.setRole(User.Role.CUSTOMER);
    }

    // --- RED: önce test yaz ---

    @Test
    @DisplayName("Tüm kullanıcılar listelenmeli")
    void getAll_ReturnsAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(testUser));

        List<User> result = userService.getAll();

        assertEquals(1, result.size());
        assertEquals("Ahmet Yilmaz", result.get(0).getName());
    }

    @Test
    @DisplayName("Yeni kullanıcı kaydedilmeli")
    void create_NewUser_SavesSuccessfully() {
        when(userRepository.existsByEmail("ahmet@test.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User result = userService.create(testUser);

        assertNotNull(result);
        assertEquals("ahmet@test.com", result.getEmail());
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    @DisplayName("Aynı email ile kayıt 409 fırlatmalı")
    void create_DuplicateEmail_ThrowsConflictException() {
        when(userRepository.existsByEmail("ahmet@test.com")).thenReturn(true);

        assertThrows(ConflictException.class, () -> userService.create(testUser));
        verify(userRepository, never()).save(any());
    }

    // --- GREEN: test geçiyor ---

    @Test
    @DisplayName("Role null ise CUSTOMER atanmalı")
    void create_NullRole_DefaultsToCustomer() {
        testUser.setRole(null);
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User result = userService.create(testUser);

        assertEquals(User.Role.CUSTOMER, result.getRole());
    }

    @Test
    @DisplayName("Olmayan kullanıcı güncellenince 404 fırlatmalı")
    void update_NonExistingUser_ThrowsResourceNotFoundException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.update(99L, testUser));
    }

    @Test
    @DisplayName("Olmayan kullanıcı silinince 404 fırlatmalı")
    void delete_NonExistingUser_ThrowsResourceNotFoundException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.delete(99L));
    }

    // --- REFACTOR: kod temizlendi, testler hâlâ geçiyor ---

    @Test
    @DisplayName("ID ile kullanıcı bulunmalı")
    void getById_ExistingId_ReturnsUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        Optional<User> result = userService.getById(1L);

        assertTrue(result.isPresent());
        assertEquals("Ahmet Yilmaz", result.get().getName());
    }
}
