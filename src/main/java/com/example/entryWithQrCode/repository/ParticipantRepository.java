
package com.example.entryWithQrCode.repository;

import com.example.entryWithQrCode.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantRepository
        extends JpaRepository<Participant, Long> {
}
