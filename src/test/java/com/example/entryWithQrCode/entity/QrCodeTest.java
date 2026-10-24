
package com.example.entryWithQrCode.entity;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class QrCodeTest {

    @Test
    void constructor_setsUuidAndCreationTime() {
        UUID uuid = UUID.randomUUID();

        QrCode qrCode = new QrCode(uuid);

        assertEquals(uuid, qrCode.getUuid());
        assertNotNull(qrCode.getCreatedAt());
        assertNull(qrCode.getUsedAt());
        assertNull(qrCode.getId());
        assertNull(qrCode.getParticipant());
    }

    @Test
    void setUuid_updatesUuid() {
        QrCode qrCode = new QrCode(UUID.randomUUID());
        UUID newUuid = UUID.randomUUID();

        qrCode.setUuid(newUuid);

        assertEquals(newUuid, qrCode.getUuid());
    }

    @Test
    void setParticipant_updatesParticipant() {
        QrCode qrCode = new QrCode(UUID.randomUUID());
        Participant participant = new Participant("Иван Иванов");

        qrCode.setParticipant(participant);

        assertSame(participant, qrCode.getParticipant());
    }

    @Test
    void setUsedAt_updatesUsedAt() {
        QrCode qrCode = new QrCode(UUID.randomUUID());
        Instant usedAt = Instant.parse("2026-10-25T10:00:00Z");

        qrCode.setUsedAt(usedAt);

        assertEquals(usedAt, qrCode.getUsedAt());
    }
}
