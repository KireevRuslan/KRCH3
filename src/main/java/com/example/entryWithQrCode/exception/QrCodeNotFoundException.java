package com.example.entryWithQrCode.exception;

public class QrCodeNotFoundException extends RuntimeException {

    public QrCodeNotFoundException(Long id) {
        super("QR-код с ID " + id + " не найден");
    }
}