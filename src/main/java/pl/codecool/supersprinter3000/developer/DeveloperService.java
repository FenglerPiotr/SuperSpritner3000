package pl.codecool.supersprinter3000.developer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.codecool.supersprinter3000.developer.dto.DeveloperDto;
import pl.codecool.supersprinter3000.developer.dto.DeveloperStatsDto;
import pl.codecool.supersprinter3000.developer.dto.NewDeveloperDto;
import pl.codecool.supersprinter3000.developer.exception.DeveloperNotFoundException;
import pl.codecool.supersprinter3000.developer.exception.UnsupportedStatsParamException;
import pl.codecool.supersprinter3000.diagnostics.LogExecutionTime;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class DeveloperService {

    private final DeveloperRepository developerRepository;
    private final DeveloperMapper developerMapper;

    public DeveloperService(DeveloperRepository developerRepository, DeveloperMapper developerMapper) {
        this.developerRepository = developerRepository;
        this.developerMapper = developerMapper;
    }

    public List<DeveloperDto> getDevelopers() {;
        return developerRepository.findAllBy().stream()
                .map(developerMapper::mapEntityToDto)
                .toList();
    }

    public List<DeveloperDto> getDevelopers(Pageable pageable) {
        return developerRepository.findAllBy(pageable).stream()
                .map(developerMapper::mapEntityToDto)
                .toList();
    }

    public DeveloperDto getDeveloperById(UUID id) {
        return developerRepository.findOneById(id)
                .map(developerMapper::mapEntityToDto)
                .orElseThrow(() -> getDeveloperNotFoundException(id));
    }

    public List<DeveloperDto> findBusiestDevelopers(int topBusyCount) {
        log.info("Getting top {} busiest developers", topBusyCount);
        List<DeveloperDto> developerDtos = developerRepository.findAllBy().stream()
                .sorted(Comparator.comparingInt(Developer::getUserStoriesCount).reversed())
                .limit(topBusyCount)
                .map(developerMapper::mapEntityToDto)
                .toList();
        log.info("Got {} busiest developers: {}", developerDtos.size(), developerDtos.stream().map(DeveloperDto::id).toList());
        return developerDtos;
    }

    public List<DeveloperStatsDto> developersStats(String statsKey) {
        if ("businessValue".equals(statsKey)) {
            return developerRepository.findAllBy().stream()
                    .map(d -> new DeveloperStatsDto(d.getFullName(), d.calcTotalBusinessValue()))
                    .toList();
        }

        throw new UnsupportedStatsParamException("Stats for " + statsKey + " not supported. Currently only businessValue supported.");
    }

    public DeveloperDto saveNewDeveloper(NewDeveloperDto newDeveloper) {
        Developer savedDeveloper = developerRepository.save(developerMapper.mapNewDtoToEntity(newDeveloper));
        return developerMapper.mapEntityToDto(savedDeveloper);
    }

    @LogExecutionTime
    public void softDeleteDeveloper(UUID id) {
        log.info("Soft deleting developer with id {}", id);
        Developer dev = developerRepository.findOneById(id)
                .orElseThrow(() -> getDeveloperNotFoundException(id));

        obfuscatePiiData(dev);
        dev.clearUserStories();

        developerRepository.save(dev);
        log.info("Developer with id {} soft deleted", id);
    }

    private void obfuscatePiiData(Developer dev) {
        int firstNameLength = dev.getFirstName().length();
        dev.setFirstName("*".repeat(firstNameLength));

        int lastNameLength = dev.getLastName().length();
        dev.setLastName("*".repeat(lastNameLength));

        int atCharPos = dev.getEmail().indexOf('@');
        String randomPrefix = "deleted-" + UUID.randomUUID();
        dev.setEmail(randomPrefix + dev.getEmail().substring(atCharPos));
    }

    private DeveloperNotFoundException getDeveloperNotFoundException(UUID id) {
        return new DeveloperNotFoundException("Developer with id " + id + " does not exist");
    }


}
