package pl.codecool.supersprinter3000.developer;

import org.assertj.core.api.Assertions;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.codecool.supersprinter3000.developer.dto.DeveloperDto;
import pl.codecool.supersprinter3000.developer.exception.DeveloperNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.instancio.Select.field;

@ExtendWith(MockitoExtension.class)
class DeveloperServiceTest {

    private final DeveloperRepository developerRepository = Mockito.mock(DeveloperRepository.class);
    private final DeveloperMapper developerMapper = Mockito.mock(DeveloperMapper.class);
    private final DeveloperService testedDeveloperService = new DeveloperService(developerRepository, developerMapper);

    @Test
    void shouldReturnDeveloperDto() {
        // given:
        Developer developer = Instancio.create(Developer.class);
        Mockito.when(developerRepository.findOneById(developer.getId()))
                .thenReturn(Optional.of(developer));

        DeveloperDto developerDto = Instancio.create(DeveloperDto.class);
        Mockito.when(developerMapper.mapEntityToDto(developer))
                .thenReturn(developerDto);

        // when:
        DeveloperDto actual = testedDeveloperService.getDeveloperById(developer.getId());

        // then:
        Assertions.assertThat(actual).isEqualTo(developerDto);
    }

    @Test
    void shouldThrowDeveloperNotFoundExceptionWHenDeveloperDoesNotExist() {
        // given:
        UUID id = UUID.randomUUID();
        Mockito.when(developerRepository.findOneById(id)).thenReturn(Optional.empty());

        // when:
        Throwable throwable = Assertions.catchThrowable(
                () -> testedDeveloperService.getDeveloperById(id)
        );

        // then:
        Assertions.assertThat(throwable)
                .isInstanceOf(DeveloperNotFoundException.class)
                .hasMessageContaining(id.toString());
    }

    @Test
    void shouldReturnTopNMostBusyDevelopers() {
        // given:
        Developer devWith4UserStories = Instancio.of(Developer.class)
                .generate(field(Developer::getUserStories), gen -> gen.collection().size(4))
                .create();
        Developer devWith5UserStories = Instancio.of(Developer.class)
                .generate(field(Developer::getUserStories), gen -> gen.collection().size(5))
                .create();
        Developer devWith2UserStories = Instancio.of(Developer.class)
                .generate(field(Developer::getUserStories), gen -> gen.collection().size(2))
                .create();
        Developer devWith3UserStories = Instancio.of(Developer.class)
                .generate(field(Developer::getUserStories), gen -> gen.collection().size(3))
                .create();

        Mockito.when(developerRepository.findAllBy())
                .thenReturn(List.of(devWith4UserStories, devWith5UserStories, devWith2UserStories, devWith3UserStories));

        DeveloperDto devWith4UserStoriesDto = Instancio.create(DeveloperDto.class);
        DeveloperDto devWith5UserStoriesDto = Instancio.create(DeveloperDto.class);
        DeveloperDto devWith3UserStoriesDto = Instancio.create(DeveloperDto.class);
        Mockito.when(developerMapper.mapEntityToDto(devWith4UserStories)).thenReturn(devWith4UserStoriesDto);
        Mockito.when(developerMapper.mapEntityToDto(devWith5UserStories)).thenReturn(devWith5UserStoriesDto);
        Mockito.when(developerMapper.mapEntityToDto(devWith3UserStories)).thenReturn(devWith3UserStoriesDto);

        // when:
        List<DeveloperDto> actual = testedDeveloperService.findBusiestDevelopers(3);

        // then:
        Assertions.assertThat(actual).isEqualTo(List.of(devWith5UserStoriesDto, devWith4UserStoriesDto, devWith3UserStoriesDto));
    }

    @Captor
    private ArgumentCaptor<Developer> developerCaptor;

    @Test
    void shouldSoftDeleteDeveloper() {
        // given:
        Developer developer = Instancio.of(Developer.class)
                .generate(field(Developer::getFirstName), gen -> gen.text().pattern("Adam"))
                .generate(field(Developer::getLastName), gen -> gen.text().pattern("Arczyński"))
                .generate(field(Developer::getEmail), gen -> gen.text().pattern("adam@gmail.com"))
                .generate(field(Developer::getUserStories), gen -> gen.collection().size(3))
                .create();

        Mockito.when(developerRepository.findOneById(developer.getId())).thenReturn(Optional.of(developer));

        // when:
        testedDeveloperService.softDeleteDeveloper(developer.getId());

        // then:
        Mockito.verify(developerRepository).save(developerCaptor.capture());
        Assertions.assertThat(developerCaptor.getValue().getId()).isEqualTo(developer.getId());
        Assertions.assertThat(developerCaptor.getValue().getFirstName()).isEqualTo("****");
        Assertions.assertThat(developerCaptor.getValue().getLastName()).isEqualTo("*********");
        Assertions.assertThat(developerCaptor.getValue().getEmail()).startsWith("deleted-");
        Assertions.assertThat(developerCaptor.getValue().getEmail()).doesNotContain("adam");
        Assertions.assertThat(developerCaptor.getValue().getUserStories().size()).isEqualTo(0);
    }

    @Test
    void shouldThrowDeveloperNotFoundExceptionWhenDeveloperDoesNotExist() {
        // given:
        UUID id = UUID.randomUUID();
        Mockito.when(developerRepository.findOneById(id)).thenReturn(Optional.empty());

        // when:
        Throwable throwable = Assertions.catchThrowable(
                () -> testedDeveloperService.softDeleteDeveloper(id)
        );

        // then:
        Assertions.assertThat(throwable)
                .isInstanceOf(DeveloperNotFoundException.class)
                .hasMessageContaining(id.toString());
    }
}
