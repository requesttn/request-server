package tn.request.data;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationTokenEntity, Long> {
  boolean existsByToken(String token);

  ConfirmationTokenEntity getByToken(String token);

  Optional<ConfirmationTokenEntity> findByToken(String token);
}
