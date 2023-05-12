package v1.amachon.domain.project.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import v1.amachon.domain.project.entity.RecruitManagement;

public interface RecruitManagementRepository extends JpaRepository<RecruitManagement, Long> {
  public List<RecruitManagement> findByProjectId(Long projectId);
}
