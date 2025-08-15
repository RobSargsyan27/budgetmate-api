package budgetMate.api.api.auth;

import budgetMate.api.api.auth.request.TokenAuthenticationRequest;
import budgetMate.api.api.auth.request.UserAuthenticationRequest;
import budgetMate.api.api.auth.request.RegistrationRequest;
import budgetMate.api.api.auth.response.UserAuthenticationResponse;
import budgetMate.api.api.auth.service.AuthService;
import budgetMate.api.util.HttpUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/auth")
public class AuthController {
    private final AuthService registrationService;
    private final HttpUtil httpUtil;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegistrationRequest request) {
        return httpUtil.handleAdd(registrationService.register(request));
    }

    @GetMapping("/register/confirm/{email}/{token}")
    public RedirectView confirmRegister(@PathVariable String email, @PathVariable String token){
        this.registrationService.confirmRegister(email, token);
        return new RedirectView("/login");
    }

    @PostMapping("/login")
    public ResponseEntity<UserAuthenticationResponse> login(@RequestBody UserAuthenticationRequest request) {
       return httpUtil.handleAdd(this.registrationService.login(request));
    }

    @PostMapping("/validate-token")
    public ResponseEntity<Map<String, Boolean>> validateToken(@RequestBody TokenAuthenticationRequest request){
        return httpUtil.handleAdd(this.registrationService.validateToken(request));
    }
}
