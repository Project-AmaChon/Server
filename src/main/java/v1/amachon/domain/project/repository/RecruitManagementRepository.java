package v1.amachon.domain.project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import v1.amachon.domain.project.entity.RecruitManagement;

public interface RecruitManagementRepository extends JpaRepository<RecruitManagement, Long> {
    List<RecruitManagement> findByProjectId(Long projectId);

    @Query("SELECT r FROM RecruitManagement r WHERE r.status = 'NORMAL' AND r.id = :id")
    Optional<RecruitManagement> findById(@Param("id") Long id);
}
