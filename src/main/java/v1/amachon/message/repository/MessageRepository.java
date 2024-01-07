package v1.amachon.message.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import v1.amachon.message.entity.Message;

import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("SELECT m FROM Message m JOIN FETCH m.messageRoom JOIN FETCH m.sender ms " +
            "JOIN FETCH ms.profile WHERE m.id = :messageId AND m.status = 'NORMAL'")
    Optional<Message> findByIdFetch(@Param("messageId") Long messageId);
}
