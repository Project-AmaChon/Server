package v1.amachon.domain.message.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import v1.amachon.domain.message.entity.MessageRoom;

import java.util.Optional;

public interface MessageRoomRepository extends JpaRepository<MessageRoom, Long> {
    @Query("SELECT m FROM MessageRoom m JOIN FETCH m.to JOIN FETCH m.from " +
            "JOIN FETCH m.toSend mt JOIN FETCH mt.to JOIN FETCH mt.from " +
            "WHERE m.from.id = :fromId AND m.to.id = :toId")
    Optional<MessageRoom> findByFromAndTo(@Param("fromId") Long fromId, @Param("toId") Long toId);

    @Query("SELECT m FROM MessageRoom m JOIN FETCH m.to JOIN FETCH m.from " +
            "JOIN FETCH m.toSend mt JOIN FETCH mt.to JOIN FETCH mt.from " +
            "WHERE m.id = :roomId AND m.status = 'NORMAL'")
    Optional<MessageRoom> findByIdFetch(@Param("roomId") Long roomId);

    @Query("SELECT m FROM MessageRoom m JOIN FETCH m.from JOIN FETCH m.messages mm " +
            "JOIN FETCH mm.sender mms JOIN FETCH mms.profile " +
            "WHERE m.id = :roomId AND m.status = 'NORMAL' " +
            "ORDER BY m.lastModifiedDate DESC, mm.lastModifiedDate DESC")
    Optional<MessageRoom> findByIdFetchMessages(@Param("roomId") Long roomId);
}
