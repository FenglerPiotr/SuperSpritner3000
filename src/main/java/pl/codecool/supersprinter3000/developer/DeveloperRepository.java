package pl.codecool.supersprinter3000.developer;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.codecool.supersprinter3000.diagnostics.LogExecutionTime;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeveloperRepository extends JpaRepository<Developer, UUID> {

    Optional<Developer> findByEmail(String email);

    @Query("SELECT d FROM Developer d LEFT JOIN FETCH d.userStories WHERE d.email NOT LIKE 'deleted-%'")
    List<Developer> findAllBy();

    @Query("SELECT d FROM Developer d LEFT JOIN FETCH d.userStories WHERE d.email NOT LIKE 'deleted-%'")
    List<Developer> findAllBy(Pageable pageable);

    @LogExecutionTime
    @Query("SELECT d FROM Developer d LEFT JOIN FETCH d.userStories WHERE d.id = :id AND d.email NOT LIKE 'deleted-%'")
    Optional<Developer> findOneById(UUID id);

    @LogExecutionTime
    Developer save(Developer developer);
}
