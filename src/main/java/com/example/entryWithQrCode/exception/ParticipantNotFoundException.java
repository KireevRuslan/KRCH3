package com.example.entryWithQrCode.exception;

public class ParticipantNotFoundException
        extends RuntimeException {

    public ParticipantNotFoundException(Long id) {
        super("Участник с ID " + id + " не найден");
    }
}