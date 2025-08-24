package budgetMate.api.api.auth;

import budgetMate.api.api.auth.request.UserAuthenticationRequest;
import budgetMate.api.api.auth.request.RegistrationRequest;
import budgetMate.api.api.auth.service.AuthService;
import budgetMate.api.util.HttpUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/auth")
public class AuthController {
    private final AuthService registrationService;
    private final HttpUtil httpUtil;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegistrationRequest request, HttpServletRequest servletRequest) {
        servletRequest.getSession();
        return httpUtil.handleAdd(registrationService.register(request));
    }

    @GetMapping("/register/confirm")
    public ResponseEntity<Void> confirmRegister(@RequestParam String email, @RequestParam String token){
        return httpUtil.handleUpdate(registrationService.confirmRegister(email, token));
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody UserAuthenticationRequest request, HttpServletRequest servletRequest) {
        registrationService.login(request);
        servletRequest.getSession(true);

       return ResponseEntity.ok().build();
    }
}
