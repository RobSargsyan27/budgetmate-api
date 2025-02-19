package com.github.RobSargsyan27.budgetMateV2.app.repository.userRepository;

import com.github.RobSargsyan27.budgetMateV2.app.domain.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    Optional<User> findUserByUsername(String username);

    Optional<User> findUserById(UUID id);

    void enableUser(String username);

    int deleteUserById(UUID id);

    User save(User user);
}
