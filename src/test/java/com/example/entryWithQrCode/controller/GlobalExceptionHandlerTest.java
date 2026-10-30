package com.example.entryWithQrCode.controller;

import com.example.entryWithQrCode.exception.ParticipantNotFoundException;
import com.example.entryWithQrCode.exception.QrCodeAlreadyUsedException;
import com.example.entryWithQrCode.exception.QrCodeNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void handleTypeMismatch_whenUuid_returnsBadRequest() {
        MethodArgumentTypeMismatchException exception =
                new MethodArgumentTypeMismatchException(
                        "invalid", UUID.class, "uuid", null, null
                );

        var response = handler.handleTypeMismatch(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(
                "UUID имеет неверный формат",
                response.getBody().message()
        );
    }

    @Test
    void handleTypeMismatch_whenOtherParameter_returnsBadRequest() {
        MethodArgumentTypeMismatchException exception =
                new MethodArgumentTypeMismatchException(
                        "invalid", Long.class, "id", null, null
                );

        var response = handler.handleTypeMismatch(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(
                "Некорректный параметр запроса",
                response.getBody().message()
        );
    }

    @Test
    void handleParticipantNotFound_returnsNotFound() {
        var response = handler.handleParticipantNotFound(
                new ParticipantNotFoundException(1L)
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(
                "Участник с ID 1 не найден",
                response.getBody().message()
        );
    }

    @Test
    void handleQrCodeNotFound_returnsNotFound() {
        var response = handler.handleQrCodeNotFound(
                new QrCodeNotFoundException(2L)
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(
                "QR-код с ID 2 не найден",
                response.getBody().message()
        );
    }

    @Test
    void handleQrCodeAlreadyUsed_returnsConflict() {
        var response = handler.handleQrCodeAlreadyUsed(
                new QrCodeAlreadyUsedException(3L)
        );

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(
                "QR-код с ID 3 уже использован и не может быть изменён",
                response.getBody().message()
        );
    }

    @Test
    void handleMethodNotAllowed_returnsMethodNotAllowed() {
        var exception = new HttpRequestMethodNotSupportedException("PATCH");

        var response = handler.handleMethodNotAllowed(exception);

        assertEquals(
                HttpStatus.METHOD_NOT_ALLOWED,
                response.getStatusCode()
        );
        assertEquals(
                "HTTP-метод не поддерживается для этого адреса",
                response.getBody().message()
        );
    }

    @Test
    void handleResourceNotFound_returnsNotFound() {
        var exception = mock(NoResourceFoundException.class);

        var response = handler.handleResourceNotFound(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(
                "Запрашиваемый адрес не найден",
                response.getBody().message()
        );
    }

    @Test
    void handleMissingParameter_returnsBadRequest() {
        var exception = new MissingServletRequestParameterException(
                "uuid", "UUID"
        );

        var response = handler.handleMissingParameter(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(
                "Отсутствует обязательный параметр: uuid",
                response.getBody().message()
        );
    }

    @Test
    void handleInvalidBody_returnsBadRequest() {
        var exception = mock(HttpMessageNotReadableException.class);

        var response = handler.handleInvalidBody(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(
                "Некорректное тело запроса. Проверьте формат JSON",
                response.getBody().message()
        );
    }

    @Test
    void handleValidationError_whenFieldErrorExists_returnsItsMessage() {
        var exception = mock(MethodArgumentNotValidException.class);
        var bindingResult = mock(
                org.springframework.validation.BindingResult.class
        );
        var fieldError = new org.springframework.validation.FieldError(
                "participantRequest",
                "fullName",
                "ФИО обязательно"
        );

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(
                java.util.List.of(fieldError)
        );

        var response = handler.handleValidationError(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("ФИО обязательно", response.getBody().message());
    }

    @Test
    void handleValidationError_whenNoFieldErrors_returnsDefaultMessage() {
        var exception = mock(MethodArgumentNotValidException.class);
        var bindingResult = mock(
                org.springframework.validation.BindingResult.class
        );

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(
                java.util.List.of()
        );

        var response = handler.handleValidationError(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(
                "Ошибка проверки входных данных",
                response.getBody().message()
        );
    }
}