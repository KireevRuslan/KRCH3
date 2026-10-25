
package com.example.entryWithQrCode.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParticipantTest {

    @Test
    void constructor_setsFullName() {
        Participant participant = new Participant("Иван Иванов");

        assertEquals("Иван Иванов", participant.getFullName());
    }

    @Test
    void setFullName_updatesFullName() {
        Participant participant = new Participant("Иван Иванов");

        participant.setFullName("Пётр Петров");

        assertEquals("Пётр Петров", participant.getFullName());
    }

    @Test
    void constructor_initializesEmptyQrCodesList() {
        Participant participant = new Participant("Иван Иванов");

        assertNotNull(participant.getQrCodes());
        assertTrue(participant.getQrCodes().isEmpty());
    }

    @Test
    void addQrCode_addsQrCodeAndSetsParticipant() {
        Participant participant = new Participant("Иван Иванов");
        QrCode qrCode = new QrCode();

        participant.addQrCode(qrCode);

        assertEquals(1, participant.getQrCodes().size());
        assertSame(qrCode, participant.getQrCodes().get(0));
        assertSame(participant, qrCode.getParticipant());
    }
    @Test
    void addQrCode_addsMultipleQrCodes() {
        Participant participant = new Participant("Иван Иванов");
        QrCode firstQr = new QrCode();
        QrCode secondQr = new QrCode();

        participant.addQrCode(firstQr);
        participant.addQrCode(secondQr);

        assertEquals(2, participant.getQrCodes().size());
        assertSame(firstQr, participant.getQrCodes().get(0));
        assertSame(secondQr, participant.getQrCodes().get(1));
    }
}
