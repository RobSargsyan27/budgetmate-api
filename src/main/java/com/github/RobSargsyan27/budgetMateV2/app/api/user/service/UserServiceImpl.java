package com.github.RobSargsyan27.budgetMateV2.app.api.user.service;

import com.github.RobSargsyan27.budgetMateV2.app.api.account.response.AccountAdditionResponse;
import com.github.RobSargsyan27.budgetMateV2.app.api.user.request.UpdateUserRequest;
import com.github.RobSargsyan27.budgetMateV2.app.api.user.response.UserResponse;
import com.github.RobSargsyan27.budgetMateV2.app.domain.User;
import com.github.RobSargsyan27.budgetMateV2.app.lib.UserLib;
import com.github.RobSargsyan27.budgetMateV2.app.repository.AccountAdditionRequestRepository;
import com.github.RobSargsyan27.budgetMateV2.app.repository.UserRepository;
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

    public UserResponse updateUser(Integer id, UpdateUserRequest request){
        final User user = userRepository.findUserById(UUID.fromString(request.getId()))
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
