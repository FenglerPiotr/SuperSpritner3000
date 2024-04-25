package pl.codecool.supersprinter3000.auth;

public record JwtTokeRequestDto(
        String username,
        String password
) {
}
