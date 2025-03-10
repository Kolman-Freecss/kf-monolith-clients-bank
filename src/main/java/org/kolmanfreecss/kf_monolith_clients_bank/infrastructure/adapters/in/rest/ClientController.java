package org.kolmanfreecss.kf_monolith_clients_bank.infrastructure.adapters.in.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.kolmanfreecss.kf_monolith_clients_bank.application.client.services.ClientService;
import org.kolmanfreecss.kf_monolith_clients_bank.domain.client.Client;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * REST Controller for managing bank clients. Provides CRUD operations and
 * additional functionality for client management.
 *
 * @author Kolman-Freecss
 * @version 1.0.0
 * @category Client
 */
@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
@Tag(name = "Client Management", description = "APIs for managing bank clients")
public class ClientController {

    private final ClientService clientService;

    @Operation(summary = "Get all clients", description = "Retrieves a list of all bank clients in the system")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of clients", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Client.class)))
    @GetMapping
    public ResponseEntity<List<Client>> getAllClients() {
        return ResponseEntity.ok(clientService.getAllClients());
    }

    @Operation(summary = "Get client by ID", description = "Retrieves a specific client by their UUID")
    @ApiResponse(responseCode = "200", description = "Client found and returned successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Client.class)))
    @ApiResponse(responseCode = "404", description = "Client not found", content = @Content)
    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@Parameter(description = "UUID of the client to retrieve")
                                                @PathVariable
                                                UUID id) {
        return clientService.getClientById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create new client", description = "Creates a new client in the system")
    @ApiResponse(responseCode = "200", description = "Client created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Client.class)))
    @ApiResponse(responseCode = "400", description = "Invalid client data provided", content = @Content)
    @PostMapping
    public ResponseEntity<Client> createClient(@Parameter(description = "Client data to create", required = true)
                                               @RequestBody
                                               Client client) {
        return ResponseEntity.ok(clientService.createClient(client));
    }

    @Operation(summary = "Update client", description = "Updates an existing client's information")
    @ApiResponse(responseCode = "200", description = "Client updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Client.class)))
    @ApiResponse(responseCode = "404", description = "Client not found", content = @Content)
    @ApiResponse(responseCode = "400", description = "Invalid client data provided", content = @Content)
    @PutMapping("/{id}")
    public ResponseEntity<Client> updateClient(@Parameter(description = "UUID of the client to update")
                                               @PathVariable
                                               UUID id, @Parameter(description = "Updated client data", required = true)
                                               @RequestBody
                                               Client client) {
        return clientService.updateClient(id, client).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete client", description = "Deletes a client from the system")
    @ApiResponse(responseCode = "204", description = "Client deleted successfully")
    @ApiResponse(responseCode = "404", description = "Client not found", content = @Content)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@Parameter(description = "UUID of the client to delete")
                                             @PathVariable
                                             UUID id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get client rights", description = "Retrieves the rights and permissions associated with a specific client")
    @ApiResponse(responseCode = "200", description = "Client rights retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(type = "array")))
    @ApiResponse(responseCode = "404", description = "Client not found", content = @Content)
    @GetMapping("/{id}/rights")
    public ResponseEntity<Set<String>> getClientRights(@Parameter(description = "UUID of the client to get rights for")
                                                       @PathVariable
                                                       UUID id) {
        try {
            Set<String> rights = clientService.getClientRights(id);
            return ResponseEntity.ok(rights);
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
