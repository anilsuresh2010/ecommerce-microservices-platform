package com.ecommerce.user_service.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UserRequest DTO Tests")
class UserRequestTest {

    private UserRequest userRequest;

    @BeforeEach
    void setUp() {
        userRequest = new UserRequest();
    }

    @Test
    @DisplayName("Should create UserRequest with default constructor")
    void testUserRequestDefaultConstructor() {
        UserRequest request = new UserRequest();
        assertNotNull(request);
        assertNull(request.getUserName());
        assertNull(request.getEmail());
        assertNull(request.getPassword());
        assertEquals(0.0, request.getBalance());
    }

    @Test
    @DisplayName("Should create UserRequest with all args constructor")
    void testUserRequestAllArgsConstructor() {
        UserRequest request = new UserRequest("john_doe", "john@example.com", "password123", 5000.0);
        assertNotNull(request);
        assertEquals("john_doe", request.getUserName());
        assertEquals("john@example.com", request.getEmail());
        assertEquals("password123", request.getPassword());
        assertEquals(5000.0, request.getBalance());
    }

    @Test
    @DisplayName("Should set and get username")
    void testSetAndGetUserName() {
        String username = "john_doe";
        userRequest.setUserName(username);
        assertEquals(username, userRequest.getUserName());
    }

    @Test
    @DisplayName("Should set and get email")
    void testSetAndGetEmail() {
        String email = "john@example.com";
        userRequest.setEmail(email);
        assertEquals(email, userRequest.getEmail());
    }

    @Test
    @DisplayName("Should set and get password")
    void testSetAndGetPassword() {
        String password = "password123";
        userRequest.setPassword(password);
        assertEquals(password, userRequest.getPassword());
    }

    @Test
    @DisplayName("Should set and get balance")
    void testSetAndGetBalance() {
        double balance = 5000.0;
        userRequest.setBalance(balance);
        assertEquals(balance, userRequest.getBalance());
    }

    @Test
    @DisplayName("Should set all fields and retrieve them")
    void testSetAllFieldsAndRetrieve() {
        String username = "test_user";
        String email = "test@example.com";
        String password = "testPass123";
        double balance = 10000.0;

        userRequest.setUserName(username);
        userRequest.setEmail(email);
        userRequest.setPassword(password);
        userRequest.setBalance(balance);

        assertEquals(username, userRequest.getUserName());
        assertEquals(email, userRequest.getEmail());
        assertEquals(password, userRequest.getPassword());
        assertEquals(balance, userRequest.getBalance());
    }

    @Test
    @DisplayName("Should handle email with various formats")
    void testEmailFormats() {
        userRequest.setEmail("user.name+tag@example.co.uk");
        assertEquals("user.name+tag@example.co.uk", userRequest.getEmail());

        userRequest.setEmail("simple@example.com");
        assertEquals("simple@example.com", userRequest.getEmail());
    }

    @Test
    @DisplayName("Should handle password with special characters")
    void testPasswordWithSpecialCharacters() {
        String complexPassword = "P@ssw0rd!#$%^&*()_+-=[]{}|;:,.<>?";
        userRequest.setPassword(complexPassword);
        assertEquals(complexPassword, userRequest.getPassword());
    }

    @Test
    @DisplayName("Should handle zero balance")
    void testZeroBalance() {
        userRequest.setBalance(0.0);
        assertEquals(0.0, userRequest.getBalance());
    }

    @Test
    @DisplayName("Should handle large balance values")
    void testLargeBalance() {
        double largeBalance = 999999999.99;
        userRequest.setBalance(largeBalance);
        assertEquals(largeBalance, userRequest.getBalance());
    }

    @Test
    @DisplayName("Should have Lombok annotations")
    void testLombokAnnotations() {
        assertNotNull(UserRequest.class.getAnnotation(lombok.Data.class));
    }
}

