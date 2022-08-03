package com.example.final_exam_homework.exceptions;

import com.example.final_exam_homework.dtos.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(InvalidLoginCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ErrorDTO handleInvalidLoginCredentialsException(RuntimeException exception) {
        return new ErrorDTO(exception.getMessage());
    }

    @ExceptionHandler(InvalidPasswordLengthException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ErrorDTO handleInvalidPasswordLengthException(RuntimeException exception) {
        return new ErrorDTO(exception.getMessage());
    }

    @ExceptionHandler(MissingUsernameAndPasswordException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ErrorDTO handleMissingUsernameAndPasswordException(RuntimeException exception) {
        return new ErrorDTO(exception.getMessage());
    }

    @ExceptionHandler(MissingUsernameOrPasswordException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ErrorDTO handleMissingUsernameOrPasswordException(RuntimeException exception) {
        return new ErrorDTO(exception.getMessage());
    }

    @ExceptionHandler(MissingUsernameException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ErrorDTO handleMissingUsernameException(RuntimeException exception) {
        return new ErrorDTO(exception.getMessage());
    }

    @ExceptionHandler(MissingPasswordException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ErrorDTO handleMissingPasswordException(RuntimeException exception) {
        return new ErrorDTO(exception.getMessage());
    }

    @ExceptionHandler(UsernameAlreadyInUseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ErrorDTO handleUsernameAlreadyInUseException(RuntimeException exception) {
        return new ErrorDTO(exception.getMessage());
    }

    @ExceptionHandler(MissingNameException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ErrorDTO handleMissingNameException(RuntimeException exception) {
        return new ErrorDTO(exception.getMessage());
    }

    @ExceptionHandler(MissingDescriptionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ErrorDTO handleMissingDescriptionException(RuntimeException exception) {
        return new ErrorDTO(exception.getMessage());
    }

    @ExceptionHandler(MissingPhotoUrlException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ErrorDTO handleMissingPhotoUrlException(RuntimeException exception) {
        return new ErrorDTO(exception.getMessage());
    }

    @ExceptionHandler(InvalidPhotoUrlException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ErrorDTO handleInvalidPhotoUrlException(RuntimeException exception) {
        return new ErrorDTO(exception.getMessage());
    }

    @ExceptionHandler(InvalidPriceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ErrorDTO handleInvalidPriceException(RuntimeException exception) {
        return new ErrorDTO(exception.getMessage());
    }
}
