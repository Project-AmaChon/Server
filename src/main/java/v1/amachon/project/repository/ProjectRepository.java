package v1.amachon.project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import v1.amachon.project.entity.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("SELECT p FROM Project p WHERE p.status = 'NORMAL' ORDER BY p.createdDate DESC")
    Page<Project> searchRecentProjects(Pageable pageable);

    @Query("SELECT p FROM Project p JOIN FETCH p.leader l JOIN FETCH p.recruitManagements r JOIN FETCH r.member rm " +
            "JOIN FETCH rm.profile WHERE p.id = :projectId AND p.status = 'NORMAL'")
    Optional<Project> getRecruitListFetch(@Param("projectId") Long projectId);

    @Query("SELECT p FROM Project p JOIN FETCH p.leader JOIN FETCH p.techTags t JOIN FETCH t.techTag " +
            "JOIN FETCH p.regionTag WHERE p.id = :projectId AND p.status = 'NORMAL'")
    Optional<Project> findByIdFetch(@Param("projectId") Long projectId);

    @Query("SELECT p FROM Project p LEFT JOIN FETCH p.recruitManagements WHERE p.id = :projectId AND p.status = 'NORMAL'")
    Optional<Project> findByIdFetchRecruitments(@Param("projectId") Long projectId);

}

