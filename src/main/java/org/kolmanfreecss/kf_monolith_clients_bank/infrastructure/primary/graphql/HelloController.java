package org.kolmanfreecss.kf_monolith_clients_bank.infrastructure.primary.graphql;

import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class HelloController {

    @QueryMapping
    public String hello() {
        return "Hello from GraphQL!";
    }
}
