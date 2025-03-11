package org.kolmanfreecss.kf_monolith_clients_bank.infrastructure.primary.graphql.types;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * GraphQL type for representing client rights information.
 * Used in GraphQL queries and mutations for client rights management.
 *
 * @author Kolman-Freecss (@https://github.com/Kolman-Freecss)
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientRightsType {
    private String clientId;
    private Set<String> rights;
}
