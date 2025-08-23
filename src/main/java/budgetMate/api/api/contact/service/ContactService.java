package budgetMate.api.api.contact.service;

import budgetMate.api.api.contact.request.SendContactRequest;

public interface ContactService {
    Void sendContactRequest(SendContactRequest request);
}
