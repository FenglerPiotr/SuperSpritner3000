package pl.codecool.supersprinter3000.userstory.dto;

import java.util.List;
import java.util.UUID;

public record UserStoryDto(
        UUID id,
        String title,
        String description,
        String acceptanceCriteria,
        int businessValue,
        double estimation,
        String status,
        List<DeveloperIdFullNamePair> developers
) {
    public record DeveloperIdFullNamePair(
            UUID id,
            String fullName
    ) {

    }
}
