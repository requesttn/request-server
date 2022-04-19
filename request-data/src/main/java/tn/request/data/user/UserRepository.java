package tn.request.data.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByEmail(String email);
    UserEntity getUserEntityByEmail(String email);
    Optional<UserEntity> findUserEntityByEmail(String email);
}
