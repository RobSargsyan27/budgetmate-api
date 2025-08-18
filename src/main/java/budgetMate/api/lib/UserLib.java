package budgetMate.api.lib;

import budgetMate.api.domain.User;
import budgetMate.api.repository.UserRepository;
import budgetMate.api.security.JwtService;
import budgetMate.api.util.FetchUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserLib {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final FetchUtil fetchUtil;

    /**
     * <h2>Fetch request </h2>
     * @param request {HttpServletRequest}
     * @return {User}
     */
    public User fetchRequestUser(HttpServletRequest request){
        final String token = request.getHeader("Authorization");
        final String _token = token.substring(7);
        final String email = jwtService.extractUsername(_token);

        return fetchUtil.fetchResource(userRepository.findUserByUsername(email), "User");
    }
}
