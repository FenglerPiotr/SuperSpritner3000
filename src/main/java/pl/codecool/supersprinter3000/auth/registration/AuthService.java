package pl.codecool.supersprinter3000.auth.registration;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.codecool.supersprinter3000.auth.config.SpringSecurityConfig;
import pl.codecool.supersprinter3000.auth.user.Role;
import pl.codecool.supersprinter3000.auth.user.RoleRepository;
import pl.codecool.supersprinter3000.auth.user.User;
import pl.codecool.supersprinter3000.auth.user.UserRepository;
import pl.codecool.supersprinter3000.auth.validation.UserAlreadyExistsException;

@Service
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public AuthService(PasswordEncoder passwordEncoder, UserRepository userRepository, RoleRepository roleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public RegisteredUserDataDto registerNewUser(NewUserRegistrationDto registrationDto) {
        verifyUserEmail(registrationDto);

        User newUser = new User(
                registrationDto.username(),
                passwordEncoder.encode(registrationDto.password())
        );

        Role userRole = roleRepository.findByName(SpringSecurityConfig.DEVELOPER_READ)
                .orElseThrow(() -> new IllegalStateException("Expected user role in database"));

        newUser.addRole(userRole);
        userRole.assignToUser(newUser);

        User savedUser = userRepository.save(newUser);

        return new RegisteredUserDataDto(savedUser.getId(), savedUser.getEmail());
    }

    // TODO: replace with custom @Validator
    private void verifyUserEmail(NewUserRegistrationDto registrationDto) {
        userRepository.findByEmail(registrationDto.username()).ifPresent(
                u -> { throw new UserAlreadyExistsException(registrationDto.username()); }
        );
    }
}
