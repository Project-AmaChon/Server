package v1.amachon.mail.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import v1.amachon.mail.entity.EmailVerification;

import java.util.Optional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {
    Optional<EmailVerification> findByEmail(String email);
}
