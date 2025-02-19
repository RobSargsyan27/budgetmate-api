package com.gitlab.robertsargsyan.budgetMate.app.repository.userRepository;

import com.gitlab.robertsargsyan.budgetMate.app.domain.User;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Profile("prod")
@Repository
public interface UserRepositoryJpaImpl extends JpaRepository<User, UUID>, UserRepository{

    Optional<User> findUserByUsername(String username);

    Optional<User> findUserById(UUID id);

    @Modifying
    @Query("UPDATE User u SET u.isEnabled = TRUE WHERE u.username = :username")
    void enableUser(String username);

    @Modifying
    @Query("DELETE User u WHERE u.id = :id")
    int deleteUserById(UUID id);
}
