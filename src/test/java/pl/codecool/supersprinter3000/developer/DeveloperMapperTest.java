package pl.codecool.supersprinter3000.developer;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.codecool.supersprinter3000.developer.dto.DeveloperDto;
import pl.codecool.supersprinter3000.developer.dto.NewDeveloperDto;
import pl.codecool.supersprinter3000.userstory.UserStory;

class DeveloperMapperTest {

    private final DeveloperMapper testedDeveloperMapper = new DeveloperMapper();

    @Test
    void shouldMapNewDeveloperDtoToEntity() {
        // given:
        NewDeveloperDto dto = new NewDeveloperDto("test-firstname", "test-lastname", "test-email");

        // when:
        Developer actual = testedDeveloperMapper.mapNewDtoToEntity(dto);

        // then:
        Assertions.assertThat(actual.getFirstName()).isEqualTo("test-firstname");
        Assertions.assertThat(actual.getLastName()).isEqualTo("test-lastname");
        Assertions.assertThat(actual.getEmail()).isEqualTo("test-email");
        Assertions.assertThat(actual.getId()).isNotNull();
        Assertions.assertThat(actual.getUserStories()).isEmpty();
    }

    @Test
    void shouldMapDeveloperEntityToDto() {
        // given:
        Developer entity = new Developer("test-firstname", "test-lastname", "test-email");
        UserStory us1 = new UserStory("test-story-1", "test-description-1", "test-acceptance-criteria-1", 1000, 10, UserStory.Status.TODO);
        UserStory us2 = new UserStory("test-story-2", "test-description-2", "test-acceptance-criteria-2", 200, 2.5, UserStory.Status.IN_PROGRESS);

        entity.addUserStory(us1);
        entity.addUserStory(us2);

        // when:
        DeveloperDto actual = testedDeveloperMapper.mapEntityToDto(entity);

        // then:
        // this randomly fails due to elements order
//        DeveloperDto expected = new DeveloperDto(
//                entity.getId(), "test-firstname test-lastname", "test-email",
//                List.of(
//                        new DeveloperDto.UserStoryIdTitlePair(us1.getId(), "test-story-1"),
//                        new DeveloperDto.UserStoryIdTitlePair(us2.getId(), "test-story-2")
//                )
//        );
//        Assertions.assertThat(actual).isEqualTo(expected);

        Assertions.assertThat(actual.id()).isEqualTo(entity.getId());
        Assertions.assertThat(actual.fullName()).isEqualTo("test-firstname test-lastname");
        Assertions.assertThat(actual.email()).isEqualTo("test-email");
        Assertions.assertThat(actual.userStories()).containsExactlyInAnyOrder(
                new DeveloperDto.UserStoryIdTitlePair(us1.getId(), "test-story-1"),
                new DeveloperDto.UserStoryIdTitlePair(us2.getId(), "test-story-2")
        );
    }
}
