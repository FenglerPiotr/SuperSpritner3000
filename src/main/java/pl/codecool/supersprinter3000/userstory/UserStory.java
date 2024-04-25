package pl.codecool.supersprinter3000.userstory;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Version;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import pl.codecool.supersprinter3000.developer.Developer;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class UserStory {

    public UserStory(String title, String description, String acceptanceCriteria, int businessValue, double estimation, Status status) {
        this.title = title;
        this.description = description;
        this.acceptanceCriteria = acceptanceCriteria;
        this.businessValue = businessValue;
        this.estimation = estimation;
        this.status = status;
    }

    @Id
    @EqualsAndHashCode.Include
    private UUID id = UUID.randomUUID();
    @Size(min = 5, max = 50, message = "Title must be between 5 and 50 characters")
    private String title;
    @NotBlank(message = "Description cannot be empty")
    private String description;
    @NotBlank(message = "Acceptance Criteria cannot be empty")
    private String acceptanceCriteria;
    @Range(min = 100, max = 1500, message = "Business Value should be between 100 and 1500 points")
    private int businessValue = 100;
    @DecimalMin(value = "0.5", message = "Estimation min value is 0.5")
    @DecimalMax(value = "40.0", message = "Estimation max value is 40.0")
    private double estimation = 0.5;
    @NotNull(message = "Status cannot be empty")
    @Enumerated(EnumType.STRING)
    private Status status;
    @Version
    private Integer version;

    @ManyToMany(mappedBy = "userStories")
    private Set<Developer> developers = new HashSet<>();

    public enum Status {
        TODO, IN_PROGRESS, REVIEW, DONE
    }

    public void assignDeveloper(Developer developer) {
        developers.add(developer);
    }
}
