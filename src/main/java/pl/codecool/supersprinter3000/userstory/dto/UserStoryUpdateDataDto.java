package pl.codecool.supersprinter3000.userstory.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Range;

public record UserStoryUpdateDataDto(
        @Size(min = 5, max = 50, message = "Title must be between 5 and 50 characters")
        String title,
        @NotBlank(message = "Description cannot be empty")
        String description,
        @NotBlank(message = "Acceptance Criteria cannot be empty")
        String acceptanceCriteria,
        @Range(min = 100, max = 1500, message = "Business Value should be between 100 and 1500 points")
        int businessValue,
        @DecimalMin(value = "0.5", message = "Estimation min value is 0.5")
        @DecimalMax(value = "40.0", message = "Estimation max value is 40.0")
        double estimation,
        @NotBlank(message = "Status cannot be empty")
        String status
) {
}
