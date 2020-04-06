package main.kotlin.nikotor.springdemo;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr353.JSR353Module;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import com.fasterxml.jackson.module.kotlin.KotlinModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import io.robusta.nikotor.InMemoryEventStore;
import io.robusta.nikotor.SimpleNikotorEngine;
import io.robusta.nikotor.core.NikotorEngine;
import io.robusta.nikotor.user.EmailSender;
import io.robusta.nikotor.user.UserBundle;
import io.robusta.nikotor.user.UsersProjectionUpdater;
import main.kotlin.nikotor.springdemo.security.CorsFilter;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import javax.servlet.Filter;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class SpringDemoApplication {

    static public final String CURRENT_API = "/api/v1";
    // Could be injected properly as a bean...
    static private InMemoryEventStore store;
    static private NikotorEngine engine;
    static private UsersProjectionUpdater updater = new UsersProjectionUpdater(
            new UserBundle(new FakeEmailSender()));

    public SpringDemoApplication() {
        createEngine();
    }

    public static void main(String[] args) {

        SpringApplication.run(SpringDemoApplication.class, args);
    }

    static public void createEngine() {

        store = new InMemoryEventStore();
        engine = new SimpleNikotorEngine(store, Arrays.asList(updater));


    }

    static public NikotorEngine getEngine() {
        return engine;
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> someFilterRegistration() {
        System.out.println("Adding Cors Filter");
        FilterRegistrationBean<CorsFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(corsFilter());
        registration.addUrlPatterns("/*");
        registration.setName("CorsFilter");
        registration.setOrder(1);
        return registration;
    }

    public CorsFilter corsFilter() {
        return new CorsFilter();
    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new Jackson2ObjectMapperBuilder()
                .modules( new AfterburnerModule(), new KotlinModule(), new JavaTimeModule(), new ParameterNamesModule())
                .build()
                .setVisibility(PropertyAccessor.SETTER, JsonAutoDetect.Visibility.ANY)
                .setVisibility(PropertyAccessor.CREATOR, JsonAutoDetect.Visibility.ANY)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        jsonConverter.setObjectMapper(objectMapper);
        return jsonConverter;
    }

}


class FakeEmailSender implements EmailSender {
    private int sent = 0;

    public void sendEmail(@NotNull String target, @NotNull String sender,
                          @NotNull String subject, @NotNull String content) {
        sent++;
    }


}