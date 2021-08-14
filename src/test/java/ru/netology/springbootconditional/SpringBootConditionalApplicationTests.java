package ru.netology.springbootconditional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringBootConditionalApplicationTests {
    private static final int DEV_PORT = 8080;
    private static final int PROD_PORT = 8081;

    @Autowired
    TestRestTemplate restTemplate;

    private static GenericContainer<?> devApp = new GenericContainer<>("devapp");

    private static GenericContainer<?> prodApp = new GenericContainer<>("prodapp")
            .withExposedPorts(PROD_PORT);

    @BeforeAll
    public static void setUp() {
        devApp.start();
        prodApp.start();
    }

    @Test
    void contextLoads_dev() {
        ResponseEntity<String> forEntity = restTemplate.getForEntity("http://localhost:" + devApp.getMappedPort(DEV_PORT) + "/profile", String.class);
        String response = forEntity.getBody();

        Assertions.assertEquals("Current profile is dev", response);
    }

    @Test
    void contextLoads_prod() {
        ResponseEntity<String> forEntity = restTemplate.getForEntity("http://localhost:" + prodApp.getMappedPort(PROD_PORT) + "/profile", String.class);
        String response = forEntity.getBody();

        Assertions.assertEquals("Current profile is production", response);
    }

}
