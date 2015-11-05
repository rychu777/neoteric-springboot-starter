package pl.poznachowski.springboot.mongo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Document
public class Person {

    private String id;
    private String name;
    private LocalDateTime local;
    private ZonedDateTime zoned;
    private int age;


    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Person(@JsonProperty("name") String name, @JsonProperty("age") int age,
                  @JsonProperty("local") LocalDateTime local, @JsonProperty("zoned") ZonedDateTime zoned) {
        this.name = name;
        this.age = age;
        this.local = local;
        this.zoned = zoned;
    }

    public LocalDateTime getLocal() {
        return local;
    }

    public ZonedDateTime getZoned() {
        return zoned;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", local=" + local +
                ", zoned=" + zoned +
                ", age=" + age +
                '}';
    }
}
