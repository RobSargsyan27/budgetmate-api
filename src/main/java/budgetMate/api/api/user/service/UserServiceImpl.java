package budgetMate.api.api.user.service;

import budgetMate.api.api.account.response.AccountAdditionResponse;
import budgetMate.api.api.user.request.UpdateUserRequest;
import budgetMate.api.api.user.response.UserResponse;
import budgetMate.api.domain.AccountAdditionRequest;
import budgetMate.api.domain.User;
import budgetMate.api.lib.UserLib;
import budgetMate.api.repository.AccountAdditionRequestRepository;
import budgetMate.api.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AccountAdditionRequestRepository accountAdditionRequestRepository;
    private final UserLib userLib;

    @Override
    public UserResponse getUser(HttpServletRequest request){
        final User user = userLib.fetchRequestUser(request);

        return UserResponse.from(user);
    }

    @Override
    public UserResponse updateUser(HttpServletRequest request, UpdateUserRequest body){
        final User user = userLib.fetchRequestUser(request);

        user.setFirstname(body.getFirstname());
        user.setLastname(body.getLastname());
        user.setCountry(body.getCountry());
        user.setCity(body.getCity());
        user.setAddress(body.getAddress());
        user.setPostalCode(body.getPostalCode());
        user.setAvatarColor(body.getAvatarColor());
        final User updatedUser = userRepository.save(user);

        return UserResponse.from(updatedUser);
    }

    @Override
    @Transactional
    public Void deleteUser(HttpServletRequest request) {
        final User user = userLib.fetchRequestUser(request);

        userRepository.deleteById(user.getId());

        return null;
    }

    @Override
    public List<AccountAdditionResponse> getNotifications(HttpServletRequest request){
        final User user = userLib.fetchRequestUser(request);

        final List<AccountAdditionRequest> requests = accountAdditionRequestRepository.findUnapprovedRequestsByOwnerUser(user);

        return AccountAdditionResponse.from(requests);
    }
}
