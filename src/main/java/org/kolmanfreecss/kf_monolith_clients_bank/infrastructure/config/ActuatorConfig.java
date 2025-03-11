package org.kolmanfreecss.kf_monolith_clients_bank.infrastructure.config;

import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for Spring Boot Actuator endpoints.
 * Configures and documents all available monitoring and management endpoints.
 *
 * Available Endpoints:
 * 1. Health and Status:
 * - /actuator/health - Application health information
 * - /actuator/info - Application information
 *
 * 2. Metrics and Performance:
 * - /actuator/metrics - Application metrics
 * - /actuator/prometheus - Prometheus format metrics
 * - /actuator/metrics/{metric.name} - Detailed metric information
 * - /actuator/metrics/method.execution.time - Method execution metrics
 * - /actuator/metrics/method.execution.time.histogram - Performance histograms
 * - /actuator/metrics/method.execution.time.percentiles - Performance
 * percentiles
 *
 * 3. Web and HTTP:
 * - /actuator/httptrace - HTTP request-response traces
 * - /actuator/mappings - Request mapping information
 *
 * 4. Application Management:
 * - /actuator/env - Environment properties
 * - /actuator/configprops - Configuration properties
 * - /actuator/beans - Spring beans and dependencies
 * - /actuator/conditions - Auto-configuration conditions
 * - /actuator/scheduledtasks - Scheduled task details
 *
 * 5. Cache Management:
 * - /actuator/caches - Cache information
 * - /actuator/caches/{cache.name} - Specific cache details
 *
 * 6. Thread and Processing:
 * - /actuator/threaddump - JVM thread dump
 * - /actuator/heapdump - JVM heap dump (download)
 *
 * 7. Logging:
 * - /actuator/loggers - Logger configurations
 * - /actuator/loggers/{logger.name} - Specific logger configuration
 *
 * @author Kolman-Freecss (@https://github.com/Kolman-Freecss)
 * @version 1.0
 */
@Configuration
public class ActuatorConfig {
    // Configuration is handled through application.properties
}
