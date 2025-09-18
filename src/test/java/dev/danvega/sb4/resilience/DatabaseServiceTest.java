package dev.danvega.sb4.resilience;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Simple test to verify the resilience features work.
 * This test demonstrates that the DatabaseService properly applies
 * Spring Boot 4's core resilience annotations.
 */
@SpringBootTest
@TestPropertySource(properties = {
    "logging.level.dev.danvega.sb4.resilience=DEBUG",
    "spring.artemis.embedded.enabled=false"
})
class DatabaseServiceTest {

    @Autowired
    private DatabaseService databaseService;

    @BeforeEach
    void setUp() {
        databaseService.resetCounters();
    }

    @Test
    void shouldRetryFailedOperations() {
        // Given
        String testData = "test-data";

        // When - The method will retry automatically if it fails
        String result = databaseService.saveData(testData);

        // Then - Should complete successfully (may take multiple attempts due to random failures)
        assertThat(result).contains("saved successfully");
        assertThat(result).contains(testData);
    }

    @Test
    void shouldFetchDataWithRetry() {
        // Given
        String testId = "123";

        // When
        String result = databaseService.fetchData(testId);

        // Then
        assertThat(result).contains("Data for id");
        assertThat(result).contains(testId);
    }

    @Test
    void shouldCompleteHeavyOperation() {
        // Given
        String taskId = "test-task";

        // When
        String result = databaseService.performHeavyOperation(taskId);

        // Then
        assertThat(result).contains("completed successfully");
        assertThat(result).contains(taskId);
    }

    @Test
    void shouldExecuteCriticalOperation() {
        // Given
        String operationId = "critical-test";

        // When
        String result = databaseService.criticalOperation(operationId);

        // Then
        assertThat(result).contains("executed successfully");
        assertThat(result).contains(operationId);
    }

    @Test
    void shouldResetCounters() {
        // Given - make a call to increment counters
        try {
            databaseService.saveData("test");
        } catch (Exception e) {
            // Ignore failures, we just want to increment counter
        }

        // When
        databaseService.resetCounters();

        // Then
        assertThat(databaseService.getAttemptCount()).isEqualTo(0);
    }
}