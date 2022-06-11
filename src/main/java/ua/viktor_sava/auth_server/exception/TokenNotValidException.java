package ua.viktor_sava.auth_server.exception;

public class TokenNotValidException extends ServiceException {
    public TokenNotValidException(String message, String... values) {
        super(message, values);
    }
}
