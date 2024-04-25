package pl.codecool.supersprinter3000.developer;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@DataJpaTest
class DeveloperRepositoryTest {

    @Autowired
    private DeveloperRepository testDeveloperRepository;

    @Test
    void shouldReadDevelopersFromDB() {
        // when:
        List<Developer> actual = testDeveloperRepository.findAllBy();

        // then
        List<String> actualFirstNames = List.of(actual.get(0).getFirstName(), actual.get(1).getFirstName());
        List<String> actualLastNames = List.of(actual.get(0).getLastName(), actual.get(1).getLastName());
        List<String> actualEmails = List.of(actual.get(0).getEmail(), actual.get(1).getEmail());

        Assertions.assertThat(actualFirstNames).containsExactlyInAnyOrder("Tomasz", "Jan");
        Assertions.assertThat(actualLastNames).containsExactlyInAnyOrder("Nowak", "Nowak");
        Assertions.assertThat(actualEmails).containsExactlyInAnyOrder("tn@test.com", "jn@test.com");
    }

    @Test
    void shouldReadDeveloperById() {
        // given:
        UUID id = UUID.fromString("d9c21f6e-c541-4d34-9293-961976204294");

        // when:
        Optional<Developer> actual = testDeveloperRepository.findOneById(id);

        // then:
        Assertions.assertThat(actual.get().getFirstName()).isEqualTo("Tomasz");
        Assertions.assertThat(actual.get().getLastName()).isEqualTo("Nowak");
        Assertions.assertThat(actual.get().getEmail()).isEqualTo("tn@test.com");
    }

    @Test
    void shouldNotReadSoftDeletedDeveloper() {
        // given:
        UUID id = UUID.fromString("04a4c4b8-c361-4681-bd4b-13c332f0ba38");

        // when:
        Optional<Developer> actual = testDeveloperRepository.findOneById(id);

        // then:
        Assertions.assertThat(actual).isEmpty();
    }
}
