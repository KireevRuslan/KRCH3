
package com.example.entryWithQrCode.controller;

import com.example.entryWithQrCode.dto.ErrorResponse;
import com.example.entryWithQrCode.exception.QrCodeAlreadyUsedException;
import com.example.entryWithQrCode.exception.QrCodeNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import com.example.entryWithQrCode.exception.ParticipantNotFoundException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(
            MethodArgumentTypeMismatchException exception
    ) {
        if ("uuid".equals(exception.getName())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("UUID имеет неверный формат"));
        }

        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse("Некорректный параметр запроса"));
    }

    @ExceptionHandler(ParticipantNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleParticipantNotFound(
            ParticipantNotFoundException exception
    ) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(QrCodeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleQrCodeNotFound(
            QrCodeNotFoundException exception
    ) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(QrCodeAlreadyUsedException.class)
    public ResponseEntity<ErrorResponse> handleQrCodeAlreadyUsed(
            QrCodeAlreadyUsedException exception
    ) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotAllowed(
            HttpRequestMethodNotSupportedException exception
    ) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new ErrorResponse(
                        "HTTP-метод не поддерживается для этого адреса"
                ));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(
            NoResourceFoundException exception
    ) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(
                        "Запрашиваемый адрес не найден"
                ));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParameter(
            MissingServletRequestParameterException exception
    ) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(
                        "Отсутствует обязательный параметр: "
                                + exception.getParameterName()
                ));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleInvalidBody(
            HttpMessageNotReadableException exception
    ) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(
                        "Некорректное тело запроса. Проверьте формат JSON"
                ));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationError(
            MethodArgumentNotValidException exception
    ) {
        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .findFirst()
                .orElse("Ошибка проверки входных данных");

        return ResponseEntity.badRequest()
                .body(new ErrorResponse(message));
    }
}

