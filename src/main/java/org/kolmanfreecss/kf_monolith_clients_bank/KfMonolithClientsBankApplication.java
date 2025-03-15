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
        }
        catch (Exception ex) {
            log.error("FATAL ERROR! - Exception: ", ex);
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
        final Environment env = context.getEnvironment();
        final String appName = context.getId(); // Get app name
        String hostAddress = "unknown";

        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        }
        catch (UnknownHostException e) {
            log.warn("The host name could not be determined, using 'unknown' as value");
        }
        log.info("""
                     ----------------------------------------------------------
                     \tApplication '{}' is running! Access URLs:
                     \tLocal: \t\thttp://localhost:{}
                     \tExternal: \t\thttp://{}:{}
                     \tProfile(s): \t{}
                     ----------------------------------------------------------""",
                 appName, env.getProperty("server.port"), hostAddress, env.getProperty("server.port"),
                 env.getActiveProfiles());

        log.info("Spring Service [{}] RUNNING - Beans loaded: {}, Profiles: {}",
                 KfMonolithClientsBankApplication.class.getSimpleName(),
                 context.getBeanDefinitionCount(),
                 env.getActiveProfiles());

        if (context.getBeanDefinitionCount() < 10) {
            log.warn("Low number of beans loaded... Possible error");
        }
    }

}
