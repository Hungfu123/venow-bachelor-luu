package org.venow.backend;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.venow.backend.domain.model.Role;
import org.venow.backend.domain.model.User;

import java.util.UUID;

@DirtiesContext
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureWireMock
public abstract class TestBase {

    protected static final String DEFAULT_PASSWORD = "password";
    protected static final  Role EXHIBITOR = Role.EXHIBITOR;
    protected static final  Role ORGANIZER = Role.ORGANIZER;

    protected static final User USER_ORGANIZER_1 = new User(1L,  UUID.fromString("39ac4d0c-1635-4587-844a-b6bd6a522339"),"maxm@venow.de", DEFAULT_PASSWORD, ORGANIZER);
    protected static final User USER_EXHIBITOR_1 = new User(1L,  UUID.fromString("39ac4d0c-1635-4587-844a-b6bd6a522310"),"malehehem@venow.de", DEFAULT_PASSWORD, EXHIBITOR);

    @Container
    public static MySQLContainer<?> jdbcDataBaseContainer = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("mydatabase")
            .withUsername("myuser")
            .withPassword("secret");

    @LocalServerPort
    public int port;

    @DynamicPropertySource
    static void configureTestDatabase(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", jdbcDataBaseContainer::getJdbcUrl);
        registry.add("spring.datasource.username", jdbcDataBaseContainer::getUsername);
        registry.add("spring.datasource.password", jdbcDataBaseContainer::getPassword);
    }

}
