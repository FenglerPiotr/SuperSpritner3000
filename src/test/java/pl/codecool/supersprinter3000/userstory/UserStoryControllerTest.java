package pl.codecool.supersprinter3000.userstory;


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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.codecool.supersprinter3000.auth.user.UserRepository;
import pl.codecool.supersprinter3000.auth.config.SpringSecurityConfig;
import pl.codecool.supersprinter3000.userstory.dto.UserStoryDto;
import pl.codecool.supersprinter3000.userstory.dto.UserStoryUpdateDataDto;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@WebMvcTest(UserStoryController.class)
@Import(SpringSecurityConfig.class)
class UserStoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserStoryService userStoryService;

    @MockBean
    private UserRepository userRepository;

    @Test
    @WithMockUser
    void shouldReturnNewUserStory() throws Exception {
        // given:
        UserStoryUpdateDataDto newUserStoryDtoTest = new UserStoryUpdateDataDto("test-title", "test-description",
                "test-acceptanceCriteria", 100, 0.5, "status-test");

        UserStoryDto userStoryDtoTest = new UserStoryDto(UUID.randomUUID(), "test-title",
                "test-description", "test-acceptanceCriteria", 100, 0.5, "status-test",
                List.of());

        Mockito.when(userStoryService.saveNewUserStory(newUserStoryDtoTest))
                .thenReturn(userStoryDtoTest);

        // when:
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/user-stories")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "title":  "test-title",
                        "description":  "test-description",
                        "acceptanceCriteria": "test-acceptanceCriteria",
                        "businessValue": 100,
                        "estimation": 0.5,
                        "status": "status-test"
                        }
                         """));

        //then:
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(userStoryDtoTest.id().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(userStoryDtoTest.title()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(userStoryDtoTest.description()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.acceptanceCriteria").value(userStoryDtoTest.acceptanceCriteria()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.businessValue").value(userStoryDtoTest.businessValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.estimation").value(userStoryDtoTest.estimation()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(userStoryDtoTest.status()));
    }


    @Test
    @WithMockUser
    void shouldReturnListOfUserStories() throws Exception {
        //given:
        UserStoryDto userStoryDto1 = Instancio
                .of(UserStoryDto.class)
                .generate(Select.field(UserStoryDto::developers),
                generators -> generators.collection().size(2))
                .create();

        UserStoryDto userStoryDto2 = Instancio
                .of(UserStoryDto.class)
                .generate(Select.field(UserStoryDto::developers),
                        generators -> generators.collection().size(0))
                .create();

        Mockito.when(userStoryService.getAllUserStories()).thenReturn(List.of(userStoryDto1, userStoryDto2));

        //when:
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/user-stories"));

        //then:
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(userStoryDto1.id().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value(userStoryDto1.title()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value(userStoryDto1.description()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].acceptanceCriteria").value(userStoryDto1.acceptanceCriteria()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].businessValue").value(userStoryDto1.businessValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].estimation").value(userStoryDto1.estimation()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].status").value(userStoryDto1.status()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].developers.size()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].developers[0].id").value(userStoryDto1.developers().get(0).id().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].developers[0].fullName").value(userStoryDto1.developers().get(0).fullName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].developers[1].id").value(userStoryDto1.developers().get(1).id().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].developers[1].fullName").value(userStoryDto1.developers().get(1).fullName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(userStoryDto2.id().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value(userStoryDto2.title()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].description").value(userStoryDto2.description()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].acceptanceCriteria").value(userStoryDto2.acceptanceCriteria()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].businessValue").value(userStoryDto2.businessValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].estimation").value(userStoryDto2.estimation()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].status").value(userStoryDto2.status()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].developers.size()").value(0));
    }

    @Test
    @WithMockUser
    void shouldReturn404WhenUpdatedUserStoryNotFound() throws Exception {
        // given:
        UUID id = UUID.randomUUID();
        Mockito.when(userStoryService.updateUserStory(Mockito.eq(id), Mockito.any(UserStoryUpdateDataDto.class)))
                .thenThrow(new UserStoryNotFoundException("User story " + id + " not found."));

        // when:
        ResultActions response = mockMvc.perform(put("/api/v1/user-stories/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "title":  "test-title",
                        "description":  "test-description",
                        "acceptanceCriteria": "test-acceptanceCriteria",
                        "businessValue": 100,
                        "estimation": 0.5,
                        "status": "status-test"
                        }
                         """));

        // then:
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.info").value("User story " + id + " not found."));
    }

    @Test
    @WithMockUser
    void shouldReturn400WhenUpdateDataIsInvalid() throws Exception {
        // when:
        ResultActions response = mockMvc.perform(put("/api/v1/user-stories/" + UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "title":  "test",
                        "description":  "test-description",
                        "acceptanceCriteria": "test-acceptanceCriteria",
                        "businessValue": 100,
                        "estimation": 50,
                        "status": "status-test"
                        }
                         """));

        // then:
        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.info").value(containsString("Title must be between 5 and 50 characters")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.info").value(containsString("Estimation max value is 40.0")));
    }


        @Test
        @WithMockUser
        void shouldReturn200WhenUserStoryWasUpdated() throws Exception {
            // given:
            UUID id = UUID.randomUUID();
            UserStoryUpdateDataDto inputDto = Instancio.of(UserStoryUpdateDataDto.class)
                    .set(Select.field(UserStoryUpdateDataDto::title), "title")
                    .set(Select.field(UserStoryUpdateDataDto::estimation), 10.05)
                    .generate(Select.field(UserStoryUpdateDataDto::businessValue), gen -> gen.ints().range(100, 1500))
                    .create();
            UserStoryDto outputDto = Instancio.of(UserStoryDto.class)
                    .generate(Select.field(UserStoryDto::developers), gen -> gen.collection().size(0))
                    .create();

            Mockito.when(userStoryService.updateUserStory(id, inputDto))
                    .thenReturn(outputDto);

            // when:
            ResultActions response = mockMvc.perform(put("/api/v1/user-stories/" + id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                        {
                        "title":  "%s",
                        "description":  "%s",
                        "acceptanceCriteria": "%s",
                        "businessValue": %d,
                        "estimation": %f,
                        "status": "%s"
                        }
                         """.formatted(inputDto.title(), inputDto.description(),
                            inputDto.acceptanceCriteria(), inputDto.businessValue(),
                            inputDto.estimation(), inputDto.status())));

            // then:
            response.andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(outputDto.id().toString()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(outputDto.title()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(outputDto.description()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.acceptanceCriteria").value(outputDto.acceptanceCriteria()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.businessValue").value(outputDto.businessValue()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.estimation").value(outputDto.estimation()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(outputDto.status()));
        }
}
