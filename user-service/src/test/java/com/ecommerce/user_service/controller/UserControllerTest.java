package com.ecommerce.user_service.controller;

import com.ecommerce.user_service.dto.UserRequest;
import com.ecommerce.user_service.entity.User;
import com.ecommerce.user_service.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
@DisplayName("UserController Tests")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private UserRequest userRequest;
    private User testUser;

    @BeforeEach
    void setUp() {
        userRequest = new UserRequest();
        userRequest.setUserName("john_doe");
        userRequest.setEmail("john@example.com");
        userRequest.setPassword("password123");
        userRequest.setBalance(1000.0);

        testUser = new User();
        testUser.setId(1L);
        testUser.setUserName("john_doe");
        testUser.setEmail("john@example.com");
        testUser.setPassword("hashedPassword123");
        testUser.setBalance(5000.0);
    }

    @Test
    @DisplayName("Should register user successfully")
    void testRegisterUserSuccess() throws Exception {
        when(userService.register(any(UserRequest.class))).thenReturn(testUser);

        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.userName", is("john_doe")))
                .andExpect(jsonPath("$.email", is("john@example.com")))
                .andExpect(jsonPath("$.balance", is(5000.0)));

        verify(userService, times(1)).register(any(UserRequest.class));
    }

    @Test
    @DisplayName("Should handle register request with valid data")
    void testRegisterWithValidData() throws Exception {
        when(userService.register(any(UserRequest.class))).thenReturn(testUser);

        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Should get user successfully")
    void testGetUserSuccess() throws Exception {
        when(userService.getUser("john_doe")).thenReturn(testUser);

        mockMvc.perform(get("/users/john_doe")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.userName", is("john_doe")))
                .andExpect(jsonPath("$.email", is("john@example.com")))
                .andExpect(jsonPath("$.balance", is(5000.0)));

        verify(userService, times(1)).getUser("john_doe");
    }

    @Test
    @DisplayName("Should return 500 when user not found")
    void testGetUserNotFound() throws Exception {
        when(userService.getUser("nonexistent")).thenThrow(new RuntimeException("User not found"));

        mockMvc.perform(get("/users/nonexistent")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Should deduct balance successfully")
    void testDeductBalanceSuccess() throws Exception {
        when(userService.deductBalance("john_doe", 1000.0)).thenReturn(true);

        mockMvc.perform(post("/users/john_doe/deduct")
                .param("amount", "1000.0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(userService, times(1)).deductBalance("john_doe", 1000.0);
    }

    @Test
    @DisplayName("Should return false when insufficient balance for deduction")
    void testDeductBalanceInsufficientFunds() throws Exception {
        when(userService.deductBalance("john_doe", 10000.0)).thenReturn(false);

        mockMvc.perform(post("/users/john_doe/deduct")
                .param("amount", "10000.0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));

        verify(userService, times(1)).deductBalance("john_doe", 10000.0);
    }

    @Test
    @DisplayName("Should add balance successfully")
    void testAddBalanceSuccess() throws Exception {
        when(userService.addBalance("john_doe", 2000.0)).thenReturn(true);

        mockMvc.perform(post("/users/john_doe/add")
                .param("amount", "2000.0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(userService, times(1)).addBalance("john_doe", 2000.0);
    }

    @Test
    @DisplayName("Should handle deduct with decimal amounts")
    void testDeductWithDecimalAmount() throws Exception {
        when(userService.deductBalance("john_doe", 99.99)).thenReturn(true);

        mockMvc.perform(post("/users/john_doe/deduct")
                .param("amount", "99.99")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @DisplayName("Should handle add with decimal amounts")
    void testAddWithDecimalAmount() throws Exception {
        when(userService.addBalance("john_doe", 50.50)).thenReturn(true);

        mockMvc.perform(post("/users/john_doe/add")
                .param("amount", "50.50")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @DisplayName("Should handle large balance amounts")
    void testHandleLargeBalanceAmount() throws Exception {
        when(userService.addBalance("john_doe", 999999.99)).thenReturn(true);

        mockMvc.perform(post("/users/john_doe/add")
                .param("amount", "999999.99")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @DisplayName("Should handle deduct from user not found")
    void testDeductFromNonExistentUser() throws Exception {
        when(userService.deductBalance("nonexistent", 1000.0))
                .thenThrow(new RuntimeException("User not found"));

        mockMvc.perform(post("/users/nonexistent/deduct")
                .param("amount", "1000.0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Should handle add to user not found")
    void testAddToNonExistentUser() throws Exception {
        when(userService.addBalance("nonexistent", 1000.0))
                .thenThrow(new RuntimeException("User not found"));

        mockMvc.perform(post("/users/nonexistent/add")
                .param("amount", "1000.0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Should register multiple users")
    void testRegisterMultipleUsers() throws Exception {
        User user2 = new User(2L, "jane_doe", "jane@example.com", "hashedPass", 5000.0);

        when(userService.register(any(UserRequest.class)))
                .thenReturn(testUser)
                .thenReturn(user2);

        // Register first user
        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk());

        // Register second user
        UserRequest secondRequest = new UserRequest("jane_doe", "jane@example.com", "password456", 1000.0);
        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(secondRequest)))
                .andExpect(status().isOk());

        verify(userService, times(2)).register(any(UserRequest.class));
    }

    @Test
    @DisplayName("Should handle get user with special characters in username")
    void testGetUserWithSpecialCharacters() throws Exception {
        when(userService.getUser("user_name.123")).thenReturn(testUser);

        mockMvc.perform(get("/users/user_name.123")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return correct status codes")
    void testStatusCodes() throws Exception {
        when(userService.register(any(UserRequest.class))).thenReturn(testUser);
        when(userService.getUser("john_doe")).thenReturn(testUser);

        // Test POST register - should be 200 OK
        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk());

        // Test GET user - should be 200 OK
        mockMvc.perform(get("/users/john_doe")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should parse JSON response correctly")
    void testJsonResponseParsing() throws Exception {
        when(userService.getUser("john_doe")).thenReturn(testUser);

        MvcResult result = mockMvc.perform(get("/users/john_doe")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        User parsedUser = objectMapper.readValue(content, User.class);

        assert parsedUser.getId() == 1L;
        assert parsedUser.getUserName().equals("john_doe");
    }
}

