package main.kotlin.nikotor.springdemo.rest;

import io.robusta.nikotor.ErrorTypes;
import io.robusta.nikotor.NikotorException;
import io.robusta.nikotor.NotFoundException;
import io.robusta.nikotor.core.NikotorEngine;
import io.robusta.nikotor.user.*;
import main.kotlin.nikotor.springdemo.SpringDemoApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(SpringDemoApplication.CURRENT_API)
public class AccountController {

    private NikotorEngine engine = SpringDemoApplication.getEngine();
    private UserDaoAsync dao = new UserDaoAsync(new UserDatasetDao());

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public String registerAccount(@Valid @RequestBody RegisterPayload payload) throws ExecutionException, InterruptedException {
        return engine.processAsync(new RegisterUserCommand(payload)).get().getEvent().getId();
    }


    /**
     * The client makes only one query
     */
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public User loginAttempt(@Valid @RequestBody LoginAttemptPayload payload) throws ExecutionException, InterruptedException {
        String token = UUID.randomUUID().toString();
        payload.setToken(token);

        // Create the attempt command
        engine.processAsync(new LoginCommand(payload)).get().getEvent().getId();

        // Return the user by query. No leak outside the server for the token
        return dao
                .queryLoginAttempt(payload.getEmail(), token)
                .get()
                .orElseThrow(() -> new NikotorException("User not found",401, ErrorTypes.COMMAND_ERROR, payload.getEmail()));

    }
}
