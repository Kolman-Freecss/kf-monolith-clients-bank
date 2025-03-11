package org.kolmanfreecss.kf_monolith_clients_bank.infrastructure.config.database;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.metrics.micrometer.MicrometerMetricsTrackerFactory;

import io.micrometer.core.instrument.MeterRegistry;

/**
 * HikariCP Connection Pool Configuration
 *
 * This class configures the HikariCP connection pool, which manages database
 * connections
 * for both direct JDBC access and Hibernate operations. HikariCP acts as a
 * connection
 * provider for Hibernate, optimizing database access performance.
 *
 * Integration points:
 * 1. Hibernate Integration:
 * - HikariCP provides connections to Hibernate's connection pool
 * - Hibernate uses these connections for all database operations
 * - Connection lifecycle is managed by HikariCP
 *
 * 2. Performance Configuration:
 * - Maximum pool size: Limits total connections to prevent database overload
 * - Minimum idle: Keeps ready connections for immediate use
 * - Connection timeout: Maximum wait time for connection acquisition
 * - Idle timeout: How long connections can remain unused
 * - Max lifetime: Maximum time a connection can exist
 *
 * 3. Monitoring Integration:
 * - Micrometer metrics for real-time monitoring
 * - Connection pool statistics
 * - Performance metrics for both Hibernate and direct JDBC usage
 *
 * @see com.zaxxer.hikari.HikariDataSource
 * @see org.hibernate.engine.jdbc.connections.spi.ConnectionProvider
 */
@Configuration
public class HikariPoolConfig {

    /**
     * Creates and configures the primary HikariCP DataSource used by both
     * Hibernate and direct JDBC operations.
     *
     * Configuration aspects:
     * 1. Pool Management:
     * - Maintains optimal connection pool size
     * - Handles connection lifecycle
     * - Provides connection validation
     *
     * 2. Hibernate Integration:
     * - Acts as Hibernate's connection provider
     * - Manages transaction connections
     * - Handles connection cleanup
     *
     * 3. Performance Monitoring:
     * - Tracks connection usage
     * - Measures response times
     * - Monitors pool health
     *
     * @param properties DataSource properties from application.properties
     * @param registry   Micrometer registry for metrics collection
     * @return Configured HikariDataSource
     */
    @Bean
    @Primary
    public HikariDataSource dataSource(DataSourceProperties properties, MeterRegistry registry) {
        HikariDataSource dataSource = properties.initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();

        // Configure metrics tracking with Micrometer
        dataSource.setMetricsTrackerFactory(new MicrometerMetricsTrackerFactory(registry));

        // Configure additional Hibernate-specific optimizations
        dataSource.setAutoCommit(false); // Let Hibernate manage transactions
        dataSource.setTransactionIsolation("TRANSACTION_READ_COMMITTED");
        dataSource.setValidationTimeout(5000); // 5 seconds
        dataSource.setConnectionTestQuery("SELECT 1"); // Validation query
        dataSource.setPoolName("HikariPool-Hibernate");

        return dataSource;
    }
}
