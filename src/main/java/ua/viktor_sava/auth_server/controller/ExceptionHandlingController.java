package ua.viktor_sava.auth_server.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ua.viktor_sava.auth_server.controller.dto.ErrorDto;
import ua.viktor_sava.auth_server.exception.ServiceException;
import ua.viktor_sava.auth_server.exception.UnauthorizedException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionHandlingController {

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDto handleUnauthorizedException(UnauthorizedException e) {
        return new ErrorDto(e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleServiceException(ServiceException e) {
        return new ErrorDto(e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDto handleUnknownException(Exception e) {
        return new ErrorDto(e.getMessage(), LocalDateTime.now());
    }


}
