package pl.poznachowski.springboot;

import com.neoteric.starter.jersey.AbstractJerseyConfig;
import org.springframework.stereotype.Component;
import pl.poznachowski.springboot.rabbit.SendRabbitEndpoint;

@Component
public class JerseyConfig extends AbstractJerseyConfig {

    @Override
    protected void configure() {
        register(SampleEndpoint.class);
        register(SampleEndpoint2.class);
        register(SendRabbitEndpoint.class);
    }
}
