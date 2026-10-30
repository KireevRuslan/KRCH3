package com.example.entryWithQrCode.entity;

import jakarta.persistence.*;


import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "participants")
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @OneToMany(
            mappedBy = "participant",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<QrCode> qrCodes = new ArrayList<>();

    protected Participant() {
    }

    public Participant(String fullName){
        this.fullName = fullName;
    }

    public Long getId(){
        return id;
    }

    public String getFullName(){
        return fullName;
    }

    public void setFullName(String fullName){
        this.fullName = fullName;
    }

    public List<QrCode> getQrCodes(){
        return qrCodes;
    }

    public void addQrCode(QrCode qrCode){
        qrCodes.add(qrCode);
        qrCode.setParticipant(this);
    }
}

