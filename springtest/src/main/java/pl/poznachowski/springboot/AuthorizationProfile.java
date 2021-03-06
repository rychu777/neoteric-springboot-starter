package pl.poznachowski.springboot;

import org.springframework.context.annotation.Profile;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Profile(AuthorizationProfile.NAME)
public @interface AuthorizationProfile {
    String NAME = "authorization";
}
