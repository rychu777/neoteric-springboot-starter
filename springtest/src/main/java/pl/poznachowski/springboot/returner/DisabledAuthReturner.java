package pl.poznachowski.springboot.returner;

import org.springframework.stereotype.Component;

@Component
@DisabledAuthorization
public class DisabledAuthReturner implements Returner {
    @Override
    public String returnString() {
        return "not authorized heheszek";
    }
}
