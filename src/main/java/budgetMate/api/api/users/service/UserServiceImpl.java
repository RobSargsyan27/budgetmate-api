package budgetMate.api.api.users.service;

import budgetMate.api.api.accountAdditionRequests.mapper.AccountAdditionResponseMapper;
import budgetMate.api.api.accountAdditionRequests.response.AccountAdditionResponse;
import budgetMate.api.api.users.mapper.UserResponseMapper;
import budgetMate.api.api.users.request.UpdateUserRequest;
import budgetMate.api.api.users.response.UserResponse;
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
    private final AccountAdditionResponseMapper accountAdditionResponseMapper;
    private final UserResponseMapper userResponseMapper;

    /**
     * <h2>Get user.</h2>
     * @param request {HttpServletRequest}
     * @return {UserResponse}
     */
    @Override
    @Transactional
    public UserResponse getUser(HttpServletRequest request){
        final User user = userLib.fetchRequestUser(request);

        return userResponseMapper.toDto(user);
    }

    /**
     * <h2>Update user.</h2>
     * @param request {HttpServletRequest}
     * @param body {UpdateUserRequest}
     * @return {UserResponse}
     */
    @Override
    @Transactional
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

        return userResponseMapper.toDto(updatedUser);
    }

    /**
     * <h2>Delete user.</h2>
     * @param request {HttpServletRequest}
     * @return {Void}
     */
    @Override
    @Transactional
    public Void deleteUser(HttpServletRequest request) {
        final User user = userLib.fetchRequestUser(request);

        userRepository.deleteById(user.getId());

        return null;
    }

    /**
     * <h2>Get user notifications.</h2>
     * @param request {HttpServletRequest}
     * @return {List<AccountAdditionResponse>}
     */
    @Override
    @Transactional
    public List<AccountAdditionResponse> getUserNotifications(HttpServletRequest request){
        final User user = userLib.fetchRequestUser(request);

        final List<AccountAdditionRequest> requests = accountAdditionRequestRepository.findUnapprovedRequestsByOwnerUser(user);

        return accountAdditionResponseMapper.toDtoList(requests);
    }
}
