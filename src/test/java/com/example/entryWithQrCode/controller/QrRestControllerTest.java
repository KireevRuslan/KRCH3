
package com.example.entryWithQrCode.controller;

import com.example.entryWithQrCode.dto.CheckQrResponse;
import com.example.entryWithQrCode.dto.QrCodeResponse;
import com.example.entryWithQrCode.exception.ParticipantNotFoundException;
import com.example.entryWithQrCode.exception.QrCodeAlreadyUsedException;
import com.example.entryWithQrCode.exception.QrCodeNotFoundException;
import com.example.entryWithQrCode.service.QrService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(QrRestController.class)
class QrRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private QrService qrService;

    @Test
    void getParticipantQrCodes_returnsQrCodes() throws Exception {
        when(qrService.findByParticipantId(1L))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/qr/participant/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void checkQr_whenQrIsValid_returnsOk() throws Exception {
        UUID uuid = UUID.randomUUID();

        when(qrService.checkQr(uuid))
                .thenReturn(Optional.of(
                        new CheckQrResponse("Ivan Ivanov", UUID.randomUUID())
                ));

        mockMvc.perform(post("/api/qr/check")
                        .param("uuid", uuid.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Ivan Ivanov"));
    }

    @Test
    void checkQr_whenQrIsInvalid_returnsForbidden() throws Exception {
        UUID uuid = UUID.randomUUID();

        when(qrService.checkQr(uuid)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/qr/check")
                        .param("uuid", uuid.toString()))
                .andExpect(status().isForbidden());
    }

    @Test
    void createQrCode_returnsCreated() throws Exception {
        UUID uuid = UUID.randomUUID();

        when(qrService.createQrCode(1L))
                .thenReturn(new QrCodeResponse(
                        10L,
                        1L,
                        uuid,
                        Instant.now(),
                        null
                ));

        mockMvc.perform(post("/api/qr/participant/1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.participantId").value(1))
                .andExpect(jsonPath("$.uuid").value(uuid.toString()));
    }

    @Test
    void updateQrCode_returnsUpdatedQrCode() throws Exception {
        UUID uuid = UUID.randomUUID();

        when(qrService.updateQrCode(10L))
                .thenReturn(new QrCodeResponse(
                        10L,
                        1L,
                        uuid,
                        Instant.now(),
                        null
                ));

        mockMvc.perform(put("/api/qr/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.uuid").value(uuid.toString()));
    }

    @Test
    void deleteQrCode_returnsNoContent() throws Exception {
        doNothing().when(qrService).deleteQrCode(10L);

        mockMvc.perform(delete("/api/qr/10"))
                .andExpect(status().isNoContent());

        verify(qrService).deleteQrCode(10L);
    }

    @Test
    void createQrCode_whenParticipantNotFound_returnsNotFound() throws Exception {
        when(qrService.createQrCode(1L))
                .thenThrow(new ParticipantNotFoundException(1L));

        mockMvc.perform(post("/api/qr/participant/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateQrCode_whenQrCodeNotFound_returnsNotFound() throws Exception {
        when(qrService.updateQrCode(10L))
                .thenThrow(new QrCodeNotFoundException(10L));

        mockMvc.perform(put("/api/qr/10"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateQrCode_whenQrCodeAlreadyUsed_returnsConflict() throws Exception {
        when(qrService.updateQrCode(10L))
                .thenThrow(new QrCodeAlreadyUsedException(10L));

        mockMvc.perform(put("/api/qr/10"))
                .andExpect(status().isConflict());
    }

    @Test
    void deleteQrCode_whenQrCodeNotFound_returnsNotFound() throws Exception {
        doThrow(new QrCodeNotFoundException(10L))
                .when(qrService).deleteQrCode(10L);

        mockMvc.perform(delete("/api/qr/10"))
                .andExpect(status().isNotFound());
    }
    @Test
    void checkQr_whenQrIsInvalid_returnsForbiddenWithMessage() throws Exception {
        UUID uuid = UUID.randomUUID();

        when(qrService.checkQr(uuid)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/qr/check")
                        .param("uuid", uuid.toString()))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message")
                        .value("QR-код недействителен или уже использован"));
    }

    @Test
    void checkQr_whenUuidIsMissing_returnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/qr/check"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Отсутствует обязательный параметр: uuid"));
    }

    @Test
    void createQrCode_whenParticipantNotFound_returnsNotFoundWithMessage()
            throws Exception {
        when(qrService.createQrCode(1L))
                .thenThrow(new ParticipantNotFoundException(1L));

        mockMvc.perform(post("/api/qr/participant/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Участник с ID 1 не найден"));
    }
}
