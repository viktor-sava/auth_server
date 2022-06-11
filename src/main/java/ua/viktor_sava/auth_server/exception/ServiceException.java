package ua.viktor_sava.auth_server.exception;

public class ServiceException extends BaseException {
    public ServiceException(String message, String... values) {
        super(message, values);
    }
}
