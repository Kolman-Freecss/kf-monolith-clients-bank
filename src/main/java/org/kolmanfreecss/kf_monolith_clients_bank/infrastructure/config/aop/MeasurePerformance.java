package org.kolmanfreecss.kf_monolith_clients_bank.infrastructure.config.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation for marking methods or classes for performance measurement.
 * When applied to a method or class, the execution time and other performance
 * metrics
 * will be automatically collected and recorded.
 *
 * Usage examples:
 * 1. On a method:
 * {@code
 * @MeasurePerformance
 * public void someMethod() {
 * // Method implementation
 * }
 * }
 *
 * 2. On a class (will measure all methods):
 * {@code
 * @MeasurePerformance
 * public class SomeService {
 * // Class implementation
 * }
 * }
 *
 * The metrics will be available through:
 * - Application logs
 * - Actuator endpoints (/actuator/metrics)
 * - Prometheus format (/actuator/prometheus)
 *
 * @author Kolman-Freecss (@https://github.com/Kolman-Freecss)
 * @version 1.0
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface MeasurePerformance {
    /**
     * Optional value to provide additional context for the measurement.
     * This can be used to group or categorize measurements.
     *
     * @return The measurement context or category
     */
    String value() default "";
}
