package budgetMate.api.api.contact;

import budgetMate.api.api.contact.request.SendContactRequest;
import budgetMate.api.api.contact.service.ContactService;
import budgetMate.api.util.HttpUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/contact")
public class ContactController {
    private final ContactService contactService;
    private final HttpUtil httpUtil;

    @PostMapping("")
    public ResponseEntity<Void> sendContactRequest(@RequestBody @Valid SendContactRequest request){
        return httpUtil.handleAdd(contactService.sendContactRequest(request));
    }
}
