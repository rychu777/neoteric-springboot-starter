package pl.poznachowski.springboot.returner;

import org.springframework.stereotype.Component;
import pl.poznachowski.springboot.Authorization;

@Component
@Authorization
public class AuthorizedReturner implements Returner {
    public String returnString() {
        return "Authorized heheszek";
    }
}