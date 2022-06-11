package ua.viktor_sava.auth_server.exception;

public class EntityExistsException extends ServiceException {

    public EntityExistsException(String message, String... values) {
        super(message, values);
    }
}
