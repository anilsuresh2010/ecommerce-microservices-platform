package com.ecommerce.user_service.service;

import com.ecommerce.user_service.dto.UserRequest;
import com.ecommerce.user_service.entity.User;
import com.ecommerce.user_service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Tests")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private UserRequest userRequest;
    private User testUser;

    @BeforeEach
    void setUp() {
        userRequest = new UserRequest();
        userRequest.setUserName("john_doe");
        userRequest.setEmail("john@example.com");
        userRequest.setPassword("plainPassword123");
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
    void testRegisterUserSuccess() {
        when(passwordEncoder.encode(userRequest.getPassword())).thenReturn("hashedPassword123");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User registeredUser = userService.register(userRequest);

        assertNotNull(registeredUser);
        assertEquals("john_doe", registeredUser.getUserName());
        assertEquals("john@example.com", registeredUser.getEmail());
        assertEquals(5000.0, registeredUser.getBalance());

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        assertEquals("john_doe", savedUser.getUserName());
        assertEquals("john@example.com", savedUser.getEmail());
        assertEquals("hashedPassword123", savedUser.getPassword());
        assertEquals(5000.0, savedUser.getBalance());
    }

    @Test
    @DisplayName("Should encode password during registration")
    void testPasswordEncodingDuringRegistration() {
        when(passwordEncoder.encode(userRequest.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        userService.register(userRequest);

        verify(passwordEncoder, times(1)).encode("plainPassword123");
    }

    @Test
    @DisplayName("Should set default balance to 5000 during registration")
    void testDefaultBalanceOnRegistration() {
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        userService.register(userRequest);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        assertEquals(5000.0, userCaptor.getValue().getBalance());
    }

    @Test
    @DisplayName("Should get user by username successfully")
    void testGetUserSuccess() {
        when(userRepository.findByUserName("john_doe")).thenReturn(Optional.of(testUser));

        User user = userService.getUser("john_doe");

        assertNotNull(user);
        assertEquals("john_doe", user.getUserName());
        assertEquals(1L, user.getId());
        verify(userRepository, times(1)).findByUserName("john_doe");
    }

    @Test
    @DisplayName("Should throw RuntimeException when user not found")
    void testGetUserNotFound() {
        when(userRepository.findByUserName("nonexistent")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.getUser("nonexistent");
        });

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findByUserName("nonexistent");
    }

    @Test
    @DisplayName("Should deduct balance successfully")
    void testDeductBalanceSuccess() {
        testUser.setBalance(5000.0);
        when(userRepository.findByUserName("john_doe")).thenReturn(Optional.of(testUser));
        when(userRepository.save(testUser)).thenReturn(testUser);

        boolean result = userService.deductBalance("john_doe", 1000.0);

        assertTrue(result);
        assertEquals(4000.0, testUser.getBalance());
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    @DisplayName("Should return false when insufficient balance for deduction")
    void testDeductBalanceInsufficientFunds() {
        testUser.setBalance(500.0);
        when(userRepository.findByUserName("john_doe")).thenReturn(Optional.of(testUser));

        boolean result = userService.deductBalance("john_doe", 1000.0);

        assertFalse(result);
        assertEquals(500.0, testUser.getBalance());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should deduct exact balance amount")
    void testDeductExactBalance() {
        testUser.setBalance(1000.0);
        when(userRepository.findByUserName("john_doe")).thenReturn(Optional.of(testUser));
        when(userRepository.save(testUser)).thenReturn(testUser);

        boolean result = userService.deductBalance("john_doe", 1000.0);

        assertTrue(result);
        assertEquals(0.0, testUser.getBalance());
    }

    @Test
    @DisplayName("Should return false when deducting from zero balance")
    void testDeductFromZeroBalance() {
        testUser.setBalance(0.0);
        when(userRepository.findByUserName("john_doe")).thenReturn(Optional.of(testUser));

        boolean result = userService.deductBalance("john_doe", 1.0);

        assertFalse(result);
        assertEquals(0.0, testUser.getBalance());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should add balance successfully")
    void testAddBalanceSuccess() {
        testUser.setBalance(5000.0);
        when(userRepository.findByUserName("john_doe")).thenReturn(Optional.of(testUser));
        when(userRepository.save(testUser)).thenReturn(testUser);

        boolean result = userService.addBalance("john_doe", 2000.0);

        assertTrue(result);
        assertEquals(7000.0, testUser.getBalance());
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    @DisplayName("Should add balance to zero balance")
    void testAddBalanceToZero() {
        testUser.setBalance(0.0);
        when(userRepository.findByUserName("john_doe")).thenReturn(Optional.of(testUser));
        when(userRepository.save(testUser)).thenReturn(testUser);

        boolean result = userService.addBalance("john_doe", 5000.0);

        assertTrue(result);
        assertEquals(5000.0, testUser.getBalance());
    }

    @Test
    @DisplayName("Should handle multiple deductions correctly")
    void testMultipleDeductions() {
        testUser.setBalance(10000.0);
        when(userRepository.findByUserName("john_doe")).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            return user;
        });

        userService.deductBalance("john_doe", 2000.0);
        userService.deductBalance("john_doe", 3000.0);

        assertEquals(5000.0, testUser.getBalance());
    }

    @Test
    @DisplayName("Should handle multiple additions correctly")
    void testMultipleAdditions() {
        testUser.setBalance(1000.0);
        when(userRepository.findByUserName("john_doe")).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            return user;
        });

        userService.addBalance("john_doe", 2000.0);
        userService.addBalance("john_doe", 3000.0);

        assertEquals(6000.0, testUser.getBalance());
    }

    @Test
    @DisplayName("Should throw RuntimeException when deducting from non-existent user")
    void testDeductFromNonExistentUser() {
        when(userRepository.findByUserName("nonexistent")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.deductBalance("nonexistent", 1000.0);
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw RuntimeException when adding to non-existent user")
    void testAddToNonExistentUser() {
        when(userRepository.findByUserName("nonexistent")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.addBalance("nonexistent", 1000.0);
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    @DisplayName("Should handle small deduction amounts")
    void testSmallDeductionAmount() {
        testUser.setBalance(100.0);
        when(userRepository.findByUserName("john_doe")).thenReturn(Optional.of(testUser));
        when(userRepository.save(testUser)).thenReturn(testUser);

        boolean result = userService.deductBalance("john_doe", 0.01);

        assertTrue(result);
        assertEquals(99.99, testUser.getBalance(), 0.001);
    }

    @Test
    @DisplayName("Should handle large balance operations")
    void testLargeBalanceOperations() {
        testUser.setBalance(1000000.0);
        when(userRepository.findByUserName("john_doe")).thenReturn(Optional.of(testUser));
        when(userRepository.save(testUser)).thenReturn(testUser);

        boolean result = userService.deductBalance("john_doe", 500000.0);

        assertTrue(result);
        assertEquals(500000.0, testUser.getBalance());
    }
}

