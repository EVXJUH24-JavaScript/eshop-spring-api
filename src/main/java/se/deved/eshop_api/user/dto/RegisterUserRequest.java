package se.deved.eshop_api.user.dto;

import lombok.Data;

@Data
public class RegisterUserRequest {
    private String username;
    private String password;
}
