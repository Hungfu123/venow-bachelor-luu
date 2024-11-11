package org.venow.backend;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.venow.backend.domain.model.Event;
import org.venow.backend.domain.model.Role;
import org.venow.backend.domain.model.User;

import java.time.LocalDateTime;
import java.util.UUID;

@DirtiesContext
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureWireMock
@ActiveProfiles("test")
public abstract class TestBase {

    protected static final String DEFAULT_PASSWORD = "password";
    protected static final  Role EXHIBITOR = Role.EXHIBITOR;
    protected static final  Role ORGANIZER = Role.ORGANIZER;

    protected static final User USER_ORGANIZER_1 = new User(1L,  UUID.fromString("39ac4d0c-1635-4587-844a-b6bd6a522339"),"maxm@venow.de", DEFAULT_PASSWORD, ORGANIZER);
    protected static final User USER_EXHIBITOR_1 = new User(1L,  UUID.fromString("39ac4d0c-1635-4587-844a-b6bd6a522310"),"malehehem@venow.de", DEFAULT_PASSWORD, EXHIBITOR);

    protected static final Event EVENT_1 = new Event(
            null,
            UUID.fromString("45353065-3834-3030-2d65-3239633d3431"),
            "Fischstaebchen-Band",
            "A large outdoor music festival",
            "Central Park",
            LocalDateTime.of(2024, 6, 1, 12, 0),
            LocalDateTime.of(2024, 6, 1, 23, 0),
            UUID.fromString("123e4567-e89b-12d3-a456-426614174000")
    );

    @Container
    public static MySQLContainer<?> jdbcDataBaseContainer = new MySQLContainer<>("mysql:latest")
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
        registry.add("spring.datasource.driver-class-name", jdbcDataBaseContainer::getDriverClassName);
    }

}
