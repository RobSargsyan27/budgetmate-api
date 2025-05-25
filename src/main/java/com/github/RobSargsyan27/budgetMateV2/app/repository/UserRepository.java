package com.github.RobSargsyan27.budgetMateV2.app.repository;

import com.github.RobSargsyan27.budgetMateV2.app.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findUserByUsername(String username);

    Optional<User> findUserById(UUID id);

    @Modifying
    @Query("UPDATE User u SET u.isEnabled = TRUE WHERE u.username = :username")
    void enableUser(String username);

    @Modifying
    @Query("DELETE User u WHERE u.id = :id")
    int deleteUserById(UUID id);
}
