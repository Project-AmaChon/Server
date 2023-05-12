package v1.amachon.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import v1.amachon.domain.member.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findByNickname(String nickname);
}
