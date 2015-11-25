package pl.poznachowski.springboot.returner;

import org.springframework.stereotype.Component;
import pl.poznachowski.springboot.AuthorizationProfile;

@Component
@AuthorizationProfile
public class AuthorizedReturner implements Returner {
    public String returnString() {
        return "Authorized heheszek";
    }
}