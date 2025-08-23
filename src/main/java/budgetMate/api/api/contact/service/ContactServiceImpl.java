package budgetMate.api.api.contact.service;

import budgetMate.api.api.contact.request.SendContactRequest;
import budgetMate.api.producer.EmailProducer;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {
    private final EmailProducer emailProducer;

    @Override
    @Transactional
    public Void sendContactRequest(SendContactRequest request) {
        emailProducer.sendContactEmail(request);

        return null;
    }
}
