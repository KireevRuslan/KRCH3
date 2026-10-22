package com.example.entryWithQrCode.service;

import com.example.entryWithQrCode.dto.ParticipantScanResult;
import com.example.entryWithQrCode.repository.ParticipantRepository;
import org.springframework.stereotype.Service;

@Service
public class ParticipantManagementService {

    private final ParticipantRepository participantRepository;

    public ParticipantManagementService(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    public ParticipantScanResult addParticipant(String fullName) {
        String name = fullName.trim();

        if (name.isEmpty()) {
            throw new IllegalArgumentException("Укажите ФИО");
        }

        return participantRepository.addParticipant(name);
    }

    public int deleteByFullName(String fullName){
        String name = fullName.trim();

        if (name.isEmpty()){
            throw new IllegalArgumentException("Укажите ФИО");

        }


        return participantRepository.deleteByFullName(name);
    }
}

