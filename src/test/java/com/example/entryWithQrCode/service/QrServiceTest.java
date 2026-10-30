
package com.example.entryWithQrCode.service;

import com.example.entryWithQrCode.dto.CheckQrResponse;
import com.example.entryWithQrCode.dto.QrCodeResponse;
import com.example.entryWithQrCode.entity.Participant;
import com.example.entryWithQrCode.entity.QrCode;
import com.example.entryWithQrCode.exception.ParticipantNotFoundException;
import com.example.entryWithQrCode.exception.QrCodeAlreadyUsedException;
import com.example.entryWithQrCode.exception.QrCodeNotFoundException;
import com.example.entryWithQrCode.repository.ParticipantRepository;
import com.example.entryWithQrCode.repository.QrCodeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QrServiceTest {

    @Mock
    private QrCodeRepository qrCodeRepository;

    @Mock
    private ParticipantRepository participantRepository;

    @InjectMocks
    private QrService qrService;

    @Test
    void checkQr_whenQrNotFound_returnsEmpty() {

        UUID uuid = UUID.randomUUID();

        when(qrCodeRepository.findUnusedByUuidForUpdate(uuid))
                .thenReturn(Optional.empty());


        Optional<CheckQrResponse> result = qrService.checkQr(uuid);


        assertTrue(result.isEmpty());

        verify(qrCodeRepository)
                .findUnusedByUuidForUpdate(uuid);

        verify(qrCodeRepository, never())
                .save(any(QrCode.class));
    }

    @Test
    void checkQr_whenQrFound_marksOldQrUsedAndCreatesNewQr() {

        UUID oldUuid = UUID.randomUUID();

        Participant participant = mock(Participant.class);
        QrCode oldQr = mock(QrCode.class);

        when(qrCodeRepository.findUnusedByUuidForUpdate(oldUuid))
                .thenReturn(Optional.of(oldQr));

        when(oldQr.getParticipant()).thenReturn(participant);
        when(participant.getFullName()).thenReturn("Иван Иванов");


        Optional<CheckQrResponse> result = qrService.checkQr(oldUuid);


        assertTrue(result.isPresent());

        CheckQrResponse response = result.orElseThrow();

        assertEquals("Иван Иванов", response.fullName());
        assertNotNull(response.newUuid());
        assertNotEquals(oldUuid, response.newUuid());

        verify(oldQr).setUsedAt(any());

        verify(participant).addQrCode(any(QrCode.class));

        verify(qrCodeRepository).save(any(QrCode.class));
    }

    @Test
    void findByParticipantId_whenNoQrCodes_returnsEmptyList() {

        Long participantId = 1L;

        when(qrCodeRepository.findAllByParticipantId(participantId))
                .thenReturn(List.of());


        var result = qrService.findByParticipantId(participantId);


        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(qrCodeRepository).findAllByParticipantId(participantId);
    }

    @Test
    void createQrCode_whenParticipantNotFound_throwsException() {

        Long participantId = 999L;

        when(participantRepository.findById(participantId))
                .thenReturn(Optional.empty());

        assertThrows(
                ParticipantNotFoundException.class,
                () -> qrService.createQrCode(participantId)
        );

        verify(qrCodeRepository, never()).save(any(QrCode.class));
    }

    @Test
    void deleteQrCode_whenQrNotFound_throwsException() {

        Long qrId = 999L;

        when(qrCodeRepository.findById(qrId))
                .thenReturn(Optional.empty());


        assertThrows(
                QrCodeNotFoundException.class,
                () -> qrService.deleteQrCode(qrId)
        );

        verify(qrCodeRepository, never()).delete(any(QrCode.class));
    }

    @Test
    void updateQrCode_whenQrNotFound_throwsException() {

        Long qrId = 999L;

        when(qrCodeRepository.findById(qrId))
                .thenReturn(Optional.empty());


        assertThrows(
                QrCodeNotFoundException.class,
                () -> qrService.updateQrCode(qrId)
        );

        verify(qrCodeRepository, never()).save(any(QrCode.class));
    }

    @Test
    void updateQrCode_whenQrAlreadyUsed_throwsException() {

        Long qrId = 1L;

        QrCode qrCode = mock(QrCode.class);

        when(qrCodeRepository.findById(qrId))
                .thenReturn(Optional.of(qrCode));


        when(qrCode.getUsedAt())
                .thenReturn(java.time.Instant.now());

        assertThrows(
                QrCodeAlreadyUsedException.class,
                () -> qrService.updateQrCode(qrId)
        );

        verify(qrCodeRepository, never()).save(any(QrCode.class));
    }


    @Test
    void createQrCode_whenParticipantExists_createsQrCode() {
        Long participantId = 1L;
        Participant participant = new Participant("Иван Иванов");

        ReflectionTestUtils.setField(participant, "id", participantId);

        when(participantRepository.findById(participantId))
                .thenReturn(Optional.of(participant));

        when(qrCodeRepository.save(any(QrCode.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        QrCodeResponse result = qrService.createQrCode(participantId);

        assertNotNull(result);
        assertEquals(participantId, result.participantId());
        assertNotNull(result.uuid());
        assertNotNull(result.createdAt());

        verify(participantRepository).findById(participantId);
        verify(qrCodeRepository).save(any(QrCode.class));

        assertEquals(1, participant.getQrCodes().size());
        assertSame(participant, participant.getQrCodes().get(0).getParticipant());
    }


    @Test
    void updateQrCode_whenQrUnused_changesUuid() {
        Long qrId = 1L;
        Long participantId = 2L;
        UUID oldUuid = UUID.randomUUID();

        Participant participant = mock(Participant.class);
        when(participant.getId()).thenReturn(participantId);

        QrCode qrCode = new QrCode(oldUuid);
        qrCode.setParticipant(participant);

        when(qrCodeRepository.findById(qrId))
                .thenReturn(Optional.of(qrCode));

        when(qrCodeRepository.save(any(QrCode.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        QrCodeResponse result = qrService.updateQrCode(qrId);

        assertNotNull(result);
        assertEquals(participantId, result.participantId());
        assertNotNull(result.uuid());
        assertNotEquals(oldUuid, result.uuid());
        assertNull(result.usedAt());

        assertEquals(result.uuid(), qrCode.getUuid());
        assertNull(qrCode.getUsedAt());

        verify(qrCodeRepository).findById(qrId);
        verify(qrCodeRepository).save(qrCode);
        verify(participant, never()).addQrCode(any(QrCode.class));
    }
}