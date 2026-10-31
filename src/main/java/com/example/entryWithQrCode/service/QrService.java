
package com.example.entryWithQrCode.service;

import com.example.entryWithQrCode.dto.CheckQrResponse;
import com.example.entryWithQrCode.dto.QrCodeResponse;
import com.example.entryWithQrCode.exception.ParticipantNotFoundException;
import com.example.entryWithQrCode.exception.QrCodeAlreadyUsedException;
import com.example.entryWithQrCode.exception.QrCodeNotFoundException;
import com.example.entryWithQrCode.repository.ParticipantRepository;
import com.example.entryWithQrCode.entity.QrCode;
import com.example.entryWithQrCode.entity.Participant;
import com.example.entryWithQrCode.repository.QrCodeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class QrService {
    private final QrCodeRepository qrCodeRepository;
    private final ParticipantRepository participantRepository;


    private QrCodeResponse toResponse(QrCode qrCode) {
        return new QrCodeResponse(
                qrCode.getId(),
                qrCode.getParticipant().getId(),
                qrCode.getUuid(),
                qrCode.getCreatedAt(),
                qrCode.getUsedAt()
        );
    }

    public QrService(QrCodeRepository qrCodeRepository,
                     ParticipantRepository participantRepository) {
        this.qrCodeRepository = qrCodeRepository;
        this.participantRepository = participantRepository;
    }

    @Transactional
    public Optional<CheckQrResponse> checkQr(UUID uuid) {

        Optional<QrCode> foundQr =
                qrCodeRepository.findUnusedByUuidForUpdate(uuid);

        if (foundQr.isEmpty()) {
            return Optional.empty();
        }

        QrCode oldQr = foundQr.get();
        Participant participant = oldQr.getParticipant();

        oldQr.setUsedAt(Instant.now());

        QrCode newQr = new QrCode(UUID.randomUUID());
        participant.addQrCode(newQr);

        qrCodeRepository.save(newQr);

        CheckQrResponse response = new CheckQrResponse(
                participant.getFullName(),
                newQr.getUuid()
        );

        return Optional.of(response);
    }

    @Transactional(readOnly = true)
    public List<QrCodeResponse> findByParticipantId(Long participantId) {
        return qrCodeRepository.findAllByParticipantId(participantId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public QrCodeResponse createQrCode(Long participantId) {
        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new ParticipantNotFoundException(participantId));

        QrCode qrCode = new QrCode(UUID.randomUUID());
        participant.addQrCode(qrCode);

        QrCode saved = qrCodeRepository.save(qrCode);

        return toResponse(saved);
    }

    @Transactional
    public void deleteQrCode(Long id) {
        QrCode qrCode = qrCodeRepository.findById(id)
                .orElseThrow(() -> new QrCodeNotFoundException(id));

        qrCodeRepository.delete(qrCode);
    }

    @Transactional
    public QrCodeResponse updateQrCode(Long id) {
        QrCode qrCode = qrCodeRepository.findById(id)
                .orElseThrow(() -> new QrCodeNotFoundException(id));

        if (qrCode.getUsedAt() != null) {
            throw new QrCodeAlreadyUsedException(id);
        }

        qrCode.setUuid(UUID.randomUUID());

        QrCode saved = qrCodeRepository.save(qrCode);

        return toResponse(saved);
    }


}
