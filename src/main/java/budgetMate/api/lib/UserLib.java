package budgetMate.api.lib;

import budgetMate.api.domain.User;
import budgetMate.api.repository.UserRepository;
import budgetMate.api.util.FetchUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserLib {
    private final UserRepository userRepository;
    private final FetchUtil fetchUtil;

    /**
     * <h2>Fetch request </h2>
     * @param request {HttpServletRequest}
     * @return {User}
     */
    public User fetchRequestUser(HttpServletRequest request){
        final String email = request.getUserPrincipal().getName();

        return fetchUtil.fetchResource(userRepository.findUserByUsername(email), "User");
    }
}
