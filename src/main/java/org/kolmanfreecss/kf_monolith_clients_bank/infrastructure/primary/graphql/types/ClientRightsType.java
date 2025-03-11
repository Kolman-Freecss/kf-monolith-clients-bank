package org.kolmanfreecss.kf_monolith_clients_bank.infrastructure.primary.graphql.types;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientRightsType {
    private String clientId;
    private Set<String> rights;
}
