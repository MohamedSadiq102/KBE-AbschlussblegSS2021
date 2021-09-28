package userManagement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import userManagement.controller.UserController;
import userManagement.repository.UserRepository;

import static org.hamcrest.text.MatchesPattern.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource(locations = "/test.properties")
public class AuthEndpointTest {

    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userRepository)).build();
    }

    @Test
    void post_to_auth_with_existing_pw_id_should_return_random_string() {
        try {
            String auth_payload = "{ \"userid\": \"mmuster\" , \"password\": \"pass1234\"}";
            mockMvc.perform(post("/auth").contentType(MediaType.APPLICATION_JSON).content(auth_payload))
                    .andExpect(status().isOk()).andExpect(content().string(matchesPattern("\\w*")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void post_to_auth_with_non_existing_combination_returns_UNOTHORIZED() {
        try {
            String auth_payload = "{ \"userid\": \"niemand\" , \"password\": \"geheim\"}";
            mockMvc.perform(post("/auth").contentType(MediaType.APPLICATION_JSON).content(auth_payload))
                    .andExpect(status().isUnauthorized());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
