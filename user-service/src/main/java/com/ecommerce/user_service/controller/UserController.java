package com.ecommerce.user_service.controller;

import com.ecommerce.user_service.dto.UserRequest;
import com.ecommerce.user_service.entity.User;
import com.ecommerce.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService service;

    @PostMapping("/register")
    public User register(@RequestBody UserRequest request) {
        log.info("API called to register user: {}", request.getUserName());
        return service.register(request);
    }

    @GetMapping("/{username}")
    public User getUser(@PathVariable String username) {
        log.info("Fetching user profile for: {}", username);
        return service.getUser(username);
    }

    @PostMapping("/{username}/deduct")
    public boolean deduct(@PathVariable String username, @RequestParam double amount) {
        log.info("API called to deduct ₹{} from user {}", amount, username);
        return service.deductBalance(username, amount);
    }
    @PostMapping("/{username}/add")
    public boolean addBalance(@PathVariable String username, @RequestParam double amount) {
        log.info("API called to add ₹{} to user {}", amount, username);
        return service.addBalance(username, amount);
    }

}
