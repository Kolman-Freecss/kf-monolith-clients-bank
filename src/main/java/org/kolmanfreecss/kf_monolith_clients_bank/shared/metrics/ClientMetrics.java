package org.kolmanfreecss.kf_monolith_clients_bank.shared.metrics;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

/**
 * Service responsible for collecting and managing metrics related to client
 * operations in the banking system.
 * This class uses Micrometer to track various metrics including:
 * - Client creation/deletion/update counters
 * - Active clients count
 * - Operation duration measurements
 * - Statistical distribution of operation times
 *
 * These metrics are useful for monitoring system health, performance analysis,
 * and business intelligence.
 */
@Component
public class ClientMetrics {
    private final Counter clientCreationCounter;
    private final Counter clientDeletionCounter;
    private final Counter clientUpdateCounter;
    private final Counter clientRightsUpdateCounter;
    private final MeterRegistry registry;
    private final AtomicInteger activeClientsGauge;
    private final DistributionSummary clientOperationSummary;

    /**
     * Initializes all metrics collectors with the provided registry.
     * Sets up counters, gauges, and distribution summaries for tracking client
     * operations.
     *
     * @param registry The Micrometer registry used to register all metrics
     */
    public ClientMetrics(MeterRegistry registry) {
        this.registry = registry;

        // Counters for client operations
        this.clientCreationCounter = Counter.builder("bank.clients.created")
                .description("Number of clients created")
                .baseUnit("clients")
                .register(registry);

        this.clientDeletionCounter = Counter.builder("bank.clients.deleted")
                .description("Number of clients deleted")
                .baseUnit("clients")
                .register(registry);

        this.clientUpdateCounter = Counter.builder("bank.clients.updated")
                .description("Number of client updates")
                .baseUnit("operations")
                .register(registry);

        this.clientRightsUpdateCounter = Counter.builder("bank.clients.rights.updated")
                .description("Number of client rights updates")
                .baseUnit("operations")
                .register(registry);

        // Gauge for active clients
        this.activeClientsGauge = registry.gauge("bank.clients.active",
                new AtomicInteger(0));

        // Distribution summary for operation timing
        this.clientOperationSummary = DistributionSummary.builder("bank.clients.operation.duration")
                .description("Distribution summary of client operation durations")
                .baseUnit("milliseconds")
                .publishPercentiles(0.5, 0.75, 0.95, 0.99)
                .register(registry);
    }

    /**
     * Increments the counter for client creation operations.
     * Should be called whenever a new client is successfully created in the system.
     */
    public void incrementClientCreation() {
        clientCreationCounter.increment();
    }

    /**
     * Increments the counter for client deletion operations.
     * Should be called whenever a client is successfully removed from the system.
     */
    public void incrementClientDeletion() {
        clientDeletionCounter.increment();
    }

    /**
     * Increments the counter for client update operations.
     * Should be called whenever a client's information is modified.
     */
    public void incrementClientUpdate() {
        clientUpdateCounter.increment();
    }

    /**
     * Increments the counter for client rights update operations.
     * Should be called whenever a client's permissions or access rights are
     * modified.
     */
    public void incrementClientRightsUpdate() {
        clientRightsUpdateCounter.increment();
    }

    /**
     * Starts a new timer for measuring operation duration.
     * Use this method in conjunction with stopTimer() to measure operation
     * execution time.
     *
     * @return Timer.Sample object that can be used to stop the timer
     */
    public Timer.Sample startTimer() {
        return Timer.start(registry);
    }

    /**
     * Stops the timer and records the operation duration.
     * Records both the specific operation timer and adds to the general
     * distribution summary.
     *
     * @param sample    The Timer.Sample object returned by startTimer()
     * @param operation The name of the operation being timed
     */
    public void stopTimer(Timer.Sample sample, String operation) {
        Timer timer = Timer.builder("bank.clients.operation.time")
                .description("Time taken for client operations")
                .tags("operation", operation)
                .publishPercentiles(0.5, 0.75, 0.95, 0.99)
                .register(registry);

        long durationNanos = sample.stop(timer);
        double durationMillis = durationNanos / 1_000_000.0;
        clientOperationSummary.record(durationMillis);
    }

    /**
     * Updates the gauge that tracks the current number of active clients in the
     * system.
     * This method should be called whenever the total number of active clients
     * changes.
     *
     * @param count The current total number of active clients
     */
    public void setActiveClients(int count) {
        if (activeClientsGauge != null) {
            activeClientsGauge.set(count);
        }
    }
}
