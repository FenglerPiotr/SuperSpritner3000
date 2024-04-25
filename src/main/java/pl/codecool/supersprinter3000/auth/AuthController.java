package pl.codecool.supersprinter3000.auth;

import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.codecool.supersprinter3000.auth.jwt.JwtTokenService;
import pl.codecool.supersprinter3000.auth.registration.AuthService;
import pl.codecool.supersprinter3000.auth.registration.NewUserRegistrationDto;
import pl.codecool.supersprinter3000.auth.registration.RegisteredUserDataDto;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final AuthService authService;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenService jwtTokenService, AuthService authService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
        this.authService = authService;
    }

    @PostMapping("/login")
    public JwtTokenResponseDto login(@Valid @RequestBody JwtTokeRequestDto jwtTokenRequest) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(
                jwtTokenRequest.username(), jwtTokenRequest.password()
        );

        authenticationManager.authenticate(authenticationToken);

        return new JwtTokenResponseDto(jwtTokenService.generateToken(jwtTokenRequest.username()));
    }

    @PostMapping("/register")
    public RegisteredUserDataDto registerUser(@Valid @RequestBody NewUserRegistrationDto registrationDto) {
        return authService.registerNewUser(registrationDto);
    }
}
