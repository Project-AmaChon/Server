package v1.amachon.domain.message.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import v1.amachon.domain.message.entity.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
