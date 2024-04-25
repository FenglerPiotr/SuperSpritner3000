package pl.codecool.supersprinter3000.developer;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.codecool.supersprinter3000.developer.dto.DeveloperDto;
import pl.codecool.supersprinter3000.developer.dto.DeveloperStatsDto;
import pl.codecool.supersprinter3000.developer.dto.NewDeveloperDto;
import pl.codecool.supersprinter3000.diagnostics.LogExecutionTime;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static pl.codecool.supersprinter3000.auth.config.SpringSecurityConfig.DEVELOPER_READ;
import static pl.codecool.supersprinter3000.auth.config.SpringSecurityConfig.DEVELOPER_WRITE;

@RestController
@RequestMapping("/api/v1/developers")
public class DeveloperController {

    private final DeveloperService developerService;

    public DeveloperController(DeveloperService developerService) {
        this.developerService = developerService;
    }

    @RolesAllowed(DEVELOPER_READ)
    @GetMapping
    public List<DeveloperDto> getAllDevelopers() {
        return developerService.getDevelopers();
    }

    @RolesAllowed(DEVELOPER_READ)
    @GetMapping(params = {"page", "size", "sort"})
    public List<DeveloperDto> getAllDevelopers(Pageable pageable) {
        return developerService.getDevelopers(pageable);
    }

    @RolesAllowed(DEVELOPER_READ)
    @GetMapping("/{id}")
    public DeveloperDto getDeveloperById(@PathVariable UUID id) {
        return developerService.getDeveloperById(id);
    }

    @RolesAllowed(DEVELOPER_READ)
    @GetMapping(params = {"busiestDev"})
    public List<DeveloperDto> getBusiestDevelopers(@RequestParam int busiestDev) {
        return developerService.findBusiestDevelopers(busiestDev);
    }

    @RolesAllowed(DEVELOPER_READ)
    @GetMapping(params = {"stats"})
    public List<DeveloperStatsDto> getDevelopersStats(@RequestParam String stats) {
        return developerService.developersStats(stats);
    }

    @RolesAllowed(DEVELOPER_WRITE)
    @PostMapping
    public DeveloperDto createNewDeveloper(@Valid @RequestBody NewDeveloperDto newDeveloper) {
        return developerService.saveNewDeveloper(newDeveloper);
    }

    @LogExecutionTime(timeUnit = ChronoUnit.MICROS)
    @RolesAllowed(DEVELOPER_WRITE)
    @DeleteMapping("/{id}")
    public void softDeleteDeveloper(@PathVariable UUID id) {
        developerService.softDeleteDeveloper(id);
    }
}
