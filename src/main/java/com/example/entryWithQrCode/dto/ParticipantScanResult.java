package com.example.entryWithQrCode.dto;

import java.util.UUID;

public record ParticipantScanResult(
        String fullName,
        UUID newUuid
) {
}
