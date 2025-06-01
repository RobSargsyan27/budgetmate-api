package budgetMate.api.api.user.service;

import budgetMate.api.api.account.response.AccountAdditionResponse;
import budgetMate.api.api.user.request.UpdateUserRequest;
import budgetMate.api.api.user.response.UserResponse;
import budgetMate.api.domain.User;
import budgetMate.api.lib.UserLib;
import budgetMate.api.repository.AccountAdditionRequestRepository;
import budgetMate.api.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AccountAdditionRequestRepository accountAdditionRequestRepository;
    private final UserLib userLib;

    public UserResponse getUser(HttpServletRequest request){
        final User user = userLib.fetchRequestUser(request);

        return UserResponse.builder()
                .id(user.getId().toString())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .username(user.getUsername())
                .country(user.getCountry())
                .city(user.getCity())
                .address(user.getAddress())
                .postalCode(user.getPostalCode())
                .role(user.getRole())
                .avatarColor(user.getAvatarColor())
                .build();

    }

    public UserResponse updateUser(UUID id, UpdateUserRequest request){
        final User user = userRepository.findUserById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));

        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setCountry(request.getCountry());
        user.setCity(request.getCity());
        user.setAddress(request.getAddress());
        user.setPostalCode(request.getPostalCode());
        user.setAvatarColor(request.getAvatarColor());

        final User updatedUser = userRepository.save(user);
        return UserResponse.builder()
                .id(user.getId().toString())
                .firstname(updatedUser.getFirstname())
                .lastname(updatedUser.getLastname())
                .username(updatedUser.getUsername())
                .country(updatedUser.getCountry())
                .city(updatedUser.getCity())
                .address(updatedUser.getAddress())
                .postalCode(updatedUser.getPostalCode())
                .role(updatedUser.getRole())
                .avatarColor(updatedUser.getAvatarColor())
                .build();
    }

    @Transactional
    public Integer deleteUser(String id) {
        return userRepository.deleteUserById(UUID.fromString(id));
    }

    public List<AccountAdditionResponse> getNotifications(HttpServletRequest request){
        final User user = userLib.fetchRequestUser(request);

        return accountAdditionRequestRepository.findUnapprovedRequestsByOwnerUser(user)
                .stream()
                .map((notification) ->
                        AccountAdditionResponse.builder()
                                .id(notification.getId())
                                .accountName(notification.getAccountName())
                                .ownerUsername(notification.getOwnerUser().getUsername())
                                .requestedUsername(notification.getRequestedUser().getUsername())
                                .build())
                .toList();
    }
}
