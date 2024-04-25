package pl.codecool.supersprinter3000.userstory;

import org.springframework.stereotype.Component;
import pl.codecool.supersprinter3000.userstory.dto.UserStoryUpdateDataDto;
import pl.codecool.supersprinter3000.userstory.dto.UserStoryDto;

import java.util.List;

@Component
public class UserStoryMapper {
    public UserStory mapNewDtoToEntity(UserStoryUpdateDataDto dto) {
        return new UserStory(
                dto.title(),
                dto.description(),
                dto.acceptanceCriteria(),
                dto.businessValue(),
                dto.estimation(),
                UserStory.Status.valueOf(dto.status().toUpperCase())
        );
    }

    public UserStoryDto mapEntityToDto(UserStory entity) {
        return new UserStoryDto(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getAcceptanceCriteria(),
                entity.getBusinessValue(),
                entity.getEstimation(),
                entity.getStatus().name(),
                mapDevelopers(entity)
        );
    }

    private List<UserStoryDto.DeveloperIdFullNamePair> mapDevelopers(UserStory entity) {
        return entity.getDevelopers().stream()
                .map(d -> new UserStoryDto.DeveloperIdFullNamePair(d.getId(), d.getFullName()))
                .toList();
    }
}
