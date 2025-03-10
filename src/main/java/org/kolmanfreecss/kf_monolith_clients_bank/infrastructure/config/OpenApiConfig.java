package org.kolmanfreecss.kf_monolith_clients_bank.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenApiConfig
 * <p>
 * This class configures the OpenAPI documentation for the application.
 * It provides metadata about the API and the server it runs on.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server()
            .url("http://localhost:8080")
            .description("Development server");

        Contact contact = new Contact()
            .name("Kolman-Freecss")
            .url("https://github.com/Kolman-Freecss");

        License mitLicense = new License()
            .name("MIT License")
            .url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
            .title("Bank Client Management API")
            .version("1.0.0")
            .contact(contact)
            .description("This API exposes endpoints to manage bank clients.")
            .license(mitLicense);

        return new OpenAPI()
            .info(info)
            .servers(List.of(devServer));
    }
}
