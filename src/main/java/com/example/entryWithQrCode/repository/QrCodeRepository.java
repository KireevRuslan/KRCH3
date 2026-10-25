
package com.example.entryWithQrCode.repository;

import com.example.entryWithQrCode.entity.QrCode;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QrCodeRepository extends JpaRepository<QrCode, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select q
            from QrCode q
            join fetch q.participant
            where q.uuid = :uuid
              and q.usedAt is null
            """)
    Optional<QrCode> findUnusedByUuidForUpdate(
            @Param("uuid") UUID uuid
    );

    List<QrCode> findAllByParticipantId(Long participantId);
    Optional<QrCode> findByParticipant_IdAndUsedAtIsNull(Long participantId);
}
