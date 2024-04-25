package pl.codecool.supersprinter3000.auth.registration;

import java.util.UUID;

public record RegisteredUserDataDto(
        UUID id,
        String login
) {
}
