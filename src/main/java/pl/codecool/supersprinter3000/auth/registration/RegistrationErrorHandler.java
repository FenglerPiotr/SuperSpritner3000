package pl.codecool.supersprinter3000.auth.registration;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.codecool.supersprinter3000.auth.validation.UserAlreadyExistsException;

@RestControllerAdvice
public class RegistrationErrorHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleUserTaken(UserAlreadyExistsException e) {
        return new ErrorResponse(e.getMessage());
    }

    public record ErrorResponse(String info) { }
}
