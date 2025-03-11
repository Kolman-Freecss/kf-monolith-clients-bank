package org.kolmanfreecss.kf_monolith_clients_bank.infrastructure.primary.graphql.types;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PagedClientRightsType {
    private List<String> rights;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean hasNext;
    private boolean hasPrevious;
}
