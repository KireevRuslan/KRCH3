package com.example.entryWithQrCode.service;

import com.example.entryWithQrCode.dto.ParticipantScanResult;
import com.example.entryWithQrCode.repository.ParticipantRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class QrService {

    private final ParticipantRepository participantRepository;

    public QrService(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    public Optional<ParticipantScanResult> checkQr(String value){
        UUID uuid;

        try {
            uuid = UUID.fromString(value);

        }catch (IllegalArgumentException e){
            throw new IllegalArgumentException("Некорректный UUID");
        }
        return participantRepository.consumeQr(uuid);
    }
}
