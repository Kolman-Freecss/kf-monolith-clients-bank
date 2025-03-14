package org.kolmanfreecss.kf_monolith_clients_bank.shared.config;

import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.Value;

@Configuration
public class CustomMetricsConfig {

    @Value("${spring.application.name:kf-bank}")
    private String applicationName;

    @Bean
    public MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
        return registry -> registry.config()
                .commonTags("application", applicationName)
                .commonTags("env", "${spring.profiles.active:default}");
    }
}
