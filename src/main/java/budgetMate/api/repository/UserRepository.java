package budgetMate.api.repository;

import budgetMate.api.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findUserByUsername(String username);

    @Modifying
    @Query("UPDATE User u SET u.isEnabled = TRUE WHERE u.username = :username")
    void enableUser(String username);
}
