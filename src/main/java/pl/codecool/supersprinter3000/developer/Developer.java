package pl.codecool.supersprinter3000.developer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.codecool.supersprinter3000.userstory.UserStory;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class Developer {

    @Id
    private UUID id = UUID.randomUUID();
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    @Column(unique = true)
    @EqualsAndHashCode.Include
    private String email;
    @ManyToMany
    @JoinTable(name = "developer_user_story",
            joinColumns = @JoinColumn(name = "developerId"),
            inverseJoinColumns = @JoinColumn(name = "userStoryId"))
    private Set<UserStory> userStories = new HashSet<>();

    public Developer(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public void addUserStory(UserStory userStory) {
        userStories.add(userStory);
    }

    void clearUserStories() {
        getUserStories().clear();
    }

    public int getUserStoriesCount() {
        return userStories.size();
    }

    int calcTotalBusinessValue() {
        return getUserStories().stream()
                .mapToInt(UserStory::getBusinessValue)
                .sum();
    }
}
