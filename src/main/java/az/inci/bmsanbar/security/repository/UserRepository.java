package az.inci.bmsanbar.security.repository;

import az.inci.bmsanbar.security.domain.ApiUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<ApiUser, Long>
{
    Optional<ApiUser> findByUsername(String username);
}
