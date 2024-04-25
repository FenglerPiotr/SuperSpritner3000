package pl.codecool.supersprinter3000.developer.validation;

public class EmailInUseException extends RuntimeException {
    public EmailInUseException(String message) {
        super(message);
    }
}
