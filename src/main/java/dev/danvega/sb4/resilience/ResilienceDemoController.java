package dev.danvega.sb4.resilience;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Simple controller to demonstrate Spring Boot 4's resilience features.
 *
 * Each endpoint clearly shows a different resilience pattern in action.
 */
@RestController
@RequestMapping("/api/resilience")
public class ResilienceDemoController {

    private final DatabaseService databaseService;
    private final ExecutorService executor = Executors.newFixedThreadPool(10);

    public ResilienceDemoController(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    /**
     * Demonstrates basic retry functionality.
     * Try this endpoint multiple times to see the retry behavior in the console logs.
     *
     * Example: POST /api/resilience/save with body: "my important data"
     */
    @PostMapping("/save")
    public Map<String, String> saveData(@RequestBody String data) {
        try {
            String result = databaseService.saveData(data);
            return Map.of(
                "status", "success",
                "message", result
            );
        } catch (Exception e) {
            return Map.of(
                "status", "failed",
                "message", "Failed after all retry attempts: " + e.getMessage()
            );
        }
    }

    /**
     * Demonstrates retry with exponential backoff.
     * Watch the console logs to see increasing delays between retry attempts.
     *
     * Example: GET /api/resilience/fetch/123
     */
    @GetMapping("/fetch/{id}")
    public Map<String, String> fetchData(@PathVariable String id) {
        try {
            String result = databaseService.fetchData(id);
            return Map.of(
                "status", "success",
                "data", result
            );
        } catch (Exception e) {
            return Map.of(
                "status", "failed",
                "message", "Failed after all retry attempts: " + e.getMessage()
            );
        }
    }

    /**
     * Demonstrates concurrency limiting.
     * Make multiple requests quickly to see only 2 running at once.
     *
     * Example: POST /api/resilience/heavy-task with body: "task-1"
     */
    @PostMapping("/heavy-task")
    public Map<String, String> performHeavyTask(@RequestBody String taskId) {
        try {
            String result = databaseService.performHeavyOperation(taskId);
            return Map.of(
                "status", "success",
                "message", result
            );
        } catch (Exception e) {
            return Map.of(
                "status", "failed",
                "message", e.getMessage()
            );
        }
    }

    /**
     * Demonstrates combining retry and concurrency limit.
     * Only one request at a time, but will retry if it fails.
     *
     * Example: POST /api/resilience/critical with body: "critical-op-1"
     */
    @PostMapping("/critical")
    public Map<String, String> criticalOperation(@RequestBody String operationId) {
        try {
            String result = databaseService.criticalOperation(operationId);
            return Map.of(
                "status", "success",
                "message", result
            );
        } catch (Exception e) {
            return Map.of(
                "status", "failed",
                "message", "Failed after all retry attempts: " + e.getMessage()
            );
        }
    }

    /**
     * For comparison - this endpoint has no resilience features.
     * It will fail sometimes with no retry.
     *
     * Example: POST /api/resilience/simple with body: "no resilience"
     */
    @PostMapping("/simple")
    public Map<String, String> simpleOperation(@RequestBody String input) {
        try {
            String result = databaseService.simpleOperation(input);
            return Map.of(
                "status", "success",
                "message", result
            );
        } catch (Exception e) {
            return Map.of(
                "status", "failed",
                "message", e.getMessage()
            );
        }
    }

    /**
     * Test concurrency limits by making multiple requests at once.
     * This will show how the concurrency limit queues requests.
     */
    @PostMapping("/test-concurrency")
    public Map<String, Object> testConcurrency(@RequestParam(defaultValue = "5") int numberOfTasks) {
        System.out.println("\n=== Starting concurrency test with " + numberOfTasks + " tasks ===");

        // Start multiple tasks at the same time
        var futures = CompletableFuture.allOf(
            java.util.stream.IntStream.range(1, numberOfTasks + 1)
                .mapToObj(i -> CompletableFuture.supplyAsync(() -> {
                    try {
                        return databaseService.performHeavyOperation("concurrent-task-" + i);
                    } catch (Exception e) {
                        return "Task " + i + " failed: " + e.getMessage();
                    }
                }, executor))
                .toArray(CompletableFuture[]::new)
        );

        try {
            futures.get(); // Wait for all to complete
        } catch (Exception e) {
            // Handle execution exceptions
        }

        return Map.of(
            "message", "Concurrency test completed",
            "numberOfTasks", numberOfTasks,
            "note", "Check console logs to see that only 2 tasks ran simultaneously"
        );
    }

    /**
     * Get information about available endpoints.
     */
    @GetMapping("/help")
    public Map<String, Object> getHelp() {
        return Map.of(
            "title", "Spring Boot 4 Resilience Features Demo",
            "endpoints", List.of(
                "POST /api/resilience/save - Test basic retry (3 attempts, 1s delay)",
                "GET /api/resilience/fetch/{id} - Test exponential backoff retry",
                "POST /api/resilience/heavy-task - Test concurrency limit (max 2 concurrent)",
                "POST /api/resilience/critical - Test retry + concurrency limit combined",
                "POST /api/resilience/simple - No resilience (for comparison)",
                "POST /api/resilience/test-concurrency - Test multiple concurrent requests"
            ),
            "tip", "Watch the console logs to see the resilience patterns in action!"
        );
    }

    @PostMapping("/reset")
    public Map<String, String> resetCounters() {
        databaseService.resetCounters();
        return Map.of("message", "Counters reset successfully");
    }
}