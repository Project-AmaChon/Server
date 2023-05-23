package v1.amachon.domain.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import v1.amachon.domain.project.entity.TeamMember;

import java.util.List;
import java.util.Optional;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {

    @Query("SELECT t FROM TeamMember t JOIN FETCH t.member tm " +
            "JOIN FETCH tm.profile WHERE t.project.id = :projectId AND t.status = 'NORMAL'")
    List<TeamMember> findByProjectId(@Param("projectId") Long projectId);

    @Query("SELECT t FROM TeamMember t JOIN FETCH t.member tm JOIN FETCH t.project tp JOIN FETCH tp.leader " +
            "WHERE t.id = :teamMemberId AND t.status = 'NORMAL'")
    Optional<TeamMember> findById(@Param("teamMemberId") Long teamMemberId);
}
