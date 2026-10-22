
package com.example.entryWithQrCode.dto;

import java.util.UUID;

public record CheckQrResponse(
        String fullName,
        UUID newUuid
) {
}
