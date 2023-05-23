package v1.amachon.domain.project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import v1.amachon.domain.project.entity.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("SELECT p FROM Project p WHERE p.status = 'NORMAL' ORDER BY p.createdDate DESC")
    Page<Project> searchRecentProjects(Pageable pageable);

    @Query("SELECT p FROM Project p JOIN FETCH p.leader l JOIN FETCH p.recruitManagements rm JOIN FETCH rm.member rmm " +
            "JOIN FETCH rmm.profile WHERE p.id = :projectId AND p.status = 'NORMAL'")
    Optional<Project> getRecruitListFetch(@Param("projectId") Long projectId);

    @Query("SELECT p FROM Project p JOIN FETCH p.leader l JOIN FETCH p.techTags t JOIN FETCH t.techTag " +
            "JOIN FETCH p.regionTag WHERE p.id = :projectId AND p.status = 'NORMAL'")
    Optional<Project> findByIdFetch(@Param("projectId") Long projectId);

    @Query("SELECT p FROM Project p LEFT JOIN FETCH p.teamMembers pt JOIN FETCH p.leader pl " +
            "WHERE pl.id = :leaderId")
    List<Project> findByLeaderId(@Param("leaderId") Long leaderId);

    @Query("SELECT p FROM Project p JOIN FETCH p.teamMembers pt " +
            "WHERE :memberId IN pt.member.id")
    List<Project> findByMemberId(@Param("memberId") Long memberId);
}

