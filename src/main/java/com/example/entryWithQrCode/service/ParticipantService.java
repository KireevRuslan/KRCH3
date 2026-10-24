
package com.example.entryWithQrCode.service;

import com.example.entryWithQrCode.dto.CreateParticipantRequest;
import com.example.entryWithQrCode.dto.ParticipantResponse;
import com.example.entryWithQrCode.entity.Participant;
import com.example.entryWithQrCode.entity.QrCode;
import com.example.entryWithQrCode.exception.ParticipantNotFoundException;
import com.example.entryWithQrCode.repository.ParticipantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ParticipantService {

    private final ParticipantRepository participantRepository;

    public ParticipantService(
            ParticipantRepository participantRepository
    ) {
        this.participantRepository = participantRepository;
    }

    @Transactional(readOnly = true)
    public List<ParticipantResponse> findAll() {
        return participantRepository.findAll()
                .stream()
                .map(participant -> new ParticipantResponse(
                        participant.getId(),
                        participant.getFullName()
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public ParticipantResponse findById(Long id) {
        Participant participant = participantRepository.findById(id)
                .orElseThrow(() -> new ParticipantNotFoundException(id));

        return new ParticipantResponse(
                participant.getId(),
                participant.getFullName()
        );
    }

    @Transactional
    public ParticipantResponse create(CreateParticipantRequest request) {
        Participant participant = new Participant(request.fullName());

        QrCode qrCode = new QrCode(UUID.randomUUID());
        participant.addQrCode(qrCode);

        Participant saved = participantRepository.save(participant);

        return new ParticipantResponse(
                saved.getId(),
                saved.getFullName()
        );
    }

    @Transactional
    public ParticipantResponse update(
            Long id,
            CreateParticipantRequest request
    ) {
        Participant participant = participantRepository
                .findById(id)
                .orElseThrow(() ->
                        new ParticipantNotFoundException(id)
                );

        participant.setFullName(request.fullName());

        Participant saved = participantRepository.save(participant);

        return new ParticipantResponse(
                saved.getId(),
                saved.getFullName()
        );
    }

    @Transactional
    public void delete(Long id) {
        Participant participant = participantRepository
                .findById(id)
                .orElseThrow(() ->
                        new ParticipantNotFoundException(id)
                );

        participantRepository.delete(participant);
    }
}
