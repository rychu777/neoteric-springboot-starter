package pl.poznachowski.springboot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.MetricFilterAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@SpringBootApplication(exclude = MetricFilterAutoConfiguration.class)
@EnableFeignClients
public class SpringbootTestApplication {

    private static final Logger LOG = LoggerFactory.getLogger(SpringbootTestApplication.class);

    public static void main(String[] args) {
//        try {
//            Manifest manifest = new Manifest(SpringbootTestApplication.class.getResourceAsStream("/META-INF/MANIFEST.MF"));
//            String applicationName = String.valueOf(manifest.getMainAttributes().get(Attributes.Name.IMPLEMENTATION_TITLE));
//            String applicationVersion = String.valueOf(manifest.getMainAttributes().get(Attributes.Name.IMPLEMENTATION_VERSION));
//            System.setProperty("logging.file", applicationName + "-" + applicationVersion + ".log");
//        } catch (IOException e) {
//            LOG.error("EEEE: {}", e);
//        }
        SpringApplication.run(SpringbootTestApplication.class, args);
    }


}
