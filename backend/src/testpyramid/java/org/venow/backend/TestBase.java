package org.venow.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;

@DirtiesContext
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureWireMock
@ActiveProfiles("testpyramid-testpyramid")
public abstract class TestBase {


    public static <T> T loadObjectFromJson(String jsonFilePath, Class<T> targetType) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(new ClassPathResource(jsonFilePath).getFile(), targetType);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load JSON file for " + targetType.getSimpleName(), e);
        }
    }

}
