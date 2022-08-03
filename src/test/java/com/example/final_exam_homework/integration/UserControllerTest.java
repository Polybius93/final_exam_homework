package com.example.final_exam_homework.integration;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:registerUser_WithAlreadyExistingUsername_before.sql")
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:usercontroller_delete_content.sql")
@Import(WebSecurityConfiguration.class)
@ActiveProfiles(profiles = "test")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void registerUser_WithValidInformation_ShouldReturnOkAndUserInformation() throws Exception {
        String userRegistrationInformation = new JSONObject()
                .put("username", "Theodred Herstina")
                .put("password", "12345678")
                .toString();

        mockMvc
                .perform(post("/registration").content(userRegistrationInformation).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("Theodred Herstina")))
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void registerUser_WithEmptyUsernameField_ShouldReturnMissingUsernameException() throws Exception {
        String userRegistrationInformation = new JSONObject()
                .put("username", "")
                .put("password", "12345678")
                .toString();

        mockMvc
                .perform(post("/registration").content(userRegistrationInformation).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Username is missing!")));
    }

    @Test
    public void registerUser_WithEmptyPasswordField_ShouldReturnMissingPasswordException() throws Exception {
        String userRegistrationInformation = new JSONObject()
                .put("username", "Theodred Herstina")
                .put("password", "")
                .toString();

        mockMvc
                .perform(post("/registration").content(userRegistrationInformation).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Password is missing!")));
    }

    @Test
    public void registerUser_WithEmptyUsernameAndPasswordField_ShouldReturnMissingUsernameAndPasswordException() throws Exception {
        String userRegistrationInformation = new JSONObject()
                .put("username", "")
                .put("password", "")
                .toString();

        mockMvc
                .perform(post("/registration").content(userRegistrationInformation).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Username and password is missing!")));
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:registerUser_WithAlreadyExistingUsername_before.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:usercontroller_delete_content.sql")
    public void registerUser_WithAlreadyExistingUsername_ShouldReturnUsernameAlreadyInUseException() throws Exception {
        String userRegistrationInformation = new JSONObject()
                .put("username", "Zeno Petceran")
                .put("password", "12345678")
                .toString();

        mockMvc
                .perform(post("/registration").content(userRegistrationInformation).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Username already in use!")));
    }

    @Test
    public void registerUser_WithShortPasswordField_ShouldReturnInvalidPasswordLengthException() throws Exception {
        String userRegistrationInformation = new JSONObject()
                .put("username", "Theodred Herstina")
                .put("password", "1234567")
                .toString();

        mockMvc
                .perform(post("/registration").content(userRegistrationInformation).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Password length should be at least 8 characters!")));
    }

    @Test
    public void loginUser_WithValidInformation_ShouldReturnOkAndUserInformationAndJwtToken() throws Exception {
        String userLoginInformation = new JSONObject()
                .put("username", "Zeno Petceran")
                .put("password", "12345678")
                .toString();

        mockMvc
                .perform(post("/login").content(userLoginInformation).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("ok")))
                .andExpect(jsonPath("$.jwt").isNotEmpty())
                .andExpect(jsonPath("$.greenbay_dollars", is(2000)));
    }

    @Test
    public void loginUser_WithInvalidInformation_ShouldReturnUsernameNotFoundException() throws Exception {
        String userLoginInformation = new JSONObject()
                .put("username", "Zeno Petceran")
                .put("password", "12345678")
                .toString();

        mockMvc
                .perform(post("/login").content(userLoginInformation).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("No user found by this username!")));
    }

    @Test
    public void loginUser_WithEmptyUsernameField_ShouldReturnMissingUsernameException() throws Exception {
        String userLoginInformation = new JSONObject()
                .put("username", "")
                .put("password", "12345678")
                .toString();

        mockMvc
                .perform(post("/login").content(userLoginInformation).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Username is missing!")));
    }

    @Test
    public void loginUser_WithEmptyPasswordField_ShouldReturnMissingPasswordException() throws Exception {
        String userLoginInformation = new JSONObject()
                .put("username", "Zeno Petceran")
                .put("password", "")
                .toString();

        mockMvc
                .perform(post("/login").content(userLoginInformation).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Password is missing!")));
    }

    @Test
    public void loginUser_WithShortPassword_InvalidPasswordLengthException() throws Exception {
        String userLoginInformation = new JSONObject()
                .put("username", "Zeno Petceran")
                .put("password", "1234567")
                .toString();

        mockMvc
                .perform(post("/login").content(userLoginInformation).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Password length should be at least 8 characters!")));
    }
}
