package com.github.RobSargsyan27.budgetMateV2.app.api.auth;

import com.github.RobSargsyan27.budgetMateV2.app.api.auth.request.TokenAuthenticationRequest;
import com.github.RobSargsyan27.budgetMateV2.app.api.auth.request.UserAuthenticationRequest;
import com.github.RobSargsyan27.budgetMateV2.app.api.auth.request.RegistrationRequest;
import com.github.RobSargsyan27.budgetMateV2.app.api.auth.response.UserAuthenticationResponse;
import com.github.RobSargsyan27.budgetMateV2.app.api.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService registrationService;

    @PostMapping("/register")
    public HttpStatus register(@RequestBody @Valid RegistrationRequest request) {
        this.registrationService.register(request);
        return HttpStatus.ACCEPTED;
    }

    @GetMapping("/register/confirm/{email}/{token}")
    public RedirectView confirmRegister(@PathVariable String email, @PathVariable String token){
        this.registrationService.confirmRegister(email, token);
        return new RedirectView("/login");
    }

    @PostMapping("/login")
    public ResponseEntity<UserAuthenticationResponse> login(@RequestBody UserAuthenticationRequest request) {
        return ResponseEntity.ok(this.registrationService.login(request));
    }

    @PostMapping("/validate-token")
    public ResponseEntity<Map<String, Boolean>> validateToken(@RequestBody TokenAuthenticationRequest request){
        return ResponseEntity.ok(this.registrationService.validateToken(request));
    }
}
