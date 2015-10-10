package pl.poznachowski.springboot;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;

public class TestJSON {

    @JsonProperty("text")
    private String text;
    @JsonProperty("info")
    private String info;
    @JsonProperty("dateTime")
    private ZonedDateTime dateTime;

    @JsonCreator
    public TestJSON(@JsonProperty("text") String text,@JsonProperty("info") String info) {
        this.text = text;
        this.info = info;
        this.dateTime = ZonedDateTime.now();
    }

    public ZonedDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(ZonedDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }


    @Override
    public String toString() {
        return "TestJSON{" +
                "text='" + text + '\'' +
                ", info='" + info + '\'' +
                ", dateTime=" + dateTime +
                '}';
    }
}
