package pl.codecool.supersprinter3000.developer.dto;

import java.util.List;
import java.util.UUID;

public record DeveloperDto(
        UUID id,
        String fullName,
        String email,
        List<UserStoryIdTitlePair> userStories
) {
    public record UserStoryIdTitlePair(
            UUID id,
            String title
    ) {

    }
}
