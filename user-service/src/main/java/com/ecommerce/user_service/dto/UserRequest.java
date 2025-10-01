package com.ecommerce.user_service.dto;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    private String userName;
    private String email;
    private String password;
    private double balance;
}
