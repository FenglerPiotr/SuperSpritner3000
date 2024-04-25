package pl.codecool.supersprinter3000.developer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import pl.codecool.supersprinter3000.developer.validation.Unoccupied;

public record NewDeveloperDto(
        @NotBlank(message = "First name cannot be empty")
        String firstName,
        @NotBlank(message = "Last name cannot be empty")
        String lastName,
        @Unoccupied
        @Email(message = "This is not a correct email")
        String email
) {

}
