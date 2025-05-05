package se.deved.eshop_api.user.dto;

import lombok.Data;

@Data
public class LoginUserRequest {
    private String username;
    private String password;
}
