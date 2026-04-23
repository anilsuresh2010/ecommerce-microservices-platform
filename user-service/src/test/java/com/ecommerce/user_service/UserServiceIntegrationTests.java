package com.ecommerce.user_service;

import com.ecommerce.user_service.controller.UserController;
import com.ecommerce.user_service.dto.UserRequest;
import com.ecommerce.user_service.entity.User;
import com.ecommerce.user_service.repository.UserRepository;
import com.ecommerce.user_service.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("User Service Integration Tests")
class UserServiceIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserController userController;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Should complete full user lifecycle - register, get, update balance")
    void testCompleteUserLifecycle() throws Exception {
        // Step 1: Register a new user
        UserRequest registrationRequest = new UserRequest();
        registrationRequest.setUserName("integration_user");
        registrationRequest.setEmail("integration@example.com");
        registrationRequest.setPassword("securePassword123");
        registrationRequest.setBalance(1000.0);

        mockMvc.perform(post("/users/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName", is("integration_user")))
                .andExpect(jsonPath("$.email", is("integration@example.com")))
                .andExpect(jsonPath("$.balance", is(5000.0))); // Should have default balance

        // Verify user is saved in database
        assertTrue(userRepository.findByUserName("integration_user").isPresent());

        // Step 2: Get the registered user
        mockMvc.perform(get("/users/integration_user")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName", is("integration_user")))
                .andExpect(jsonPath("$.balance", is(5000.0)));

        // Step 3: Deduct balance
        mockMvc.perform(post("/users/integration_user/deduct")
                .param("amount", "1000.0")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        // Verify balance was deducted
        User updatedUser = userRepository.findByUserName("integration_user").orElseThrow();
        assertEquals(4000.0, updatedUser.getBalance());

        // Step 4: Add balance
        mockMvc.perform(post("/users/integration_user/add")
                .param("amount", "2500.0")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        // Verify balance was added
        updatedUser = userRepository.findByUserName("integration_user").orElseThrow();
        assertEquals(6500.0, updatedUser.getBalance());
    }

    @Test
    @DisplayName("Should handle multiple users with concurrent operations")
    void testMultipleUsersWithConcurrentOperations() throws Exception {
        // Register User 1
        UserRequest user1Request = new UserRequest("user1", "user1@test.com", "pass1", 0);
        mockMvc.perform(post("/users/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(user1Request)))
                .andExpect(status().isOk());

        // Register User 2
        UserRequest user2Request = new UserRequest("user2", "user2@test.com", "pass2", 0);
        mockMvc.perform(post("/users/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(user2Request)))
                .andExpect(status().isOk());

        // Verify both users exist
        assertEquals(2, userRepository.count());

        // Deduct from User 1
        mockMvc.perform(post("/users/user1/deduct")
                .param("amount", "500.0")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        // Add to User 2
        mockMvc.perform(post("/users/user2/add")
                .param("amount", "1000.0")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        // Verify independent operations
        User user1 = userRepository.findByUserName("user1").orElseThrow();
        User user2 = userRepository.findByUserName("user2").orElseThrow();

        assertEquals(4500.0, user1.getBalance()); // 5000 - 500
        assertEquals(6000.0, user2.getBalance()); // 5000 + 1000
    }

    @Test
    @DisplayName("Should prevent deduction when insufficient balance")
    void testInsufficientBalanceScenario() throws Exception {
        // Register user with initial balance
        UserRequest registrationRequest = new UserRequest("poor_user", "poor@test.com", "pass", 0);
        mockMvc.perform(post("/users/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isOk());

        // Try to deduct more than balance
        mockMvc.perform(post("/users/poor_user/deduct")
                .param("amount", "10000.0")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));

        // Verify balance unchanged
        User user = userRepository.findByUserName("poor_user").orElseThrow();
        assertEquals(5000.0, user.getBalance());
    }

    @Test
    @DisplayName("Should handle password encoding for registered users")
    void testPasswordEncodingOnRegistration() throws Exception {
        String plainPassword = "mySecurePassword123!";
        UserRequest registrationRequest = new UserRequest("security_user", "security@test.com", plainPassword, 0);

        mockMvc.perform(post("/users/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isOk());

        // Verify password is stored encoded in database
        User registeredUser = userRepository.findByUserName("security_user").orElseThrow();
        assertNotEquals(plainPassword, registeredUser.getPassword());
        assertNotNull(registeredUser.getPassword());
        assertTrue(registeredUser.getPassword().length() > plainPassword.length());
    }

    @Test
    @DisplayName("Should return 500 error for non-existent user operations")
    void testOperationsOnNonExistentUser() throws Exception {
        mockMvc.perform(get("/users/nonexistent_user")
                .contentType("application/json"))
                .andExpect(status().isInternalServerError());

        mockMvc.perform(post("/users/nonexistent_user/deduct")
                .param("amount", "100.0")
                .contentType("application/json"))
                .andExpect(status().isInternalServerError());

        mockMvc.perform(post("/users/nonexistent_user/add")
                .param("amount", "100.0")
                .contentType("application/json"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Should validate database persistence after balance operations")
    void testDatabasePersistenceAfterOperations() throws Exception {
        // Register user
        UserRequest registrationRequest = new UserRequest("persistent_user", "persist@test.com", "pass", 0);
        mockMvc.perform(post("/users/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isOk());

        // Perform operations
        mockMvc.perform(post("/users/persistent_user/deduct")
                .param("amount", "1500.0")
                .contentType("application/json"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/users/persistent_user/add")
                .param("amount", "500.0")
                .contentType("application/json"))
                .andExpect(status().isOk());

        // Query database directly
        User user = userRepository.findByUserName("persistent_user").orElseThrow();
        assertEquals(4000.0, user.getBalance()); // 5000 - 1500 + 500

        // Verify via API endpoint as well
        mockMvc.perform(get("/users/persistent_user")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance", is(4000.0)));
    }

    @Test
    @DisplayName("Should handle rapid sequential operations on same user")
    void testRapidSequentialOperations() throws Exception {
        // Register user
        UserRequest registrationRequest = new UserRequest("rapid_user", "rapid@test.com", "pass", 0);
        mockMvc.perform(post("/users/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isOk());

        // Perform multiple rapid operations
        for (int i = 0; i < 5; i++) {
            mockMvc.perform(post("/users/rapid_user/deduct")
                    .param("amount", "200.0")
                    .contentType("application/json"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("true"));
        }

        // Verify final balance
        User user = userRepository.findByUserName("rapid_user").orElseThrow();
        assertEquals(4000.0, user.getBalance()); // 5000 - (200 * 5)
    }

    @Test
    @DisplayName("Should validate decimal precision in balance operations")
    void testDecimalPrecisionInOperations() throws Exception {
        // Register user
        UserRequest registrationRequest = new UserRequest("decimal_user", "decimal@test.com", "pass", 0);
        mockMvc.perform(post("/users/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isOk());

        // Perform operations with decimal amounts
        mockMvc.perform(post("/users/decimal_user/deduct")
                .param("amount", "123.45")
                .contentType("application/json"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/users/decimal_user/add")
                .param("amount", "67.89")
                .contentType("application/json"))
                .andExpect(status().isOk());

        // Verify precise calculation
        User user = userRepository.findByUserName("decimal_user").orElseThrow();
        assertEquals(5000.0 - 123.45 + 67.89, user.getBalance(), 0.01);
    }

    @Test
    @DisplayName("Should maintain data consistency across operations")
    void testDataConsistency() throws Exception {
        // Register multiple users
        for (int i = 1; i <= 3; i++) {
            UserRequest request = new UserRequest("user" + i, "user" + i + "@test.com", "pass", 0);
            mockMvc.perform(post("/users/register")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }

        // Each user should have 5000 balance
        for (int i = 1; i <= 3; i++) {
            User user = userRepository.findByUserName("user" + i).orElseThrow();
            assertEquals(5000.0, user.getBalance());
        }

        assertEquals(3, userRepository.count());
    }
}

