package pl.codecool.supersprinter3000.userstory;

import org.assertj.core.api.Assertions;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.codecool.supersprinter3000.developer.DeveloperRepository;
import pl.codecool.supersprinter3000.userstory.dto.UserStoryUpdateDataDto;
import pl.codecool.supersprinter3000.userstory.dto.UserStoryDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class UserStoryServiceTest {
    private final DeveloperRepository developerRepository = Mockito.mock(DeveloperRepository.class);
    private final UserStoryRepository userStoryRepository = Mockito.mock(UserStoryRepository.class);
    private final UserStoryMapper userStoryMapper = Mockito.mock(UserStoryMapper.class);
    private final UserStoryService userStoryService = new UserStoryService(developerRepository, userStoryRepository, userStoryMapper);

    @Captor
    private ArgumentCaptor<UserStory> userStoryCaptor;

    @Test
    void checkIfUserStoryUpdated() {
        //given:
        UUID id = UUID.randomUUID();
        UserStory userStoryFromDB = Instancio.of(UserStory.class)
                .set(Select.field(UserStory::getId), id)
                .create();
        UserStoryUpdateDataDto userStoryUpdateData = Instancio.of(UserStoryUpdateDataDto.class)
                .set(Select.field(UserStoryUpdateDataDto::status), "TODO")
                .create();

        UserStory updatedUserStoryFromDB = new UserStory(
                userStoryUpdateData.title(),
                userStoryUpdateData.description(),
                userStoryUpdateData.acceptanceCriteria(),
                userStoryUpdateData.businessValue(),
                userStoryUpdateData.estimation(),
                UserStory.Status.valueOf(userStoryUpdateData.status().toUpperCase())
        );
        updatedUserStoryFromDB.setId(id);
        UserStoryDto updatedUserStoryDto = new UserStoryDto(id, userStoryUpdateData.title(), userStoryUpdateData.description(), userStoryUpdateData.acceptanceCriteria(),
                userStoryUpdateData.businessValue(), userStoryUpdateData.estimation(), userStoryUpdateData.status(), List.of());

        Mockito.when(userStoryRepository.findById(id)).thenReturn(Optional.of(userStoryFromDB));
        Mockito.when(userStoryRepository.save(userStoryFromDB)).thenReturn(updatedUserStoryFromDB);
        Mockito.when(userStoryMapper.mapEntityToDto(userStoryFromDB)).thenReturn(updatedUserStoryDto);

        //when:
        UserStoryDto actual = userStoryService.updateUserStory(id, userStoryUpdateData);

        //then:
        verifySavedData(id, userStoryUpdateData);
        Assertions.assertThat(actual).isEqualTo(updatedUserStoryDto);
    }

    private void verifySavedData(UUID id, UserStoryUpdateDataDto userStoryUpdateData) {
        Mockito.verify(userStoryRepository).save(userStoryCaptor.capture());
        Assertions.assertThat(userStoryCaptor.getValue().getId()).isEqualTo(id);
        Assertions.assertThat(userStoryCaptor.getValue().getTitle()).isEqualTo(userStoryUpdateData.title());
        Assertions.assertThat(userStoryCaptor.getValue().getDescription()).isEqualTo(userStoryUpdateData.description());
        Assertions.assertThat(userStoryCaptor.getValue().getAcceptanceCriteria()).isEqualTo(userStoryUpdateData.acceptanceCriteria());
        Assertions.assertThat(userStoryCaptor.getValue().getBusinessValue()).isEqualTo(userStoryUpdateData.businessValue());
        Assertions.assertThat(userStoryCaptor.getValue().getEstimation()).isEqualTo(userStoryUpdateData.estimation());
        Assertions.assertThat(userStoryCaptor.getValue().getStatus()).isEqualTo(UserStory.Status.valueOf(userStoryUpdateData.status().toUpperCase()));
    }
}
