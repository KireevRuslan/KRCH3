
package com.example.entryWithQrCode.controller;

import com.example.entryWithQrCode.dto.CheckQrResponse;
import com.example.entryWithQrCode.dto.ErrorResponse;
import com.example.entryWithQrCode.dto.QrCodeResponse;
import com.example.entryWithQrCode.service.QrService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/qr")
public class QrRestController {

    private final QrService qrService;

    public QrRestController(QrService qrService) {
        this.qrService = qrService;
    }

    @GetMapping("/participant/{participantId}")
    public List<QrCodeResponse> getParticipantQrCodes(
            @PathVariable Long participantId
    ) {
        return qrService.findByParticipantId(participantId);
    }


    @PostMapping("/check")
    public ResponseEntity<?> checkQr(@RequestParam UUID uuid) {
        Optional<CheckQrResponse> result = qrService.checkQr(uuid);

        if (result.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(new ErrorResponse(
                            "QR-код недействителен или уже использован"
                    ));
        }

        return ResponseEntity.ok(result.get());
    }


    @PostMapping("/participant/{participantId}")
    public ResponseEntity<QrCodeResponse> createQrCode(
            @PathVariable Long participantId
    ) {
        QrCodeResponse result = qrService.createQrCode(participantId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQrCode(@PathVariable Long id) {
        qrService.deleteQrCode(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<QrCodeResponse> updateQrCode(@PathVariable Long id) {
        QrCodeResponse result = qrService.updateQrCode(id);
        return ResponseEntity.ok(result);
    }
}