package pl.poznachowski.springboot.returner;

import org.springframework.stereotype.Component;

@Component
@DisabledAuthorizationProfile
public class DisabledAuthReturner implements Returner {
    @Override
    public String returnString() {
        return "not authorized heheszek";
    }
}
