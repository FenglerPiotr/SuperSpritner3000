package pl.codecool.supersprinter3000.errorhandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.codecool.supersprinter3000.developer.exception.DeveloperNotFoundException;
import pl.codecool.supersprinter3000.developer.exception.UnsupportedStatsParamException;
import pl.codecool.supersprinter3000.developer.validation.EmailInUseException;
import pl.codecool.supersprinter3000.userstory.UserStoryNotFoundException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(EmailInUseException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleOccupiedEmail(EmailInUseException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler({UserStoryNotFoundException.class, DeveloperNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleResourceNotFound(RuntimeException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequest(MethodArgumentNotValidException e) {
        String errMsg = e.getAllErrors().stream()
                .map(ex -> ex.getDefaultMessage())
                .collect(Collectors.joining(" | "));

        return new ErrorResponse(errMsg);
    }

    @ExceptionHandler(UnsupportedStatsParamException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleUnsupportedDeveloperStatsParam(UnsupportedStatsParamException e) {
        return new ErrorResponse(e.getMessage());
    }

    public record ErrorResponse(String info) {

    }
}
