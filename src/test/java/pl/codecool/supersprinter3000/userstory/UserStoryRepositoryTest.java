package pl.codecool.supersprinter3000.userstory;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@DataJpaTest
class UserStoryRepositoryTest {

    @Autowired
    private UserStoryRepository userStoryRepository;

    @Test
    void shouldFindUserStoryById() {
        // given:
        UUID id = UUID.fromString("68466722-a5a7-49da-af86-b2dcbd6b8203");

        // when:
        Optional<UserStory> actual = userStoryRepository.findOneById(id);

        // then:
        Assertions.assertThat(actual.get().getId()).isEqualTo(id);
        Assertions.assertThat(actual.get().getTitle()).isEqualTo("Test story");
        Assertions.assertThat(actual.get().getDescription()).isEqualTo("Implement tests");
        Assertions.assertThat(actual.get().getAcceptanceCriteria()).isEqualTo("Cover all cases");
        Assertions.assertThat(actual.get().getEstimation()).isEqualTo(30.0);
        Assertions.assertThat(actual.get().getBusinessValue()).isEqualTo(100);
        Assertions.assertThat(actual.get().getStatus()).isEqualTo(UserStory.Status.TODO);
        Assertions.assertThat(actual.get().getDevelopers().size()).isEqualTo(1);
    }

    @Test
    void shouldReturnEmptyOptionalWhenUserStoryIsNotFound() {
        // given:
        UUID id = UUID.randomUUID();

        // when:
        Optional<UserStory> actual = userStoryRepository.findOneById(id);

        // then:
        Assertions.assertThat(actual).isEmpty();
    }

    @Test
    void shouldReturnAllUserStoriesSortedByTitle() {
        // when:
        List<UserStory> actual = userStoryRepository.findAllByOrderByTitle();

        // then:
        Assertions.assertThat(actual.get(0).getId()).isEqualTo(UUID.fromString("b8081184-c21c-40ce-b806-32cdf73a82db"));
        Assertions.assertThat(actual.get(0).getTitle()).isEqualTo("Super Test story");
        Assertions.assertThat(actual.get(1).getId()).isEqualTo(UUID.fromString("68466722-a5a7-49da-af86-b2dcbd6b8203"));
        Assertions.assertThat(actual.get(1).getTitle()).isEqualTo("Test story");

    }
}
