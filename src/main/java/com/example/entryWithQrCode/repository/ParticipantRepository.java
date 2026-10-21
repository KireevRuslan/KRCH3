package com.example.entryWithQrCode.repository;

import com.example.entryWithQrCode.dto.ParticipantScanResult;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ParticipantRepository {

    private final JdbcTemplate jdbcTemplate;

    public ParticipantRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<ParticipantScanResult> consumeQr(UUID uuid) {

        String sql = """
                UPDATE participants
                SET uuid = gen_random_uuid()
                WHERE uuid = ?
                RETURNING full_name, uuid
                """;

        List<ParticipantScanResult> results = jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new ParticipantScanResult(
                        rs.getString("full_name"),
                        rs.getObject("uuid", UUID.class)
                ),
                uuid
        );

        return results.stream().findFirst();
    }
}