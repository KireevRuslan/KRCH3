
package com.example.entryWithQrCode.controller;

import com.example.entryWithQrCode.dto.CreateParticipantRequest;
import com.example.entryWithQrCode.dto.ParticipantResponse;
import com.example.entryWithQrCode.exception.ParticipantNotFoundException;
import com.example.entryWithQrCode.service.ParticipantService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ParticipantRestController.class)
class ParticipantRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ParticipantService participantService;

    @Test
    void findAll_returnsParticipants() throws Exception {
        when(participantService.findAll()).thenReturn(List.of(
                new ParticipantResponse(1L, "Ivan Ivanov"),
                new ParticipantResponse(2L, "Petr Petrov")
        ));

        mockMvc.perform(get("/api/participants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].fullName").value("Ivan Ivanov"))
                .andExpect(jsonPath("$[1].fullName").value("Petr Petrov"));
    }

    @Test
    void findById_returnsParticipant() throws Exception {
        when(participantService.findById(1L))
                .thenReturn(new ParticipantResponse(1L, "Ivan Ivanov"));

        mockMvc.perform(get("/api/participants/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.fullName").value("Ivan Ivanov"));
    }

    @Test
    void findById_whenParticipantNotFound_returnsNotFound() throws Exception {
        when(participantService.findById(1L))
                .thenThrow(new ParticipantNotFoundException(1L));

        mockMvc.perform(get("/api/participants/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_returnsCreated() throws Exception {
        CreateParticipantRequest request =
                new CreateParticipantRequest("Ivan Ivanov");

        when(participantService.create(any(CreateParticipantRequest.class)))
                .thenReturn(new ParticipantResponse(1L, "Ivan Ivanov"));

        mockMvc.perform(post("/api/participants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fullName\":\"Ivan Ivanov\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.fullName").value("Ivan Ivanov"));
    }

    @Test
    void update_returnsUpdatedParticipant() throws Exception {
        CreateParticipantRequest request =
                new CreateParticipantRequest("Petr Petrov");

        when(participantService.update(
                eq(1L),
                any(CreateParticipantRequest.class)
        )).thenReturn(new ParticipantResponse(1L, "Petr Petrov"));

        mockMvc.perform(put("/api/participants/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fullName\":\"Petr Petrov\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.fullName").value("Petr Petrov"));
    }

    @Test
    void delete_returnsNoContent() throws Exception {
        doNothing().when(participantService).delete(1L);

        mockMvc.perform(delete("/api/participants/1"))
                .andExpect(status().isNoContent());

        verify(participantService).delete(1L);
    }
    @Test
    void findAll_whenNoParticipants_returnsEmptyList() throws Exception {
        when(participantService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/participants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void update_whenParticipantNotFound_returnsNotFound() throws Exception {
        when(participantService.update(
                eq(1L),
                any(CreateParticipantRequest.class)
        )).thenThrow(new ParticipantNotFoundException(1L));

        mockMvc.perform(put("/api/participants/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fullName\":\"Petr Petrov\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_whenParticipantNotFound_returnsNotFound() throws Exception {
        doThrow(new ParticipantNotFoundException(1L))
                .when(participantService).delete(1L);

        mockMvc.perform(delete("/api/participants/1"))
                .andExpect(status().isNotFound());
    }
    @Test
    void create_whenFullNameIsBlank_returnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/participants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fullName\":\"   \"}"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(participantService);
    }
    @Test
    void findById_whenIdIsNotNumeric_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/participants/abc"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(participantService);
    }
}
