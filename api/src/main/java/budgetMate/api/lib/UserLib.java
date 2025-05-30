package budgetMate.api.lib;

import budgetMate.api.domain.User;
import budgetMate.api.repository.UserRepository;
import budgetMate.api.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserLib {
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public User fetchRequestUser(HttpServletRequest request){
        final String token = request.getHeader("Authorization");
        final String _token = token.substring(7);
        final String email = jwtService.extractUsername(_token);

        return userRepository.findUserByUsername(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
    }
}
