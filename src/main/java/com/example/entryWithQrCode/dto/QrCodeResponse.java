package com.example.entryWithQrCode.dto;

import java.time.Instant;
import java.util.UUID;

public record QrCodeResponse(
        Long id,
        Long participantId,
        UUID uuid,
        Instant createdAt,
        Instant usedAt
) {
}
