package dev.danvega.sb4.resilience;

import org.springframework.resilience.annotation.ConcurrencyLimit;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Simple service that demonstrates Spring Boot 4's core resilience features.
 *
 * This service simulates a database that occasionally fails, showing how
 * Spring's built-in resilience features can handle failures gracefully.
 */
@Service
public class DatabaseService {

    private final Random random = new Random();
    private final AtomicInteger attemptCounter = new AtomicInteger(0);

    /**
     * Demonstrates basic @Retryable functionality.
     * This method will retry up to 3 times if it fails, with a 1-second delay between attempts.
     */
    @Retryable(maxAttempts = 3, delay = 1000)
    public String saveData(String data) {
        int attempt = attemptCounter.incrementAndGet();
        System.out.println("Attempting to save data '" + data + "' - Attempt: " + attempt + " at " + LocalDateTime.now());

        // Simulate random database failures (50% chance)
        if (random.nextBoolean()) {
            throw new DatabaseException("Database connection failed - simulated error");
        }

        attemptCounter.set(0); // Reset on success
        return "Data '" + data + "' saved successfully on attempt " + attempt;
    }

    /**
     * Demonstrates @Retryable with exponential backoff.
     * Delays start at 500ms and double each time (500ms, 1000ms, 2000ms, 4000ms).
     */
    @Retryable(
        maxAttempts = 4,
        delay = 500,
        multiplier = 2.0,
        maxDelay = 5000
    )
    public String fetchData(String id) {
        System.out.println("Fetching data with id: " + id + " at " + LocalDateTime.now());

        // 60% chance of failure to show retry behavior
        if (random.nextDouble() < 0.6) {
            throw new DatabaseException("Database timeout - simulated error");
        }

        return "Data for id '" + id + "': Sample database record";
    }

    /**
     * Demonstrates @ConcurrencyLimit.
     * Only allows 2 concurrent executions of this method at a time.
     */
    @ConcurrencyLimit(2)
    public String performHeavyOperation(String taskId) {
        System.out.println("Starting heavy operation: " + taskId + " at " + LocalDateTime.now());

        try {
            // Simulate a long-running operation
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Operation interrupted");
        }

        System.out.println("Completed heavy operation: " + taskId + " at " + LocalDateTime.now());
        return "Heavy operation '" + taskId + "' completed successfully";
    }

    /**
     * Demonstrates combining @Retryable and @ConcurrencyLimit.
     * This limits concurrent access AND retries on failure.
     */
    @ConcurrencyLimit(1) // Only one at a time
    @Retryable(maxAttempts = 2, delay = 1000) // Retry once if it fails
    public String criticalOperation(String operationId) {
        System.out.println("Executing critical operation: " + operationId + " at " + LocalDateTime.now());

        // 40% chance of failure
        if (random.nextDouble() < 0.4) {
            throw new DatabaseException("Critical operation failed - simulated error");
        }

        return "Critical operation '" + operationId + "' executed successfully";
    }

    /**
     * A method without any resilience annotations for comparison.
     */
    public String simpleOperation(String input) {
        System.out.println("Executing simple operation with: " + input);

        // This will fail 30% of the time with no retry
        if (random.nextDouble() < 0.3) {
            throw new DatabaseException("Simple operation failed - no retry configured");
        }

        return "Simple operation completed with: " + input;
    }

    public void resetCounters() {
        attemptCounter.set(0);
    }

    public int getAttemptCount() {
        return attemptCounter.get();
    }

    public static class DatabaseException extends RuntimeException {
        public DatabaseException(String message) {
            super(message);
        }
    }
}