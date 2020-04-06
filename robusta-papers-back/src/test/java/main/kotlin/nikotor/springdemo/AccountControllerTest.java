package main.kotlin.nikotor.springdemo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.kotlin.KotlinModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import io.robusta.nikotor.user.LoginAttemptPayload;
import io.robusta.nikotor.user.RegisterPayload;
import io.robusta.nikotor.user.User;
import main.kotlin.nikotor.springdemo.rest.AccountController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import javax.annotation.PostConstruct;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AccountController.class)
public class AccountControllerTest {

    String api = SpringDemoApplication.CURRENT_API;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    public void setUp() {
        objectMapper.registerModule(new JavaTimeModule()).registerModule(new KotlinModule()).registerModule(new ParameterNamesModule());
    }

    User johnDoe = new User("john@doe.com");
    User jane = new User("jane@doe.com");

    @Test
    void whenValidInput_thenReturns200() throws Exception {
        mockMvc.perform(post(api + "/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(new RegisterPayload(johnDoe, "abc"))))
                .andExpect(status().isCreated());

        mockMvc.perform(post(api + "/login")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(new LoginAttemptPayload("john@doe.com", "abc", ""))))
                .andExpect(status().isOk());

    }
}
