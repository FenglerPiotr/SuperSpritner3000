package pl.codecool.supersprinter3000.developer.validation;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pl.codecool.supersprinter3000.developer.Developer;
import pl.codecool.supersprinter3000.developer.DeveloperRepository;

import java.util.Optional;

class UnoccupiedValidatorTest {

    private final DeveloperRepository developerRepository = Mockito.mock(DeveloperRepository.class);
    private final UnoccupiedValidator testedValidator = new UnoccupiedValidator(developerRepository);

    @Test
    void shouldReturnTrueWhenEmailIsNotTaken() {
        // given:
        Mockito.when(developerRepository.findByEmail(Mockito.anyString()))
                .thenReturn(Optional.empty());

        // when:
        boolean actual = testedValidator.isValid("test@email.com", null);

        // then:
        Assertions.assertThat(actual).isTrue();
    }

    @Test
    void shouldThrowEmailInUseExceptionWhenEmailIsTaken() {
        // given:
        Mockito.when(developerRepository.findByEmail(Mockito.anyString()))
                .thenReturn(Optional.of(new Developer()));

        // when:
        Throwable throwable = Assertions.catchThrowable(
                () -> testedValidator.isValid("test@email.com", null)
        );

        //  then:
        Assertions.assertThat(throwable)
                .isInstanceOf(EmailInUseException.class)
                .hasMessage("test@email.com is already in use");
    }
}
