package v1.amachon.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import v1.amachon.domain.member.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query("SELECT m FROM Member m JOIN FETCH m.authorities a JOIN FETCH m.profile WHERE m.email = :email")
    Optional<Member> findByEmail(@Param("email") String email);
    Optional<Member> findByNickname(String nickname);
}
