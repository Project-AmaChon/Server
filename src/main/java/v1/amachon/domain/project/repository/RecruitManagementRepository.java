package v1.amachon.domain.project.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import v1.amachon.domain.project.entity.RecruitManagement;

public interface RecruitManagementRepository extends JpaRepository<RecruitManagement, Long> {
  List<RecruitManagement> findByProjectId(Long projectId);
  Optional<RecruitManagement> findByProjectIdAndTeamMemberId(Long projectId, Long teamMemberId);
}
