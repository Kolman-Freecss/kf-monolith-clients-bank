package org.kolmanfreecss.kf_monolith_clients_bank.infrastructure.config.aop;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Aspect for measuring and monitoring method execution performance across the
 * application.
 * This aspect automatically captures timing metrics for:
 * - All Service methods
 * - All GraphQL queries and mutations
 * - Any method or class annotated with @MeasurePerformance
 *
 * Metrics collected:
 * - Execution time (in milliseconds)
 * - Success/failure status
 * - Histograms for performance distribution
 * - Percentiles for performance analysis
 *
 * All metrics are exposed through:
 * - Application logs
 * - Micrometer metrics (/actuator/metrics)
 * - Prometheus format (/actuator/prometheus)
 *
 * @author Kolman-Freecss (@https://github.com/Kolman-Freecss)
 * @version 1.0
 */
@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class PerformanceAspect {

    private final MeterRegistry meterRegistry;

    /**
     * Measures the execution time of methods and records various performance
     * metrics.
     * This advice targets:
     * - Methods in @Service classes
     * - GraphQL @QueryMapping methods
     * - GraphQL @MutationMapping methods
     * - Methods/Classes with @MeasurePerformance annotation
     *
     * @param joinPoint The join point representing the intercepted method
     * @return The result of the method execution
     * @throws Throwable If the underlying method throws an exception
     */
    @Around("@within(org.springframework.stereotype.Service) || " +
            "@within(org.springframework.graphql.data.method.annotation.QueryMapping) || " +
            "@within(org.springframework.graphql.data.method.annotation.MutationMapping) || " +
            "@within(org.kolmanfreecss.kf_monolith_clients_bank.infrastructure.config.aop.MeasurePerformance) || " +
            "@annotation(org.kolmanfreecss.kf_monolith_clients_bank.infrastructure.config.aop.MeasurePerformance)")
    public Object measureMethodExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = method.getName();
        String fullMethodName = className + "." + methodName;

        long startTime = System.nanoTime();
        Object result = null;
        boolean isSuccess = true;
        try {
            result = joinPoint.proceed();
            return result;
        } catch (Throwable throwable) {
            isSuccess = false;
            throw throwable;
        } finally {
            long duration = System.nanoTime() - startTime;
            double durationInMs = duration / 1_000_000.0;

            // Log the execution time
            log.info("Method {} executed in {} ms (success: {})",
                    fullMethodName,
                    String.format("%.2f", durationInMs),
                    isSuccess);

            // Record basic timing metrics with tags
            Timer timer = Timer.builder("method.execution.time")
                    .tag("class", className)
                    .tag("method", methodName)
                    .tag("success", String.valueOf(isSuccess))
                    .register(meterRegistry);

            timer.record(duration, TimeUnit.NANOSECONDS);

            // Record detailed histogram for performance distribution analysis
            meterRegistry.summary("method.execution.time.histogram",
                    "class", className,
                    "method", methodName,
                    "success", String.valueOf(isSuccess))
                    .record(durationInMs);

            // Record percentile metrics for SLA analysis
            meterRegistry.timer("method.execution.time.percentiles",
                    "class", className,
                    "method", methodName)
                    .record(duration, TimeUnit.NANOSECONDS);
        }
    }
}