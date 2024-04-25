package pl.codecool.supersprinter3000.userstory;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.codecool.supersprinter3000.userstory.dto.UserStoryUpdateDataDto;
import pl.codecool.supersprinter3000.userstory.dto.UserStoryDto;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user-stories")
public class UserStoryController {

    private final UserStoryService userStoryService;

    public UserStoryController(UserStoryService userStoryService) {
        this.userStoryService = userStoryService;
    }

    @GetMapping
    public List<UserStoryDto> getUserStories() {
        return userStoryService.getAllUserStories();
    }

    @GetMapping(params = {"unassignedOnly"})
    public List<UserStoryDto> getUnassignedUserStories(@RequestParam boolean unassignedOnly) {
        return userStoryService.getAllUserStories(unassignedOnly);
    }

    @GetMapping("/{id}")
    public UserStoryDto getUserStory(@PathVariable UUID id) {
        return userStoryService.getUserStory(id);
    }

    @PostMapping
    public UserStoryDto createNewUserStory(@Valid @RequestBody UserStoryUpdateDataDto userStoryUpdateDataDto) {
        return userStoryService.saveNewUserStory(userStoryUpdateDataDto);
    }

    @PutMapping("/{id}")
    public UserStoryDto updateUserStory(@PathVariable UUID id, @Valid @RequestBody UserStoryUpdateDataDto updatedUserStoryDto) {
        return userStoryService.updateUserStory(id, updatedUserStoryDto);
    }

    @PutMapping("/{userStoryId}/developers/{developerId}")
    public void assignUserStoryToDeveloper(@PathVariable UUID userStoryId, @PathVariable UUID developerId) {
        userStoryService.assignUserStoryToDeveloper(userStoryId, developerId);
    }
}
