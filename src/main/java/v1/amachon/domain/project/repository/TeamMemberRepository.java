package v1.amachon.domain.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import v1.amachon.domain.project.entity.TeamMember;

import java.util.List;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {

    @Query("SELECT t FROM TeamMember t JOIN FETCH t.member tm " +
            "JOIN FETCH tm.profile WHERE t.project.id = :projectId")
    List<TeamMember> findByProjectId(@Param("projectId") Long projectId);
}
