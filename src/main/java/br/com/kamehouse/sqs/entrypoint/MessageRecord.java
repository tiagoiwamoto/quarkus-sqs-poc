package br.com.kamehouse.sqs.entrypoint;

import java.time.LocalDate;
import java.util.UUID;

public record MessageRecord(
        Long id,
        UUID uuid,
        String name,
        LocalDate birthdate
) {
}
