package pl.codecool.supersprinter3000.auth.registration;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NewUserRegistrationDto(
        @NotBlank(message = "username cannot be empty")
        @Email(message = "Username must be a valid email")
        // TODO: unique email validation
        String username,
        @Size(min = 10, message = "Password must have at least 12 characters")
        String password
) {
}
