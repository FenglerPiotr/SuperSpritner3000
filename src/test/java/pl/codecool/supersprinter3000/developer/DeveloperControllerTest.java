package pl.codecool.supersprinter3000.developer;

import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import pl.codecool.supersprinter3000.auth.user.UserRepository;
import pl.codecool.supersprinter3000.auth.config.SpringSecurityConfig;
import pl.codecool.supersprinter3000.developer.dto.DeveloperDto;
import pl.codecool.supersprinter3000.developer.dto.NewDeveloperDto;
import pl.codecool.supersprinter3000.developer.exception.DeveloperNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.codecool.supersprinter3000.auth.config.SpringSecurityConfig.DEVELOPER_READ;
import static pl.codecool.supersprinter3000.auth.config.SpringSecurityConfig.DEVELOPER_WRITE;

@WebMvcTest(controllers = DeveloperController.class)
@Import(SpringSecurityConfig.class)
class DeveloperControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeveloperService developerService;

    @MockBean
    private DeveloperRepository developerRepository;

    @MockBean
    private UserRepository userRepository;

    @Test
    @WithMockUser(roles = DEVELOPER_READ)
    void shouldReturn404WhenDeveloperIsNotFound() throws Exception {
        // given:
        UUID id = UUID.randomUUID();
        Mockito.when(developerService.getDeveloperById(id))
                .thenThrow(new DeveloperNotFoundException("Developer with id " + id + " not found"));

        // when:
        ResultActions response = mockMvc.perform(get("/api/v1/developers/" + id));

        // then:
        response
//                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.info").value("Developer with id " + id + " not found"));
    }

    @Test
    @WithMockUser(roles = DEVELOPER_READ)
    void shouldReturnDeveloperJson() throws Exception {
        // given:
        UUID id = UUID.randomUUID();
        DeveloperDto developerDto = Instancio
                .of(DeveloperDto.class)
                .generate(Select.field(DeveloperDto::userStories), gen -> gen.collection().size(2))
                .create();
        Mockito.when(developerService.getDeveloperById(id))
                .thenReturn(developerDto);

        // when:
        ResultActions response = mockMvc.perform(get("/api/v1/developers/" + id));

        // then:
        response
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(developerDto.id().toString()))
                .andExpect(jsonPath("$.fullName").value(developerDto.fullName()))
                .andExpect(jsonPath("$.email").value(developerDto.email()))
                .andExpect(jsonPath("$.userStories.size()").value(2))
                .andExpect(jsonPath("$.userStories[0].id").value(developerDto.userStories().get(0).id().toString()))
                .andExpect(jsonPath("$.userStories[0].title").value(developerDto.userStories().get(0).title()))
                .andExpect(jsonPath("$.userStories[1].id").value(developerDto.userStories().get(1).id().toString()))
                .andExpect(jsonPath("$.userStories[1].title").value(developerDto.userStories().get(1).title()));
    }

    @Test
    @WithMockUser(roles = DEVELOPER_READ)
    void shouldReturnDevelopersJson() throws Exception {
        // given:
        List<DeveloperDto> developerDtos = Instancio.ofList(DeveloperDto.class)
                .size(1)
                .generate(Select.field(DeveloperDto::userStories), gen -> gen.collection().size(2))
                .create();

        Mockito.when(developerService.getDevelopers())
                .thenReturn(developerDtos);

        // when:
        ResultActions response = mockMvc.perform(get("/api/v1/developers"));

        // then:
        response
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(developerDtos.get(0).id().toString()))
                .andExpect(jsonPath("$[0].fullName").value(developerDtos.get(0).fullName()))
                .andExpect(jsonPath("$[0].email").value(developerDtos.get(0).email()))
                .andExpect(jsonPath("$[0].userStories.size()").value(2))
                .andExpect(jsonPath("$[0].userStories[0].id").value(developerDtos.get(0).userStories().get(0).id().toString()))
                .andExpect(jsonPath("$[0].userStories[0].title").value(developerDtos.get(0).userStories().get(0).title()))
                .andExpect(jsonPath("$[0].userStories[1].id").value(developerDtos.get(0).userStories().get(1).id().toString()))
                .andExpect(jsonPath("$[0].userStories[1].title").value(developerDtos.get(0).userStories().get(1).title()));
    }

    @Test
    @WithMockUser(roles = DEVELOPER_WRITE)
    void shouldReturnNewDeveloperJson() throws Exception {
        // given:
        NewDeveloperDto newDeveloperDto = new NewDeveloperDto("test-firstname", "test-lastname", "test-email@email.com");
        DeveloperDto developerDto = new DeveloperDto(UUID.randomUUID(), "test-firstname test-lastname", "test-email@email.com", List.of());
        Mockito.when(developerService.saveNewDeveloper(newDeveloperDto))
                .thenReturn(developerDto);

        // when:
        ResultActions response = mockMvc.perform(post("/api/v1/developers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "firstName": "test-firstname",
                            "lastName": "test-lastname",
                            "email": "test-email@email.com"
                        }
                        """));

        // then:
        response
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                                {
                                    "id": "%s",
                                    "fullName": "test-firstname test-lastname",
                                    "email": "test-email@email.com",
                                    "userStories": []
                                    }
                                 """.formatted(developerDto.id().toString()
                        )));
    }

    @Test
    @WithMockUser
    void shouldReturn400WhenInputDataIsInvalid() throws Exception {
        // when:
        ResultActions response = mockMvc.perform(post("/api/v1/developers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "firstName": "",
                            "lastName": "",
                            "email": "test-email@email.com"
                        }
                        """));

        // then:
        response
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.info").value(containsString("First name cannot be empty")))
                .andExpect(jsonPath("$.info").value(containsString("Last name cannot be empty")));
    }

    @Test
    @WithMockUser
    void shouldReturn409WhenEmailIsTaken() throws Exception {
        // given:
        Mockito.when(developerRepository.findByEmail("test-email@email.com"))
                .thenReturn(Optional.of(Instancio.of(Developer.class).create()));

        // when:
        ResultActions response = mockMvc.perform(post("/api/v1/developers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "firstName": "Testing",
                            "lastName": "Tester",
                            "email": "test-email@email.com"
                        }
                        """));

        // then:
        response
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.info").value("test-email@email.com is already in use"));

    }

    @Test
    void shouldReturn401WhenNoCredentialsProvided() throws Exception {
        // when:
        ResultActions response = mockMvc.perform(get("/api/v1/developers"));

        // then:
        response.andExpect(status().isUnauthorized());
    }

    @WithMockUser(roles = {})
    @Test
    void shouldReturn403WhenRoleIsMissing() throws Exception {
        // when:
        ResultActions response = mockMvc.perform(get("/api/v1/developers"));

        // then:
        response.andExpect(status().isForbidden());
    }
}
