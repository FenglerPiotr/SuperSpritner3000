package pl.codecool.supersprinter3000.userstory;

import org.springframework.stereotype.Service;
import pl.codecool.supersprinter3000.developer.Developer;
import pl.codecool.supersprinter3000.developer.exception.DeveloperNotFoundException;
import pl.codecool.supersprinter3000.developer.DeveloperRepository;
import pl.codecool.supersprinter3000.userstory.dto.UserStoryUpdateDataDto;
import pl.codecool.supersprinter3000.userstory.dto.UserStoryDto;

import java.util.List;
import java.util.UUID;

@Service
public class UserStoryService {

    private final DeveloperRepository developerRepository;
    private final UserStoryRepository userStoryRepository;
    private final UserStoryMapper userStoryMapper;

    public UserStoryService(DeveloperRepository developerRepository, UserStoryRepository userStoryRepository, UserStoryMapper userStoryMapper) {
        this.developerRepository = developerRepository;
        this.userStoryRepository = userStoryRepository;
        this.userStoryMapper = userStoryMapper;
    }

    public List<UserStoryDto> getAllUserStories() {
        return userStoryRepository.findAllByOrderByTitle().stream()
                .map(userStoryMapper::mapEntityToDto)
                .toList();
    }

    public List<UserStoryDto> getAllUserStories(boolean unassignedOnly) {
        if (unassignedOnly) {
            return userStoryRepository.findAllUnassigned().stream()
                    .map(userStoryMapper::mapEntityToDto)
                    .toList();
        }

        return getAllUserStories();
    }

    public UserStoryDto getUserStory(UUID id) {
        return userStoryRepository.findOneById(id)
                .map(userStoryMapper::mapEntityToDto)
                .orElseThrow(() -> getUserStoryNotFoundException(id));
    }

    public UserStoryDto saveNewUserStory(UserStoryUpdateDataDto userStoryUpdateDataDto) {
        UserStory savedUserStory = userStoryRepository.save(userStoryMapper.mapNewDtoToEntity(userStoryUpdateDataDto));
        return userStoryMapper.mapEntityToDto(savedUserStory);
    }

    public UserStoryDto updateUserStory(UUID id, UserStoryUpdateDataDto updatedUserStoryDto) {
        UserStory us = userStoryRepository.findById(id)
                .orElseThrow(() -> getUserStoryNotFoundException(id));

        us.setTitle(updatedUserStoryDto.title());
        us.setDescription(updatedUserStoryDto.description());
        us.setAcceptanceCriteria(updatedUserStoryDto.acceptanceCriteria());
        us.setBusinessValue(updatedUserStoryDto.businessValue());
        us.setEstimation(updatedUserStoryDto.estimation());
        us.setStatus(UserStory.Status.valueOf(updatedUserStoryDto.status()));

        UserStory saved = userStoryRepository.save(us);
        return userStoryMapper.mapEntityToDto(saved);
    }

    public void assignUserStoryToDeveloper(UUID userStoryId, UUID developerId) {
        UserStory userStory = userStoryRepository.findOneById(userStoryId)
                .orElseThrow(() -> getUserStoryNotFoundException(userStoryId));

        Developer developer = developerRepository.findOneById(developerId)
                .orElseThrow(() -> getDeveloperNotFoundException(developerId));

        userStory.assignDeveloper(developer);
        developer.addUserStory(userStory);

        developerRepository.save(developer);
    }

    private UserStoryNotFoundException getUserStoryNotFoundException(UUID userStoryId) {
        return new UserStoryNotFoundException("User story with id " + userStoryId + " not found");
    }

    private DeveloperNotFoundException getDeveloperNotFoundException(UUID developerId) {
        return new DeveloperNotFoundException("Developer with id " + developerId + " not found");
    }
}
