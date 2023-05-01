package v1.amachon.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import v1.amachon.domain.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
