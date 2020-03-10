package main.kotlin.nikotor.springdemo.rest;

import io.robusta.nikotor.core.NikotorEngine;
import io.robusta.nikotor.user.RegisterPayload;
import io.robusta.nikotor.user.RegisterUserCommand;
import io.robusta.nikotor.user.User;
import main.kotlin.nikotor.springdemo.SpringDemoApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1")
public class AccountResource {

    private NikotorEngine engine = SpringDemoApplication.getEngine();

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public String registerAccount(@Valid @RequestBody RegisterPayload payload) throws ExecutionException, InterruptedException {
        return engine.processAsync(new RegisterUserCommand(payload)).get().getEvent().getId();
    }

}
