package ua.viktor_sava.auth_server.exception;

public class EntityNotFoundException extends ServiceException {
    public EntityNotFoundException(String message, String... values) {
        super(message, values);
    }
}
