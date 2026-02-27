package com.devx.todo.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthDtos {

    public record AuthRequest(
            @Email @NotBlank String email,
            @NotBlank @Size(min = 6, max = 72) String password
    ) {
    }

    public record AuthResponse(String token, String email) {
    }
}
