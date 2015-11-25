package pl.poznachowski.springboot;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import pl.poznachowski.springboot.returner.AuthorizedReturner;

@Configuration
@Profile("authorizationDisabled")
public class TextReturnerTestConfig {

    @Bean
    public AuthorizedReturner registerReturner() {
        return new AuthorizedReturner() {
            @Override
            public String returnString() {
                return "HEHESZEK";
            }
        };
    }
}
