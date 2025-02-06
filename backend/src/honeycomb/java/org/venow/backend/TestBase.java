package org.venow.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import groovy.util.logging.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.venow.backend.config.TestSecurityConfig;
import org.venow.backend.domain.model.Event;
import org.venow.backend.domain.model.User;
import org.venow.backend.testfactory.ApplicationDataFactory;
import org.venow.backend.testfactory.UserTestData;
import org.venow.backend.web.reponse.ApplicationResponse;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.UUID;
@Import(TestSecurityConfig.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@Slf4j
@AutoConfigureWireMock
@ActiveProfiles("test")
public abstract class TestBase {


    protected static final User USER_EXHIBITOR_3 = UserTestData.createExhibitor3();
    protected static final User USER_EXHIBITOR_2 = UserTestData.createExhibitor2();
    protected static final User USER_ORGANIZER_1 = UserTestData.createOrganizer2();
    protected static final User USER_ORGANIZER_2 = UserTestData.createOrganizer2();

    protected static final Event EVENT_1 = new Event(
            null,
            UUID.fromString("45353065-3834-3030-2d65-3239633d3431"),
            "Test",
            "Ablblalball",
            "Test Park",
            LocalDateTime.of(2024, 6, 1, 12, 0),
            LocalDateTime.of(2024, 6, 1, 23, 0),
            50,
            UUID.fromString("123e4567-e89b-12d3-a456-426614174000")
    );


    protected static final UUID EVENT_ID_1 = EVENT_1.getEventId();
    protected static final UUID ORGANIZER_ID = UserTestData.createOrganizer1().getUserId();
    protected static final UUID ORGANIZER_ID_EMPTY_APP = UserTestData.createOrganizer3().getUserId();
    protected static final UUID EXHIBITOR_ID_1 = UserTestData.createExhibitor1().getUserId();
    protected static final UUID EXHIBITOR_ID_2 = USER_EXHIBITOR_3.getUserId();

    protected static final ApplicationResponse APPLICATION_1 = ApplicationDataFactory.createITApplicationResponse();
    protected static final ApplicationResponse APPLICATION_2 = ApplicationDataFactory.createDefaultApplicationResponse();
    protected static final ApplicationResponse APPLICATION_3 = ApplicationDataFactory.createOtherOrganizerApplicationResponse();
    private static final Logger log = LoggerFactory.getLogger(TestBase.class);


    @AfterEach
    void cleanDatabase(@Autowired DataSource dataSource) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            cleanDatabaseForTestcontainers(connection);
        }
    }

    private void cleanDatabaseForTestcontainers(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("SET FOREIGN_KEY_CHECKS = 0");

            statement.execute("TRUNCATE TABLE contracts");
            statement.execute("TRUNCATE TABLE events");
            statement.execute("TRUNCATE TABLE users");
            statement.execute("TRUNCATE TABLE applications");
            log.info("Database cleaned up for Testcontainers.");
        } catch (SQLException e) {
            log.error("Failed to clean database for Testcontainers: " + e.getMessage());
            throw e;
        }
    }


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
            mapper.registerModule(new JavaTimeModule());
            return mapper.readValue(new ClassPathResource(jsonFilePath).getFile(), targetType);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load JSON file for " + targetType.getSimpleName(), e);
        }
    }

}
