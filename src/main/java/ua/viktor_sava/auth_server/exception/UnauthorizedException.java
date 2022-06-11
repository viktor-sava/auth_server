package ua.viktor_sava.auth_server.exception;

public class UnauthorizedException extends BaseException {
    public UnauthorizedException(String message, String... values) {
        super(message, values);
    }
}
