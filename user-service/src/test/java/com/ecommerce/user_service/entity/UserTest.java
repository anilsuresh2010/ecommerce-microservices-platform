package com.ecommerce.user_service.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("User Entity Tests")
class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    @DisplayName("Should create user with default constructor")
    void testUserDefaultConstructor() {
        User newUser = new User();
        assertNotNull(newUser);
        assertNull(newUser.getId());
        assertNull(newUser.getUserName());
        assertNull(newUser.getEmail());
        assertNull(newUser.getPassword());
        assertEquals(0.0, newUser.getBalance());
    }

    @Test
    @DisplayName("Should create user with all args constructor")
    void testUserAllArgsConstructor() {
        User newUser = new User(1L, "john_doe", "john@example.com", "password123", 5000.0);
        assertNotNull(newUser);
        assertEquals(1L, newUser.getId());
        assertEquals("john_doe", newUser.getUserName());
        assertEquals("john@example.com", newUser.getEmail());
        assertEquals("password123", newUser.getPassword());
        assertEquals(5000.0, newUser.getBalance());
    }

    @Test
    @DisplayName("Should set and get user id")
    void testSetAndGetId() {
        user.setId(1L);
        assertEquals(1L, user.getId());
    }

    @Test
    @DisplayName("Should set and get username")
    void testSetAndGetUserName() {
        String username = "john_doe";
        user.setUserName(username);
        assertEquals(username, user.getUserName());
    }

    @Test
    @DisplayName("Should set and get email")
    void testSetAndGetEmail() {
        String email = "john@example.com";
        user.setEmail(email);
        assertEquals(email, user.getEmail());
    }

    @Test
    @DisplayName("Should set and get password")
    void testSetAndGetPassword() {
        String password = "hashedPassword123";
        user.setPassword(password);
        assertEquals(password, user.getPassword());
    }

    @Test
    @DisplayName("Should set and get balance")
    void testSetAndGetBalance() {
        double balance = 5000.0;
        user.setBalance(balance);
        assertEquals(balance, user.getBalance());
    }

    @Test
    @DisplayName("Should update balance correctly")
    void testUpdateBalance() {
        user.setBalance(5000.0);
        user.setBalance(user.getBalance() - 500);
        assertEquals(4500.0, user.getBalance());
    }

    @Test
    @DisplayName("Should handle negative balance")
    void testNegativeBalance() {
        user.setBalance(-100.0);
        assertEquals(-100.0, user.getBalance());
    }

    @Test
    @DisplayName("Should handle zero balance")
    void testZeroBalance() {
        user.setBalance(0.0);
        assertEquals(0.0, user.getBalance());
    }

    @Test
    @DisplayName("Should handle large balance values")
    void testLargeBalance() {
        double largeBalance = 999999999.99;
        user.setBalance(largeBalance);
        assertEquals(largeBalance, user.getBalance());
    }

    @Test
    @DisplayName("Should have correct JPA annotations")
    void testJPAAnnotations() {
        assertNotNull(User.class.getAnnotation(jakarta.persistence.Entity.class));
        assertNotNull(User.class.getAnnotation(jakarta.persistence.Table.class));
    }
}

