package ua.viktor_sava.auth_server.exception;

public class BaseException extends RuntimeException {

    public BaseException(String message, String... values) {
        super(setAllValues(message, values));
    }

    private static String setAllValues(String message, String[] values) {
        for (String value : values) {
            message = message.replace("{}", value);
        }
        return message;
    }

}
