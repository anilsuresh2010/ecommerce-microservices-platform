package com.ecommerce.user_service.repository;

import com.ecommerce.user_service.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("UserRepository Tests")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        testUser = new User();
        testUser.setUserName("john_doe");
        testUser.setEmail("john@example.com");
        testUser.setPassword("hashedPassword123");
        testUser.setBalance(5000.0);
    }

    @Test
    @DisplayName("Should save user successfully")
    void testSaveUser() {
        User savedUser = userRepository.save(testUser);
        assertNotNull(savedUser.getId());
        assertEquals("john_doe", savedUser.getUserName());
        assertEquals("john@example.com", savedUser.getEmail());
        assertEquals(5000.0, savedUser.getBalance());
    }

    @Test
    @DisplayName("Should find user by username")
    void testFindByUserName() {
        userRepository.save(testUser);
        Optional<User> foundUser = userRepository.findByUserName("john_doe");
        assertTrue(foundUser.isPresent());
        assertEquals("john_doe", foundUser.get().getUserName());
        assertEquals("john@example.com", foundUser.get().getEmail());
    }

    @Test
    @DisplayName("Should return empty optional when user not found")
    void testFindByUserNameNotFound() {
        Optional<User> foundUser = userRepository.findByUserName("nonexistent_user");
        assertFalse(foundUser.isPresent());
        assertTrue(foundUser.isEmpty());
    }

    @Test
    @DisplayName("Should find user by id")
    void testFindById() {
        User savedUser = userRepository.save(testUser);
        Optional<User> foundUser = userRepository.findById(savedUser.getId());
        assertTrue(foundUser.isPresent());
        assertEquals(savedUser.getId(), foundUser.get().getId());
    }

    @Test
    @DisplayName("Should return empty optional when user id not found")
    void testFindByIdNotFound() {
        Optional<User> foundUser = userRepository.findById(999L);
        assertFalse(foundUser.isPresent());
    }

    @Test
    @DisplayName("Should update user successfully")
    void testUpdateUser() {
        User savedUser = userRepository.save(testUser);
        savedUser.setEmail("newemail@example.com");
        savedUser.setBalance(7500.0);

        User updatedUser = userRepository.save(savedUser);

        Optional<User> retrievedUser = userRepository.findById(updatedUser.getId());
        assertTrue(retrievedUser.isPresent());
        assertEquals("newemail@example.com", retrievedUser.get().getEmail());
        assertEquals(7500.0, retrievedUser.get().getBalance());
    }

    @Test
    @DisplayName("Should delete user successfully")
    void testDeleteUser() {
        User savedUser = userRepository.save(testUser);
        Long userId = savedUser.getId();

        userRepository.deleteById(userId);

        Optional<User> deletedUser = userRepository.findById(userId);
        assertFalse(deletedUser.isPresent());
    }

    @Test
    @DisplayName("Should return all users")
    void testFindAll() {
        User user1 = new User(null, "user1", "user1@example.com", "password1", 1000.0);
        User user2 = new User(null, "user2", "user2@example.com", "password2", 2000.0);

        userRepository.save(user1);
        userRepository.save(user2);

        assertEquals(2, userRepository.findAll().size());
    }

    @Test
    @DisplayName("Should handle multiple users with different usernames")
    void testMultipleUsers() {
        User user1 = new User(null, "alice", "alice@example.com", "pass1", 1000.0);
        User user2 = new User(null, "bob", "bob@example.com", "pass2", 2000.0);
        User user3 = new User(null, "charlie", "charlie@example.com", "pass3", 3000.0);

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        assertTrue(userRepository.findByUserName("alice").isPresent());
        assertTrue(userRepository.findByUserName("bob").isPresent());
        assertTrue(userRepository.findByUserName("charlie").isPresent());
        assertEquals(3, userRepository.findAll().size());
    }

    @Test
    @DisplayName("Should handle duplicate username check")
    void testDuplicateUsername() {
        userRepository.save(testUser);
        Optional<User> foundUser1 = userRepository.findByUserName("john_doe");
        Optional<User> foundUser2 = userRepository.findByUserName("john_doe");

        assertTrue(foundUser1.isPresent());
        assertTrue(foundUser2.isPresent());
        assertEquals(foundUser1.get().getId(), foundUser2.get().getId());
    }

    @Test
    @DisplayName("Should count users correctly")
    void testCountUsers() {
        assertEquals(0, userRepository.count());

        userRepository.save(testUser);
        assertEquals(1, userRepository.count());

        User anotherUser = new User(null, "jane_doe", "jane@example.com", "password456", 3000.0);
        userRepository.save(anotherUser);
        assertEquals(2, userRepository.count());
    }
}

