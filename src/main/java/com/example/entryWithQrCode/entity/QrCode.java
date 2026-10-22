package com.example.entryWithQrCode.entity;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "qr_codes")
public class QrCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "participant_id", nullable = false)
    private Participant participant;

    @Column(nullable = false, unique = true)
    private UUID uuid;

    @Column(name = "created_at", nullable = false)
    private Instant createAt;

    @Column(name = "used_at")
    private Instant usedAt;

    protected QrCode(){

    }

    public QrCode(UUID uuid){
        this.uuid = uuid;
        this.createAt = Instant.now();
    }

    public Long getId(){
        return id;
    }

    public Participant getParticipant(){
        return participant;
    }

    public void setParticipant(Participant participant){
        this.participant = participant;
    }

    public UUID getUuid(){
        return uuid;
    }

    public void setUuid(UUID uuid){
        this.uuid = uuid;
    }

    public Instant getCreatedAt(){
        return createAt;
    }

    public Instant getUsedAt(){
        return usedAt;
    }

    public void setUsedAt(Instant usedAt){
        this.usedAt = usedAt;
    }
}
