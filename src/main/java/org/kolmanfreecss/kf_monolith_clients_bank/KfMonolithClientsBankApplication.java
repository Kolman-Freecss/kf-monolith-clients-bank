package org.kolmanfreecss.kf_monolith_clients_bank;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Optional;

/**
 * Main class for the Spring Boot application.
 *
 * @version 1.0
 * @uthor Kolman-Freecss
 */
@Slf4j
@SpringBootApplication
public class KfMonolithClientsBankApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(KfMonolithClientsBankApplication.class, args);
        } catch (Exception ex) {
            /**
             * Check if the exception is a SilentExitException (DevTools restart)
             * This is a common exception when using Spring Boot DevTools.
             * It happens when the application is restarted due to changes in the code.
             * It's safe to ignore this error and just log a warning.
             */
            final String name = ex.getClass().getSimpleName();
            if ("SilentExitException".equals(name)) { // Just ignore it if appears in the console
                log.warn("DevTools restart detected. Application is restarting...", ex);
            } else {
                log.error("FATAL ERROR! - Exception: ", ex);
            }
        }
    }

    /**
     * Event executed after the application context is refreshed.
     *
     * @param contextRefreshedEvent: Param received from the event
     */
    @EventListener(ContextRefreshedEvent.class)
    public void testLogging(ContextRefreshedEvent contextRefreshedEvent) {
        printLogLevel();
        logApplicationStartup((ConfigurableApplicationContext) contextRefreshedEvent.getApplicationContext());
    }

    private void printLogLevel() {
        log.trace("This is a TRACE level message - Very detailed information");
        log.debug("This is a DEBUG level message - Detailed information");
        log.info("This is an INFO level message - General information");
        log.warn("This is a WARN level message - Warning");
        log.error("This is an ERROR level message - Application error");
    }

    private void logApplicationStartup(ConfigurableApplicationContext context) {
        Environment env = context.getEnvironment();
        String appName = context.getId();
        String hostAddress = "unknown";
        String protocol = "http";

        if (env.getProperty("server.ssl.key-store") != null) {
            protocol = "https";
        }

        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("The host name could not be determined, using 'unknown' as value.");
        }

        String contextPath = env.getProperty("server.servlet.context-path");
        if (contextPath == null || contextPath.trim().isEmpty()) {
            contextPath = "/";
        } else if (!contextPath.startsWith("/")) {
            contextPath = "/" + contextPath;
        }

        log.info("\n----------------------------------------------------------\n\t" +
                         "Application '{}' is running! Access URLs:\n\t" +
                         "Local: \t\t{}://localhost:{}{}\n\t" +
                         "External: \t{}://{}:{}{}\n\t" +
                         "Profile(s): \t{}\n\t" +
                         "----------------------------------------------------------",
                 appName, protocol, env.getProperty("server.port"), contextPath, protocol, hostAddress,
                 env.getProperty("server.port"), contextPath, Arrays.toString(env.getActiveProfiles()));

        if (context.getBeanDefinitionCount() < 10) {
            log.warn("Low number of beans loaded... Possible error");
        }

        logEnvironmentDetails(env);
        logDependencies(env);
        logExternalServices(env);
        logConfigurationValidation(env); // Important: Validate config!
    }

    private void logEnvironmentDetails(Environment env) {
        log.info("-------------------- Environment Details --------------------");
        log.info("Operating System: {} {}", System.getProperty("os.name"), System.getProperty("os.version"));
        log.info("Java Version: {}", System.getProperty("java.version"));
        log.info("Java Vendor: {}", System.getProperty("java.vendor"));
        log.info("Java Home: {}", System.getProperty("java.home"));
        log.info("Current Working Directory: {}", System.getProperty("user.dir"));

        Runtime runtime = Runtime.getRuntime();
        log.info("Available Processors: {}", runtime.availableProcessors());
        log.info("Total Memory: {} MB", runtime.totalMemory() / (1024 * 1024));
        log.info("Max Memory: {} MB", runtime.maxMemory() / (1024 * 1024));
        log.info("Free Memory: {} MB", runtime.freeMemory() / (1024 * 1024));
        log.info("Database URL: {} (masked)", maskDatabaseUrl(env.getProperty("spring.datasource.url")));
        log.info("App Version: {}", env.getProperty("spring.application.version", "Unknown"));
        log.info("Cache system used: {}", env.getProperty("spring.cache.type", "Not Configured"));
        log.info("-------------------------------------------------------------");
    }

    private void logDependencies(Environment env) {
        log.info("-------------------- Key Dependencies --------------------");
        Optional.ofNullable(org.springframework.boot.SpringBootVersion.getVersion())
                .ifPresent(version -> log.info("Spring Boot Version: {}", version));
        log.info("Database Driver: {}", env.getProperty("spring.datasource.driver-class-name",
                                                        "Not Configured")); // Better to get actual version if possible.
//        log.info("Message Broker: {} (URL: {})", env.getProperty("spring.rabbitmq.host", "N/A"),
        env.getProperty("spring.rabbitmq.addresses", "N/A");
        log.info("-------------------------------------------------------------");
    }

    private void logExternalServices(Environment env) {
        log.info("-------------------- External Services --------------------");
        // Example (adapt to your actual external service properties)
        log.info("External Service 1: {}", env.getProperty("external.service1.url", "Not Configured"));
        log.info("-------------------------------------------------------------");
    }

    private void logConfigurationValidation(Environment env) {
        log.info("----------------- Configuration Validation ------------------");
        // Example: Check for required database URL
        if (env.getProperty("spring.datasource.url") == null || env.getProperty("spring.datasource.url").isEmpty()) {
            log.error("FATAL: Database URL is not configured! (spring.datasource.url)");
            // Consider throwing an exception here to prevent startup:
            // throw new IllegalStateException("Database URL is not configured!");
        } else {
            log.info("Database URL configured correctly."); //Inform that is correctly configured.
        }

        // Example: Check for a required API key (but don't log the key itself!)
        if (env.getProperty("external.service1.api-key") == null) {
            log.warn("Warning: API key for External Service 1 is not configured! (external.service1.api-key)");
            // Don't throw exception, just a warning
        }

        log.info("-------------------------------------------------------------");
    }

    /**
     * Helper function to mask sensitive parts of the database URL.
     *
     * @param url
     * @return String
     */
    private String maskDatabaseUrl(final String url) {
        if (url == null) {
            return null;
        }
        return url.replaceAll("://[^@]*@", "//****:****@");
    }

}
