package com.ecommerce.user_service.service;

import com.ecommerce.user_service.dto.UserRequest;
import com.ecommerce.user_service.entity.User;
import com.ecommerce.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;

    public User register(UserRequest request) {
        log.info("Registering new user: {}", request.getUserName());
        User user = new User();
        user.setUserName(request.getUserName());
        user.setEmail(request.getEmail());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setBalance(5000); // default balance
        return repository.save(user);
    }

    public User getUser(String username) {
        return repository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public boolean deductBalance(String username, double amount) {
        User user = getUser(username);
        if (user.getBalance() >= amount) {
            user.setBalance(user.getBalance() - amount);
            repository.save(user);
            log.info("Deducted ₹{} from user {}. Remaining balance: ₹{}", amount, username, user.getBalance());
            return true;
        } else {
            log.warn("Insufficient balance for user {}", username);
            return false;
        }
    }
    public boolean addBalance(String username, double amount) {
        User user = getUser(username);
        user.setBalance(user.getBalance() + amount);
        repository.save(user);
        log.info("Added ₹{} to user {}. New balance: ₹{}", amount, username, user.getBalance());
        return true;
    }

}
