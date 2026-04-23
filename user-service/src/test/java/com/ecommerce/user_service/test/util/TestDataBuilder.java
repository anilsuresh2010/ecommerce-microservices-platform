package com.ecommerce.user_service.test.util;

import com.ecommerce.user_service.dto.UserRequest;
import com.ecommerce.user_service.entity.User;

/**
 * Test data builder utility class for creating test fixtures.
 * Provides convenient factory methods for creating test entities and DTOs.
 */
public class TestDataBuilder {

    /**
     * Creates a default User entity for testing.
     */
    public static User createDefaultUser() {
        return new User(
                1L,
                "test_user",
                "test@example.com",
                "hashedPassword123",
                5000.0
        );
    }

    /**
     * Creates a custom User entity.
     */
    public static User createUserWithBalance(String username, double balance) {
        return new User(
                null,
                username,
                username + "@example.com",
                "hashedPassword",
                balance
        );
    }

    /**
     * Creates a User with all custom parameters.
     */
    public static User createCustomUser(Long id, String username, String email, String password, double balance) {
        return new User(id, username, email, password, balance);
    }

    /**
     * Creates a default UserRequest DTO for testing.
     */
    public static UserRequest createDefaultUserRequest() {
        return new UserRequest(
                "test_user",
                "test@example.com",
                "plainPassword123",
                1000.0
        );
    }

    /**
     * Creates a custom UserRequest DTO.
     */
    public static UserRequest createUserRequest(String username, String email, String password, double balance) {
        return new UserRequest(username, email, password, balance);
    }

    /**
     * Creates a User with minimum required fields.
     */
    public static User createMinimalUser(String username) {
        return new User(
                null,
                username,
                null,
                null,
                0.0
        );
    }

    /**
     * Creates multiple users for testing.
     */
    public static User[] createMultipleUsers(int count) {
        User[] users = new User[count];
        for (int i = 1; i <= count; i++) {
            users[i - 1] = new User(
                    (long) i,
                    "user" + i,
                    "user" + i + "@example.com",
                    "password" + i,
                    5000.0 * i
            );
        }
        return users;
    }

    /**
     * Creates a User with special characters in username and email.
     */
    public static User createUserWithSpecialCharacters() {
        return new User(
                null,
                "user_name.123-test",
                "user+tag@example.co.uk",
                "P@ssw0rd!#$%",
                5000.0
        );
    }

    /**
     * Creates a User with zero balance.
     */
    public static User createUserWithZeroBalance() {
        return new User(
                null,
                "zero_balance_user",
                "zero@example.com",
                "password",
                0.0
        );
    }

    /**
     * Creates a User with negative balance (for testing edge cases).
     */
    public static User createUserWithNegativeBalance() {
        return new User(
                null,
                "negative_user",
                "negative@example.com",
                "password",
                -500.0
        );
    }

    /**
     * Creates a User with very large balance.
     */
    public static User createUserWithLargeBalance() {
        return new User(
                null,
                "rich_user",
                "rich@example.com",
                "password",
                999999999.99
        );
    }

    /**
     * Creates a UserRequest with minimum required fields.
     */
    public static UserRequest createMinimalUserRequest(String username) {
        return new UserRequest(username, null, null, 0.0);
    }

    /**
     * Creates a UserRequest with very long password.
     */
    public static UserRequest createUserRequestWithLongPassword() {
        String longPassword = "a".repeat(100);
        return new UserRequest("long_pass_user", "longpass@example.com", longPassword, 1000.0);
    }

    /**
     * Creates a UserRequest with special characters.
     */
    public static UserRequest createUserRequestWithSpecialChars() {
        return new UserRequest(
                "special_user",
                "special+tag@example.co.uk",
                "P@ssw0rd!#$%^&*()_+-=[]{}|;:,.<>?",
                5000.0
        );
    }

    /**
     * Creates a UserRequest with unicode characters.
     */
    public static UserRequest createUserRequestWithUnicode() {
        return new UserRequest(
                "unicode_user",
                "unicode@example.com",
                "पासवर्ड密码🔐",
                5000.0
        );
    }
}

