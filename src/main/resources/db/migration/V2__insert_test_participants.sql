INSERT INTO participants (full_name)
VALUES ('Пушкин Александр Сергеевич'),
       ('Лермонтов Михаил Юрьевич'),
       ('Толстой Лев Николаевич');

INSERT INTO qr_codes (participant_id,
                      uuid,
                      created_at,
                      used_at)
SELECT p.id,
       gen_random_uuid(),
       CURRENT_TIMESTAMP,
       NULL
FROM participants p
WHERE p.full_name IN (
                      'Пушкин Александр Сергеевич',
                      'Лермонтов Михаил Юрьевич',
                      'Толстой Лев Николаевич'
    );