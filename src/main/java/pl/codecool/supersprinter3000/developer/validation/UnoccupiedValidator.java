package pl.codecool.supersprinter3000.developer.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.codecool.supersprinter3000.developer.DeveloperRepository;

public class UnoccupiedValidator implements ConstraintValidator<Unoccupied, String> {

    private final DeveloperRepository developerRepository;

    public UnoccupiedValidator(DeveloperRepository developerRepository) {
        this.developerRepository = developerRepository;
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        developerRepository.findByEmail(email).ifPresent(
                d -> {
                    throw new EmailInUseException(email + " is already in use");
                }
        );

        return true;
    }
}
