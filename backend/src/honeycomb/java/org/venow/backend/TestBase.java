package org.venow.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.venow.backend.domain.model.*;
import org.venow.backend.testfactory.UserTestData;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@DirtiesContext
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureWireMock
@ActiveProfiles("test")
public abstract class TestBase {


    protected static final User USER_EXHIBITOR_2 = UserTestData.createExhibitor3();

    protected static final Event EVENT_1 = new Event(
            null,
            UUID.fromString("45353065-3834-3030-2d65-3239633d3431"),
            "Test",
            "Ablblalball",
            "Test Park",
            LocalDateTime.of(2024, 6, 1, 12, 0),
            LocalDateTime.of(2024, 6, 1, 23, 0),
            UUID.fromString("123e4567-e89b-12d3-a456-426614174000")
    );


    protected static final UUID EVENT_ID_1 = EVENT_1.getEventId();
    protected static final UUID ORGANIZER_ID = UserTestData.createOrganizer1().getUserId();
    protected static final UUID ORGANIZER_ID_EMPTY_APP = UserTestData.createOrganizer3().getUserId();
    protected static final UUID EXHIBITOR_ID_1 = UserTestData.createExhibitor1().getUserId();
    protected static final UUID EXHIBITOR_ID_2 = USER_EXHIBITOR_2.getUserId();


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

    protected <T> T loadObjectFromJson(String jsonFilePath, Class<T> targetType) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(new ClassPathResource(jsonFilePath).getFile(), targetType);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load JSON file for " + targetType.getSimpleName(), e);
        }
    }

}
