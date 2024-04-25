package pl.codecool.supersprinter3000.developer;

import org.springframework.stereotype.Component;
import pl.codecool.supersprinter3000.developer.dto.DeveloperDto;
import pl.codecool.supersprinter3000.developer.dto.NewDeveloperDto;

import java.util.List;

@Component
public class DeveloperMapper {
    public Developer mapNewDtoToEntity(NewDeveloperDto dto) {
        return new Developer(
                dto.firstName(),
                dto.lastName(),
                dto.email()
        );
    }

    public DeveloperDto mapEntityToDto(Developer entity) {
        return new DeveloperDto(
                entity.getId(),
                entity.getFullName(),
                entity.getEmail(),
                mapUserStories(entity)
        );
    }

    private List<DeveloperDto.UserStoryIdTitlePair> mapUserStories(Developer entity) {
        return entity.getUserStories().stream()
                .map(us -> new DeveloperDto.UserStoryIdTitlePair(us.getId(), us.getTitle()))
                .toList();
    }
}
