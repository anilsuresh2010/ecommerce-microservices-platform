package com.ecommerce.user_service.controller;

import com.ecommerce.user_service.entity.User;
import com.ecommerce.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private  final UserService service;

    @GetMapping
    public List<User> getAllUser(){
        return service.getAllUser();
    }
    @PostMapping
    public User createUser(@RequestBody User user){
        return service.createUser(user);
    }
}
