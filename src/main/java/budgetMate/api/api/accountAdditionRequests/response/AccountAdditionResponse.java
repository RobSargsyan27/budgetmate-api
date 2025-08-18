package budgetMate.api.api.accountAdditionRequests.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record AccountAdditionResponse (UUID id, String ownerUsername, String requestedUsername, String accountName) { }