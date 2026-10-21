package com.example.entryWithQrCode.controller;

import com.example.entryWithQrCode.dto.ParticipantScanResult;
import com.example.entryWithQrCode.service.QrService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Controller

public class QrController {

    private final QrService qrService;

    public QrController(QrService qrService) {
        this.qrService = qrService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/check-qr")
    public ResponseEntity<?> checrQr(
            @RequestParam("uuid") String uuid

    ) {
        try {
            Optional<ParticipantScanResult> result = qrService.checkQr(uuid);
            if (result.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "message", "Участник не найден или QR-код уже использован"
                        ));
            }
            return ResponseEntity.ok(result.get());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("massage", e.getMessage()));
        }
    }
}
