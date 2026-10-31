package com.example.entryWithQrCode.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateParticipantRequest(
        @NotBlank(message = "ФИО участника не должно быть пустым")
        String fullName
) {
}