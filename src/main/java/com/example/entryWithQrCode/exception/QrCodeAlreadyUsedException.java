package com.example.entryWithQrCode.exception;

public class QrCodeAlreadyUsedException extends RuntimeException {

    public QrCodeAlreadyUsedException(Long id) {
        super("QR-код с ID " + id + " уже использован и не может быть изменён");
    }
}