
package com.example.entryWithQrCode.service;

import com.example.entryWithQrCode.dto.CreateParticipantRequest;
import com.example.entryWithQrCode.dto.ParticipantResponse;
import com.example.entryWithQrCode.entity.Participant;
import com.example.entryWithQrCode.exception.ParticipantNotFoundException;
import com.example.entryWithQrCode.repository.ParticipantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParticipantServiceTest {

    @Mock
    private ParticipantRepository participantRepository;

    @InjectMocks
    private ParticipantService participantService;

    @Test
    void findAll_returnsParticipantResponses() {
        Participant first = new Participant("Ivan Ivanov");
        Participant second = new Participant("Petr Petrov");

        when(participantRepository.findAll())
                .thenReturn(List.of(first, second));

        List<ParticipantResponse> result = participantService.findAll();

        assertEquals(2, result.size());
        assertEquals("Ivan Ivanov", result.get(0).fullName());
        assertEquals("Petr Petrov", result.get(1).fullName());
    }

    @Test
    void findAll_whenNoParticipants_returnsEmptyList() {
        when(participantRepository.findAll()).thenReturn(List.of());

        List<ParticipantResponse> result = participantService.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findById_whenParticipantExists_returnsResponse() {
        Participant participant = new Participant("Ivan Ivanov");

        when(participantRepository.findById(1L))
                .thenReturn(Optional.of(participant));

        ParticipantResponse result = participantService.findById(1L);

        assertEquals("Ivan Ivanov", result.fullName());
    }

    @Test
    void findById_whenParticipantDoesNotExist_throwsException() {
        when(participantRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ParticipantNotFoundException.class,
                () -> participantService.findById(1L)
        );
    }

    @Test
    void create_createsParticipantAndReturnsResponse() {
        CreateParticipantRequest request =
                new CreateParticipantRequest("Ivan Ivanov");

        when(participantRepository.save(any(Participant.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ParticipantResponse result = participantService.create(request);

        assertEquals("Ivan Ivanov", result.fullName());

        ArgumentCaptor<Participant> captor =
                ArgumentCaptor.forClass(Participant.class);

        verify(participantRepository).save(captor.capture());

        Participant savedParticipant = captor.getValue();

        assertEquals(1, savedParticipant.getQrCodes().size());
        assertNotNull(savedParticipant.getQrCodes().get(0).getUuid());
    }

    @Test
    void update_whenParticipantExists_updatesFullName() {
        Participant participant = new Participant("Ivan Ivanov");

        CreateParticipantRequest request =
                new CreateParticipantRequest("Petr Petrov");

        when(participantRepository.findById(1L))
                .thenReturn(Optional.of(participant));

        when(participantRepository.save(any(Participant.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ParticipantResponse result =
                participantService.update(1L, request);

        assertEquals("Petr Petrov", result.fullName());
        verify(participantRepository).save(participant);
    }

    @Test
    void update_whenParticipantDoesNotExist_throwsException() {
        CreateParticipantRequest request =
                new CreateParticipantRequest("Petr Petrov");

        when(participantRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ParticipantNotFoundException.class,
                () -> participantService.update(1L, request)
        );

        verify(participantRepository, never())
                .save(any(Participant.class));
    }

    @Test
    void delete_whenParticipantExists_deletesParticipant() {
        Participant participant = new Participant("Ivan Ivanov");

        when(participantRepository.findById(1L))
                .thenReturn(Optional.of(participant));

        participantService.delete(1L);

        verify(participantRepository).delete(participant);
    }

    @Test
    void delete_whenParticipantDoesNotExist_throwsException() {
        when(participantRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ParticipantNotFoundException.class,
                () -> participantService.delete(1L)
        );

        verify(participantRepository, never())
                .delete(any(Participant.class));
    }
}
