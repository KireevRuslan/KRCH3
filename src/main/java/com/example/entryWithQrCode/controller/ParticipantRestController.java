
package com.example.entryWithQrCode.controller;

import com.example.entryWithQrCode.dto.CreateParticipantRequest;
import com.example.entryWithQrCode.dto.ParticipantResponse;
import com.example.entryWithQrCode.service.ParticipantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/participants")
public class ParticipantRestController {

    private final ParticipantService participantService;

    public ParticipantRestController(
            ParticipantService participantService
    ) {
        this.participantService = participantService;
    }

    @GetMapping
    public List<ParticipantResponse> findAll() {
        return participantService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParticipantResponse> findById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(participantService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ParticipantResponse> create(
            @Valid @RequestBody CreateParticipantRequest request
    ) {
        ParticipantResponse response =
                participantService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ParticipantResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody CreateParticipantRequest request
    ) {
        ParticipantResponse response =
                participantService.update(id, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {
        participantService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
