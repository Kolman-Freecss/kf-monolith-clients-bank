package org.kolmanfreecss.kf_monolith_clients_bank.infrastructure.config.aop;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;

/**
 * Configuration class for Aspect-Oriented Programming (AOP) setup.
 * This class enables and configures AOP functionality for the application,
 * particularly focusing on performance measurement aspects.
 *
 * Features enabled:
 * - AspectJ auto-proxy for AOP support
 * - Micrometer's TimedAspect for basic timing metrics
 * - Custom performance measurement aspects
 *
 * This configuration works in conjunction with {@link PerformanceAspect}
 * to provide comprehensive performance monitoring capabilities.
 *
 * @author Kolman-Freecss (@https://github.com/Kolman-Freecss)
 * @version 1.0
 * @see PerformanceAspect
 * @see MeasurePerformance
 */
@Configuration
@EnableAspectJAutoProxy
public class AopConfig {

    /**
     * Creates and configures a TimedAspect bean for Micrometer metrics.
     * This aspect enables the use of @Timed annotation for basic method timing.
     *
     * @param registry The Micrometer registry for recording metrics
     * @return Configured TimedAspect bean
     */
    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }
}
