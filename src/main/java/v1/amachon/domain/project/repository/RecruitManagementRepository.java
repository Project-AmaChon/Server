package v1.amachon.domain.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import v1.amachon.domain.project.entity.RecruitManagement;

public interface RecruitManagementRepository extends JpaRepository<RecruitManagement, Long> {
}
