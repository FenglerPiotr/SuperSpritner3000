package pl.codecool.supersprinter3000.userstory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserStoryRepository extends JpaRepository<UserStory, UUID> {

    @Query("SELECT us FROM UserStory us LEFT JOIN FETCH us.developers order by us.title")
    List<UserStory> findAllByOrderByTitle();

    @Query("SELECT us FROM UserStory us LEFT JOIN FETCH us.developers WHERE us.developers IS EMPTY")
    List<UserStory> findAllUnassigned();

    @Query("SELECT us FROM UserStory us LEFT JOIN FETCH us.developers")
    List<UserStory> findAllBy();

    @Query("SELECT us FROM UserStory us LEFT JOIN FETCH us.developers where us.id = :id")
    Optional<UserStory> findOneById(UUID id);
}
