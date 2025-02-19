package com.gitlab.robertsargsyan.budgetMate.app.lib;

import com.gitlab.robertsargsyan.budgetMate.app.domain.User;
import com.gitlab.robertsargsyan.budgetMate.app.repository.userRepository.UserRepository;
import com.gitlab.robertsargsyan.budgetMate.app.repository.userRepository.UserRepositoryJpaImpl;
import com.gitlab.robertsargsyan.budgetMate.app.security.JwtService;
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
