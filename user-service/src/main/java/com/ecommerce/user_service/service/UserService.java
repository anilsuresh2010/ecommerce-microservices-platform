package com.ecommerce.user_service.service;

import com.ecommerce.user_service.entity.User;
import com.ecommerce.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public List<User> getAllUser(){
        return repository.findAll();
    }

    public User createUser(User user){
        if(user.getBalance() == null){
            user.setBalance(BigDecimal.ZERO);
        }
        return repository.save(user);
    }

    public User getUserById(Long id){
        return repository.findById(id).orElseThrow(() -> new RuntimeException("User not found.."));
    }

    public User addBalance(Long userId, BigDecimal amount){
        User user = getUserById(userId);
        user.setBalance(user.getBalance().add(amount));
        return repository.save(user);
    }

    public User deductBalance(Long userId, BigDecimal amount){
        User user = getUserById(userId);
        if(user.getBalance().compareTo(amount) <0)
        {
        throw new RuntimeException("Insufficient balance Please add the balance..");
        }
        user.setBalance(user.getBalance().subtract(amount));
        return repository.save(user);
    }
}
