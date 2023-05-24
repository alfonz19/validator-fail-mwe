package mwe.validatorfail.config;

import mwe.validatorfail.validation.AnyUuid;

import javax.annotation.PostConstruct;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;

import java.util.UUID;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;


@Configuration
public class Config {

    public Config(Validator validator) {
        System.out.println("Validating");
        validator.validate(new ValidatorHealer());
        System.out.println("OK");

    }

    @EventListener(ApplicationReadyEvent.class)
    public void started(ApplicationReadyEvent event) {
        System.out.println("READY!");
    }

    private static class ValidatorHealer {
        @AnyUuid
        private final String whatever = UUID.randomUUID().toString();

    }

}
