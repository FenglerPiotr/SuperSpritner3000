package pl.codecool.supersprinter3000.developer;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.codecool.supersprinter3000.userstory.UserStory;

class DeveloperTest {

    @Test
    void shouldReturnFullName() {
        // given:
        Developer developer = new Developer("test-firstname", "test-lastname", "test-email");

        // when:
        String actual = developer.getFullName();

        // then:
        Assertions.assertThat(actual).isEqualTo("test-firstname test-lastname");
    }

    @Test
    void shouldRemoveUserStories() {
        // given:
        Developer developer = new Developer("test-firstname", "test-lastname", "test-email");
        developer.addUserStory(new UserStory("test-story-1", "test-description-1", "test-acceptance-criteria-1", 1000, 10, UserStory.Status.TODO));
        developer.addUserStory(new UserStory("test-story-2", "test-description-2", "test-acceptance-criteria-2", 200, 2.5, UserStory.Status.IN_PROGRESS));

        // when:
        developer.clearUserStories();

        // then:
        Assertions.assertThat(developer.getUserStories()).isEmpty();
    }

    @Test
    void shouldCalculateTotalBusinessValue() {
        // given:
        Developer developer = new Developer("test-firstname", "test-lastname", "test-email");
        developer.addUserStory(new UserStory("test-story-1", "test-description-1", "test-acceptance-criteria-1", 1000, 10, UserStory.Status.TODO));
        developer.addUserStory(new UserStory("test-story-2", "test-description-2", "test-acceptance-criteria-2", 200, 2.5, UserStory.Status.IN_PROGRESS));

        // when:
        int actual = developer.calcTotalBusinessValue();

        // then:
        Assertions.assertThat(actual).isEqualTo(1200);
    }
}
