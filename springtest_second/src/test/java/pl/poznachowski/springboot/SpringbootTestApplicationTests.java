package pl.poznachowski.springboot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Springboot2TestApplication.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class SpringbootTestApplicationTests {

	@Test
	public void contextLoads() {
	}


	@Test
	public void testName() throws Exception {



	}
}
