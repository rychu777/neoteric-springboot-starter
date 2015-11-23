package pl.poznachowski.springboot;

import org.springframework.stereotype.Component;

@Component
public class TextReturner {

    public String returnString() {
        return "HEHE";
    }
}
