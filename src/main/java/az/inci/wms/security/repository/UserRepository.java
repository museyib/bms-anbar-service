package az.inci.wms.security.repository;

import az.inci.wms.security.domain.ApiUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<ApiUser, Long> {
    Optional<ApiUser> findByUsername(String username);
}
