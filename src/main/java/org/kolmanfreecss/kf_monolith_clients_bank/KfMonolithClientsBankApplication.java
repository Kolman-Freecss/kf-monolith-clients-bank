package org.kolmanfreecss.kf_monolith_clients_bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class KfMonolithClientsBankApplication {

    public static void main(String[] args) {
        SpringApplication.run(KfMonolithClientsBankApplication.class, args);
    }

    @EventListener(ContextRefreshedEvent.class)
    public void testLogging() {
        log.trace("This is a TRACE level message - Very detailed information");
        log.debug("This is a DEBUG level message - Detailed information");
        log.info("This is an INFO level message - General information");
        log.warn("This is a WARN level message - Warning");
        log.error("This is an ERROR level message - Application error");
    }
}
