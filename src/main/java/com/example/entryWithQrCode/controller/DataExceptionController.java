package com.example.entryWithQrCode.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class DataExceptionController {
    private static final Logger log =
            LoggerFactory.getLogger(DataExceptionController.class);

    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public String dataExceptionController(DataAccessException exception, Model model) {
        log.error("Ошибка при работе с базой данных", exception);
        model.addAttribute(
                "message",
                "Сервис временно недоступен. Попробуйте позже."
        );
        return "error";
    }
}
