package v1.amachon.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import v1.amachon.member.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query("SELECT m FROM Member m LEFT JOIN FETCH m.techTags mt " +
            "LEFT JOIN FETCH mt.techTag LEFT JOIN FETCH m.regionTag WHERE m.email = :email")
    Optional<Member> findByEmail(@Param("email") String email);

    @Query("SELECT m FROM Member m LEFT JOIN FETCH m.authorities WHERE m.email = :email")
    Optional<Member> findByEmailWithAuthorities(@Param("email") String email);

    Optional<Member> findByNickname(String nickname);

    @Query("SELECT m FROM Member m LEFT JOIN FETCH m.techTags mt " +
            "LEFT JOIN FETCH mt.techTag LEFT JOIN FETCH m.regionTag WHERE m.id = :memberId")
    Optional<Member> findByIdFetchTags(@Param("memberId") Long memberId);
}
