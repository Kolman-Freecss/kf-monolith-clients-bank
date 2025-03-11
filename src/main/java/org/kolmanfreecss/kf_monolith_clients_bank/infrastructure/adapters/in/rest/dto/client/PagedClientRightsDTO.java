package org.kolmanfreecss.kf_monolith_clients_bank.infrastructure.adapters.in.rest.dto.client;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Paginated response containing client rights")
public record PagedClientRightsDTO(
        @Schema(description = "List of client rights for the current page") List<String> rights,

        @Schema(description = "Current page number (0-based)") int page,

        @Schema(description = "Number of items per page") int size,

        @Schema(description = "Total number of rights across all pages") long totalElements,

        @Schema(description = "Total number of pages") int totalPages,

        @Schema(description = "Whether there is a next page") boolean hasNext,

        @Schema(description = "Whether there is a previous page") boolean hasPrevious) {
}
