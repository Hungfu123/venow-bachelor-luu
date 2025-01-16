package org.venow.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import groovy.util.logging.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
@Testcontainers
@AutoConfigureWireMock
@ActiveProfiles("testpyramid-h2")
public abstract class TestBase {


    private static final Logger log = LoggerFactory.getLogger(TestBase.class);

    public static <T> T loadObjectFromJson(String jsonFilePath, Class<T> targetType) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Resource resource = new DefaultResourceLoader().getResource("classpath:" + jsonFilePath);
            return mapper.readValue(resource.getInputStream(), targetType);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load JSON file for " + targetType.getSimpleName(), e);
        }
    }

    @AfterEach
    void cleanDatabase(@Autowired DataSource dataSource) throws SQLException {
            try (Connection connection = dataSource.getConnection()) {
                if (isUsingTestcontainers(connection)) {
                    log.info("Cleaning database using Testcontainers script...");
                    cleanDatabaseForTestcontainers(connection);
                } else if (isUsingH2(connection)) {
                    log.info("Cleaning database using H2 script...");
                    cleanDatabaseForH2(connection);
                } else {
                    throw new UnsupportedOperationException("Unsupported database type for cleanup.");
                }
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
            log.info("Failed to clean database for Testcontainers: " + e.getMessage());
            throw e;
        }
    }

    private void cleanDatabaseForH2(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("SET REFERENTIAL_INTEGRITY FALSE");

            statement.execute("TRUNCATE TABLE contracts");
            statement.execute("TRUNCATE TABLE events");
            statement.execute("TRUNCATE TABLE users");
            statement.execute("TRUNCATE TABLE applications");

            log.info("Database cleaned up for H2.");
        } catch (SQLException e) {
            log.info("Failed to clean database for H2: " + e.getMessage());
            throw e;
        }
    }

    private boolean isUsingTestcontainers(Connection connection) throws SQLException {
        String url = connection.getMetaData().getURL();
        return url.contains("mysql");
    }

    private boolean isUsingH2(Connection connection) throws SQLException {
        String url = connection.getMetaData().getURL();
        return url.contains("h2:mem");
    }


}
